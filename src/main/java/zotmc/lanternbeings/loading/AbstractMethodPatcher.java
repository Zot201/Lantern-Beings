package zotmc.lanternbeings.loading;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import zotmc.lanternbeings.util.init.Messod;
import zotmc.lanternbeings.util.init.Typo;

abstract class AbstractMethodPatcher implements Patcher {
	
	private final Messod target;
	
	public AbstractMethodPatcher(Typo type, String name, String desc) {
		this(type.mess(name).desc(desc));
	}
	public AbstractMethodPatcher(Messod target) {
		this.target = target;
	}
	
	@Override public Typo targetType() {
		return target.getOwner();
	}
	
	protected abstract boolean processMethod(MethodNode methodNode);
	
	protected abstract boolean addMethod(ClassWriter cw);
	
	
	@Override public byte[] patch(byte[] basicClass, Logger log) throws Throwable {
		ClassNode classNode = new ClassNode();
		new ClassReader(basicClass).accept(classNode, 0);
		
		for (MethodNode methodNode : classNode.methods)
			if (target.covers(methodNode)) {
				if (processMethod(methodNode)) {
					ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
					classNode.accept(cw);
					
					log.info("Processed %s.", target);
					
					return cw.toByteArray();
				}
				
				break;
			}
		
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		new ClassReader(basicClass).accept(cw, 0);
		
		if (addMethod(cw)) {
			log.info("Added %s.", target);
			
			return cw.toByteArray();
		}
		
		
		log.error("Failed to find %s", target);
		
		return basicClass;
	}
	
}
