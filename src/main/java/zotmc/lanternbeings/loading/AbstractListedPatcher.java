package zotmc.lanternbeings.loading;

import org.apache.logging.log4j.Logger;

import zotmc.lanternbeings.util.init.Typo;

abstract class AbstractListedPatcher implements Patcher {
	
	private final Typo type;
	
	public AbstractListedPatcher(Typo type) {
		this.type = type;
	}
	
	@Override public Typo targetType() {
		return type;
	}

	protected abstract Iterable<Patcher> patchers();
	
	@Override public byte[] patch(byte[] basicClass, Logger log) throws Throwable {
		for (Patcher p : patchers())
			basicClass = p.patch(basicClass, log);
		
		return basicClass;
	}
	
}
