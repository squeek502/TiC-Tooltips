package squeek.tictooltips.helpers;

import java.lang.reflect.Method;
import tconstruct.library.tools.ToolMaterial;

// for backwards-compatibility-related things
public class CompatibilityHelper
{
	public static Method localizedAbility = null;
	static
	{
		try
		{
			localizedAbility = Class.forName("tconstruct.library.tools.ToolMaterial").getDeclaredMethod("localizedAbility");
		}
		catch (Exception e)
		{
		}
	}

	public static String getLocalizedAbility(ToolMaterial mat)
	{
		try
		{
			return localizedAbility != null ? (String) localizedAbility.invoke(mat) : mat.ability();
		}
		catch (Exception e)
		{
			return "<error>";
		}
	}
}
