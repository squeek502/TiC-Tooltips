package squeek.tictooltips.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import squeek.tictooltips.helpers.ToolPartHelper;
import tconstruct.common.TContent;
import tconstruct.items.ToolPart;

public class ProxyIguanaTweaks
{
	private static Class<?> IguanaTweaksTConstruct = null;
	public static List<Item> defaultToolParts = null;
	private static Method proxyGetHarvestLevelName;
	
	private static enum toolPartIndexes { 
		toolRod, pickaxeHead, shovelHead, hatchetHead,
		binding, toughBinding, toughRod, largePlate,
		swordBlade, wideGuard, handGuard, crossbar,
		knifeBlade, fullGuard, frypanHead, signHead,
		chiselHead, scytheBlade, broadAxeHead, excavatorHead,
		largeSwordBlade, hammerHead, bowstring, fletching,
		arrowhead
	};
	
	public static List<Item> iguanaToolParts = Arrays.asList(
     	TContent.toolRod, TContent.pickaxeHead, TContent.shovelHead, TContent.hatchetHead,
     	TContent.binding, TContent.toughBinding, TContent.toughRod, TContent.largePlate,
     	TContent.swordBlade, TContent.wideGuard, TContent.handGuard, TContent.crossbar,
     	TContent.knifeBlade, TContent.fullGuard, TContent.frypanHead, TContent.signHead,
     	TContent.chiselHead, TContent.scytheBlade, TContent.broadAxeHead, TContent.excavatorHead,
     	TContent.largeSwordBlade, TContent.hammerHead, TContent.bowstring, TContent.fletching,
     	TContent.arrowhead
	);
	
	public static void init()
	{
		try
		{
			IguanaTweaksTConstruct = Class.forName("iguanaman.iguanatweakstconstruct.IguanaTweaksTConstruct");
			proxyGetHarvestLevelName = IguanaTweaksTConstruct.getDeclaredMethod("getHarvestLevelName", int.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void registerParts()
	{
		try
		{
			defaultToolParts = (List<Item>) IguanaTweaksTConstruct.getDeclaredField("toolParts").get(null);

			ToolPartHelper.toolHeads.add(defaultToolParts.get(toolPartIndexes.pickaxeHead.ordinal()));
			ToolPartHelper.toolHeads.add(defaultToolParts.get(toolPartIndexes.shovelHead.ordinal()));
			ToolPartHelper.toolHeads.add(defaultToolParts.get(toolPartIndexes.excavatorHead.ordinal()));
			
			ToolPartHelper.weaponMiningHeads.add(TContent.hammerHead);

			ToolPartHelper.weaponToolHeads.add(defaultToolParts.get(toolPartIndexes.hatchetHead.ordinal()));
			ToolPartHelper.weaponToolHeads.add(defaultToolParts.get(toolPartIndexes.scytheBlade.ordinal()));
			ToolPartHelper.weaponToolHeads.add(defaultToolParts.get(toolPartIndexes.broadAxeHead.ordinal()));

			ToolPartHelper.weaponHeads.add(defaultToolParts.get(toolPartIndexes.swordBlade.ordinal()));
			ToolPartHelper.weaponHeads.add(defaultToolParts.get(toolPartIndexes.largeSwordBlade.ordinal()));
			ToolPartHelper.weaponHeads.add(defaultToolParts.get(toolPartIndexes.knifeBlade.ordinal()));
			ToolPartHelper.weaponHeads.add(defaultToolParts.get(toolPartIndexes.frypanHead.ordinal()));
			ToolPartHelper.weaponHeads.add(defaultToolParts.get(toolPartIndexes.signHead.ordinal()));

			ToolPartHelper.weaponGuards.add(defaultToolParts.get(toolPartIndexes.crossbar.ordinal()));
			ToolPartHelper.weaponGuards.add(defaultToolParts.get(toolPartIndexes.handGuard.ordinal()));
			ToolPartHelper.weaponGuards.add(defaultToolParts.get(toolPartIndexes.wideGuard.ordinal()));
			ToolPartHelper.weaponGuards.add(defaultToolParts.get(toolPartIndexes.fullGuard.ordinal()));

			ToolPartHelper.bindings.add(defaultToolParts.get(toolPartIndexes.binding.ordinal()));
			ToolPartHelper.bindings.add(defaultToolParts.get(toolPartIndexes.toughBinding.ordinal()));

			ToolPartHelper.rods.add(defaultToolParts.get(toolPartIndexes.toolRod.ordinal()));
			ToolPartHelper.rods.add(defaultToolParts.get(toolPartIndexes.toughRod.ordinal()));

			ToolPartHelper.plates.add(defaultToolParts.get(toolPartIndexes.largePlate.ordinal()));

			//ToolPartHelper.shards.add(defaultToolParts.get(toolPartIndexes.toolShard.ordinal()));

			ToolPartHelper.arrowHeads.add(defaultToolParts.get(toolPartIndexes.arrowhead.ordinal()));

			ToolPartHelper.arrowRods.add(defaultToolParts.get(toolPartIndexes.toolRod.ordinal()));

			ToolPartHelper.chisels.add(defaultToolParts.get(toolPartIndexes.chiselHead.ordinal()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	public static String getHarvestLevelName(int num)
	{
		String harvestLevelName = "<Unknown>";
		
		try
		{
			harvestLevelName = (String) proxyGetHarvestLevelName.invoke(IguanaTweaksTConstruct, num);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return harvestLevelName;
	}
	
	public static List<String> getPartTooltip(ItemStack itemStack, EntityPlayer player, boolean par4)
	{
		List<String> toolTip = new ArrayList<String>();
		
		Item item = itemStack.getItem();
		Item iguanaPart = null;
		
		if (item instanceof ToolPart)
			iguanaPart = findCorrespondingIguanaToolPart((ToolPart) item);
		else
			iguanaPart = findCorrespondingIguanaToolPart(ProxyExtraTiC.getPartName(item));
		
		if (iguanaPart != null)
			iguanaPart.addInformation(itemStack, player, toolTip, par4);
		
		return toolTip;
	}
	
	public static Item findCorrespondingIguanaToolPart(ToolPart part)
	{
		return findCorrespondingIguanaToolPart(part.partName);
	}

	public static Item findCorrespondingIguanaToolPart(String partName)
	{
		for (Item iguanaPart : iguanaToolParts)
		{
			if (iguanaPart instanceof ToolPart && ((ToolPart) iguanaPart).partName.equals(partName))
				return iguanaPart;
		}
		
		return null;
	}
}
