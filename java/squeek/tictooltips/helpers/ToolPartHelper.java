package squeek.tictooltips.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.Item;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.*;
import tconstruct.library.weaponry.ArrowShaftMaterial;

public class ToolPartHelper
{

	public static List<Item> toolHeads = new ArrayList<Item>( 
		Arrays.asList
		(
			TinkerTools.pickaxeHead, 
			TinkerTools.shovelHead,
			TinkerTools.excavatorHead
		)
	);
	public static List<Item> weaponMiningHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TinkerTools.hammerHead
		)
	);
	public static List<Item> weaponToolHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TinkerTools.hatchetHead,
			TinkerTools.scytheBlade,
			TinkerTools.broadAxeHead
		)
	);
	public static List<Item> weaponHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TinkerTools.swordBlade,
			TinkerTools.largeSwordBlade,
			TinkerTools.knifeBlade,
			TinkerTools.frypanHead,
			TinkerTools.signHead
		)
	);
	public static List<Item> weaponGuards = new ArrayList<Item>(
		Arrays.asList
		(
		 	TinkerTools.crossbar,
		 	TinkerTools.handGuard,
		 	TinkerTools.wideGuard
		)
	);
	public static List<Item> fullWeaponGuards = new ArrayList<Item>(
		Arrays.asList
		(
		 	TinkerTools.fullGuard
		)
	);
	public static List<Item> bindings = new ArrayList<Item>(
		Arrays.asList
		(
			TinkerTools.binding
		)
	);
	public static List<Item> toughBindings = new ArrayList<Item>(
			Arrays.asList
			(
				TinkerTools.toughBinding
			)
		);
	public static List<Item> rods = new ArrayList<Item>(
		Arrays.asList
		(
			TinkerTools.toolRod,
			TinkerTools.toughRod
		)
	);
	public static List<Item> plates = new ArrayList<Item>(
		Arrays.asList(
			TinkerTools.largePlate
		)
	);
	public static List<Item> shards = new ArrayList<Item>(
		Arrays.asList(
			TinkerTools.toolShard
		)
	);
	public static List<Item> arrowHeads = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.arrowhead
		)
	);
	public static List<Item> arrowFletchings = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.fletching
		)
	);
	public static List<Item> bowStrings = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.bowstring
		)
	);
	public static List<Item> arrowRods = new ArrayList<Item>(
		Arrays.asList(
			TinkerTools.toolRod,
			TinkerWeaponry.partArrowShaft
		)
	);
	public static List<Item> chisels = new ArrayList<Item>(
		Arrays.asList(
			TinkerTools.chiselHead
		)
	);
	// stuff added in 1.8.0
	public static List<Item> shurikenParts = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.partShuriken
		)
	);
	public static List<Item> arrowShafts = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.partArrowShaft
		)
	);
	public static List<Item> bowLimbs = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.partBowLimb
		)
	);
	public static List<Item> crossbowLimbs = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.partCrossbowLimb
		)
	);
	public static List<Item> crossbowBodies = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.partCrossbowBody
		)
	);
	public static List<Item> boltParts = new ArrayList<Item>(
		Arrays.asList(
			TinkerWeaponry.partBolt
		)
	);

	public static boolean isToolHead(Item item)
	{
		return toolHeads.contains(item);
	}

	public static boolean isWeaponToolHead(Item item)
	{
		return weaponToolHeads.contains(item);
	}

	public static boolean isWeaponMiningHead(Item item)
	{
		return weaponMiningHeads.contains(item);
	}

	public static boolean isWeaponHead(Item item)
	{
		return weaponHeads.contains(item);
	}

	public static boolean isWeaponGuard(Item item)
	{
		return isNormalWeaponGuard(item) || isFullWeaponGuard(item);
	}

	public static boolean isNormalWeaponGuard(Item item)
	{
		return weaponGuards.contains(item);
	}

	public static boolean isFullWeaponGuard(Item item)
	{
		return fullWeaponGuards.contains(item);
	}

	public static boolean isPlate(Item item)
	{
		return plates.contains(item);
	}

	public static boolean isBinding(Item item)
	{
		return isNormalBinding(item) || isToughBinding(item);
	}

	public static boolean isNormalBinding(Item item)
	{
		return bindings.contains(item);
	}

	public static boolean isToughBinding(Item item)
	{
		return toughBindings.contains(item);
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

	public static boolean isChiselHead(Item item)
	{
		return chisels.contains(item);
	}

	public static boolean isShurikenPart(Item item)
	{
		return shurikenParts.contains(item);
	}

	public static boolean isArrowShaft(Item item)
	{
		return arrowShafts.contains(item);
	}

	public static boolean isBowLimb(Item item)
	{
		return bowLimbs.contains(item);
	}

	public static boolean isCrossbowLimb(Item item)
	{
		return crossbowLimbs.contains(item);
	}

	public static boolean isCrossbowBody(Item item)
	{
		return crossbowBodies.contains(item);
	}

	public static boolean isBoltPart(Item item)
	{
		return boltParts.contains(item);
	}

	public static boolean hasCustomMaterial(Item item)
	{
		return isArrowShaft(item) || isArrowFletching(item) || isBowString(item);
	}

	// shoddiness
	public static float minShoddiness = 0f;
	public static float maxPositiveShoddiness = minShoddiness;
	public static float maxNegativeShoddiness = minShoddiness;

	// tools/weapons
	public static int minAttack;
	public static int maxAttack;
	public static int minHarvestLevel;
	public static int maxHarvestLevel;
	public static int minDurability;
	public static int maxDurability;
	public static int minMiningSpeed;
	public static int maxMiningSpeed;
	public static float minHandleModifier;
	public static float maxHandleModifier;
	public static int minReinforcedLevel;
	public static int maxReinforcedLevel;

	// arrows
	public static float minAccuracy;
	public static float maxAccuracy;
	public static float minWeight;
	public static float maxWeight;
	// added in 1.8.0
	public static float minBreakChance;
	public static float maxBreakChance;
	public static float minArrowDurabilityModifier;
	public static float maxArrowDurabilityModifier;

	// bows
	public static int minBowDrawSpeed;
	public static int maxBowDrawSpeed;
	public static float minBowArrowSpeedModifier;
	public static float maxBowArrowSpeedModifier;
	// added in 1.8.0
	public static float minFlightSpeedMax;
	public static float maxFlightSpeedMax;

	// bowstrings
	public static float minBowStringDrawspeedModifier;
	public static float maxBowStringDrawspeedModifier;
	public static float minBowStringDurabilityModifier;
	public static float maxBowStringDurabilityModifier;
	public static float minBowStringArrowSpeedModifier;
	public static float maxBowStringArrowSpeedModifier;

	// fletchings
	// added in 1.8.0
	public static float minFletchingDurabilityModifier;
	public static float maxFletchingDurabilityModifier;

	public static void determineMinAndMaxValues()
	{
		boolean needsInit = true;
		for (ToolMaterial mat : TConstructRegistry.toolMaterials.values())
		{
			if (needsInit)
			{
				minAttack = maxAttack = mat.attack();
				minHarvestLevel = maxHarvestLevel = mat.harvestLevel();
				minDurability = maxDurability = mat.durability();
				minMiningSpeed = maxMiningSpeed = mat.toolSpeed();
				minHandleModifier = maxHandleModifier = mat.handleDurability();
				minReinforcedLevel = maxReinforcedLevel = mat.reinforced();
				needsInit = false;
			}
			else
			{
				if (mat.shoddy() != 0f)
				{
					if (mat.shoddy() > 0f && mat.shoddy() > maxPositiveShoddiness)
						maxPositiveShoddiness = mat.shoddy();
					if (mat.shoddy() < 0f && mat.shoddy() < maxNegativeShoddiness)
						maxNegativeShoddiness = mat.shoddy();
				}
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
				if (mat.reinforced() > maxReinforcedLevel)
					maxReinforcedLevel = mat.reinforced();
				else if (mat.reinforced() < minReinforcedLevel)
					minReinforcedLevel = mat.reinforced();
			}
		}

		needsInit = true;
		for (ArrowMaterial mat : TConstructRegistry.arrowMaterials.values())
		{
			if (needsInit)
			{
				minBreakChance = maxBreakChance = mat.breakChance;
				minWeight = maxWeight = mat.mass;
				needsInit = false;
			}
			else
			{
				if (mat.breakChance > maxBreakChance)
					maxBreakChance = mat.breakChance;
				else if (mat.breakChance < minBreakChance)
					minBreakChance = mat.breakChance;
				if (mat.mass > maxWeight)
					maxWeight = mat.mass;
				else if (mat.mass < minWeight)
					minWeight = mat.mass;
			}
		}

		needsInit = true;
		for (BowMaterial mat : TConstructRegistry.bowMaterials.values())
		{
			if (needsInit)
			{
				minBowDrawSpeed = maxBowDrawSpeed = mat.drawspeed;
				minBowArrowSpeedModifier = maxBowArrowSpeedModifier = mat.flightSpeedMax;
				needsInit = false;
			}
			else
			{
				if (mat.drawspeed > maxBowDrawSpeed)
					maxBowDrawSpeed = mat.drawspeed;
				else if (mat.drawspeed < minBowDrawSpeed)
					minBowDrawSpeed = mat.drawspeed;
				if (mat.flightSpeedMax > maxFlightSpeedMax)
					maxFlightSpeedMax = mat.flightSpeedMax;
				else if (mat.flightSpeedMax < minFlightSpeedMax)
					minFlightSpeedMax = mat.flightSpeedMax;
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

			FletchingMaterial fletchingMat = CompatibilityHelper.getFletchingMaterial(customMat.materialID);
			if (fletchingMat != null)
			{
				if (fletchingMat.accuracy > maxAccuracy)
					maxAccuracy = fletchingMat.accuracy;
				else if (fletchingMat.accuracy < minAccuracy)
					minAccuracy = fletchingMat.accuracy;
				if (fletchingMat.breakChance > maxBreakChance)
					maxBreakChance = fletchingMat.breakChance;
				else if (fletchingMat.breakChance < minBreakChance)
					minBreakChance = fletchingMat.breakChance;
				if (fletchingMat.durabilityModifier > maxFletchingDurabilityModifier)
					maxFletchingDurabilityModifier = fletchingMat.durabilityModifier;
				else if (fletchingMat.durabilityModifier < minFletchingDurabilityModifier)
					minFletchingDurabilityModifier = fletchingMat.durabilityModifier;
			}

			ArrowShaftMaterial arrowShaftMat = (ArrowShaftMaterial) TConstructRegistry.getCustomMaterial(customMat.materialID, ArrowShaftMaterial.class);
			if (arrowShaftMat != null)
			{
				if (arrowShaftMat.durabilityModifier > maxArrowDurabilityModifier)
					maxArrowDurabilityModifier = arrowShaftMat.durabilityModifier;
				else if (arrowShaftMat.durabilityModifier < minArrowDurabilityModifier)
					minArrowDurabilityModifier = arrowShaftMat.durabilityModifier;
				if (arrowShaftMat.fragility > maxBreakChance)
					maxBreakChance = arrowShaftMat.fragility;
				else if (arrowShaftMat.fragility < minBreakChance)
					minBreakChance = arrowShaftMat.fragility;
				if (arrowShaftMat.weight > maxWeight)
					maxWeight = arrowShaftMat.weight;
				else if (arrowShaftMat.weight < minWeight)
					minWeight = arrowShaftMat.weight;
			}
		}
	}

	// shoddiness
	public static String getShoddinessString(float val)
	{
		return ColorHelper.getRelativeColor(val, minShoddiness, val > 0 ? maxPositiveShoddiness : maxNegativeShoddiness) + StringHelper.getShoddinessString(val);
	}

	// tools/weapons
	public static String getAttackString(int val)
	{
		return ColorHelper.getRelativeColor(val, minAttack, maxAttack) + StringHelper.getDamageString(val);
	}

	public static String getHarvestLevelString(int val)
	{
		return ColorHelper.getRelativeColor(val, minHarvestLevel, maxHarvestLevel) + StringHelper.getHarvestLevelName(val);
	}

	public static String getDurabilityString(int val)
	{
		return ColorHelper.getRelativeColor(val, minDurability, maxDurability) + StringHelper.getDurabilityString(val);
	}

	public static String getMiningSpeedString(int val)
	{
		return ColorHelper.getRelativeColor(val, minMiningSpeed, maxMiningSpeed) + StringHelper.getSpeedString(val);
	}

	public static String getHandleModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minHandleModifier, maxHandleModifier) + StringHelper.getModifierString(val);
	}

	public static String getReinforcedString(int val)
	{
		return ColorHelper.getRelativeColor(val, minReinforcedLevel, maxReinforcedLevel) + StringHelper.getReinforcedString(val);
	}

	// arrows
	public static String getAccuracyString(float val)
	{
		return ColorHelper.getRelativeColor(val, minAccuracy, maxAccuracy) + StringHelper.getAccuracyString(val);
	}

	public static String getWeightString(float val)
	{
		return ColorHelper.getRelativeColor(val, maxWeight, minWeight) + StringHelper.getWeightString(val);
	}

	// bows
	public static String getBowDrawSpeedString(int val)
	{
		return ColorHelper.getRelativeColor(val, maxBowDrawSpeed, minBowDrawSpeed) + StringHelper.getDrawSpeedString(val);
	}

	public static String getArrowSpeedString(float val)
	{
		return getArrowSpeedString(val, 1.0f);
	}

	public static String getArrowSpeedString(float val, float multiplier)
	{
		return ColorHelper.getRelativeColor(val, minBowArrowSpeedModifier * multiplier, maxBowArrowSpeedModifier * multiplier) + StringHelper.getArrowSpeedString(val);
	}

	// bowstrings
	public static String getBowStringDrawspeedModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowStringDrawspeedModifier, maxBowStringDrawspeedModifier) + StringHelper.getModifierString(val);
	}

	public static String getBowStringDurabilityModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowStringDrawspeedModifier, maxBowStringDrawspeedModifier) + StringHelper.getModifierString(val);
	}

	public static String getBowStringArrowSpeedModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowStringArrowSpeedModifier, maxBowStringArrowSpeedModifier) + StringHelper.getModifierString(val);
	}

	public static String getBreakChanceString(float val)
	{
		return ColorHelper.getRelativeColor(val, maxBreakChance, minBreakChance) + StringHelper.getBreakChanceString(val);
	}

	public static String getDurabilityModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minFletchingDurabilityModifier, maxFletchingDurabilityModifier) + StringHelper.getModifierString(val);
	}

}
