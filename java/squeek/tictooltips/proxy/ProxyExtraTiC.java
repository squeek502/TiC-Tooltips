package squeek.tictooltips.proxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.Item;
import squeek.tictooltips.ModTiCTooltips;
import squeek.tictooltips.helpers.ToolPartHelper;

public class ProxyExtraTiC implements IModPartHandler
{

	private static Class<?> ExtraTiCPartsHandler;
	private static Class<?> ExtraTiCPart;
	private static Field unlocalizedPartName;
	public static List<Item> extraTiCParts = new ArrayList<Item>();
	
	public static void init()
	{
		try
		{
			ExtraTiCPart = Class.forName("glassmaker.extratic.parts.Part");
			unlocalizedPartName = ExtraTiCPart.getDeclaredField("unlocalizedPartName");
			unlocalizedPartName.setAccessible(true);
			if (registerParts())
				ProxyIguanaTweaks.registerModPartHandler(new ProxyExtraTiC());
		}
		catch (Exception e)
		{
			ModTiCTooltips.Log.error("Failed to load Extra TiC integration: " + e.toString());
		}
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

	// ExtraTiC unlocalizedName -> TiC partName
	public static HashMap<String, String> partNameDictionary = new HashMap<String, String>();
	static
	{
		partNameDictionary.put("toolrod", "ToolRod");
		partNameDictionary.put("pickaxe.head", "PickHead");
		partNameDictionary.put("shovel.head", "ShovelHead");
		partNameDictionary.put("axe.head", "AxeHead");
		partNameDictionary.put("binding", "Binding");
		partNameDictionary.put("toughbind", "ToughBind");
		partNameDictionary.put("toughrod", "ToughRod");
		partNameDictionary.put("largeplate", "LargePlate");
		partNameDictionary.put("sword.blade", "SwordBlade");
		partNameDictionary.put("large.guard", "LargeGuard");
		partNameDictionary.put("medium.guard", "MediumGuard");
		partNameDictionary.put("crossbar", "Crossbar");
		partNameDictionary.put("knife.blade", "KnifeBlade");
		partNameDictionary.put("full.guard", "FullGuard");
		partNameDictionary.put("frypan.head", "FrypanHead");
		partNameDictionary.put("battle.sign", "SignHead");
		partNameDictionary.put("chisel.head", "ChiselHead");
		partNameDictionary.put("scythe.head", "ScytheHead");
		partNameDictionary.put("lumberaxe.head", "LumberHead");
		partNameDictionary.put("excavator.head", "ExcavatorHead");
		partNameDictionary.put("large.sword.blade", "LargeSwordBlade");
		partNameDictionary.put("hammer.head", "HammerHead");
		partNameDictionary.put("arrowhead", "ArrowHead");
	}

	@Override
	public String getPartName(Item part)
	{
		String partName = "";
		
		try
		{
			partName = (String) unlocalizedPartName.get(part);
		}
		catch (Exception e)
		{
		}

		partName = partNameDictionary.get(partName);
		
		return partName;
	}

	@Override
	public boolean isModdedPart(Item part)
	{
		return extraTiCParts.contains(part);
	}

}
