package squeek.tictooltips.proxy;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import squeek.tictooltips.ModTiCTooltips;
import squeek.tictooltips.helpers.ToolPartHelper;

public class ProxyMariculture
{

	private static Class<?> MariculturePartsHandler;
	public static List<Item> MaricultureParts = new ArrayList<Item>();

	public static void init()
	{
		registerParts();
	}

	public static boolean registerParts()
	{
		try
		{
			MariculturePartsHandler = Class.forName("mariculture.plugins.tconstruct.TitaniumTools");

			// tool heads
			Item part = (Item) MariculturePartsHandler.getDeclaredField("pickaxe_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.toolHeads.add(part);

			part = (Item) MariculturePartsHandler.getDeclaredField("shovel_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.toolHeads.add(part);

			part = (Item) MariculturePartsHandler.getDeclaredField("excavator_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.toolHeads.add(part);

			// weapon mining heads
			part = (Item) MariculturePartsHandler.getDeclaredField("hammer_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponMiningHeads.add(part);

			// weapon tool heads
			part = (Item) MariculturePartsHandler.getDeclaredField("axe_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponToolHeads.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("scythe_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponToolHeads.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("broad_axe_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponToolHeads.add(part);

			// weapon heads
			part = (Item) MariculturePartsHandler.getDeclaredField("sword_blade").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("large_sword_blade").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("knife_blade").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("frypan_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("battle_sign_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponHeads.add(part);

			// weapon guards
			part = (Item) MariculturePartsHandler.getDeclaredField("crossbar").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponGuards.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("hand_guard").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponGuards.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("large_guard").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.weaponGuards.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("full_guard").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.fullWeaponGuards.add(part);

			// bindings
			part = (Item) MariculturePartsHandler.getDeclaredField("binding").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.bindings.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("tough_binding").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.toughBindings.add(part);

			// rods
			part = (Item) MariculturePartsHandler.getDeclaredField("tool_rod").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.rods.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("tough_rod").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.rods.add(part);

			// plates
			part = (Item) MariculturePartsHandler.getDeclaredField("large_plate").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.plates.add(part);

			// shards
			part = (Item) MariculturePartsHandler.getDeclaredField("chunk").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.shards.add(part);

			// arrow rods
			part = (Item) MariculturePartsHandler.getDeclaredField("tool_rod").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.arrowRods.add(part);

			// chisel heads
			part = (Item) MariculturePartsHandler.getDeclaredField("chisel_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.chisels.add(part);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.error("Failed to register Mariculture tool parts: " + e.toString());
			return false;
		}
		
		return true;
	}

}
