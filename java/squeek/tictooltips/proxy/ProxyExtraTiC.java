package squeek.tictooltips.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import squeek.tictooltips.ModTiCTooltips;
import squeek.tictooltips.helpers.ToolPartHelper;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;

public class ProxyExtraTiC
{

	private static Class<?> ExtraTiCPartsHandler;
	public static List<Item> extraTiCParts = new ArrayList<Item>();
	public static Map<Item, Item> ticToExtraTiCParts = new HashMap<Item, Item>();

	public enum PartInfo
	{
		// tool heads
		PICKAXE_HEAD(TinkerTools.pickaxeHead, ToolPartHelper.toolHeads),
		SHOVEL_HEAD(TinkerTools.shovelHead, ToolPartHelper.toolHeads),
		EXCAVATOR_HEAD(TinkerTools.excavatorHead, ToolPartHelper.toolHeads),
		// weapon mining heads
		HAMMER_HEAD(TinkerTools.hammerHead, ToolPartHelper.weaponMiningHeads),
		// weapon tool heads
		AXE_HEAD(TinkerTools.hatchetHead, ToolPartHelper.weaponToolHeads),
		SCYTHE_HEAD(TinkerTools.scytheBlade, ToolPartHelper.weaponToolHeads),
		LUMBERAXE_HEAD(TinkerTools.broadAxeHead, ToolPartHelper.weaponToolHeads),
		// weapon heads
		SWORD_BLADE(TinkerTools.swordBlade, ToolPartHelper.weaponHeads),
		LARGE_SWORD_BLADE(TinkerTools.largeSwordBlade, ToolPartHelper.weaponHeads),
		KNIFE_BLADE(TinkerTools.knifeBlade, ToolPartHelper.weaponHeads),
		FRYPAN_HEAD(TinkerTools.frypanHead, ToolPartHelper.weaponHeads),
		BATTLE_SIGN_HEAD(TinkerTools.signHead, ToolPartHelper.weaponHeads),
		// weapon guards
		CROSSBAR(TinkerTools.crossbar, ToolPartHelper.weaponGuards),
		MEDIUM_GUARD(TinkerTools.handGuard, ToolPartHelper.weaponGuards),
		LARGE_GUARD(TinkerTools.wideGuard, ToolPartHelper.weaponGuards),
		FULL_GUARD(TinkerTools.fullGuard, ToolPartHelper.fullWeaponGuards),
		// bindings
		BINDING(TinkerTools.binding, ToolPartHelper.bindings),
		TOUGHBIND(TinkerTools.toughBinding, ToolPartHelper.toughBindings),
		// rods
		TOOLROD(TinkerTools.toolRod, ToolPartHelper.rods),
		TOUGHROD(TinkerTools.toughRod, ToolPartHelper.rods),
		// plates
		LARGEPLATE(TinkerTools.largePlate, ToolPartHelper.plates),
		// shards
		CHUNK(TinkerTools.toolShard, ToolPartHelper.shards),
		// arrowheads
		ARROWHEAD(TinkerWeaponry.arrowhead, ToolPartHelper.arrowHeads),
		// chisel heads
		CHISEL_HEAD(TinkerTools.chiselHead, ToolPartHelper.chisels),
		// parts added in TiC 1.8.0
		SHURIKEN(TinkerWeaponry.partShuriken, ToolPartHelper.shurikenParts),
		BOW_LIMB(TinkerWeaponry.partBowLimb, ToolPartHelper.bowLimbs),
		CROSSBOW_LIMB(TinkerWeaponry.partCrossbowLimb, ToolPartHelper.crossbowLimbs),
		CROSSBOW_BODY(TinkerWeaponry.partCrossbowBody, ToolPartHelper.crossbowBodies),
		BOLT(TinkerWeaponry.partBolt, ToolPartHelper.boltParts);

		public final Item ticPart;
		public final List<Item> relevantList;
		private Item partItem;

		PartInfo(Item ticPart, List<Item> relevantList)
		{
			this.ticPart = ticPart;
			this.relevantList = relevantList;
		}

		void setPartItem(Item partItem)
		{
			this.partItem = partItem;
			this.relevantList.add(partItem);
			extraTiCParts.add(partItem);
			ticToExtraTiCParts.put(ticPart, partItem);
		}

		public Item getPartItem()
		{
			return this.partItem;
		}
	}

	public static void init()
	{
		registerParts();
	}

	public static boolean registerParts()
	{
		try
		{
			ExtraTiCPartsHandler = Class.forName("glassmaker.extratic.common.PartsHandler");

			for (PartInfo partInfo : PartInfo.values())
			{
				Item part = (Item) ExtraTiCPartsHandler.getDeclaredField(partInfo.name()).get(null);
				partInfo.setPartItem(part);

				if (partInfo.name().equals("TOOLROD"))
					ToolPartHelper.arrowRods.add(part);
			}
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.error("Failed to register Extra TiC tool parts: " + e.toString());
			return false;
		}

		return true;
	}

	public static boolean isExtraTiCMaterialID(int matID)
	{
		return (matID >= 100 && matID < 200) || (matID >= 2000 && matID < 3000);
	}

}
