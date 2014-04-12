package squeek.tictooltips.proxy;

import net.minecraft.item.Item;

public interface IModPartHandler
{
	public String getPartName(Item part);
	public boolean isModdedPart(Item part);
}
