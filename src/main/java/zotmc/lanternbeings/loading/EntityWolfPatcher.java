package zotmc.lanternbeings.loading;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.MethodNode;

import zotmc.lanternbeings.data.AsmData.EntityWolfs;

class EntityWolfPatcher extends AbstractMethodPatcher {

	public EntityWolfPatcher() {
		super(EntityWolfs.INTERACT);
	}

	@Override protected boolean processMethod(MethodNode methodNode) {
		EntityWolfs.INTERACT_WRITER.accept(methodNode);
		return true;
	}

	@Override protected boolean addMethod(ClassWriter cw) { return false; }

}
