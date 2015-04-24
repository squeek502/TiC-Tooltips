package squeek.tictooltips.proxy;

import java.lang.reflect.Field;
import net.minecraft.item.Item;
import squeek.tictooltips.ModTiCTooltips;

public class ProxyIguanaTweaks
{
	private static Class<?> IguanaTweaksConfig = null;
	public static boolean toolsRequireBoost = false;

	public static void init()
	{
		try
		{
			IguanaTweaksConfig = Class.forName("iguanaman.iguanatweakstconstruct.reference.Config");
			Field levelingPickaxeBoost = IguanaTweaksConfig.getDeclaredField("levelingPickaxeBoost");
			Field pickaxeBoostRequired = IguanaTweaksConfig.getDeclaredField("pickaxeBoostRequired");
			toolsRequireBoost = levelingPickaxeBoost.getBoolean(null) && pickaxeBoostRequired.getBoolean(null);
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.error("Failed to load Iguana Tweaks integration: " + e.toString());
		}
	}

	public static int getUnboostedHarvestLevel(Item item, int harvestLevel)
	{
		if (toolsRequireBoost && (item.getUnlocalizedName().toLowerCase().contains("pick") || item.getUnlocalizedName().toLowerCase().contains("hammer")))
			return Math.max(0, harvestLevel - 1);
		else
			return harvestLevel;
	}
}
