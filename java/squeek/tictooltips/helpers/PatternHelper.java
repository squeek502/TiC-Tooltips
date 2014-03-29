package squeek.tictooltips.helpers;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import tconstruct.items.Pattern;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.CustomMaterial;

public class PatternHelper
{

	public static boolean isBowstringPattern(Item item, int meta)
	{
		return getPatternName(item, meta).equals("bowstring");
	}

	public static boolean isFletchingPattern(Item item, int meta)
	{
		return getPatternName(item, meta).equals("fletching");
	}

	public static String getPatternName(Item item, int meta)
	{
		if (item instanceof Pattern)
		{
			return ((Pattern) item).unlocalizedNames[meta];
		}
		return "";
	}

	public static List<String> getValidCustomMaterialsOfType(Class<? extends CustomMaterial> matClass)
	{
		List<String> validMats = new ArrayList<String>();
		for (CustomMaterial customMat : TConstructRegistry.customMaterials)
		{
			customMat = TConstructRegistry.getCustomMaterial(customMat.materialID, matClass);
			if (customMat != null && !validMats.contains(customMat.input.getDisplayName()))
			{
				validMats.add(customMat.input.getDisplayName());
			}
		}
		return validMats;
	}

}
