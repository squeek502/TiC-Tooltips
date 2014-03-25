package squeek.tictooltips.helpers;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import tconstruct.common.TContent;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.BowstringMaterial;
import tconstruct.library.tools.CustomMaterial;
import tconstruct.library.tools.FletchingMaterial;
import tconstruct.library.tools.ToolMaterial;

public class ToolPartHelper {
	
	private static DecimalFormat df = new DecimalFormat("##.##");
	
	public static List<Item> toolHeads = new ArrayList<Item>( 
		Arrays.asList
		(
			TContent.pickaxeHead, 
			TContent.shovelHead,
			TContent.hatchetHead,
			TContent.chiselHead,
			TContent.scytheBlade,
			TContent.broadAxeHead,
			TContent.excavatorHead,
			TContent.hammerHead 
		)
	);
	public static List<Item> weaponHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TContent.swordBlade,
			TContent.largeSwordBlade,
			TContent.knifeBlade,
			TContent.frypanHead,
			TContent.signHead
		)
	);
	public static List<Item> bindings = new ArrayList<Item>(
		Arrays.asList
		(
			TContent.binding,
			TContent.toughBinding
		)
	);
	public static List<Item> rods = new ArrayList<Item>(
		Arrays.asList
		(
			TContent.toolRod,
			TContent.toughRod
		)
	);
	public static List<Item> plates = new ArrayList<Item>(
		Arrays.asList(
			TContent.largePlate
		)
	);
	public static List<Item> shards = new ArrayList<Item>(
		Arrays.asList(
			TContent.toolShard
		)
	);
	public static List<Item> arrowHeads = new ArrayList<Item>(
		Arrays.asList(
			TContent.arrowhead
		)
	);
	public static List<Item> arrowFletchings = new ArrayList<Item>(
		Arrays.asList(
			TContent.fletching
		)
	);
	public static List<Item> bowStrings = new ArrayList<Item>(
		Arrays.asList(
			TContent.bowstring
		)
	);
	public static List<Item> arrowRods = new ArrayList<Item>(
		Arrays.asList(
			TContent.toolRod
		)
	);

	public static boolean isToolHead(Item item)
	{
		return toolHeads.contains(item);
	}
	
	public static boolean isWeaponHead(Item item)
	{
		return weaponHeads.contains(item);
	}

	public static boolean isPlate(Item item)
	{
		return plates.contains(item);
	}

	public static boolean isBinding(Item item)
	{
		return bindings.contains(item);
	}
	
	public static boolean isRod(Item item)
	{
		return rods.contains(item);
	}

	public static boolean isShard(Item item)
	{
		return shards.contains(item);
	}
	
	public static boolean isArrowHead(Item item)
	{
		return arrowHeads.contains(item);
	}
	
	public static boolean isArrowFletching(Item item)
	{
		return arrowFletchings.contains(item);
	}
	
	public static boolean isBowString(Item item)
	{
		return bowStrings.contains(item);
	}
	
	public static boolean isArrowRod(Item item)
	{
		return arrowRods.contains(item);
	}

	// tools/weapons
	private static int minAttack;
	private static int maxAttack;
	private static int minHarvestLevel;
	private static int maxHarvestLevel;
	private static int minDurability;
	private static int maxDurability;
	private static int minMiningSpeed;
	private static int maxMiningSpeed;
	private static float minHandleModifier;
	private static float maxHandleModifier;
	
	// arrows
	private static float minAccuracy;
	private static float maxAccuracy;
	private static float minWeight;
	private static float maxWeight;
	
	// bows
	private static int minBowDrawSpeed;
	private static int maxBowDrawSpeed;
	private static int minBowDurability;
	private static int maxBowDurability;
	private static float minBowArrowSpeedModifier;
	private static float maxBowArrowSpeedModifier;
	
	// bowstrings
	private static float minBowStringDrawspeedModifier;
	private static float maxBowStringDrawspeedModifier;
	private static float minBowStringDurabilityModifier;
	private static float maxBowStringDurabilityModifier;
	private static float minBowStringArrowSpeedModifier;
	private static float maxBowStringArrowSpeedModifier;
	
	public static void determineMinAndMaxValues()
	{
		boolean needsInit = true;
		for (int key : TConstructRegistry.toolMaterials.keySet())
		{
			ToolMaterial mat = TConstructRegistry.toolMaterials.get(key);

			if (needsInit)
			{
				minAttack = maxAttack = mat.attack();
				minHarvestLevel = maxHarvestLevel = mat.harvestLevel();
				minDurability = maxDurability = mat.durability();
				minMiningSpeed = maxMiningSpeed = mat.toolSpeed();
				minHandleModifier = maxHandleModifier = mat.handleDurability();
				needsInit = false;
			}
			else
			{
				if (mat.attack() > maxAttack)
					maxAttack = mat.attack();
				else if (mat.attack() < minAttack)
					minAttack = mat.attack();
				if (mat.harvestLevel() > maxHarvestLevel)
					maxHarvestLevel = mat.harvestLevel();
				else if (mat.harvestLevel() < minHarvestLevel)
					minHarvestLevel = mat.harvestLevel();
				if (mat.durability() > maxDurability)
					maxDurability = mat.durability();
				else if (mat.durability() < minDurability)
					minDurability = mat.durability();
				if (mat.toolSpeed() > maxMiningSpeed)
					maxMiningSpeed = mat.toolSpeed();
				else if (mat.toolSpeed() < minMiningSpeed)
					minMiningSpeed = mat.toolSpeed();
				if (mat.handleDurability() > maxHandleModifier)
					maxHandleModifier = mat.handleDurability();
				else if (mat.handleDurability() < minHandleModifier)
					minHandleModifier = mat.handleDurability();
			}
		}
		
		needsInit = true;
		for (int key : TConstructRegistry.arrowMaterials.keySet())
		{
			ArrowMaterial mat = TConstructRegistry.arrowMaterials.get(key);

			if (needsInit)
			{
				minAccuracy = maxAccuracy = mat.accuracy;
				minWeight = maxWeight = mat.mass;
				needsInit = false;
			}
			else
			{
				if (mat.accuracy > maxAccuracy)
					maxAccuracy = mat.accuracy;
				else if (mat.accuracy < minAccuracy)
					minAccuracy = mat.accuracy;
				if (mat.mass > maxWeight)
					maxWeight = mat.mass;
				else if (mat.mass < minWeight)
					minWeight = mat.mass;
			}
		}

		needsInit = true;
		for (int key : TConstructRegistry.bowMaterials.keySet())
		{
			BowMaterial mat = TConstructRegistry.bowMaterials.get(key);

			if (needsInit)
			{
				minBowDrawSpeed = maxBowDrawSpeed = mat.drawspeed;
				minBowDurability = maxBowDurability = mat.durability;
				minBowArrowSpeedModifier = maxBowArrowSpeedModifier = mat.flightSpeedMax;
				needsInit = false;
			}
			else
			{
				if (mat.drawspeed > maxBowDrawSpeed)
					maxBowDrawSpeed = mat.drawspeed;
				else if (mat.drawspeed < minBowDrawSpeed)
					minBowDrawSpeed = mat.drawspeed;
				if (mat.durability > maxBowDurability)
					maxBowDurability = mat.durability;
				else if (mat.durability < minBowDurability)
					minBowDurability = mat.durability;
				if (mat.flightSpeedMax > maxBowArrowSpeedModifier)
					maxBowArrowSpeedModifier = mat.flightSpeedMax;
				else if (mat.flightSpeedMax < minBowArrowSpeedModifier)
					minBowArrowSpeedModifier = mat.flightSpeedMax;
			}
		}
		
		needsInit = true;
		for (CustomMaterial customMat : TConstructRegistry.customMaterials)
		{
			BowstringMaterial mat = (BowstringMaterial) TConstructRegistry.getCustomMaterial(customMat.materialID, BowstringMaterial.class);
			if (mat != null)
			{
				if (needsInit)
				{
					minBowStringDrawspeedModifier = maxBowStringDrawspeedModifier = mat.drawspeedModifier;
					minBowStringDurabilityModifier = maxBowStringDurabilityModifier = mat.durabilityModifier;
					minBowStringArrowSpeedModifier = maxBowStringArrowSpeedModifier = mat.flightSpeedModifier;
					needsInit = false;
				}
				else
				{
					if (mat.drawspeedModifier > maxBowStringDrawspeedModifier)
						maxBowStringDrawspeedModifier = mat.drawspeedModifier;
					else if (mat.drawspeedModifier < minBowStringDrawspeedModifier)
						minBowStringDrawspeedModifier = mat.drawspeedModifier;
					if (mat.durabilityModifier > maxBowStringDurabilityModifier)
						maxBowStringDurabilityModifier = mat.durabilityModifier;
					else if (mat.durabilityModifier < minBowStringDurabilityModifier)
						minBowStringDurabilityModifier = mat.durabilityModifier;
					if (mat.flightSpeedModifier > maxBowStringArrowSpeedModifier)
						maxBowStringArrowSpeedModifier = mat.flightSpeedModifier;
					else if (mat.flightSpeedModifier < minBowStringArrowSpeedModifier)
						minBowStringArrowSpeedModifier = mat.flightSpeedModifier;
				}
			}
			
			FletchingMaterial fletchingMat = (FletchingMaterial) TConstructRegistry.getCustomMaterial(customMat.materialID, FletchingMaterial.class);
			if (fletchingMat != null)
			{
				if (fletchingMat.accuracy > maxAccuracy)
					maxAccuracy = fletchingMat.accuracy;
				else if (fletchingMat.accuracy < minAccuracy)
					minAccuracy = fletchingMat.accuracy;
				if (fletchingMat.mass > maxWeight)
					maxWeight = fletchingMat.mass;
				else if (fletchingMat.mass < minWeight)
					minWeight = fletchingMat.mass;
			}
		}
	}
	
	// tools/weapons
	public static String getAttackString(int val)
	{
		return ColorHelper.getRelativeColor(val, minAttack, maxAttack)+""+getDamageString(val);
	}
	public static String getHarvestLevelString(int val)
	{
		return ColorHelper.getRelativeColor(val, minHarvestLevel, maxHarvestLevel)+""+getHarvestLevelName(val);
	}
	public static String getDurabilityString(int val)
	{
		return ColorHelper.getRelativeColor(val, minDurability, maxDurability)+""+val;
	}
	public static String getMiningSpeedString(int val)
	{
		return ColorHelper.getRelativeColor(val, minMiningSpeed, maxMiningSpeed)+""+df.format(val / 100f);
	}
	public static String getHandleModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minHandleModifier, maxHandleModifier)+""+val+"x";
	}
	
	// arrows
	public static String getAccuracyString(float val)
	{
		return ColorHelper.getRelativeColor(val, minAccuracy, maxAccuracy)+df.format(val - 4)+"%";
	}
	public static String getWeightString(float val)
	{
		return ColorHelper.getRelativeColor(val, maxWeight, minWeight)+""+val;
	}

	// bows
	public static String getBowDrawSpeedString(int val)
	{
		return ColorHelper.getRelativeColor(val, maxBowDrawSpeed, minBowDrawSpeed)+df.format(val / 20f)+"s";
	}
	public static String getBowDurabilityString(int val)
	{
		return ColorHelper.getRelativeColor(val, minBowDurability, maxBowDurability)+""+val;
	}
	public static String getBowArrowSpeedModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowArrowSpeedModifier, maxBowArrowSpeedModifier)+""+val+"x";
	}
	
	// bowstrings
	public static String getBowStringDrawspeedModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowStringDrawspeedModifier, maxBowStringDrawspeedModifier)+""+val+"x";
	}
	public static String getBowStringDurabilityModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowStringDrawspeedModifier, maxBowStringDrawspeedModifier)+""+val+"x";
	}
	public static String getBowStringArrowSpeedModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowStringArrowSpeedModifier, maxBowStringArrowSpeedModifier)+""+val+"x";
	}
	
	// Taken from tconstruct.client.gui.ToolStationGui
    public static String getHarvestLevelName (int num)
    {
        switch (num)
        {
        case 0:
            return (StatCollector.translateToLocal("gui.partcrafter.mining1"));
        case 1:
            return (StatCollector.translateToLocal("gui.partcrafter.mining2"));
        case 2:
            return (StatCollector.translateToLocal("gui.partcrafter.mining3"));
        case 3:
            return (StatCollector.translateToLocal("gui.partcrafter.mining4"));
        case 4:
            return (StatCollector.translateToLocal("gui.partcrafter.mining5"));
        case 5:
            return (StatCollector.translateToLocal("gui.partcrafter.mining6"));
        default:
            return String.valueOf(num);
        }
    }
    
    // Taken from tconstruct.library.tools.ToolCore
    public static String getReinforcedString(int reinforced)
    {
        if (reinforced > 9)
            return "Unbreakable";
        String ret = "Reinforced ";
        switch (reinforced)
        {
        case 1:
            ret += "I";
            break;
        case 2:
            ret += "II";
            break;
        case 3:
            ret += "III";
            break;
        case 4:
            ret += "IV";
            break;
        case 5:
            ret += "V";
            break;
        case 6:
            ret += "VI";
            break;
        case 7:
            ret += "VII";
            break;
        case 8:
            ret += "VIII";
            break;
        case 9:
            ret += "IX";
            break;
        default:
            ret += "X";
            break;
        }
        return ret;
    }
    
    // Taken from tconstruct.library.tools.ToolCore
    public static String getDamageString(int attack)
    {
    	String heart = attack == 2 ? StatCollector.translateToLocal("gui.partcrafter8") : StatCollector.translateToLocal("gui.partcrafter9");
    	if (attack % 2 == 0)
    		return attack / 2 + heart;
    	else
    		return attack / 2f + heart;
    }
}
