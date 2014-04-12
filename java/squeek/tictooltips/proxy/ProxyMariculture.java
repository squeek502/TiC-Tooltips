package squeek.tictooltips.proxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.Item;
import squeek.tictooltips.helpers.ToolPartHelper;

public class ProxyMariculture
{

	private static Class<?> MariculturePartsHandler;
	public static List<Item> MaricultureParts = new ArrayList<Item>();

	public static void init()
	{
	}

	public static void registerParts()
	{
		try
		{
			MariculturePartsHandler = Class.forName("mariculture.plugins.PluginTConstruct");

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
			ToolPartHelper.weaponGuards.add(part);

			// bindings
			part = (Item) MariculturePartsHandler.getDeclaredField("binding").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.bindings.add(part);
			part = (Item) MariculturePartsHandler.getDeclaredField("tough_binding").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.bindings.add(part);

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

			// arrowheads
			part = (Item) MariculturePartsHandler.getDeclaredField("arrowhead").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.arrowHeads.add(part);

			// arrow rods
			part = (Item) MariculturePartsHandler.getDeclaredField("tool_rod").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.arrowRods.add(part);

			// chisel heads
			part = (Item) MariculturePartsHandler.getDeclaredField("chisel_head").get(null);
			MaricultureParts.add(part);
			ToolPartHelper.chisels.add(part);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

	public static boolean isMariculturePart(Item part)
	{
		return MaricultureParts.contains(part);
	}

	// Mariculture unlocalizedName -> TiC partName
	public static HashMap<String, String> partNameDictionary = new HashMap<String, String>();
	static
	{
		partNameDictionary.put("titanium.arrow.head", "ArrowHead");
		partNameDictionary.put("titanium.axe.head", "AxeHead");
		partNameDictionary.put("titanium.battlesign.head", "SignHead");
		partNameDictionary.put("titanium.binding", "Binding");
		partNameDictionary.put("titanium.chisel.head", "ChiselHead");
		partNameDictionary.put("titanium.chunk", "ToolShard");
		partNameDictionary.put("titanium.crossbar", "Crossbar");
		partNameDictionary.put("titanium.excavator.head", "ExcavatorHead");
		partNameDictionary.put("titanium.frypan.head", "FrypanHead");
		partNameDictionary.put("titanium.full.guard", "FullGuard");
		partNameDictionary.put("titanium.hammer.head", "HammerHead");
		partNameDictionary.put("titanium.knife.blade", "KnifeBlade");
		partNameDictionary.put("titanium.large.guard", "LargeGuard");
		partNameDictionary.put("titanium.large.sword.blade", "LargeSwordBlade");
		partNameDictionary.put("titanium.large.plate", "LargePlate");
		partNameDictionary.put("titanium.lumberaxe.head", "LumberHead");
		partNameDictionary.put("titanium.medium.guard", "MediumGuard");
		partNameDictionary.put("titanium.pickaxe.head", "PickHead");
		partNameDictionary.put("titanium.scythe.head", "ScytheHead");
		partNameDictionary.put("titanium.shovel.head", "ShovelHead");
		partNameDictionary.put("titanium.sword.blade", "SwordBlade");
		partNameDictionary.put("titanium.tool.rod", "ToolRod");
		partNameDictionary.put("titanium.tough.binding", "ToughBind");
		partNameDictionary.put("titanium.tough.rod", "ToughRod");
	}

	public static String getPartName(Item part)
	{
		String partName = "";

		try
		{
			partName = part.getUnlocalizedName().substring(5);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		partName = partNameDictionary.get(partName);

		return partName;
	}

}
