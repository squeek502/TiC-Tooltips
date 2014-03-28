package squeek.tictooltips.helpers;

import java.text.DecimalFormat;

import net.minecraft.util.StatCollector;

public class StringHelper {

	private static DecimalFormat df = new DecimalFormat("##.##");
	
	// Taken from tconstruct.client.gui.ToolStationGui
	public static String getHarvestLevelName (int num)
	{
		switch (num)
		{
		case 0:
			return (StatCollector.translateToLocal("gui.partcrafter.mining1"));
		case 1:
			return (StatCollector.translateToLocal("gui.partcrafter.mining2"));
		case 2:
			return (StatCollector.translateToLocal("gui.partcrafter.mining3"));
		case 3:
			return (StatCollector.translateToLocal("gui.partcrafter.mining4"));
		case 4:
			return (StatCollector.translateToLocal("gui.partcrafter.mining5"));
		case 5:
			return (StatCollector.translateToLocal("gui.partcrafter.mining6"));
		default:
			return String.valueOf(num);
		}
	}
	
	// Taken from tconstruct.library.tools.ToolCore
	public static String getReinforcedString(int reinforced)
	{
		if (reinforced > 9)
			return "Unbreakable";
		String ret = "Reinforced ";
		switch (reinforced)
		{
		case 1:
			ret += "I";
			break;
		case 2:
			ret += "II";
			break;
		case 3:
			ret += "III";
			break;
		case 4:
			ret += "IV";
			break;
		case 5:
			ret += "V";
			break;
		case 6:
			ret += "VI";
			break;
		case 7:
			ret += "VII";
			break;
		case 8:
			ret += "VIII";
			break;
		case 9:
			ret += "IX";
			break;
		default:
			ret += "X";
			break;
		}
		return ret;
	}
	
	// Taken from tconstruct.library.tools.ToolCore
	public static String getDamageString(int attack)
	{
		String heart = attack == 2 ? StatCollector.translateToLocal("gui.partcrafter8") : StatCollector.translateToLocal("gui.partcrafter9");
		if (attack % 2 == 0)
			return attack / 2 + heart;
		else
			return attack / 2f + heart;
	}
	
	public static String getModifierString(float modifier)
	{
		return modifier+"x";
	}
	
	public static String getSpeedString(int speed)
	{
		return df.format(speed / 100f);
	}
	
	public static String getAccuracyString(float accuracy)
	{
		return df.format(accuracy - 4)+"%";
	}
	
	public static String getDrawSpeedString(int drawSpeed)
	{
		return df.format(drawSpeed / 20f)+"s";
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
		
		String heart = StatCollector.translateToLocal("gui.partcrafter9");
		return df.format(minAttack / 2f) + "-" + df.format(maxAttack / 2f) + heart;
	}
}
