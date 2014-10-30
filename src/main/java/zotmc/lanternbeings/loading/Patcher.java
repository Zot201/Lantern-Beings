package zotmc.lanternbeings.loading;

import org.apache.logging.log4j.Logger;

import zotmc.lanternbeings.util.init.Typo;

interface Patcher {
	
	public Typo targetType();
	
	public byte[] patch(byte[] basicClass, Logger log) throws Throwable;
	
}
