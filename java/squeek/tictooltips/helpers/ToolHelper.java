package squeek.tictooltips.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.items.tools.FryingPan;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.armor.ArmorCore;
import tconstruct.library.tools.HarvestTool;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.weaponry.AmmoWeapon;
import tconstruct.library.weaponry.IAmmo;
import tconstruct.tools.TinkerTools;

public class ToolHelper
{
	public static final int INFINITE_DURABILITY = Integer.MAX_VALUE;

	public static void init()
	{
	}

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
		for (String category : tool.getTraits())
		{
			if (category.equals(searchCategory))
				return true;
		}
		return false;
	}

	public static boolean isWeapon(ToolCore tool)
	{
		return hasToolCategory(tool, "weapon");
	}

	public static boolean isBow(ToolCore tool)
	{
		return hasToolCategory(tool, "bow");
	}

	public static boolean isAmmo(ToolCore tool)
	{
		return hasToolCategory(tool, "ammo");
	}

	public static boolean isDualHarvestTool(ToolCore tool)
	{
		return hasToolCategory(tool, "dualharvest");
	}

	public static boolean isHarvestTool(ToolCore tool)
	{
		return hasToolCategory(tool, "harvest");
	}

	public static boolean isUtilityTool(ToolCore tool)
	{
		return hasToolCategory(tool, "utility");
	}

	public static boolean isThrown(ToolCore tool)
	{
		return hasToolCategory(tool, "thrown");
	}

	public static boolean isProjectile(ToolCore tool)
	{
		return hasToolCategory(tool, "projectile");
	}

	public static boolean hasAmmoCount(Item item)
	{
		return item instanceof IAmmo;
	}

	public static boolean hasDurability(Item item)
	{
		return !hasAmmoCount(item) && item instanceof ToolCore || item instanceof ArmorCore;
	}

	public static int getUsedDurability(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Damage");
	}

	public static int getMaxDurability(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("TotalDurability");
	}

	public static int getEffectiveDurability(NBTTagCompound toolTag)
	{
		int reinforcedLevel = getReinforcedLevel(toolTag);

		if (isUnbreakable(reinforcedLevel))
			return INFINITE_DURABILITY;
		else
			return (int) (getMaxDurability(toolTag) / (1f - reinforcedLevel / 10f));
	}

	public static int getReinforcedLevel(NBTTagCompound toolTag)
	{
		return toolTag.getInteger("Unbreaking");
	}

	public static boolean isUnbreakable(NBTTagCompound toolTag)
	{
		return isUnbreakable(getReinforcedLevel(toolTag));
	}

	public static boolean isUnbreakable(int reinforcedLevel)
	{
		return reinforcedLevel >= 10;
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

	public static int getDamage(ToolCore tool, NBTTagCompound toolTag, float damageModifier)
	{
		float attack = toolTag.getInteger("Attack") * damageModifier;
		attack += getShoddinessDamageBonus(tool, toolTag);
		attack *= tool.getDamageModifier();
		if (tool instanceof AmmoWeapon)
		{
			attack *= ((AmmoWeapon) tool).getProjectileSpeed();
		}
		if (attack < 1)
			attack = 1;

		return (int) attack;
	}

	public static int getDamage(ToolCore tool, NBTTagCompound toolTag)
	{
		return getDamage(tool, toolTag, 1f);
	}

	public static int getSprintDamage(ToolCore tool, NBTTagCompound toolTag)
	{
		int attack = getDamage(tool, toolTag);
		float lunge = tool.chargeAttack();
		if (lunge > 1f)
		{
			attack *= lunge;
		}
		return attack;
	}

	public static int[] getSmiteDamageRange(ToolCore tool, NBTTagCompound toolTag)
	{
		int staticBonus = 0;
		int variableBonus = 0;
		if (TinkerTools.hammer.getClass().isInstance(tool))
		{
			int level = 2;
			staticBonus += level * 2;
			variableBonus += level * 2 + 1;
		}
		if (toolTag.hasKey("ModSmite"))
		{
			int[] array = toolTag.getIntArray("ModSmite");
			int base = array[0] / 18;
			staticBonus += 1 + base;
			variableBonus += base + 1;
		}
		return new int[]{staticBonus, staticBonus + variableBonus};
	}

	public static int[] getAntiSpiderDamageRange(ToolCore tool, NBTTagCompound toolTag)
	{
		int staticBonus = 0;
		int variableBonus = 0;
		if (toolTag.hasKey("ModAntiSpider"))
		{
			int[] array = toolTag.getIntArray("ModAntiSpider");
			int base = array[0] / 2;
			staticBonus += 1 + base;
			variableBonus += base + 1;
		}
		return new int[]{staticBonus, staticBonus + variableBonus};
	}

	public static int getBurnDuration(ToolCore tool, NBTTagCompound toolTag)
	{
		int burnDuration = 0;
		if (toolTag.hasKey("Fiery"))
		{
			burnDuration += toolTag.getInteger("Fiery") / 5 + 1;
		}
		if (toolTag.getBoolean("Lava"))
		{
			burnDuration += 3;
		}
		return burnDuration;
	}

	public static float getChanceToBehead(ToolCore tool, NBTTagCompound toolTag)
	{
		float chanceToBehead = toolTag.getInteger("Beheading");
		if (TinkerTools.cleaver.getClass().isInstance(tool))
			chanceToBehead += 2;
		chanceToBehead = chanceToBehead / 10;
		return Math.min(1, chanceToBehead);
	}

	public static float getKnockback(ToolCore tool, NBTTagCompound toolTag)
	{
		float knockback = 0f;
		if (tool instanceof FryingPan)
			knockback += 1.7f;
		if (toolTag.hasKey("Knockback"))
		{
			float level = toolTag.getFloat("Knockback");
			knockback += level;
		}
		return knockback;
	}

	public static float getSprintKnockback(ToolCore tool, NBTTagCompound toolTag)
	{
		float knockback = getKnockback(tool, toolTag);
		knockback++;
		float lunge = tool.chargeAttack();
		if (lunge > 1f)
		{
			knockback += lunge - 1.0f;
		}
		return knockback;
	}

	public static float getShoddinessDamageBonus(ToolCore tool, NBTTagCompound toolTag)
	{
		return -getShoddinessBonus(getUsedDurability(toolTag), getStonebound(toolTag), getShoddinessModifierConstant(tool));
	}

	public static float getMaxShoddinessDamageBonus(ToolCore tool, NBTTagCompound toolTag)
	{
		return -getShoddinessBonus(getMaxDurability(toolTag), getStonebound(toolTag), getShoddinessModifierConstant(tool));
	}

	public static float getShoddinessModifierConstant(ToolCore tool)
	{
		if (tool == null || !(tool instanceof HarvestTool))
			return 72f;

		return ((HarvestTool) tool).stoneboundModifier();
	}

	public static float getShoddinessBonus(int usedDurability, float shoddiness, float modifierConstant)
	{
		return (float) Math.log(usedDurability / modifierConstant + 1) * 2 * shoddiness;
	}

	public static float getShoddinessSpeedBonus(ToolCore tool, NBTTagCompound toolTag)
	{
		return getShoddinessBonus(getUsedDurability(toolTag), getStonebound(toolTag), getShoddinessModifierConstant(tool));
	}

	public static float getMaxShoddinessSpeedBonus(ToolCore tool, NBTTagCompound toolTag)
	{
		return getShoddinessBonus(getMaxDurability(toolTag), getStonebound(toolTag), getShoddinessModifierConstant(tool));
	}

	public static int getDrawSpeed(ToolCore tool, NBTTagCompound toolTag)
	{
		return toolTag.getInteger("DrawSpeed");
	}

	public static float getArrowSpeed(ToolCore tool, NBTTagCompound toolTag)
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

	public static float getBreakChance(NBTTagCompound toolTag)
	{
		return toolTag.getFloat("BreakChance");
	}

	public static float getMiningSpeedModifier(ToolCore tool)
	{
		if (tool == null || !(tool instanceof HarvestTool))
			return 1f;

		return ((HarvestTool) tool).breakSpeedModifier();
	}

	public static int getPrimaryMiningSpeed(ToolCore tool, NBTTagCompound toolTag)
	{
		return (int) (toolTag.getInteger("MiningSpeed") * getMiningSpeedModifier(tool));
	}

	public static int getSecondaryMiningSpeed(ToolCore tool, NBTTagCompound toolTag)
	{
		return (int) (toolTag.getInteger("MiningSpeed2") * getMiningSpeedModifier(tool));
	}

	public static int getTotalMiningSpeed(ToolCore tool, NBTTagCompound toolTag)
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

		return (int) ((float) mineSpeed / heads * getMiningSpeedModifier(tool));
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
