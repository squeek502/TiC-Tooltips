package squeek.tictooltips;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import squeek.tictooltips.helpers.*;
import squeek.tictooltips.proxy.ProxyIguanaTweaks;
import tconstruct.items.Bowstring;
import tconstruct.items.Fletching;
import tconstruct.items.ToolPart;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.armor.ArmorCore;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.BowstringMaterial;
import tconstruct.library.tools.FletchingMaterial;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.util.IPattern;
import tconstruct.library.util.IToolPart;

public class TooltipHandler
{

	@ForgeSubscribe
	public void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.entityPlayer == null)
			return;

		Item item = event.itemStack.getItem();

		// Tool Parts
		if (item instanceof IToolPart)
		{
			if (ToolPartHelper.isShard(item))
				return;

			if (ModTiCTooltips.hasIguanaTweaks)
			{
				if (ToolPartHelper.isArrowFletching(item) || ToolPartHelper.isBowString(item))
				{
					event.toolTip.addAll(getMaterialTooltip(event.itemStack));
				}
				else if (ProxyIguanaTweaks.isModdedPart(item))
				{
					event.toolTip.addAll(ProxyIguanaTweaks.getPartTooltip(event.itemStack, event.entityPlayer, event.showAdvancedItemTooltips));
				}
			}
			else
			{
				event.toolTip.addAll(getMaterialTooltip(event.itemStack));
			}
		}
		// Patterns
		else if (item instanceof IPattern && !ModTiCTooltips.hasIguanaTweaks)
		{
			event.toolTip.addAll(getPatternTooltip(event.itemStack));
		}
		// Tools
		else if (item instanceof ToolCore && ToolHelper.hasToolTag(event.itemStack))
		{
			// start at the last line of the tooltip that was added by Tinkers
			List<String> tinkersTooltip = new ArrayList<String>();
			String plusPrefix = "\u00A79+";
			int toolTipIndex = 0;
			if (event.toolTip.size() > 1)
			{
				toolTipIndex = -1;

				// skip to the last + modifier if it's easy to find
				if (event.toolTip.get(event.toolTip.size() - 1).startsWith(plusPrefix))
					toolTipIndex = event.toolTip.size() - 1;
				// otherwise skip to the first enchant string
				else if (event.itemStack.isItemEnchanted())
				{
					NBTTagList enchantTagList = event.itemStack.getEnchantmentTagList();
					short enchantID = ((NBTTagCompound) enchantTagList.tagAt(0)).getShort("id");
					short enchantLevel = ((NBTTagCompound) enchantTagList.tagAt(0)).getShort("lvl");
					String enchantName = Enchantment.enchantmentsList[enchantID].getTranslatedName(enchantLevel);
					toolTipIndex = event.toolTip.indexOf(enchantName);
				}
				// otherwise skip to the first + modifier
				else
				{
					for (int toolTipSearchIndex = 0; toolTipSearchIndex < event.toolTip.size(); toolTipSearchIndex++)
					{
						if (event.toolTip.get(toolTipSearchIndex).startsWith(plusPrefix))
						{
							toolTipIndex = toolTipSearchIndex;
							break;
						}
					}
				}

				// as a last resort, skip to the end of the TiC additions (potentially expensive)
				if (toolTipIndex == -1)
				{
					((ToolCore) item).addInformation(event.itemStack, event.entityPlayer, tinkersTooltip, event.showAdvancedItemTooltips);
					toolTipIndex = tinkersTooltip.size() > 0 ? Math.min(event.toolTip.size(), event.toolTip.indexOf(tinkersTooltip.get(0)) + tinkersTooltip.size()) : event.toolTip.size();
				}
			}

			// work backwards past any + attack strings
			while (toolTipIndex > 1 && event.toolTip.get(toolTipIndex - 1).startsWith(plusPrefix))
			{
				toolTipIndex--;
			}

			// work backwards and remove any spacing
			while (toolTipIndex > 1 && event.toolTip.get(toolTipIndex - 1).equals(""))
			{
				event.toolTip.remove(toolTipIndex - 1);
				toolTipIndex--;
			}

			boolean ctrlDown = KeyHelper.isCtrlKeyDown();
			boolean shiftDown = KeyHelper.isShiftKeyDown();
			// Shift held
			if (shiftDown && !ctrlDown)
			{
				if (toolTipIndex > 1)
					event.toolTip.add(toolTipIndex++, "");

				List<String> toolStats = getToolStatsTooltip(event.itemStack);
				event.toolTip.addAll(toolTipIndex, toolStats);
				toolTipIndex += toolStats.size();

				if (toolTipIndex >= event.toolTip.size() || !event.toolTip.get(toolTipIndex).equals(""))
					event.toolTip.add(toolTipIndex++, "");
			}
			// Ctrl held
			else if (ctrlDown)
			{
				while (event.toolTip.size() > 1)
					event.toolTip.remove(1);

				toolTipIndex = event.toolTip.size();

				List<String> toolMaterials = getToolMaterialsTooltip(event.itemStack);
				event.toolTip.addAll(toolTipIndex, toolMaterials);
				toolTipIndex += toolMaterials.size();
			}
			// No buttons held
			else
			{
				event.toolTip.add(toolTipIndex++, StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.stats", EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
				event.toolTip.add(toolTipIndex++, StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.materials", EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "Ctrl" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
			}
		}
		// Armor
		else if (item instanceof ArmorCore && ArmorHelper.hasArmorTag(event.itemStack))
		{
			int toolTipIndex = Math.min(1, event.toolTip.size());

			// find first empty line
			while (toolTipIndex < event.toolTip.size() && !event.toolTip.get(toolTipIndex).equals(""))
			{
				toolTipIndex++;
			}

			event.toolTip.addAll(toolTipIndex, getArmorStatsTooltip(event.itemStack));
		}
	}

	/*
	 * 	Material Stats Tool Tip
	 */
	private List<String> getMaterialTooltip(ItemStack itemStack)
	{
		Item item = itemStack.getItem();
		int matID = ((IToolPart) item).getMaterialID(itemStack);
		return getMaterialTooltip(matID, item);
	}

	private List<String> getMaterialTooltip(int matID, Item item)
	{
		return getMaterialTooltip(matID, item, null);
	}

	private List<String> getMaterialTooltip(int matID, Item item, ToolCore tool)
	{
		List<String> toolTip = new ArrayList<String>();
		ToolMaterial mat = TConstructRegistry.getMaterial(matID);
		boolean hasTool = tool != null;

		if (mat == null)
			return toolTip;

		if (!mat.ability().equals(""))
			toolTip.add(mat.style() + mat.ability());

		if (mat.shoddy() != 0)
		{
			for (int index = 0; index < toolTip.size(); index++)
			{
				if (toolTip.get(index).contains(StringHelper.getShoddinessTypeString(mat.shoddy())))
				{
					toolTip.set(index, toolTip.get(index) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("tictooltips.material.shoddiness.modifier", ToolPartHelper.getShoddinessString(mat.shoddy()) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
					break;
				}
			}
		}

		if (ToolPartHelper.isArrowHead(item))
		{
			ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation9") + ToolPartHelper.getAccuracyString(arrowMat.accuracy));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(arrowMat.mass / 5f));
		}
		else if (ToolPartHelper.isArrowFletching(item))
		{
			FletchingMaterial fletchingMat = (FletchingMaterial) TConstructRegistry.getCustomMaterial(matID, FletchingMaterial.class);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation9") + ToolPartHelper.getAccuracyString(fletchingMat.accuracy));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(fletchingMat.mass));
		}
		else if (ToolPartHelper.isBowString(item))
		{
			BowstringMaterial bowstringMat = (BowstringMaterial) TConstructRegistry.getCustomMaterial(matID, BowstringMaterial.class);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation6") + ToolPartHelper.getBowStringDrawspeedModifierString(bowstringMat.drawspeedModifier));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getBowStringDurabilityModifierString(bowstringMat.durabilityModifier));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getBowStringArrowSpeedModifierString(bowstringMat.flightSpeedModifier));
		}
		else
		{
			if (mat.reinforced() > 0)
				toolTip.add(mat.style() + StringHelper.getReinforcedString(mat.reinforced()));

			if (ToolPartHelper.isRod(item))
			{
				boolean isArrowMat = ToolPartHelper.isArrowRod(item);
				boolean isToolAnArrow = hasTool && ToolHelper.isAmmoTool(tool);
				boolean isToolABow = hasTool && ToolHelper.isBowTool(tool);
				if ((hasTool && (isToolAnArrow || isToolABow)) || (isArrowMat && KeyHelper.isShiftKeyDown()))
				{
					if (TConstructRegistry.validBowMaterial(matID) && (!hasTool || isToolABow))
					{
						String prefix = hasTool ? "" : "Bow ";
						BowMaterial bowMat = (BowMaterial) TConstructRegistry.getBowMaterial(matID);
						toolTip.add(prefix + StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
						toolTip.add(prefix + StringHelper.getLocalizedString("gui.toolstation6") + ToolPartHelper.getBowDrawSpeedString(bowMat.drawspeed));
						toolTip.add(prefix + StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getBowDurabilityString(bowMat.durability));
						toolTip.add(prefix + StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getBowArrowSpeedModifierString(bowMat.flightSpeedMax));
					}
					if (TConstructRegistry.validArrowMaterial(matID) && (!hasTool || isToolAnArrow))
					{
						String prefix = hasTool ? "" : StringHelper.getLocalizedString("item.InfiTool.Arrow.name") + " ";
						ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);
						toolTip.add(prefix + StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(arrowMat.mass));
					}
				}
				else
				{
					toolTip.add(StringHelper.getLocalizedString("gui.partcrafter5") + ToolPartHelper.getHandleModifierString(mat.handleModifier));
					if (!hasTool && isArrowMat)
						toolTip.add(StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.bow.stats", EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
				}
			}
			else if (ToolPartHelper.isToolHead(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(mat.harvestLevel()));
			}
			else if (ToolPartHelper.isWeaponMiningHead(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(mat.harvestLevel()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
			else if (ToolPartHelper.isWeaponToolHead(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
			else if (ToolPartHelper.isChiselHead(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
			}
			else if (ToolPartHelper.isWeaponHead(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
			}
			else if (ToolPartHelper.isPlate(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				if (!hasTool || ToolHelper.isHarvestTool(tool))
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				if (!hasTool || ToolHelper.isWeaponTool(tool))
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
			/* weapon guards don't affect anything except abilities
			else if (ToolPartHelper.isWeaponGuard(item))
			{
			}
			*/
		}

		return toolTip;
	}

	/*
	 * 	Pattern Tool Tip
	 */
	private List<String> getPatternTooltip(ItemStack itemStack)
	{
		List<String> validMats = null;
		List<String> toolTip = new ArrayList<String>();

		if (PatternHelper.isBowstringPattern(itemStack.getItem(), itemStack.getItemDamage()))
		{
			validMats = PatternHelper.getValidCustomMaterialsOfType(BowstringMaterial.class);
		}
		else if (PatternHelper.isFletchingPattern(itemStack.getItem(), itemStack.getItemDamage()))
		{
			validMats = PatternHelper.getValidCustomMaterialsOfType(FletchingMaterial.class);
		}

		if (validMats != null && !validMats.isEmpty())
		{
			toolTip.add(StatCollector.translateToLocal("tictooltips.pattern.valid.materials"));
			for (String matName : validMats)
			{
				if (toolTip.size() < 7 || KeyHelper.isShiftKeyDown())
					toolTip.add(" - " + matName);
				else
				{
					toolTip.add(StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.more", EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
					break;
				}
			}
		}

		return toolTip;
	}

	/*
	 * 	Tool Stats Tool Tip
	 */
	private List<String> getToolStatsTooltip(ItemStack itemStack)
	{
		List<String> toolTip = new ArrayList<String>();

		ToolCore tool = (ToolCore) itemStack.getItem();
		NBTTagCompound toolTag = ToolHelper.getToolTag(itemStack);

		float shoddiness = ToolHelper.getStonebound(toolTag);
		boolean isShoddy = shoddiness != 0;
		String shoddinessType = StringHelper.getShoddinessTypeString(shoddiness);

		ToolMaterial repairMat = ToolHelper.getHeadMaterial(toolTag);
		toolTip.add(StatCollector.translateToLocal("tictooltips.tool.repair.material") + repairMat.style() + repairMat.displayName);

		int maxDurability = ToolHelper.getMaxDurability(toolTag);

		if (maxDurability > 0)
		{
			int curDurability = maxDurability - ToolHelper.getUsedDurability(toolTag);
			boolean isUnbreakable = ToolHelper.isUnbreakable(toolTag);
			int effectiveDurability = isUnbreakable ? -1 : ToolHelper.getEffectiveDurability(toolTag);

			String curOfMax = curDurability == maxDurability ? StringHelper.getDurabilityString(maxDurability) : StringHelper.getDurabilityString(curDurability) + " / " + StringHelper.getDurabilityString(maxDurability);
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ColorHelper.getRelativeColor(curDurability, 0, maxDurability) + curOfMax);

			if (maxDurability != effectiveDurability)
				toolTip.add(StatCollector.translateToLocalFormatted("tictooltips.tool.effective.durability", StringHelper.getLocalizedString("gui.toolstation2")) + ColorHelper.getRelativeColor(ToolHelper.getReinforcedLevel(toolTag), ToolPartHelper.minReinforcedLevel - 3, ToolPartHelper.maxReinforcedLevel) + StringHelper.getDurabilityString(effectiveDurability));
		}

		if (isShoddy)
		{
			toolTip.add(StatCollector.translateToLocalFormatted("tictooltips.tool.shoddiness.modifier", shoddinessType) + ToolPartHelper.getShoddinessString(shoddiness));
		}

		if (ToolHelper.isWeaponTool(tool))
		{
			int damage = ToolHelper.getDamage(tool, toolTag);
			float stoneboundDamage = ToolHelper.getShoddinessDamageBonus(toolTag);
			float maxStoneboundDamage = ToolHelper.getMaxShoddinessDamageBonus(toolTag);
			int smiteDamageRange[] = ToolHelper.getSmiteDamageRange(tool, toolTag);
			int antiSpiderDamageRange[] = ToolHelper.getAntiSpiderDamageRange(tool, toolTag);
			int burnDuration = ToolHelper.getBurnDuration(tool, toolTag);
			float chanceToBehead = ToolHelper.getChanceToBehead(tool, toolTag);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ColorHelper.getRelativeColor(ToolHelper.getRawDamage(tool, toolTag) + stoneboundDamage, ToolPartHelper.minAttack, ToolPartHelper.maxAttack) + StringHelper.getDamageString(damage));
			if (stoneboundDamage != 0)
			{
				EnumChatFormatting textColor = stoneboundDamage > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (stoneboundDamage > 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5")) + textColor;
				String maxString = "";
				if (stoneboundDamage == maxStoneboundDamage)
					bonusOrLoss += EnumChatFormatting.BOLD;
				else
					maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[" + StatCollector.translateToLocal("tictooltips.maximum") + ": " + StringHelper.getDamageNumberString((int) maxStoneboundDamage) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessType + " " + bonusOrLoss + StringHelper.getDamageString((int) stoneboundDamage) + maxString);
			}
			else if (maxStoneboundDamage != 0 && stoneboundDamage != maxStoneboundDamage)
			{
				String bonusOrLoss = maxStoneboundDamage > 0 ? StringHelper.getLocalizedString("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StringHelper.getLocalizedString("gui.toolstation5") + EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.maximum") + " " + shoddinessType + " " + bonusOrLoss + StringHelper.getDamageString((int) maxStoneboundDamage));
			}
			if (smiteDamageRange[1] != 0 && smiteDamageRange[0] != 0)
			{
				EnumChatFormatting textColor = smiteDamageRange[0] >= 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (smiteDamageRange[0] >= 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5"));
				bonusOrLoss = bonusOrLoss.substring(0, bonusOrLoss.length() - 2);
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocalFormatted("tictooltips.tool.bonus.vs.undead", bonusOrLoss) + textColor + StringHelper.getDamageNumberString(smiteDamageRange[0]) + "-" + StringHelper.getDamageString(smiteDamageRange[1]));
			}
			if (antiSpiderDamageRange[1] != 0 && antiSpiderDamageRange[0] != 0)
			{
				EnumChatFormatting textColor = antiSpiderDamageRange[0] >= 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (antiSpiderDamageRange[0] >= 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5"));
				bonusOrLoss = bonusOrLoss.substring(0, bonusOrLoss.length() - 2);
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocalFormatted("tictooltips.tool.bonus.vs.spiders", bonusOrLoss) + textColor + StringHelper.getDamageNumberString(antiSpiderDamageRange[0]) + "-" + StringHelper.getDamageString(antiSpiderDamageRange[1]));
			}
			if (burnDuration != 0)
			{
				EnumChatFormatting textColor = EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.tool.burn.duration") + textColor + StringHelper.getDurationString(burnDuration));
			}
			if (chanceToBehead != 0)
			{
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.tool.chance.to.behead") + ColorHelper.getRelativeColor(chanceToBehead, 0, 1) + StringHelper.getPercentageString(chanceToBehead));
			}
		}

		if (ToolHelper.isBowTool(tool))
		{
			int drawSpeed = ToolHelper.getDrawSpeed(toolTag);
			float arrowSpeedModifier = ToolHelper.getArrowSpeedModifier(toolTag);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation6") + ToolPartHelper.getBowDrawSpeedString(drawSpeed));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getBowArrowSpeedModifierString(arrowSpeedModifier));
		}

		if (ToolHelper.isAmmoTool(tool))
		{
			int damage = ToolHelper.getAmmoDamage(toolTag);
			float weight = ToolHelper.getWeight(toolTag);
			float accuracy = ToolHelper.getAccuracy(toolTag);

			String damageColor = ColorHelper.getRelativeColor(ToolHelper.getRawDamage(tool, toolTag), ToolPartHelper.minAttack, ToolPartHelper.maxAttack);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation10") + " " + damageColor + StringHelper.getDamageString(damage));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation11") + " " + damageColor + StringHelper.getAmmoDamageRangeString(damage));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(weight));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation9") + ToolPartHelper.getAccuracyString(accuracy));
		}

		if (ToolHelper.isDualHarvestTool(tool))
		{
			int mineSpeed1 = ToolHelper.getPrimaryMiningSpeed(tool, toolTag);
			int mineSpeed2 = ToolHelper.getSecondaryMiningSpeed(tool, toolTag);
			float stoneboundSpeed = ToolHelper.getShoddinessSpeedBonus(tool, toolTag);
			float maxStoneboundSpeed = ToolHelper.getMaxShoddinessSpeedBonus(tool, toolTag);

			mineSpeed1 += stoneboundSpeed * 100f;
			mineSpeed2 += stoneboundSpeed * 100f;

			int harvestLevel1 = ToolHelper.getPrimaryHarvestLevel(toolTag);
			int harvestLevel2 = ToolHelper.getSecondaryHarvestLevel(toolTag);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation12") + ToolPartHelper.getMiningSpeedString(mineSpeed1) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + ", " + ToolPartHelper.getMiningSpeedString(mineSpeed2));
			if (stoneboundSpeed != 0)
			{
				EnumChatFormatting textColor = stoneboundSpeed > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (stoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5")) + textColor;
				String maxString = "";
				if (stoneboundSpeed == maxStoneboundSpeed)
					bonusOrLoss += EnumChatFormatting.BOLD;
				else
					maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[" + StatCollector.translateToLocal("tictooltips.maximum") + ": " + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (stoneboundSpeed * 100f)) + maxString);
			}
			else if (maxStoneboundSpeed != 0 && stoneboundSpeed != maxStoneboundSpeed)
			{
				String bonusOrLoss = maxStoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StringHelper.getLocalizedString("gui.toolstation5") + EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.maximum") + " " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)));
			}

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation13") + " " + ToolPartHelper.getHarvestLevelString(harvestLevel1) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + ", " + ToolPartHelper.getHarvestLevelString(harvestLevel2));
		}
		else if (ToolHelper.isHarvestTool(tool))
		{
			int mineSpeed = ToolHelper.getTotalMiningSpeed(tool, toolTag);
			float stoneboundSpeed = ToolHelper.getShoddinessSpeedBonus(tool, toolTag);
			float maxStoneboundSpeed = ToolHelper.getMaxShoddinessSpeedBonus(tool, toolTag);

			mineSpeed += stoneboundSpeed * 100f;

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mineSpeed));
			if (stoneboundSpeed != 0)
			{
				EnumChatFormatting textColor = stoneboundSpeed > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (stoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5")) + textColor;
				String maxString = "";
				if (stoneboundSpeed == maxStoneboundSpeed)
					bonusOrLoss += EnumChatFormatting.BOLD;
				else
					maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[" + StatCollector.translateToLocal("tictooltips.maximum") + ": " + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (stoneboundSpeed * 100f)) + maxString);
			}
			else if (maxStoneboundSpeed != 0 && stoneboundSpeed != maxStoneboundSpeed)
			{
				String bonusOrLoss = maxStoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StringHelper.getLocalizedString("gui.toolstation5") + EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.maximum") + " " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)));
			}

			if (!ModTiCTooltips.hasIguanaTweaks)
			{
				int harvestLevel = ToolHelper.getPrimaryHarvestLevel(toolTag);

				toolTip.add(StringHelper.getLocalizedString("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(harvestLevel));
			}
		}
		else if (ToolHelper.isUtilityTool(tool))
		{
			int mineSpeed = ToolHelper.getPrimaryMiningSpeed(tool, toolTag);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mineSpeed));
		}

		int modifiersAvailable = toolTag.getInteger("Modifiers");
		if (modifiersAvailable > 0)
		{
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation18") + EnumChatFormatting.WHITE + modifiersAvailable);
		}

		boolean hasModifiers = toolTag.hasKey("ModifierTip1") && !toolTag.getString("ModifierTip1").trim().equals("");
		if (hasModifiers)
			toolTip.add(StatCollector.translateToLocal("gui.toolstation17")+":");

		boolean displayToolTips = true;
		int tipNum = 0;
		while (hasModifiers && displayToolTips)
		{
			tipNum++;
			String tooltip = "ModifierTip" + tipNum;
			if (toolTag.hasKey(tooltip))
			{
				String tipName = toolTag.getString(tooltip);
				if (!tipName.trim().equals(""))
					toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + tipName);
			}
			else
				displayToolTips = false;
		}

		return toolTip;
	}

	/*
	 * 	Tool Materials Tool Tip
	 */
	private List<String> getToolMaterialsTooltip(ItemStack itemStack)
	{
		List<String> toolTip = new ArrayList<String>();

		ToolCore tool = (ToolCore) itemStack.getItem();
		NBTTagCompound toolTag = ToolHelper.getToolTag(itemStack);
		Item itemPart;
		int matID;

		itemPart = tool.getHeadItem();
		if (itemPart != null)
		{
			matID = toolTag.getInteger("Head");

			toolTip.addAll(getToolPartTooltip(itemPart, matID, tool));
			toolTip.add("");
		}

		itemPart = tool.getAccessoryItem();
		if (itemPart != null)
		{
			matID = toolTag.getInteger("Accessory");

			toolTip.addAll(getToolPartTooltip(itemPart, matID, tool));
			toolTip.add("");
		}

		itemPart = tool.getExtraItem();
		if (itemPart != null)
		{
			matID = toolTag.getInteger("Extra");

			toolTip.addAll(getToolPartTooltip(itemPart, matID, tool));
			toolTip.add("");
		}

		itemPart = tool.getHandleItem();
		if (itemPart != null)
		{
			matID = toolTag.getInteger("Handle");

			toolTip.addAll(getToolPartTooltip(itemPart, matID, tool));
		}

		return toolTip;
	}

	/*
	 * 	Tool Part Tool Tip
	 */
	private List<String> getToolPartTooltip(Item itemPart, int matID, ToolCore tool)
	{
		List<String> toolTip = new ArrayList<String>();

		if (itemPart instanceof ToolPart)
		{
			ToolPart part = (ToolPart) itemPart;
			ToolMaterial mat = TConstructRegistry.getMaterial(matID);
			toolTip.add(mat.style() + EnumChatFormatting.UNDERLINE + StringHelper.getLocalizedString("toolpart." + part.partName).replaceAll("%%material", mat.displayName.trim()));
		}
		else if (itemPart instanceof Bowstring)
		{
			Bowstring bowstring = (Bowstring) itemPart;
			ItemStack tempStack = new ItemStack(bowstring, 1, matID);
			toolTip.add(EnumChatFormatting.UNDERLINE + tempStack.getDisplayName());
		}
		else if (itemPart instanceof Fletching)
		{
			Fletching fletching = (Fletching) itemPart;
			ItemStack tempStack = new ItemStack(fletching, 1, matID);
			toolTip.add(EnumChatFormatting.UNDERLINE + tempStack.getDisplayName());
		}
		else
		{
			toolTip.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("tictooltips.unknown.part"));
		}

		List<String> partStats = getMaterialTooltip(matID, itemPart, tool);
		toolTip.addAll(partStats);

		return toolTip;
	}

	/*
	 * 	Armor Tool Tip
	 */
	private List<String> getArmorStatsTooltip(ItemStack itemStack)
	{
		List<String> toolTip = new ArrayList<String>();

		//ArmorCore armor = (ArmorCore) itemStack.getItem();
		NBTTagCompound armorTag = ArmorHelper.getArmorTag(itemStack);

		int modifiersAvailable = armorTag.getInteger("Modifiers");
		toolTip.add(StringHelper.getLocalizedString("gui.toolstation18") + EnumChatFormatting.WHITE + modifiersAvailable);

		return toolTip;
	}

}
