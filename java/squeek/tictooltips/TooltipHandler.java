package squeek.tictooltips;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;
import squeek.tictooltips.helpers.ColorHelper;
import squeek.tictooltips.helpers.PatternHelper;
import squeek.tictooltips.helpers.StringHelper;
import squeek.tictooltips.helpers.ToolHelper;
import squeek.tictooltips.helpers.ToolPartHelper;
import tconstruct.items.Bowstring;
import tconstruct.items.Fletching;
import tconstruct.items.ToolPart;
import tconstruct.library.TConstructRegistry;
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

		// Tools
		if (item instanceof IToolPart)
		{
			if (ToolPartHelper.isShard(item))
				return;

			event.toolTip.addAll(getMaterialTooltip(event.itemStack));
		}
		// Patterns
		else if (item instanceof IPattern)
		{
			event.toolTip.addAll(getPatternTooltip(event.itemStack));
		}
		// Tools
		else if (item instanceof ToolCore && ToolHelper.hasToolTag(event.itemStack))
		{
			int toolTipIndex = event.toolTip.size() > 0 ? Math.max(0, event.toolTip.size() - 2) : 0;

			int i = toolTipIndex;
			while (i < event.toolTip.size() && event.toolTip.get(i).equals(""))
			{
				event.toolTip.remove(i);
				i++;
			}

			boolean ctrlDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
			boolean shiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
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

				toolTipIndex = 1;

				List<String> toolMaterials = getToolMaterialsTooltip(event.itemStack);
				event.toolTip.addAll(toolTipIndex, toolMaterials);
				toolTipIndex += toolMaterials.size();
			}
			// No buttons held
			else
			{
				event.toolTip.add(toolTipIndex++, "Hold " + EnumChatFormatting.YELLOW + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for Stats");
				event.toolTip.add(toolTipIndex++, "Hold " + EnumChatFormatting.DARK_AQUA + EnumChatFormatting.ITALIC + "Ctrl" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for Materials");
			}
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

		if (!mat.ability().equals(""))
			toolTip.add(mat.style() + mat.ability());

		if (mat.shoddy() != 0)
		{
			for (int index = 0; index < toolTip.size(); index++)
			{
				if (toolTip.get(index).contains(StringHelper.getShoddinessTypeString(mat.shoddy())))
				{
					toolTip.set(index, toolTip.get(index) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " [Modifier: " + ToolPartHelper.getShoddinessString(mat.shoddy()) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + "]");
					break;
				}
			}
		}

		if (ToolPartHelper.isArrowHead(item))
		{
			ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation9") + ToolPartHelper.getAccuracyString(arrowMat.accuracy));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation8") + ToolPartHelper.getWeightString(arrowMat.mass / 5f));
		}
		else if (ToolPartHelper.isArrowFletching(item))
		{
			FletchingMaterial fletchingMat = (FletchingMaterial) TConstructRegistry.getCustomMaterial(matID, FletchingMaterial.class);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation9") + ToolPartHelper.getAccuracyString(fletchingMat.accuracy));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation8") + ToolPartHelper.getWeightString(fletchingMat.mass));
		}
		else if (ToolPartHelper.isBowString(item))
		{
			BowstringMaterial bowstringMat = (BowstringMaterial) TConstructRegistry.getCustomMaterial(matID, BowstringMaterial.class);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation6") + ToolPartHelper.getBowStringDrawspeedModifierString(bowstringMat.drawspeedModifier));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getBowStringDurabilityModifierString(bowstringMat.durabilityModifier));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation7") + ToolPartHelper.getBowStringArrowSpeedModifierString(bowstringMat.flightSpeedModifier));
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
				if ((hasTool && (isToolAnArrow || isToolABow)) || (isArrowMat && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))))
				{
					if (TConstructRegistry.validBowMaterial(matID))
					{
						BowMaterial bowMat = (BowMaterial) TConstructRegistry.getBowMaterial(matID);
						toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
						if (!hasTool || isToolABow)
						{
							String prefix = hasTool ? "" : "Bow ";
							toolTip.add(prefix + StatCollector.translateToLocal("gui.toolstation6") + ToolPartHelper.getBowDrawSpeedString(bowMat.drawspeed));
							toolTip.add(prefix + StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getBowDurabilityString(bowMat.durability));
							toolTip.add(prefix + StatCollector.translateToLocal("gui.toolstation7") + ToolPartHelper.getBowArrowSpeedModifierString(bowMat.flightSpeedMax));
						}
					}
					if (TConstructRegistry.validArrowMaterial(matID) && (!hasTool || isToolAnArrow))
					{
						String prefix = hasTool ? "" : StatCollector.translateToLocal("item.InfiTool.Arrow.name") + " ";
						ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);
						toolTip.add(prefix + StatCollector.translateToLocal("gui.toolstation8") + ToolPartHelper.getWeightString(arrowMat.mass));
					}
				}
				else
				{
					toolTip.add(StatCollector.translateToLocal("gui.partcrafter5") + ToolPartHelper.getHandleModifierString(mat.handleModifier));
					if (!hasTool && isArrowMat)
						toolTip.add("Hold " + EnumChatFormatting.YELLOW + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for Bow/Arrow Stats");
				}
			}
			else if (ToolPartHelper.isToolHead(item))
			{
				toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(mat.harvestLevel()));
			}
			else if (ToolPartHelper.isWeaponMiningHead(item))
			{
				toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(mat.harvestLevel()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
			else if (ToolPartHelper.isWeaponToolHead(item))
			{
				toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
			else if (ToolPartHelper.isChiselHead(item))
			{
				toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
			}
			else if (ToolPartHelper.isWeaponHead(item))
			{
				toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
			}
			else if (ToolPartHelper.isPlate(item))
			{
				toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
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
			toolTip.add("Valid Materials:");
			for (String matName : validMats)
			{
				if (toolTip.size() < 7 || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
					toolTip.add(" - " + matName);
				else
				{
					toolTip.add("Hold " + EnumChatFormatting.YELLOW + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for More");
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
		toolTip.add("Repair Material: " + repairMat.style() + repairMat.displayName);

		int maxDurability = ToolHelper.getMaxDurability(toolTag);

		if (maxDurability > 0)
		{
			int curDurability = maxDurability - ToolHelper.getUsedDurability(toolTag);

			String curOfMax = curDurability == maxDurability ? StringHelper.getDurabilityString(maxDurability) : StringHelper.getDurabilityString(curDurability) + " / " + StringHelper.getDurabilityString(maxDurability);
			toolTip.add(StatCollector.translateToLocal("gui.toolstation2") + ColorHelper.getRelativeColor(curDurability, 0, maxDurability) + curOfMax);
		}

		if (isShoddy)
		{
			toolTip.add(shoddinessType + " Modifier: " + ToolPartHelper.getShoddinessString(shoddiness));
		}

		if (ToolHelper.isWeaponTool(tool))
		{
			int damage = ToolHelper.getDamage(tool, toolTag);
			float stoneboundDamage = ToolHelper.getShoddinessDamageBonus(toolTag);
			float maxStoneboundDamage = ToolHelper.getMaxShoddinessDamageBonus(toolTag);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation3") + ColorHelper.getRelativeColor(ToolHelper.getRawDamage(tool, toolTag) + stoneboundDamage, ToolPartHelper.minAttack, ToolPartHelper.maxAttack) + StringHelper.getDamageString(damage));
			if (stoneboundDamage != 0)
			{
				EnumChatFormatting textColor = stoneboundDamage > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (stoneboundDamage > 0 ? StatCollector.translateToLocal("gui.toolstation4") : StatCollector.translateToLocal("gui.toolstation5")) + textColor;
				String maxString = "";
				if (stoneboundDamage == maxStoneboundDamage)
					bonusOrLoss += EnumChatFormatting.BOLD;
				else
					maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[Max: " + StringHelper.getDamageNumberString((int) maxStoneboundDamage) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessType + " " + bonusOrLoss + StringHelper.getDamageString((int) stoneboundDamage) + maxString);
			}
			else if (maxStoneboundDamage != 0 && stoneboundDamage != maxStoneboundDamage)
			{
				String bonusOrLoss = maxStoneboundDamage > 0 ? StatCollector.translateToLocal("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StatCollector.translateToLocal("gui.toolstation5") + EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- Max " + shoddinessType + " " + bonusOrLoss + StringHelper.getDamageString((int) maxStoneboundDamage));
			}
		}

		if (ToolHelper.isBowTool(tool))
		{
			int drawSpeed = ToolHelper.getDrawSpeed(toolTag);
			float arrowSpeedModifier = ToolHelper.getArrowSpeedModifier(toolTag);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation6") + ToolPartHelper.getBowDrawSpeedString(drawSpeed));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation7") + ToolPartHelper.getBowArrowSpeedModifierString(arrowSpeedModifier));
		}

		if (ToolHelper.isAmmoTool(tool))
		{
			int damage = ToolHelper.getAmmoDamage(toolTag);
			float weight = ToolHelper.getWeight(toolTag);
			float accuracy = ToolHelper.getAccuracy(toolTag);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation10") + " " + StringHelper.getDamageString(damage));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation11") + " " + StringHelper.getAmmoDamageRangeString(damage));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation8") + ToolPartHelper.getWeightString(weight));
			toolTip.add(StatCollector.translateToLocal("gui.toolstation9") + ToolPartHelper.getAccuracyString(accuracy));
		}

		if (ToolHelper.isDualHarvestTool(tool))
		{
			int mineSpeed1 = ToolHelper.getPrimaryMiningSpeed(toolTag);
			int mineSpeed2 = ToolHelper.getSecondaryMiningSpeed(toolTag);
			float stoneboundSpeed = ToolHelper.getShoddinessSpeedBonus(toolTag);
			float maxStoneboundSpeed = ToolHelper.getMaxShoddinessSpeedBonus(toolTag);

			mineSpeed1 += stoneboundSpeed * 100f;
			mineSpeed2 += stoneboundSpeed * 100f;

			int harvestLevel1 = ToolHelper.getPrimaryHarvestLevel(toolTag);
			int harvestLevel2 = ToolHelper.getSecondaryHarvestLevel(toolTag);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation12") + ToolPartHelper.getMiningSpeedString(mineSpeed1) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + ", " + ToolPartHelper.getMiningSpeedString(mineSpeed2));
			if (stoneboundSpeed != 0)
			{
				EnumChatFormatting textColor = stoneboundSpeed > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (stoneboundSpeed > 0 ? StatCollector.translateToLocal("gui.toolstation4") : StatCollector.translateToLocal("gui.toolstation5")) + textColor;
				String maxString = "";
				if (stoneboundSpeed == maxStoneboundSpeed)
					bonusOrLoss += EnumChatFormatting.BOLD;
				else
					maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[Max: " + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (stoneboundSpeed * 100f)) + maxString);
			}
			else if (maxStoneboundSpeed != 0 && stoneboundSpeed != maxStoneboundSpeed)
			{
				String bonusOrLoss = maxStoneboundSpeed > 0 ? StatCollector.translateToLocal("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StatCollector.translateToLocal("gui.toolstation5") + EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- Max " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)));
			}

			toolTip.add(StatCollector.translateToLocal("gui.toolstation13") + " " + ToolPartHelper.getHarvestLevelString(harvestLevel1) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + ", " + ToolPartHelper.getHarvestLevelString(harvestLevel2));
		}
		else if (ToolHelper.isHarvestTool(tool))
		{
			int mineSpeed = ToolHelper.getTotalMiningSpeed(toolTag);
			float stoneboundSpeed = ToolHelper.getShoddinessSpeedBonus(toolTag);
			float maxStoneboundSpeed = ToolHelper.getMaxShoddinessSpeedBonus(toolTag);

			mineSpeed += stoneboundSpeed * 100f;

			toolTip.add(StatCollector.translateToLocal("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mineSpeed));
			if (stoneboundSpeed != 0)
			{
				EnumChatFormatting textColor = stoneboundSpeed > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
				String bonusOrLoss = (stoneboundSpeed > 0 ? StatCollector.translateToLocal("gui.toolstation4") : StatCollector.translateToLocal("gui.toolstation5")) + textColor;
				String maxString = "";
				if (stoneboundSpeed == maxStoneboundSpeed)
					bonusOrLoss += EnumChatFormatting.BOLD;
				else
					maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[Max: " + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (stoneboundSpeed * 100f)) + maxString);
			}
			else if (maxStoneboundSpeed != 0 && stoneboundSpeed != maxStoneboundSpeed)
			{
				String bonusOrLoss = maxStoneboundSpeed > 0 ? StatCollector.translateToLocal("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StatCollector.translateToLocal("gui.toolstation5") + EnumChatFormatting.DARK_RED;
				toolTip.add(EnumChatFormatting.DARK_GRAY + "- Max " + shoddinessType + " " + bonusOrLoss + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)));
			}

			int harvestLevel = ToolHelper.getPrimaryHarvestLevel(toolTag);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(harvestLevel));
		}
		else if (ToolHelper.isUtilityTool(tool))
		{
			int mineSpeed = ToolHelper.getPrimaryMiningSpeed(toolTag);

			toolTip.add(StatCollector.translateToLocal("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mineSpeed));
		}

		int modifiers = toolTag.getInteger("Modifiers");
		if (modifiers > 0)
		{
			toolTip.add(StatCollector.translateToLocal("gui.toolstation18") + EnumChatFormatting.WHITE + modifiers);
		}

		if (toolTag.hasKey("Tooltip1"))
			toolTip.add("Modifiers:");

		boolean displayToolTips = true;
		int tipNum = 0;
		int written = 0;
		while (displayToolTips)
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
			toolTip.add(mat.style() + EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("toolpart." + part.partName).replaceAll("%%material ", mat.displayName));
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
			toolTip.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "<Unknown Part>");
		}

		List<String> partStats = getMaterialTooltip(matID, itemPart, tool);
		toolTip.addAll(partStats);

		return toolTip;
	}

}
