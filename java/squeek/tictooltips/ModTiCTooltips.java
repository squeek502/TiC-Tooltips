package squeek.tictooltips;

import net.minecraftforge.common.MinecraftForge;
import squeek.tictooltips.helpers.ToolPartHelper;
import squeek.tictooltips.proxy.ProxyExtraTiC;
import squeek.tictooltips.proxy.ProxyIguanaTweaks;
import squeek.tictooltips.proxy.ProxyMariculture;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModTiCTooltips.MODID, version = ModTiCTooltips.VERSION, dependencies = "required-after:TConstruct;after:ExtraTiC;after:TSteelworks;after:Mariculture")
public class ModTiCTooltips
{
	public static final String MODID = "TiCTooltips";
	public static final String VERSION = "${version}";
	
	public static boolean hasIguanaTweaks;

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
	}

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		if (Loader.isModLoaded("ExtraTiC"))
		{
			ProxyExtraTiC.init();
			ProxyExtraTiC.registerParts();
		}
		if (Loader.isModLoaded("Mariculture"))
		{
			ProxyMariculture.init();
			ProxyMariculture.registerParts();
		}
		if (Loader.isModLoaded("IguanaTweaksTConstruct"))
		{
			hasIguanaTweaks = true;
			ProxyIguanaTweaks.init();
			ProxyIguanaTweaks.registerParts();
		}

		ToolPartHelper.determineMinAndMaxValues();
	}
}
