package squeek.tictooltips.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.Item;
import tconstruct.common.TRepo;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.BowstringMaterial;
import tconstruct.library.tools.CustomMaterial;
import tconstruct.library.tools.FletchingMaterial;
import tconstruct.library.tools.TToolMaterial;

public class ToolPartHelper
{

	public static List<Item> toolHeads = new ArrayList<Item>( 
		Arrays.asList
		(
			TRepo.pickaxeHead, 
			TRepo.shovelHead,
			TRepo.excavatorHead
		)
	);
	public static List<Item> weaponMiningHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TRepo.hammerHead
		)
	);
	public static List<Item> weaponToolHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TRepo.hatchetHead,
			TRepo.scytheBlade,
			TRepo.broadAxeHead
		)
	);
	public static List<Item> weaponHeads = new ArrayList<Item>(
		Arrays.asList
		(
			TRepo.swordBlade,
			TRepo.largeSwordBlade,
			TRepo.knifeBlade,
			TRepo.frypanHead,
			TRepo.signHead
		)
	);
	public static List<Item> weaponGuards = new ArrayList<Item>(
		Arrays.asList
		(
		 	TRepo.crossbar,
		 	TRepo.handGuard,
		 	TRepo.wideGuard,
		 	TRepo.fullGuard
		)
	);
	public static List<Item> bindings = new ArrayList<Item>(
		Arrays.asList
		(
			TRepo.binding,
			TRepo.toughBinding
		)
	);
	public static List<Item> rods = new ArrayList<Item>(
		Arrays.asList
		(
			TRepo.toolRod,
			TRepo.toughRod
		)
	);
	public static List<Item> plates = new ArrayList<Item>(
		Arrays.asList(
			TRepo.largePlate
		)
	);
	public static List<Item> shards = new ArrayList<Item>(
		Arrays.asList(
			TRepo.toolShard
		)
	);
	public static List<Item> arrowHeads = new ArrayList<Item>(
		Arrays.asList(
			TRepo.arrowhead
		)
	);
	public static List<Item> arrowFletchings = new ArrayList<Item>(
		Arrays.asList(
			TRepo.fletching
		)
	);
	public static List<Item> bowStrings = new ArrayList<Item>(
		Arrays.asList(
			TRepo.bowstring
		)
	);
	public static List<Item> arrowRods = new ArrayList<Item>(
		Arrays.asList(
			TRepo.toolRod
		)
	);
	public static List<Item> chisels = new ArrayList<Item>(
		Arrays.asList(
			TRepo.chiselHead
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
		return weaponGuards.contains(item);
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

	public static boolean isChiselHead(Item item)
	{
		return chisels.contains(item);
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

	// bows
	public static int minBowDrawSpeed;
	public static int maxBowDrawSpeed;
	public static int minBowDurability;
	public static int maxBowDurability;
	public static float minBowArrowSpeedModifier;
	public static float maxBowArrowSpeedModifier;

	// bowstrings
	public static float minBowStringDrawspeedModifier;
	public static float maxBowStringDrawspeedModifier;
	public static float minBowStringDurabilityModifier;
	public static float maxBowStringDurabilityModifier;
	public static float minBowStringArrowSpeedModifier;
	public static float maxBowStringArrowSpeedModifier;

	public static void determineMinAndMaxValues()
	{
		boolean needsInit = true;
		for (int key : TConstructRegistry.toolMaterials.keySet())
		{
			TToolMaterial mat = TConstructRegistry.toolMaterials.get(key);

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

	public static String getBowDurabilityString(int val)
	{
		return ColorHelper.getRelativeColor(val, minBowDurability, maxBowDurability) + StringHelper.getDurabilityString(val);
	}

	public static String getBowArrowSpeedModifierString(float val)
	{
		return ColorHelper.getRelativeColor(val, minBowArrowSpeedModifier, maxBowArrowSpeedModifier) + StringHelper.getModifierString(val);
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

}
