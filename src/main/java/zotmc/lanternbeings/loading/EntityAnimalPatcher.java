package zotmc.lanternbeings.loading;

import java.util.List;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import zotmc.lanternbeings.data.AsmData.ClientEntityAnimals;
import zotmc.lanternbeings.data.AsmData.EntityAnimals;
import zotmc.lanternbeings.util.BiConsumer;
import zotmc.lanternbeings.util.init.Messod;

import com.google.common.collect.Lists;

class EntityAnimalPatcher extends AbstractListedPatcher {

	public EntityAnimalPatcher() {
		super(EntityAnimals.ENTITY_ANIMAL);
	}
	
	@Override protected Iterable<Patcher> patchers() {
		List<Patcher> ret = Lists.newArrayList(
				new ConsumerMethodPatcher(targetType(), EntityAnimals.LB_EXT,
						EntityAnimals.LB_EXT_METHOD_DESC, EntityAnimals.LB_EXT_WRITER),
				new InteractExt(),
				new AttactExt(),
				new SpawnExt(),
				new DeathExt(),
				new NbtExt(EntityAnimals.WRITE_ENTITY_TO_NBT, EntityAnimals.WRITE_ENTITY_TO_NBT_WRITER),
				new NbtExt(EntityAnimals.READ_ENTITY_FROM_NBT, EntityAnimals.READ_ENTITY_FROM_NBT_WRITER)
		);
		
		if (FMLLaunchHandler.side().isClient())
			ret.add(new BrightnessPatch());
		
		return ret;
	}
	
	
	private static class InteractExt extends AbstractMethodPatcher {
		public InteractExt() {
			super(EntityAnimals.INTERACT);
		}
		
		@Override protected boolean processMethod(MethodNode methodNode) {
			EntityAnimals.INTERACT_WRITER.accept(methodNode);
			return true;
		}
		
		@Override protected boolean addMethod(ClassWriter cw) { return false; }
	}
	
	private static class AttactExt extends AbstractMethodPatcher {
		public AttactExt() {
			super(EntityAnimals.ATTACK_ENTITY_AS_MOB);
		}

		@Override protected boolean processMethod(MethodNode methodNode) {
			EntityAnimals.ATTACK_ENTITY_AS_MOB_WRITER_P.accept(methodNode);
			return true;
		}

		@Override protected boolean addMethod(ClassWriter cw) {
			EntityAnimals.ATTACK_ENTITY_AS_MOB_WRITER.accept(cw);
			return true;
		}
	}
	
	private static class SpawnExt extends AbstractMethodPatcher {
		public SpawnExt() {
			super(EntityAnimals.ON_RANDOM_SPAWN);
		}
		
		@Override protected boolean processMethod(MethodNode methodNode) {
			EntityAnimals.ON_RANDOM_SPAWN_WRITER_P.accept(methodNode);
			return true;
		}
		
		@Override protected boolean addMethod(ClassWriter cw) {
			EntityAnimals.ON_RANDOM_SPAWN_WRITER.accept(cw);
			return true;
		}
	}
	
	private static class DeathExt extends AbstractMethodPatcher {
		public DeathExt() {
			super(EntityAnimals.ON_DEATH);
		}
		
		@Override protected boolean processMethod(MethodNode methodNode) {
			EntityAnimals.ON_DEATH_WRITER_P.accept(methodNode);
			return true;
		}
		
		@Override protected boolean addMethod(ClassWriter cw) {
			EntityAnimals.ON_DEATH_WRITER.accept(cw);
			return true;
		}
	}
	
	private static class NbtExt extends AbstractInsnPatcher {
		final BiConsumer<InsnList, AbstractInsnNode> consumer;

		public NbtExt(Messod target, BiConsumer<InsnList, AbstractInsnNode> consumer) {
			super(target);
			this.consumer = consumer;
		}
		
		@Override protected boolean isTargetInsn(AbstractInsnNode insnNode) {
			return insnNode.getOpcode() == Opcodes.RETURN;
		}

		@Override protected void patchInsn(InsnList list, AbstractInsnNode insnNode) {
			consumer.accept(list, insnNode);
		}
	}
	
	private static class BrightnessPatch extends AbstractMethodPatcher {
		public BrightnessPatch() {
			super(ClientEntityAnimals.GET_BRIGHTNESS_FOR_RENDER);
		}
		
		@Override protected boolean processMethod(MethodNode methodNode) {
			ClientEntityAnimals.GET_BRIGHTNESS_FOR_RENDER_WRITER_P.accept(methodNode);
			return true;
		}
		
		@Override protected boolean addMethod(ClassWriter cw) {
			ClientEntityAnimals.GET_BRIGHTNESS_FOR_RENDER_WRITER.accept(cw);
			return true;
		}
	}
	
}
