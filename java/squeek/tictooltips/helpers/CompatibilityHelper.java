package squeek.tictooltips.helpers;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.FletchingMaterial;
import tconstruct.library.tools.FletchlingLeafMaterial;
import tconstruct.library.tools.ToolMaterial;

// for backwards-compatibility-related things
public class CompatibilityHelper
{
	public static String getLocalizedAbility(ToolMaterial mat)
	{
		return mat.ability();
	}

	public static String getLocalizedName(ToolMaterial mat)
	{
		return mat.localizedName();
	}

	public static FletchingMaterial getFletchingMaterial(int matID)
	{
		FletchingMaterial fletching = (FletchingMaterial) TConstructRegistry.getCustomMaterial(matID, FletchingMaterial.class);

		if (fletching == null)
			fletching = (FletchingMaterial) TConstructRegistry.getCustomMaterial(matID, FletchlingLeafMaterial.class);

		return fletching;
	}
}
