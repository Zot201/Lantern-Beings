package zotmc.lanternbeings.data;

import net.minecraft.launchwrapper.Launch;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import zotmc.lanternbeings.data.ModData.LanternBeings;
import zotmc.lanternbeings.util.BiConsumer;
import zotmc.lanternbeings.util.Consumer;
import zotmc.lanternbeings.util.init.Messod;
import zotmc.lanternbeings.util.init.Typo;

public class AsmData {
	
	public static final String MODID = LanternBeings.MODID;
	
	private static boolean mcpNames() {
		return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}
	
	
	private static class LbImplss {
		// types
		
		public static final Typo
		LB_IMPLS = Typo.of("zotmc/lanternbeings/LbImpls");
	}
	
	private static class ExtEntityAnimals {
		// types
		
		public static final Typo
		EXT_ENTITY_ANIMAL = Typo.of("zotmc/lanternbeings/ExtEntityAnimal");
	}
	
	
	public static class EntityAnimals {
		// types
		
		public static final Typo
		ENTITY_ANIMAL = Typo.of("net/minecraft/entity/passive/EntityAnimal");
		
		
		// methods
		
		public static final Messod
		INTERACT = ENTITY_ANIMAL
			.mess("interact", "func_70085_c")
			.desc("(Lnet/minecraft/entity/player/EntityPlayer;)Z"),
		WRITE_ENTITY_TO_NBT = ENTITY_ANIMAL
			.mess("writeEntityToNBT", "func_70014_b")
			.desc("(Lnet/minecraft/nbt/NBTTagCompound;)V"),
		READ_ENTITY_FROM_NBT = ENTITY_ANIMAL
			.mess("readEntityFromNBT", "func_70037_a")
			.desc("(Lnet/minecraft/nbt/NBTTagCompound;)V"),
		ATTACK_ENTITY_AS_MOB = ENTITY_ANIMAL
			.mess("attackEntityAsMob", "func_70652_k")
			.desc("(Lnet/minecraft/entity/Entity;)Z"),
		ON_RANDOM_SPAWN = ENTITY_ANIMAL
			.mess("func_180482_a")
			.desc("(Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/IEntityLivingData;)"
					+ "Lnet/minecraft/entity/IEntityLivingData;"),
		ON_DEATH = ENTITY_ANIMAL
			.mess("onDeath", "func_70645_a")
			.desc("(Lnet/minecraft/util/DamageSource;)V");
		
		public static final String
		LB_EXT = "lb_ext",
		LB_EXT_METHOD_DESC = "()Lzotmc/lanternbeings/ExtEntityAnimal;",
		LB_EXT_FIELD_DESC = "Lzotmc/lanternbeings/ExtEntityAnimal;";
		
		public static final Consumer<ClassWriter>
		LB_EXT_WRITER = new Consumer<ClassWriter>() { public void accept(ClassWriter cw) {
			cw.visitField(Opcodes.ACC_PRIVATE, LB_EXT, LB_EXT_FIELD_DESC, null, null);
			
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, LB_EXT, LB_EXT_METHOD_DESC, null, null);
			Label l0 = new Label();
			
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, ENTITY_ANIMAL.toString(), LB_EXT, LB_EXT_FIELD_DESC);
			mv.visitInsn(Opcodes.DUP);
			mv.visitJumpInsn(Opcodes.IFNONNULL, l0);
			
			mv.visitInsn(Opcodes.POP);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitTypeInsn(Opcodes.NEW, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString());
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(), "<init>",
					"(Lnet/minecraft/entity/passive/EntityAnimal;)V", false);
			mv.visitInsn(Opcodes.DUP_X1);
			mv.visitFieldInsn(Opcodes.PUTFIELD, ENTITY_ANIMAL.toString(), LB_EXT, LB_EXT_FIELD_DESC);
			
			mv.visitLabel(l0);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
		}};
		
		public static final Consumer<MethodNode>
		INTERACT_WRITER = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			LabelNode l0 = new LabelNode(new Label());
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(),
					LB_EXT, LB_EXT_METHOD_DESC, false));
			before.add(new VarInsnNode(Opcodes.ALOAD, 1));
			before.add(new InsnNode(Opcodes.ICONST_0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"interactExt", "(Lnet/minecraft/entity/player/EntityPlayer;Z)Z", false));
			before.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			
			before.add(new InsnNode(Opcodes.ICONST_1));
			before.add(new InsnNode(Opcodes.IRETURN));
			
			before.add(l0);
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};
		
		public static final BiConsumer<InsnList, AbstractInsnNode>
		WRITE_ENTITY_TO_NBT_WRITER = new BiConsumer<InsnList, AbstractInsnNode>() {
			@Override public void accept(InsnList list, AbstractInsnNode returnNode) {
				InsnList before = new InsnList();
				
				before.add(new VarInsnNode(Opcodes.ALOAD, 0));
				before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(),
						LB_EXT, LB_EXT_METHOD_DESC, false));
				before.add(new VarInsnNode(Opcodes.ALOAD, 1));
				before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
						"writeEntityToNBTExt", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false));
				
				list.insertBefore(returnNode, before);
			}
		},
		READ_ENTITY_FROM_NBT_WRITER = new BiConsumer<InsnList, AbstractInsnNode>() {
			@Override public void accept(InsnList list, AbstractInsnNode returnNode) {
				InsnList before = new InsnList();
				
				before.add(new VarInsnNode(Opcodes.ALOAD, 0));
				before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(),
						LB_EXT, LB_EXT_METHOD_DESC, false));
				before.add(new VarInsnNode(Opcodes.ALOAD, 1));
				before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
						"readEntityFromNBTExt", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false));
				
				list.insertBefore(returnNode, before);
			}
		};
		
		public static final Consumer<ClassWriter>
		ATTACK_ENTITY_AS_MOB_WRITER = new Consumer<ClassWriter>() { public void accept(ClassWriter cw) {
			String name = mcpNames() ? "attackEntityAsMob" : "func_70652_k";
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/entity/Entity;)Z", null, null);
			Label l0 = new Label();
			
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(), LB_EXT, LB_EXT_METHOD_DESC, false);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"attackEntityAsMobExt", "(Lnet/minecraft/entity/Entity;)Z", false);
			mv.visitJumpInsn(Opcodes.IFEQ, l0);
			
			mv.visitInsn(Opcodes.ICONST_1);
			mv.visitInsn(Opcodes.IRETURN);
			
			mv.visitLabel(l0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/entity/EntityLivingBase", name,
					"(Lnet/minecraft/entity/Entity;)Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			
			mv.visitMaxs(0, 0);
		}};
		
		public static final Consumer<MethodNode>
		ATTACK_ENTITY_AS_MOB_WRITER_P = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			LabelNode l0 = new LabelNode(new Label());
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(),
					LB_EXT, LB_EXT_METHOD_DESC, false));
			before.add(new VarInsnNode(Opcodes.ALOAD, 1));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"attackEntityAsMobExt", "(Lnet/minecraft/entity/Entity;)Z", false));
			before.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			
			before.add(new InsnNode(Opcodes.ICONST_1));
			before.add(new InsnNode(Opcodes.IRETURN));
			
			before.add(l0);
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};

		public static final Consumer<ClassWriter>
		ON_RANDOM_SPAWN_WRITER = new Consumer<ClassWriter>() { public void accept(ClassWriter cw) {
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "func_180482_a",
					"(Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/IEntityLivingData;)"
					+ "Lnet/minecraft/entity/IEntityLivingData;", null, null);
			
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(), LB_EXT, LB_EXT_METHOD_DESC, false);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"onRandomSpawnExt", "()V", false);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/entity/EntityLiving", "func_180482_a",
					"(Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/IEntityLivingData;)"
					+ "Lnet/minecraft/entity/IEntityLivingData;", false);
			mv.visitInsn(Opcodes.ARETURN);
			
			mv.visitMaxs(0, 0);
		}};
		
		public static final Consumer<MethodNode>
		ON_RANDOM_SPAWN_WRITER_P = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(),
					LB_EXT, LB_EXT_METHOD_DESC, false));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"onRandomSpawnExt", "()V", false));
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};

		public static final Consumer<ClassWriter>
		ON_DEATH_WRITER = new Consumer<ClassWriter>() { public void accept(ClassWriter cw) {
			String name = mcpNames() ? "onDeath" : "func_70645_a";
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/util/DamageSource;)V", null, null);
			
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(), LB_EXT, LB_EXT_METHOD_DESC, false);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"onDeathExt", "()V", false);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/entity/EntityLivingBase", name,
					"(Lnet/minecraft/util/DamageSource;)V", false);
			mv.visitInsn(Opcodes.RETURN);
			
			mv.visitMaxs(0, 0);
		}};
		
		public static final Consumer<MethodNode>
		ON_DEATH_WRITER_P = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_ANIMAL.toString(),
					LB_EXT, LB_EXT_METHOD_DESC, false));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"onDeathExt", "()V", false));
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};
	}
	
	
	public static class ClientEntityAnimals {
		// methods
		
		public static final Messod
		GET_BRIGHTNESS_FOR_RENDER = EntityAnimals.ENTITY_ANIMAL
			.mess("getBrightnessForRender", "func_70070_b")
			.desc("(F)I");
		
		public static final Consumer<ClassWriter>
		GET_BRIGHTNESS_FOR_RENDER_WRITER = new Consumer<ClassWriter>() { public void accept(ClassWriter cw) {
			String name = mcpNames() ? "getBrightnessForRender" : "func_70070_b";
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(F)I", null, null);
			Label l0 = new Label();
			
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, LbImplss.LB_IMPLS.toString(), "isEntityGlowing",
					"(Lnet/minecraft/entity/passive/EntityAnimal;)Z", false);
			mv.visitJumpInsn(Opcodes.IFEQ, l0);
			
			mv.visitLdcInsn(240);
			mv.visitInsn(Opcodes.IRETURN);
			
			mv.visitLabel(l0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/entity/Entity", name, "(F)I", false);
			mv.visitInsn(Opcodes.IRETURN);
			
			mv.visitMaxs(0, 0);
		}};
		
		public static final Consumer<MethodNode>
		GET_BRIGHTNESS_FOR_RENDER_WRITER_P = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			LabelNode l0 = new LabelNode(new Label());
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKESTATIC, LbImplss.LB_IMPLS.toString(), "isEntityGlowing",
					"(Lnet/minecraft/entity/passive/EntityAnimal;)Z", false));
			before.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			
			before.add(new LdcInsnNode(240));
			before.add(new InsnNode(Opcodes.IRETURN));
			
			before.add(l0);
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};
	}
	
	public static class EntityHorses {
		// types
		
		private static final Typo
		ENTITY_HORSE = Typo.of("net/minecraft/entity/passive/EntityHorse");
		
		
		// methods
		
		public static final Messod
		INTERACT = ENTITY_HORSE
			.mess("interact", "func_70085_c")
			.desc("(Lnet/minecraft/entity/player/EntityPlayer;)Z");
		
		public static final Consumer<MethodNode>
		INTERACT_WRITER = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			LabelNode l0 = new LabelNode(new Label());
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_HORSE.toString(),
					EntityAnimals.LB_EXT, EntityAnimals.LB_EXT_METHOD_DESC, false));
			before.add(new VarInsnNode(Opcodes.ALOAD, 1));
			before.add(new InsnNode(Opcodes.ICONST_1));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"interactExt", "(Lnet/minecraft/entity/player/EntityPlayer;Z)Z", false));
			before.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			
			before.add(new InsnNode(Opcodes.ICONST_1));
			before.add(new InsnNode(Opcodes.IRETURN));
			
			before.add(l0);
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};
	}
	
	public static class EntityWolfs {
		// types
		
		private static final Typo
		ENTITY_WOLF = Typo.of("net/minecraft/entity/passive/EntityWolf");
		
		
		// methods
		
		public static final Messod
		INTERACT = ENTITY_WOLF
			.mess("interact", "func_70085_c")
			.desc("(Lnet/minecraft/entity/player/EntityPlayer;)Z");
		
		public static final Consumer<MethodNode>
		INTERACT_WRITER = new Consumer<MethodNode>() { public void accept(MethodNode methodNode) {
			InsnList before = new InsnList();
			LabelNode l0 = new LabelNode(new Label());
			
			before.add(new VarInsnNode(Opcodes.ALOAD, 0));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENTITY_WOLF.toString(),
					EntityAnimals.LB_EXT, EntityAnimals.LB_EXT_METHOD_DESC, false));
			before.add(new VarInsnNode(Opcodes.ALOAD, 1));
			before.add(new InsnNode(Opcodes.ICONST_1));
			before.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ExtEntityAnimals.EXT_ENTITY_ANIMAL.toString(),
					"interactExt", "(Lnet/minecraft/entity/player/EntityPlayer;Z)Z", false));
			before.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			
			before.add(new InsnNode(Opcodes.ICONST_1));
			before.add(new InsnNode(Opcodes.IRETURN));
			
			before.add(l0);
			
			InsnList list = methodNode.instructions;
			list.insertBefore(list.getFirst(), before);
		}};
	}
	
}
