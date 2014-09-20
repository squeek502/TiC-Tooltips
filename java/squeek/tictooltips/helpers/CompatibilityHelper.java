package squeek.tictooltips.helpers;

import java.lang.reflect.Method;
import tconstruct.library.tools.ToolMaterial;

// for backwards-compatibility-related things
public class CompatibilityHelper
{
	public static Method localizedMaterialAbility = null;
	public static Method localizedMaterialName = null;
	static
	{
		try
		{
			localizedMaterialAbility = Class.forName("tconstruct.library.tools.ToolMaterial").getDeclaredMethod("localizedAbility");
		}
		catch (Exception e)
		{
		}
		try
		{
			localizedMaterialName = Class.forName("tconstruct.library.tools.ToolMaterial").getDeclaredMethod("localizedName");
		}
		catch (Exception e)
		{
		}
	}

	public static String getLocalizedAbility(ToolMaterial mat)
	{
		try
		{
			return localizedMaterialAbility != null ? (String) localizedMaterialAbility.invoke(mat) : mat.ability();
		}
		catch (Exception e)
		{
			return "<error>";
		}
	}

	@SuppressWarnings("deprecation")
	public static String getLocalizedName(ToolMaterial mat)
	{
		try
		{
			return localizedMaterialName != null ? (String) localizedMaterialName.invoke(mat) : mat.displayName;
		}
		catch (Exception e)
		{
			return "<error>";
		}
	}
}
