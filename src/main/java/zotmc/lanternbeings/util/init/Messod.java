package zotmc.lanternbeings.util.init;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.Objects;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.base.Joiner;

public final class Messod {
	
	private final Typo owner;
	private final List<String> names;
	private final String desc;
	
	Messod(Typo owner, List<String> names, String desc) {
		this.owner = owner;
		this.names = names;
		this.desc = desc;
	}
	
	public Typo getOwner() {
		return owner;
	}
	
	public Messod desc(String desc) {
		checkState(this.desc == null);
		return new Messod(owner, names, desc);
	}
	
	
	public boolean covers(String name, String desc) {
		String owner = FMLDeobfuscatingRemapper.INSTANCE.unmap(this.owner.toString());
		String mappedName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
		String mappedDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		
		if (this.desc == null || this.desc.equals(mappedDesc))
			for (String s : names)
				if (s.equals(mappedName))
					return true;
		
		return false;
	}
	
	public boolean covers(MethodNode node) {
		return covers(node.name, node.desc);
	}
	
	public boolean covers(String owner, String name, String desc) {
		return this.owner.covers(owner) && covers(name, desc);
	}
	
	public boolean covers(MethodInsnNode node) {
		return covers(node.owner, node.name, node.desc);
	}
	
	
	@Override public int hashCode() {
		return Objects.hash(owner, names, desc);
	}
	
	@Override public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Messod) {
			Messod o = (Messod) obj;
			return owner.equals(o.owner) && names.equals(o.names) && desc.equals(o.desc);
		}
		return false;
	}
	
	@Override public String toString() {
		return String.format("%s/[%s]", owner, Joiner.on(", ").join(names));
	}
	
}
