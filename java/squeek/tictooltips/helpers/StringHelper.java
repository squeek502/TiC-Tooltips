package squeek.tictooltips.helpers;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;
import cpw.mods.fml.common.Loader;
import squeek.tictooltips.ModTiCTooltips;
import squeek.tictooltips.proxy.ProxyIguanaTweaks;
import net.minecraft.util.StatCollector;

public class StringHelper
{

	private static DecimalFormat df = new DecimalFormat("##.##");
	public static Class<?> HarvestLevels = null;
	public static Method getHarvestLevelName = null;
	static
	{
		try
		{
			HarvestLevels = Class.forName("tconstruct.library.util.HarvestLevels");
			getHarvestLevelName = HarvestLevels.getDeclaredMethod("getHarvestLevelName", int.class);
		}
		catch (Exception e)
		{
		}
	}

	// Taken from tconstruct.client.gui.ToolStationGui
	public static String getHarvestLevelName(int num)
	{
		if (getHarvestLevelName != null)
		{
			try
			{
				return (String) getHarvestLevelName.invoke(null, num);
			}
			catch (Exception e)
			{
			}
		}
		else
		{
			if (ModTiCTooltips.hasIguanaTweaks)
				return ProxyIguanaTweaks.getHarvestLevelName(num);

			String unlocalized = "gui.partcrafter.mining" + (num + 1);
			String localized = StringHelper.getLocalizedString(unlocalized);
			if (!unlocalized.equals(localized))
				return localized;
		}
		return String.valueOf(num);
	}

	// Taken from tconstruct.library.tools.ToolCore
	public static String getReinforcedString(int reinforced)
	{
		if (reinforced > 9)
			return "Unbreakable";
		else
			return "Reinforced " + RomanNumeralHelper.toRoman(reinforced);
	}

	// Taken from tconstruct.library.tools.ToolCore
	public static String getDamageString(int attack)
	{
		String damageNum = getDamageNumberString(attack);
		String heart = Math.abs(attack) == 2 ? StringHelper.getLocalizedString("gui.partcrafter8") : StringHelper.getLocalizedString("gui.partcrafter9");
		return damageNum + heart;
	}

	public static String getDamageNumberString(int attack)
	{
		return df.format(attack / 2f);
	}

	public static String getShoddinessTypeString(float shoddiness)
	{
		return shoddiness > 0 ? "Stonebound" : (shoddiness < 0 ? "Jagged" : "");
	}

	public static String getShoddinessString(float shoddiness)
	{
		if ((int) shoddiness == shoddiness)
			return Float.toString(Math.abs(shoddiness));
		else
			return df.format(Math.abs(shoddiness));
	}

	public static String getModifierString(float modifier)
	{
		return modifier + "x";
	}

	public static String getSpeedString(int speed)
	{
		return df.format(speed / 100f);
	}

	public static String getAccuracyString(float accuracy)
	{
		return df.format(accuracy - 4) + "%";
	}

	public static String getDrawSpeedString(int drawSpeed)
	{
		return df.format(drawSpeed / 20f) + "s";
	}

	public static String getDurabilityString(int durability)
	{
		return durability != ToolHelper.INFINITE_DURABILITY ? Integer.toString(durability) : StatCollector.translateToLocal("tictooltips.infinite");
	}

	public static String getWeightString(float weight)
	{
		return df.format(weight);
	}

	public static String getArrowSpeedString(float arrowSpeed)
	{
		return df.format(arrowSpeed);
	}

	public static String getAmmoDamageRangeString(int attack)
	{
		int minAttack = attack;
		int maxAttack = attack * 2;

		String heart = StringHelper.getLocalizedString("gui.partcrafter9");
		return df.format(minAttack / 2f) + "-" + df.format(maxAttack / 2f) + heart;
	}

	public static String getDurationString(double duration)
	{
		return df.format(duration) + "s";
	}

	public static String getPercentageString(double percent)
	{
		return df.format(percent * 100f) + "%";
	}

	// for TCon version < 1.5.3
	public static HashMap<String, String> localizationAlternatives = new HashMap<String, String>();
	static
	{
		localizationAlternatives.put("gui.partcrafter4", "tictooltips.base.durability");
		localizationAlternatives.put("gui.partcrafter5", "tictooltips.handle.modifier");
		localizationAlternatives.put("gui.partcrafter6", "tictooltips.mining.speed");
		localizationAlternatives.put("gui.partcrafter7", "tictooltips.mining.level");
		localizationAlternatives.put("gui.partcrafter8", "tictooltips.heart");
		localizationAlternatives.put("gui.partcrafter9", "tictooltips.hearts");
		localizationAlternatives.put("gui.partcrafter10", "tictooltips.attack");
		localizationAlternatives.put("gui.partcrafter.mining1", "tictooltips.mining.level.1");
		localizationAlternatives.put("gui.partcrafter.mining2", "tictooltips.mining.level.2");
		localizationAlternatives.put("gui.partcrafter.mining3", "tictooltips.mining.level.3");
		localizationAlternatives.put("gui.partcrafter.mining4", "tictooltips.mining.level.4");
		localizationAlternatives.put("gui.partcrafter.mining5", "tictooltips.mining.level.5");
		localizationAlternatives.put("gui.partcrafter.mining6", "tictooltips.mining.level.6" + (Loader.isModLoaded("Metallurgy3Base") ? ".with.metallurgy" : ""));
		localizationAlternatives.put("gui.partcrafter.mining7", "tictooltips.mining.level.7");
		localizationAlternatives.put("gui.partcrafter.mining8", "tictooltips.mining.level.8");
		localizationAlternatives.put("gui.toolstation2", "tictooltips.durability");
		localizationAlternatives.put("gui.toolstation3", "tictooltips.attack");
		localizationAlternatives.put("gui.toolstation4", "tictooltips.bonus");
		localizationAlternatives.put("gui.toolstation5", "tictooltips.loss");
		localizationAlternatives.put("gui.toolstation6", "tictooltips.draw.speed");
		localizationAlternatives.put("gui.toolstation7", "tictooltips.arrow.speed");
		localizationAlternatives.put("gui.toolstation8", "tictooltips.weight");
		localizationAlternatives.put("gui.toolstation9", "tictooltips.accuracy");
		localizationAlternatives.put("gui.toolstation10", "tictooltips.base.attack");
		localizationAlternatives.put("gui.toolstation11", "tictooltips.shortbow.attack");
		localizationAlternatives.put("gui.toolstation12", "tictooltips.mining.speeds");
		localizationAlternatives.put("gui.toolstation13", "tictooltips.harvest.levels");
		localizationAlternatives.put("gui.toolstation14", "tictooltips.mining.speed");
		localizationAlternatives.put("gui.toolstation15", "tictooltips.mining.level");
		localizationAlternatives.put("gui.toolstation16", "tictooltips.usage.speed");
		localizationAlternatives.put("gui.toolstation17", "tictooltips.modifiers");
		localizationAlternatives.put("gui.toolstation18", "tictooltips.modifiers.remaining");
	}

	public static String getAlternativeLocalizedString(String unlocalized)
	{
		String unlocalizedAlternative = localizationAlternatives.get(unlocalized);
		return unlocalizedAlternative != null ? StatCollector.translateToLocal(unlocalizedAlternative) : unlocalized;
	}

	public static String getLocalizedString(String unlocalized)
	{
		if (unlocalized.equals("gui.partcrafter.mining6"))
			return getAlternativeLocalizedString(unlocalized);

		String localized = StatCollector.translateToLocal(unlocalized);
		if (localized.equals(unlocalized))
			return getAlternativeLocalizedString(unlocalized);
		else
			return localized;
	}
}
