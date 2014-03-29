package squeek.tictooltips.helpers;

import java.text.DecimalFormat;
import java.util.HashMap;
import net.minecraft.util.StatCollector;

public class StringHelper
{

	private static DecimalFormat df = new DecimalFormat("##.##");

	// Taken from tconstruct.client.gui.ToolStationGui
	public static String getHarvestLevelName(int num)
	{
		String unlocalized = "gui.partcrafter.mining" + (num + 1);
		String localized = StringHelper.getLocalizedString(unlocalized);
		if (!unlocalized.equals(localized))
			return localized;
		else
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
		return Integer.toString(durability);
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

	// for TCon version < 1.5.3
	public static HashMap<String, String> localizationAlternatives = new HashMap<String, String>();
	static
	{
		localizationAlternatives.put("gui.smeltery1", "Fuel");
		localizationAlternatives.put("gui.landmine", "Landmine");
		localizationAlternatives.put("gui.partcrafter1", "Tool Part Crafting");
		localizationAlternatives.put("gui.partcrafter2", "Tool Part Building");
		localizationAlternatives.put("gui.partcrafter3", "Place a pattern and a material on the left to get started.");
		localizationAlternatives.put("gui.partcrafter4", "Base Durability: ");
		localizationAlternatives.put("gui.partcrafter5", "Handle Modifier: ");
		localizationAlternatives.put("gui.partcrafter6", "Mining Speed: ");
		localizationAlternatives.put("gui.partcrafter7", "Mining Level: ");
		localizationAlternatives.put("gui.partcrafter8", " Heart");
		localizationAlternatives.put("gui.partcrafter9", " Hearts");
		localizationAlternatives.put("gui.partcrafter10", "Attack: ");
		localizationAlternatives.put("gui.partcrafter.mining1", "Stone");
		localizationAlternatives.put("gui.partcrafter.mining2", "Iron");
		localizationAlternatives.put("gui.partcrafter.mining3", "Redstone");
		localizationAlternatives.put("gui.partcrafter.mining4", "Obsidian");
		localizationAlternatives.put("gui.partcrafter.mining5", "Cobalt");
		localizationAlternatives.put("gui.partcrafter.mining6", "Manyullyn");
		//localizationAlternatives.put("gui.partcrafter.mining7", "Atlarus");
		//localizationAlternatives.put("gui.partcrafter.mining8", "Tartarite");
		localizationAlternatives.put("gui.stenciltable1", "Next Pattern");
		localizationAlternatives.put("gui.stenciltable2", "Previous Pattern");
		localizationAlternatives.put("gui.toolforge1", "Repair and Modification");
		localizationAlternatives.put("gui.toolforge2", "The main way to repair or change your tools. Place a tool and a material on the left to get started.");
		localizationAlternatives.put("gui.toolstation1", "Durability:");
		localizationAlternatives.put("gui.toolstation2", "Durability: ");
		localizationAlternatives.put("gui.toolstation3", "Attack: ");
		localizationAlternatives.put("gui.toolstation4", "Bonus: ");
		localizationAlternatives.put("gui.toolstation5", "Loss: ");
		localizationAlternatives.put("gui.toolstation6", "Draw Speed: ");
		localizationAlternatives.put("gui.toolstation7", "Arrow Speed: ");
		localizationAlternatives.put("gui.toolstation8", "Weight: ");
		localizationAlternatives.put("gui.toolstation9", "Accuracy: ");
		localizationAlternatives.put("gui.toolstation10", "Base Attack:");
		localizationAlternatives.put("gui.toolstation11", "Shortbow Attack:");
		localizationAlternatives.put("gui.toolstation12", "Mining Speeds: ");
		localizationAlternatives.put("gui.toolstation13", "Harvest Levels:");
		localizationAlternatives.put("gui.toolstation14", "Mining Speed: ");
		localizationAlternatives.put("gui.toolstation15", "Mining Level: ");
		localizationAlternatives.put("gui.toolstation16", "Usage Speed: ");
		localizationAlternatives.put("gui.toolstation17", "Modifiers");
		localizationAlternatives.put("gui.toolstation18", "Modifiers remaining: ");
	}

	public static String getLocalizedString(String unlocalized)
	{
		String localized = StatCollector.translateToLocal(unlocalized);
		if (localized.equals(unlocalized))
		{
			localized = localizationAlternatives.get(unlocalized);

			if (localized == null)
				localized = unlocalized;
		}
		return localized;
	}
}
