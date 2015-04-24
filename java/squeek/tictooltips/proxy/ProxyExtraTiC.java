package squeek.tictooltips.proxy;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import squeek.tictooltips.ModTiCTooltips;
import squeek.tictooltips.helpers.ToolPartHelper;

public class ProxyExtraTiC
{

	private static Class<?> ExtraTiCPartsHandler;
	public static List<Item> extraTiCParts = new ArrayList<Item>();
	
	public static void init()
	{
		registerParts();
	}

	public static boolean registerParts()
	{
		try
		{
			ExtraTiCPartsHandler = Class.forName("glassmaker.extratic.common.PartsHandler");

			// tool heads
			Item part = (Item) ExtraTiCPartsHandler.getDeclaredField("PICKAXE_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.toolHeads.add(part);
			
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("SHOVEL_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.toolHeads.add(part);

			part = (Item) ExtraTiCPartsHandler.getDeclaredField("EXCAVATOR_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.toolHeads.add(part);
			
			// weapon mining heads
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("HAMMER_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponMiningHeads.add(part);

			// weapon tool heads
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("AXE_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponToolHeads.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("SCYTHE_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponToolHeads.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("LUMBERAXE_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponToolHeads.add(part);

			// weapon heads
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("SWORD_BLADE").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("LARGE_SWORD_BLADE").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("KNIFE_BLADE").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("FRYPAN_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("BATTLE_SIGN_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponHeads.add(part);
			
			// weapon guards
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("CROSSBAR").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponGuards.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("MEDIUM_GUARD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponGuards.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("LARGE_GUARD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.weaponGuards.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("FULL_GUARD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.fullWeaponGuards.add(part);

			// bindings
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("BINDING").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.bindings.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("TOUGHBIND").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.toughBindings.add(part);

			// rods
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("TOOLROD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.rods.add(part);
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("TOUGHROD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.rods.add(part);

			// plates
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("LARGEPLATE").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.plates.add(part);

			// shards
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("CHUNK").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.shards.add(part);

			// arrowheads
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("ARROWHEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.arrowHeads.add(part);

			// arrow rods
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("TOOLROD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.arrowRods.add(part);

			// chisel heads
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("CHISEL_HEAD").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.chisels.add(part);

			// parts added in TiC 1.8.0
			part = (Item) ExtraTiCPartsHandler.getDeclaredField("SHURIKEN").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.shurikenParts.add(part);

			part = (Item) ExtraTiCPartsHandler.getDeclaredField("BOW_LIMB").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.bowLimbs.add(part);

			part = (Item) ExtraTiCPartsHandler.getDeclaredField("CROSSBOW_LIMB").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.crossbowLimbs.add(part);

			part = (Item) ExtraTiCPartsHandler.getDeclaredField("CROSSBOW_BODY").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.crossbowBodies.add(part);

			part = (Item) ExtraTiCPartsHandler.getDeclaredField("BOLT").get(null);
			extraTiCParts.add(part);
			ToolPartHelper.boltParts.add(part);
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.error("Failed to register Extra TiC tool parts: " + e.toString());
			return false;
		}
		
		return true;
	}

}
