package zotmc.lanternbeings.loading;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.MethodNode;

import zotmc.lanternbeings.data.AsmData.EntityHorses;

class EntityHorsePatcher extends AbstractMethodPatcher {

	public EntityHorsePatcher() {
		super(EntityHorses.INTERACT);
	}

	@Override protected boolean processMethod(MethodNode methodNode) {
		EntityHorses.INTERACT_WRITER.accept(methodNode);
		return true;
	}

	@Override protected boolean addMethod(ClassWriter cw) { return false; }

}
