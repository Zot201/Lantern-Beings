package zotmc.lanternbeings.data;

import net.minecraftforge.fml.common.Loader;
import zotmc.lanternbeings.util.Utils.Dependency;
import zotmc.lanternbeings.util.Utils.Modid;
import zotmc.lanternbeings.util.Utils.Requirements;

public class ModData {
	
	public static class LanternBeings {
		public static final String
		MODID = "lanternbeings",
		MC_STRING = Loader.MC_VERSION;
	}
	
	@Dependency
	@Requirements("1.8 = 8.0.5.1012")
	public static class FML {
		@Modid public static final String MODID = "FML";
	}

}
