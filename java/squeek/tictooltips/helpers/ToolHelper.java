package squeek.tictooltips.helpers;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class ToolHelper {

	public static boolean hasToolTag(ItemStack itemStack)
	{
		return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("InfiTool");
	}
	
	public static NBTTagCompound getToolTag(ItemStack tool)
	{
		NBTTagCompound tag;
		tag = tool.getTagCompound().getCompoundTag("InfiTool");
		return tag;
	}
	
	public static ToolMaterial getHeadMaterial(NBTTagCompound toolTag)
	{
		ToolMaterial mat = null;
		int matID = toolTag.getInteger("Head");
		if (matID >= 0)
		{
			mat = TConstructRegistry.getMaterial(matID);
		}
		return mat;
	}

	public static boolean hasToolCategory(ToolCore tool, String searchCategory)
	{
		for (String category : tool.toolCategories())
		{
			if (category.equals(searchCategory))
				return true;
		}
		return false;
	}
	
	public static boolean isWeaponTool(ToolCore tool) { return hasToolCategory(tool, "weapon"); }
	public static boolean isBowTool(ToolCore tool) { return hasToolCategory(tool, "bow"); }
	public static boolean isAmmoTool(ToolCore tool) { return hasToolCategory(tool, "ammo"); }
	public static boolean isDualHarvestTool(ToolCore tool) { return hasToolCategory(tool, "dualharvest"); }
	public static boolean isHarvestTool(ToolCore tool) { return hasToolCategory(tool, "harvest"); }
	public static boolean isUtilityTool(ToolCore tool) { return hasToolCategory(tool, "utility"); }
	
	public static int getUsedDurability(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Damage");
	}

	public static int getMaxDurability(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("TotalDurability");
	}
	
	public static float getStonebound(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("Shoddy");
	}
	
	public static int getRawDamage(ToolCore tool, NBTTagCompound toolTag)
	{
		int rawDamage = toolTag.getInteger("Attack") - tool.getDamageVsEntity(null);
		return rawDamage;
	}

	public static int getDamage(ToolCore tool, NBTTagCompound toolTag)
	{
		int attack = toolTag.getInteger("Attack");
		attack += getStoneboundDamage(toolTag);
		attack *= tool.getDamageModifier();
		if (attack < 1)
			attack = 1;
		
		return attack;
	}
	
	public static float getStoneboundDamage(NBTTagCompound toolTag)
	{
		return (float) Math.log(getUsedDurability(toolTag) / 72f + 1) * -2 * getStonebound(toolTag);
	}
	
	public static float getStoneboundSpeed(NBTTagCompound toolTag)
	{
		return (float) Math.log(getUsedDurability(toolTag) / 72f + 1) * 2 * getStonebound(toolTag);
	}
	
	public static int getDrawSpeed(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("DrawSpeed");
	}

	public static float getArrowSpeedModifier(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("FlightSpeed");
	}
	
	public static int getAmmoDamage(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Attack");
	}

	public static float getWeight(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("Mass");
	}

	public static float getAccuracy(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("Accuracy");
	}
	
	public static int getPrimaryMiningSpeed(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("MiningSpeed");
	}
	
	public static int getSecondaryMiningSpeed(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("MiningSpeed2");
	}
	
	public static int getTotalMiningSpeed(NBTTagCompound toolTag)
	{
		int mineSpeed = toolTag.getInteger("MiningSpeed");
		int heads = 1;

		if (toolTag.hasKey("MiningSpeed2"))
		{
			mineSpeed += toolTag.getInteger("MiningSpeed2");
			heads++;
		}

		if (toolTag.hasKey("MiningSpeedHandle"))
		{
			mineSpeed += toolTag.getInteger("MiningSpeedHandle");
			heads++;
		}

		if (toolTag.hasKey("MiningSpeedExtra"))
		{
			mineSpeed += toolTag.getInteger("MiningSpeedExtra");
			heads++;
		}

		return mineSpeed / heads;
	}
	
	public static int getPrimaryHarvestLevel(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("HarvestLevel");
	}

	public static int getSecondaryHarvestLevel(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("HarvestLevel2");
	}
	
}
