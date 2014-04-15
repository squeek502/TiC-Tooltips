package squeek.tictooltips.helpers;

import tconstruct.library.armor.ArmorCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ArmorHelper
{

	public static boolean hasArmorTag(ItemStack itemStack)
	{
		return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(ArmorCore.SET_NAME);
	}

	public static NBTTagCompound getArmorTag(ItemStack armor)
	{
		NBTTagCompound tag;
		tag = armor.getTagCompound().getCompoundTag(ArmorCore.SET_NAME);
		return tag;
	}

}
