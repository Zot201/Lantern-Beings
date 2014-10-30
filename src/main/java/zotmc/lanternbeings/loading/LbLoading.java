package zotmc.lanternbeings.loading;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.LogManager;

import zotmc.lanternbeings.data.AsmData;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@TransformerExclusions({"zotmc.lanternbeings.loading", "zotmc.lanternbeings.data.AsmData", "zotmc.lanternbeings.util.init"})
public class LbLoading implements IFMLLoadingPlugin {
	
	@Override public String[] getASMTransformerClass() {
		return new String[] {
				MappedTransformer.class.getName()
		};
	}
	
	@Override public String getModContainerClass() {
		return null;
	}
	
	@Override public String getSetupClass() {
		return null;
	}
	
	@Override public void injectData(Map<String, Object> data) { }
	
	@Override public String getAccessTransformerClass() {
		return null;
	}
	
	
	static List<Patcher> getPatcherList() {
		List<Patcher> ret = Lists.<Patcher>newArrayList(
				new EntityAnimalPatcher(),
				new EntityHorsePatcher(),
				new EntityWolfPatcher()
		);
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private static List<IClassTransformer> getEntireTransformerList() {
		try {
			ClassLoader cl = LbLoading.class.getClassLoader();
			Field f = cl.getClass().getDeclaredField("transformers");
			f.setAccessible(true);
			return (List<IClassTransformer>) f.get(cl);
			
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	
	public static class Post {
		private Post() {
			final Set<String> errored = new MappedTransformer().transformAll();
			if (!errored.isEmpty()) {
				final List<String> msg = ImmutableList.of(
						"Found type(s) being loaded before they can be transformed.",
						"Loading cannot be proceeded without causing in-game errors.",
						"Please tell your core mod authors to fix it.",
						"",
						"Type(s) affected:"
				);
				IllegalStateException e =
						new IllegalStateException(Joiner.on(" ").join(msg) + " " + errored.toString());
				
				throw FMLLaunchHandler.side().isServer() ? e : getClientException(e, errored, msg);
			}
			
			boolean removed =
					Iterables.removeIf(getEntireTransformerList(), Predicates.instanceOf(MappedTransformer.class));
			LogManager.getFormatterLogger(AsmData.MODID)
				.info(removed ? "Removed effected transformers." : "Failed to retrieve effected transformers.");
		}
		
		@SideOnly(Side.CLIENT)
		private RuntimeException getClientException(
				IllegalStateException cause, Set<String> errored, List<String> msg) {
			
			try {
				// prevent directly calling new of a side only class.
				Constructor<? extends RuntimeException> ctor = TypesAlreadyLoadedErrorDisplayException.class
						.getConstructor(IllegalStateException.class, Set.class, List.class);
				
				return ctor.newInstance(cause, errored, msg);
				
			} catch (Throwable e) {
				throw Throwables.propagate(e);
			}
		}
	}

}
