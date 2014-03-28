package squeek.tictooltips.helpers;

import net.minecraft.util.EnumChatFormatting;

public class ColorHelper {
	private static final EnumChatFormatting colorRange[] = {
		EnumChatFormatting.DARK_RED,
		EnumChatFormatting.RED,
		EnumChatFormatting.GOLD,
		EnumChatFormatting.YELLOW,
		EnumChatFormatting.DARK_GREEN,
		EnumChatFormatting.GREEN,
		EnumChatFormatting.AQUA
	};
	
	public static String getRelativeColor(double val, double min, double max)
	{
		if (min == max)
			return EnumChatFormatting.RESET.toString();
		else if ((max > min && val > max) || (min > max && val < max))
			return EnumChatFormatting.WHITE+EnumChatFormatting.BOLD.toString();
		else if ((max > min && val < min) || (min > max && val > min))
			return colorRange[0]+EnumChatFormatting.BOLD.toString();
		
		int index = (int)(((val - min) / (max - min)) * (colorRange.length-1));
		return colorRange[Math.max(0, Math.min(colorRange.length-1, index))].toString();
	}
}