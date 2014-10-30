package zotmc.lanternbeings.loading;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.MethodNode;

import zotmc.lanternbeings.util.Consumer;
import zotmc.lanternbeings.util.init.Typo;

class ConsumerMethodPatcher extends AbstractMethodPatcher {
	
	private final Consumer<ClassWriter> consumer;
	
	public ConsumerMethodPatcher(Typo type, String name, String desc, Consumer<ClassWriter> consumer) {
		super(type, name, desc);
		this.consumer = consumer;
	}
	
	@Override protected boolean processMethod(MethodNode methodNode) { return false; }
	
	@Override protected boolean addMethod(ClassWriter cw) {
		consumer.accept(cw);
		return true;
	}
	
}
