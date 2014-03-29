package squeek.tictooltips.proxy;

import net.minecraft.item.Item;
import squeek.tictooltips.helpers.ToolPartHelper;

public class ProxyExtraTiC
{

	private static Class<?> ExtraTiCPartsHandler;

	public static void registerParts()
	{
		try
		{
			ExtraTiCPartsHandler = Class.forName("glassmaker.extratic.common.PartsHandler");

			ToolPartHelper.toolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("PICKAXE_HEAD").get(null));
			ToolPartHelper.toolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("SHOVEL_HEAD").get(null));
			ToolPartHelper.toolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("EXCAVATOR_HEAD").get(null));
			ToolPartHelper.toolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("HAMMER_HEAD").get(null));

			ToolPartHelper.weaponToolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("AXE_HEAD").get(null));
			ToolPartHelper.weaponToolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("SCYTHE_HEAD").get(null));
			ToolPartHelper.weaponToolHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("LUMBERAXE_HEAD").get(null));

			ToolPartHelper.weaponHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("SWORD_BLADE").get(null));
			ToolPartHelper.weaponHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("LARGE_SWORD_BLADE").get(null));
			ToolPartHelper.weaponHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("KNIFE_BLADE").get(null));
			ToolPartHelper.weaponHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("FRYPAN_HEAD").get(null));
			ToolPartHelper.weaponHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("BATTLE_SIGN_HEAD").get(null));

			ToolPartHelper.bindings.add((Item) ExtraTiCPartsHandler.getDeclaredField("BINDING").get(null));
			ToolPartHelper.bindings.add((Item) ExtraTiCPartsHandler.getDeclaredField("TOUGHBIND").get(null));

			ToolPartHelper.rods.add((Item) ExtraTiCPartsHandler.getDeclaredField("TOOLROD").get(null));
			ToolPartHelper.rods.add((Item) ExtraTiCPartsHandler.getDeclaredField("TOUGHROD").get(null));

			ToolPartHelper.plates.add((Item) ExtraTiCPartsHandler.getDeclaredField("LARGEPLATE").get(null));

			ToolPartHelper.shards.add((Item) ExtraTiCPartsHandler.getDeclaredField("CHUNK").get(null));

			ToolPartHelper.arrowHeads.add((Item) ExtraTiCPartsHandler.getDeclaredField("ARROWHEAD").get(null));

			ToolPartHelper.arrowRods.add((Item) ExtraTiCPartsHandler.getDeclaredField("TOOLROD").get(null));

			ToolPartHelper.chisels.add((Item) ExtraTiCPartsHandler.getDeclaredField("CHISEL_HEAD").get(null));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}
