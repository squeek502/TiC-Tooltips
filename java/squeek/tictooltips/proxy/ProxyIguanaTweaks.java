package squeek.tictooltips.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import squeek.tictooltips.ModTiCTooltips;
import squeek.tictooltips.helpers.ToolPartHelper;
import tconstruct.common.TContent;
import tconstruct.items.ToolPart;

public class ProxyIguanaTweaks
{
	private static Class<?> IguanaTweaksTConstruct = null;
	private static Method proxyGetHarvestLevelName;
	private static List<IModPartHandler> modPartHandlers = new ArrayList<IModPartHandler>();
	
	public static void registerModPartHandler(IModPartHandler modPartHandler)
	{
		modPartHandlers.add(modPartHandler);
	}
	
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
	
	public static class ProxyDefaultToolHandler implements IModPartHandler
	{
		public static List<Item> defaultToolParts = null;

		@Override
		public String getPartName(Item part)
		{
			return ((ToolPart) part).partName;
		}

		@Override
		public boolean isModdedPart(Item part)
		{
			return defaultToolParts.contains(part);
		}
		
	}
	
	public static void init()
	{
		try
		{
			IguanaTweaksTConstruct = Class.forName("iguanaman.iguanatweakstconstruct.IguanaTweaksTConstruct");
			proxyGetHarvestLevelName = IguanaTweaksTConstruct.getDeclaredMethod("getHarvestLevelName", int.class);
			if (registerParts())
				ProxyIguanaTweaks.registerModPartHandler(new ProxyDefaultToolHandler());
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.warning("Failed to load Iguana Tweaks integration: " + e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean registerParts()
	{
		try
		{
			ProxyDefaultToolHandler.defaultToolParts = (List<Item>) IguanaTweaksTConstruct.getDeclaredField("toolParts").get(null);

			ToolPartHelper.toolHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.pickaxeHead.ordinal()));
			ToolPartHelper.toolHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.shovelHead.ordinal()));
			ToolPartHelper.toolHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.excavatorHead.ordinal()));
			
			ToolPartHelper.weaponMiningHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.hammerHead.ordinal()));

			ToolPartHelper.weaponToolHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.hatchetHead.ordinal()));
			ToolPartHelper.weaponToolHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.scytheBlade.ordinal()));
			ToolPartHelper.weaponToolHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.broadAxeHead.ordinal()));

			ToolPartHelper.weaponHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.swordBlade.ordinal()));
			ToolPartHelper.weaponHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.largeSwordBlade.ordinal()));
			ToolPartHelper.weaponHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.knifeBlade.ordinal()));
			ToolPartHelper.weaponHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.frypanHead.ordinal()));
			ToolPartHelper.weaponHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.signHead.ordinal()));

			ToolPartHelper.weaponGuards.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.crossbar.ordinal()));
			ToolPartHelper.weaponGuards.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.handGuard.ordinal()));
			ToolPartHelper.weaponGuards.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.wideGuard.ordinal()));
			ToolPartHelper.weaponGuards.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.fullGuard.ordinal()));

			ToolPartHelper.bindings.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.binding.ordinal()));
			ToolPartHelper.bindings.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.toughBinding.ordinal()));

			ToolPartHelper.rods.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.toolRod.ordinal()));
			ToolPartHelper.rods.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.toughRod.ordinal()));

			ToolPartHelper.plates.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.largePlate.ordinal()));

			//ToolPartHelper.shards.add(defaultToolParts.get(toolPartIndexes.toolShard.ordinal()));

			ToolPartHelper.arrowHeads.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.arrowhead.ordinal()));

			ToolPartHelper.arrowRods.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.toolRod.ordinal()));

			ToolPartHelper.chisels.add(ProxyDefaultToolHandler.defaultToolParts.get(toolPartIndexes.chiselHead.ordinal()));
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.warning("Failed to register Iguana Tweaks tool parts: " + e.toString());
			return false;
		}
		
		return true;
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
		}
		
		return harvestLevelName;
	}
	
	public static List<String> getPartTooltip(ItemStack itemStack, EntityPlayer player, boolean par4)
	{
		List<String> toolTip = new ArrayList<String>();
		
		Item item = itemStack.getItem();
		Item iguanaPart = null;
		
		for (IModPartHandler modPartHandler : modPartHandlers)
		{
			if (modPartHandler.isModdedPart(item))
			{
				iguanaPart = findCorrespondingIguanaToolPart(modPartHandler.getPartName(item));
				if (iguanaPart != null)
					break;
			}
		}
		
		if (iguanaPart != null)
			iguanaPart.addInformation(itemStack, player, toolTip, par4);
		
		return toolTip;
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

	public static String getPartName(Item part)
	{
		for (IModPartHandler modPartHandler : modPartHandlers)
		{
			if (modPartHandler.isModdedPart(part))
				return modPartHandler.getPartName(part);
		}
		return "";
	}

	public static boolean isModdedPart(Item part)
	{
		for (IModPartHandler modPartHandler : modPartHandlers)
		{
			if (modPartHandler.isModdedPart(part))
				return true;
		}
		return false;
	}
}
