package zotmc.lanternbeings.loading;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zotmc.lanternbeings.data.AsmData;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MappedTransformer implements IClassTransformer {
	
	private static final Set<String> transformedTypes = Sets.newHashSet();
	
	protected final Logger log = LogManager.getFormatterLogger(AsmData.MODID);
	private final Map<String, Patcher> patchers;
	
	public MappedTransformer() {
		this(LbLoading.getPatcherList());
	}
	
	private MappedTransformer(List<Patcher> patchers) {
		this.patchers = Maps.uniqueIndex(patchers, new Function<Patcher, String>() {
			@Override public String apply(Patcher input) {
				return input.targetType().toString().replace('/', '.');
			}
		});
	}
	
	
	@Override public byte[] transform(String name, String transformedName, byte[] basicClass) {
		Patcher patcher = patchers.get(transformedName);
		if (patcher != null)
			try {
				basicClass = patcher.patch(basicClass, log);
				transformedTypes.add(transformedName);
				
			} catch (Throwable e) {
				log.catching(e);
				throw Throwables.propagate(e);
			}
		
		return basicClass;
	}
	
	Set<String> transformAll() {
		for (String s : patchers.keySet())
			try {
				Class.forName(s);
			} catch (ClassNotFoundException e) {
				throw Throwables.propagate(e);
			}
		
		return Sets.difference(patchers.keySet(), transformedTypes);
	}
	
}
