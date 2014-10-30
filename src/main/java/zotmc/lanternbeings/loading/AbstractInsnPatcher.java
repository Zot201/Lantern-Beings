package zotmc.lanternbeings.loading;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import zotmc.lanternbeings.util.init.Messod;
import zotmc.lanternbeings.util.init.Typo;

abstract class AbstractInsnPatcher implements Patcher {
	
	private final Messod target;
	
	public AbstractInsnPatcher(Messod target) {
		this.target = target;
	}
	
	@Override public Typo targetType() {
		return target.getOwner();
	}
	
	protected abstract boolean isTargetInsn(AbstractInsnNode insnNode);
	
	@Override public byte[] patch(byte[] basicClass, Logger log) throws Throwable {
		ClassNode classNode = new ClassNode();
		new ClassReader(basicClass).accept(classNode, 0);
		
		for (MethodNode methodNode : classNode.methods)
			if (target.covers(methodNode)) {
				InsnList list = methodNode.instructions;
				
				int count = 0;
				for (AbstractInsnNode insnNode : list.toArray())
					if (isTargetInsn(insnNode)) {
						patchInsn(list, insnNode);
						count++;
					}
				
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				classNode.accept(cw);
				
				log.info("Processed %d insn%s in %s.", count, count == 1 ? "" : "s", target);
				return cw.toByteArray();
			}
		
		log.error("Failed to find %s", target);
		return basicClass;
	}
	
	protected abstract void patchInsn(InsnList list, AbstractInsnNode insnNode);
	
}
