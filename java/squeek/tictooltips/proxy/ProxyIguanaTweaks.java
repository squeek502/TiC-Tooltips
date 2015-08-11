package squeek.tictooltips.proxy;

import java.lang.reflect.Field;
import java.util.Locale;
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
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.error("Failed to load Iguana Tweaks integration: " + e.toString());
		}
	}

	public static int getUnboostedHarvestLevel(Item item, int harvestLevel)
	{
		if (toolsRequireBoost && (item.getUnlocalizedName().toLowerCase(Locale.ROOT).contains("pick") || item.getUnlocalizedName().toLowerCase(Locale.ROOT).contains("hammer")))
			return Math.max(0, harvestLevel - 1);
		else
			return harvestLevel;
	}
}
