package zotmc.lanternbeings.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zotmc.lanternbeings.util.init.SimpleVersion;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;

public class Utils {
	
	public static final SimpleVersion MC_VERSION = new SimpleVersion(
			Fields.<String>get(null, findField(Loader.class, "MC_VERSION", "mccversion"))); // prevent inlining
	
	
	
	// Minecraft
	
	@SideOnly(Side.CLIENT)
	public static class RenderManagers {
		@SuppressWarnings("unchecked")
		public static Map<Class<? extends Entity>, Render> entityRenderMap() {
			return Minecraft.getMinecraft().getRenderManager().entityRenderMap;
		}
	}
	
	
	
	// collections
	
	public static <E> Set<E> newIdentityHashSet() {
		return Collections.newSetFromMap(Utils.<E, Boolean>newIdentityHashMap());
	}
	
	public static <K, V> Map<K, V> newIdentityHashMap() {
		return new NullSafeMap<>(Maps.<K, V>newIdentityHashMap());
	}
	
	private static class NullSafeMap<K, V> extends ForwardingMap<K, V> {
		private Map<K, V> delegate;
		private NullSafeMap(Map<K, V> delegate) {
			this.delegate = delegate;
		}
		@Override protected Map<K, V> delegate() {
			return delegate;
		}
		@Override public V put(K key, V value) {
			return super.put(checkNotNull(key), checkNotNull(value));
		}
		@Override public void putAll(Map<? extends K, ? extends V> map) {
			standardPutAll(map);
		}
	}
	
	
	
	// reflections
	
	public static Field findField(Class<?> clz, String... names) {
		String owner = unmapTypeName(clz);
		for (String s : names)
			try {
				Field f = clz.getDeclaredField(remapFieldName(owner, s));
				f.setAccessible(true);
				return f;
			} catch (Throwable ignored) { }
		
		throw propagate(new NoSuchFieldException(clz.getName() + ".[" + Joiner.on(", ").join(names) + "]"));
	}
	
	public static <T> MethodFinder<T> findMethod(Class<T> clz, String... names) {
		return new MethodFinder<T>(clz, names);
	}
	public static class MethodFinder<T> {
		private final Class<T> clz;
		private final String[] names;
		private MethodFinder(Class<T> clz, String[] names) {
			this.clz = clz;
			this.names = names;
		}
		
		public Method withArgs(Class<?>... parameterTypes) {
			String owner = unmapTypeName(clz);
			for (String s : names)
				try {
					Method m = clz.getDeclaredMethod(remapMethodName(owner, s), parameterTypes);
					m.setAccessible(true);
					return m;
				} catch (Throwable ignored) { }
			
			throw propagate(new NoSuchMethodException(String.format(
					"%s.[%s](%s)",
					clz.getName(),
					Joiner.on(", ").join(names),
					Joiner.on(", ").join(Iterables.transform(Arrays.asList(parameterTypes), ClassNameFunction.INSTANCE))
			)));
		}
		public Invokable<T, Object> asInvokable(Class<?>... parameterTypes) {
			return TypeToken.of(clz).method(withArgs(parameterTypes));
		}
		
		private enum ClassNameFunction implements Function<Class<?>, String> {
			INSTANCE;
			@Override public String apply(Class<?> input) {
				return input == null ? "null" : input.getName();
			}
		}
	}
	
	private static String unmapTypeName(Class<?> clz) {
		return FMLDeobfuscatingRemapper.INSTANCE.unmap(org.objectweb.asm.Type.getInternalName(clz));
	}
	private static String remapFieldName(String owner, String field) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, field, null);
	}
	private static String remapMethodName(String owner, String method) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, method, null);
	}
	
	public static <T> T construct(Class<T> clz) {
		try {
			Constructor<T> ctor = clz.getDeclaredConstructor();
			ctor.setAccessible(true);
			return ctor.newInstance();
		} catch (InvocationTargetException e) {
			throw propagate(e.getCause());
		} catch (Throwable e) {
			throw propagate(e);
		}
	}
	
	public static List<Field> getAnnotatedFields(Class<?> clz, final Class<? extends Annotation> annotationClass) {
		return filterFields(clz, new Predicate<Field>() { public boolean apply(Field input) {
			return input.getAnnotation(annotationClass) != null;
		}});
	}
	
	public static List<Field> getFieldsOfType(Class<?> clz, final Class<?> type) {
		return filterFields(clz, new Predicate<Field>() { public boolean apply(Field input) {
			return type.isAssignableFrom(input.getType());
		}});
	}
	
	private static List<Field> filterFields(Class<?> clz, Predicate<Field> predicate) {
		return FluentIterable.from(Arrays.asList(clz.getDeclaredFields())).filter(predicate).toList();
	}
	
	
	
	// version validations
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Modid { }
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Dependency { }

	/**
	 * When applied to an outer class, this represents a map from building MC versions to the required MC versions.
	 * When applied to an inner class, this represents a map from actual MC versions to the required mod versions.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Requirements {
		public String[] value();
	}
	
	public static Set<ArtifactVersion> checkRequirements(Class<?> clz, String mcString) {
		Set<ArtifactVersion> missing = Sets.newHashSet();
		
		ModContainer mc = Loader.instance().getMinecraftModContainer();
		ArtifactVersion m0 = check(clz, "Minecraft", new SimpleVersion(mcString), mc);
		if (m0 != null)
			missing.add(m0);
		
		Map<String, ModContainer> mods = Loader.instance().getIndexedModList();
		for (Class<?> c : clz.getDeclaredClasses()) {
			String modid = null;
			
			for (Field f : c.getDeclaredFields())
				if (f.getAnnotation(Modid.class) != null) {
					checkArgument(modid == null);
					checkArgument(Modifier.isStatic(f.getModifiers()));
					
					f.setAccessible(true);
					modid = Fields.get(null, f);
				}
			
			if (modid != null) {
				ArtifactVersion m = check(c, modid, MC_VERSION, mods.get(modid));
				if (m != null)
					missing.add(m);
			}
		}
		
		return missing;
	}
	
	private static ArtifactVersion check(Class<?> c, String modid, SimpleVersion key, ModContainer mc) {
		boolean isLoaded = Loader.isModLoaded(modid);
		
		if (isLoaded || c.getAnnotation(Dependency.class) != null) {
			Requirements requirements = c.getAnnotation(Requirements.class);
			
			if (requirements != null) {
				for (String s : requirements.value()) {
					List<String> entry = Splitter.on('=').trimResults().splitToList(s);
					checkArgument(entry.size() == 2);
					
					if (key.isAtLeast(entry.get(0))) {
						ArtifactVersion r = parse(modid, entry.get(1));
						
						if (!isLoaded || !r.containsVersion(mc.getProcessedVersion()))
							return r;
						break;
					}
				}
			}
			
			if (!isLoaded)
				return VersionParser.parseVersionReference(modid);
		}
		
		return null;
	}
	
	private static ArtifactVersion parse(String modid, String versionRange) {
		char c = versionRange.charAt(0);
		if (c != '[' && c != '(') versionRange = "[" + versionRange + ",)";
		return VersionParser.parseVersionReference(modid + "@" + versionRange);
	}
	
	
	
	// exceptions
	
	public static RuntimeException propagate(Throwable t) {
		return propagateWhatever(checkNotNull(t)); // sneaky throw
	}
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T propagateWhatever(Throwable t) throws T {
		throw (T) t;
	}
	
}
