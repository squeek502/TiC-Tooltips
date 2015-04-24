package squeek.tictooltips;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import squeek.tictooltips.helpers.ColorHelper;
import squeek.tictooltips.helpers.CompatibilityHelper;
import squeek.tictooltips.helpers.KeyHelper;
import squeek.tictooltips.helpers.PatternHelper;
import squeek.tictooltips.helpers.StringHelper;
import squeek.tictooltips.helpers.ToolHelper;
import squeek.tictooltips.helpers.ToolPartHelper;
import squeek.tictooltips.proxy.ProxyIguanaTweaks;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.*;
import tconstruct.library.util.IPattern;
import tconstruct.library.util.IToolPart;
import tconstruct.library.weaponry.ArrowShaftMaterial;
import tconstruct.library.weaponry.IAmmo;
import tconstruct.library.weaponry.ProjectileWeapon;
import tconstruct.tools.TinkerTools.MaterialID;
import tconstruct.weaponry.weapons.Crossbow;
import tconstruct.weaponry.weapons.LongBow;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TooltipHandler
{

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.entityPlayer == null)
			return;

		if (event.getResult() == Result.DENY)
			return;

		Item item = event.itemStack.getItem();

		// Tool Parts
		if (item instanceof IToolPart)
		{
			if (ToolPartHelper.isShard(item))
				return;

			event.toolTip.addAll(getMaterialTooltip(event.itemStack));
		}
		// Patterns
		else if (item instanceof IPattern && !ModTiCTooltips.hasIguanaTweaks)
		{
			event.toolTip.addAll(getPatternTooltip(event.itemStack));
		}
		// Tools
		else if (item instanceof ToolCore && ToolHelper.hasToolTag(event.itemStack))
		{
			String plusPrefix = "\u00A79+";
			String maxDamagePrefix = StatCollector.translateToLocal("attribute.name.ammo.maxAttackDamage");
			int toolTipIndex = 0;
			if (event.toolTip.size() > 1)
			{
				toolTipIndex = -1;

				// skip to the last + modifier if it's easy to find
				if (event.toolTip.get(event.toolTip.size() - 1).startsWith(plusPrefix))
					toolTipIndex = event.toolTip.size() - 1;
				// weapons with ammo remove the + and add a Max Damage line, so look for that
				else if (event.toolTip.get(event.toolTip.size() - 1).startsWith(maxDamagePrefix))
				{
					toolTipIndex = event.toolTip.size() - 1;
				}
				// otherwise skip to the first enchant string
				else if (event.itemStack.isItemEnchanted())
				{
					NBTTagList enchantTagList = event.itemStack.getEnchantmentTagList();
					short enchantID = ((NBTTagCompound) enchantTagList.getCompoundTagAt(0)).getShort("id");
					short enchantLevel = ((NBTTagCompound) enchantTagList.getCompoundTagAt(0)).getShort("lvl");
					String enchantName = Enchantment.enchantmentsList[enchantID].getTranslatedName(enchantLevel);
					toolTipIndex = event.toolTip.indexOf(enchantName);
				}
				// otherwise skip to the first + modifier or Max Damage line
				else
				{
					for (int toolTipSearchIndex = 0; toolTipSearchIndex < event.toolTip.size(); toolTipSearchIndex++)
					{
						if (event.toolTip.get(toolTipSearchIndex).startsWith(plusPrefix) || event.toolTip.get(toolTipSearchIndex).startsWith(maxDamagePrefix))
						{
							toolTipIndex = toolTipSearchIndex;
							break;
						}
					}
				}

				// as a last resort, skip to the end of the TiC additions (potentially expensive)
				if (toolTipIndex == -1)
				{
					List<String> tinkersTooltip = new ArrayList<String>();
					((ToolCore) item).addInformation(event.itemStack, event.entityPlayer, tinkersTooltip, event.showAdvancedItemTooltips);
					toolTipIndex = Math.max(0, (tinkersTooltip.size() > 0 ? Math.min(event.toolTip.size(), event.toolTip.indexOf(tinkersTooltip.get(0)) + tinkersTooltip.size()) : event.toolTip.size()) - 1);
				}
			}

			// remove the Max Damage string
			if (toolTipIndex > 1 && event.toolTip.get(toolTipIndex).startsWith(maxDamagePrefix))
			{
				event.toolTip.remove(toolTipIndex);
				// skip to above the ammo line that accompanies the max damage line
				toolTipIndex -= 1;
			}

			// remove any preceding Max Damage strings
			while (toolTipIndex > 1 && event.toolTip.get(toolTipIndex - 1).startsWith(maxDamagePrefix))
			{
				event.toolTip.remove(toolTipIndex - 1);
				// skip to above the ammo line that accompanies the max damage line
				toolTipIndex -= 2;
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
				// add a gap if Iguana Tweaks is present
				if (ModTiCTooltips.hasIguanaTweaks)
					event.toolTip.add(toolTipIndex, "");

				event.toolTip.add(toolTipIndex++, StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.stats", EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
				event.toolTip.add(toolTipIndex++, StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.materials", EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "Ctrl" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
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

		if (mat == null)
			return toolTip;

		if (!mat.ability().equals(""))
			toolTip.add(mat.style() + CompatibilityHelper.getLocalizedAbility(mat));

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
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));

			ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);
			boolean isToolAnArrow = hasTool && ToolHelper.isProjectile(tool) && ToolHelper.isAmmo(tool);
			if (arrowMat != null && (!hasTool || isToolAnArrow))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation22") + ToolPartHelper.getBreakChanceString(arrowMat.breakChance));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(arrowMat.mass));
			}
		}
		else if (ToolPartHelper.isArrowFletching(item))
		{
			FletchingMaterial fletchingMat = CompatibilityHelper.getFletchingMaterial(matID);

			if (fletchingMat != null)
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityModifierString(fletchingMat.durabilityModifier));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation22") + ToolPartHelper.getBreakChanceString(fletchingMat.breakChance));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation9") + ToolPartHelper.getAccuracyString(fletchingMat.accuracy));
			}
		}
		else if (ToolPartHelper.isBowString(item))
		{
			BowstringMaterial bowstringMat = (BowstringMaterial) TConstructRegistry.getCustomMaterial(matID, BowstringMaterial.class);

			if (bowstringMat != null)
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation6") + ToolPartHelper.getBowStringDrawspeedModifierString(bowstringMat.drawspeedModifier));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getBowStringDurabilityModifierString(bowstringMat.durabilityModifier));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getBowStringArrowSpeedModifierString(bowstringMat.flightSpeedModifier));
			}
		}
		else
		{
			if (mat.reinforced() > 0)
				toolTip.add(mat.style() + StringHelper.getReinforcedString(mat.reinforced()));

			if (ToolPartHelper.isRod(item) || ToolPartHelper.isArrowShaft(item))
			{
				ArrowShaftMaterial arrowShaftMat = (ArrowShaftMaterial) TConstructRegistry.getCustomMaterial(matID, ArrowShaftMaterial.class);

				// this is terribly hacky but I'm not sure what a proper fix would look like.
				// material ids for reed/blaze arrow shafts overlap with iron/flint, 
				// so just ignore anything above stone
				boolean isArrowMat = arrowShaftMat != null && matID <= MaterialID.Wood && ToolPartHelper.isArrowRod(item);
				boolean isToolAnArrow = hasTool && ToolHelper.isProjectile(tool) && ToolHelper.isAmmo(tool);

				if (isToolAnArrow || (isArrowMat && KeyHelper.isShiftKeyDown()))
				{
					if (arrowShaftMat != null && (!hasTool || isToolAnArrow))
					{
						toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityModifierString(arrowShaftMat.durabilityModifier));
						toolTip.add(StringHelper.getLocalizedString("gui.toolstation22") + ToolPartHelper.getBreakChanceString(arrowShaftMat.fragility));
						toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(arrowShaftMat.weight));
					}
				}
				else
				{
					String durabilityModifierTitle = StatCollector.translateToLocalFormatted("tictooltips.material.durability.modifier", StringHelper.getLocalizedString("gui.toolstation2").replaceFirst(": ", ""));
					toolTip.add(durabilityModifierTitle + ToolPartHelper.getHandleModifierString(mat.handleModifier));
					if (!hasTool && isArrowMat)
						toolTip.add(StatCollector.translateToLocalFormatted("tictooltips.hold.key.for.bow.stats", EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY));
				}
			}
			else if (ToolPartHelper.isToolHead(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));

				int harvestLevel = mat.harvestLevel();
				if (ModTiCTooltips.hasIguanaTweaks)
					harvestLevel = ProxyIguanaTweaks.getUnboostedHarvestLevel(item, harvestLevel);

				toolTip.add(StringHelper.getLocalizedString("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(harvestLevel));
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
			else if (ToolPartHelper.isWeaponHead(item) || ToolPartHelper.isShurikenPart(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
			}
			else if (ToolPartHelper.isPlate(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				if (!hasTool || ToolHelper.isHarvestTool(tool))
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				if (!hasTool || ToolHelper.isWeapon(tool))
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ToolPartHelper.getAttackString(mat.attack()));
			}
			else if (ToolPartHelper.isBowLimb(item) || ToolPartHelper.isCrossbowLimb(item) || ToolPartHelper.isCrossbowBody(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));
				BowMaterial bowMat = TConstructRegistry.getBowMaterial(matID);
				if (bowMat != null)
				{
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation6") + ToolPartHelper.getBowDrawSpeedString(bowMat.drawspeed));
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getArrowSpeedString(bowMat.flightSpeedMax));
				}
			}
			else if (ToolPartHelper.isBoltPart(item))
			{
				toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ToolPartHelper.getDurabilityString(mat.durability()));

				ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);
				if (arrowMat != null)
				{
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation22") + ToolPartHelper.getBreakChanceString(arrowMat.breakChance));
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(arrowMat.mass));
				}
			}
			// tough bindings and full guards, specifically, count as 'handles'
			// apparently this is due to them having 3 material cost, but
			// that is arbitrary, it's not *actually* due to them having 3 material cost
			// as far as the code is concerned; that is just the justification mDiyo used
			else if (ToolPartHelper.isToughBinding(item) || ToolPartHelper.isFullWeaponGuard(item))
			{
				String durabilityModifierTitle = StatCollector.translateToLocalFormatted("tictooltips.material.durability.modifier", StringHelper.getLocalizedString("gui.toolstation2").replaceFirst(": ", ""));
				toolTip.add(durabilityModifierTitle + ToolPartHelper.getHandleModifierString(mat.handleDurability()));
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
			validMats.addAll(PatternHelper.getValidCustomMaterialsOfType(FletchlingLeafMaterial.class));
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

	public List<String> getDurabilityTooltip(NBTTagCompound toolTag, ItemStack itemStack)
	{
		List<String> toolTip = new ArrayList<String>();

		int maxDurability = ToolHelper.getMaxDurability(toolTag);

		if (maxDurability > 0)
		{
			int curDurability = maxDurability - ToolHelper.getUsedDurability(toolTag);
			int effectiveDurability = ToolHelper.getEffectiveDurability(toolTag);

			String curOfMax = curDurability == maxDurability ? StringHelper.getDurabilityString(maxDurability) : StringHelper.getDurabilityString(curDurability) + " / " + StringHelper.getDurabilityString(maxDurability);
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation2") + ColorHelper.getRelativeColor(curDurability, 0, maxDurability) + curOfMax);

			if (maxDurability != effectiveDurability)
			{
				String effectiveDurabilityTitle = StatCollector.translateToLocalFormatted("tictooltips.tool.effective.durability", StringHelper.getLocalizedString("gui.toolstation2"));
				String effectiveDurabilityColor = ColorHelper.getRelativeColor(ToolHelper.getReinforcedLevel(toolTag), ToolPartHelper.minReinforcedLevel - 3, ToolPartHelper.maxReinforcedLevel);
				String effectiveDurabilityValue = StringHelper.getDurabilityString(effectiveDurability);
				toolTip.add(effectiveDurabilityTitle + effectiveDurabilityColor + effectiveDurabilityValue);
			}
		}

		return toolTip;
	}

	public List<String> getAmmoCountTooltip(IAmmo ammo, NBTTagCompound toolTag, ItemStack itemStack)
	{
		List<String> toolTip = new ArrayList<String>();

		int maxAmmo = ammo.getMaxAmmo(itemStack);

		if (maxAmmo > 0)
		{
			int curAmmo = ammo.getAmmoCount(itemStack);

			String curOfMax = curAmmo == maxAmmo ? StringHelper.getAmmoCountString(maxAmmo) : StringHelper.getAmmoCountString(curAmmo) + " / " + StringHelper.getAmmoCountString(maxAmmo);
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation21") + ColorHelper.getRelativeColor(curAmmo, 0, maxAmmo) + curOfMax);
		}

		return toolTip;
	}

	public List<String> getWeaponTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		return getWeaponTooltip(tool, toolTag, 1.0f);
	}

	public List<String> getWeaponTooltip(ToolCore tool, NBTTagCompound toolTag, float damageModifier)
	{
		List<String> toolTip = new ArrayList<String>();

		int damage = ToolHelper.getDamage(tool, toolTag, damageModifier);
		float stoneboundDamage = ToolHelper.hasDurability(tool) ? ToolHelper.getShoddinessDamageBonus(tool, toolTag) : 0f;
		float maxStoneboundDamage = ToolHelper.hasDurability(tool) ? ToolHelper.getMaxShoddinessDamageBonus(tool, toolTag) : 0f;
		int smiteDamageRange[] = ToolHelper.getSmiteDamageRange(tool, toolTag);
		int antiSpiderDamageRange[] = ToolHelper.getAntiSpiderDamageRange(tool, toolTag);
		int burnDuration = ToolHelper.getBurnDuration(tool, toolTag);
		float chanceToBehead = ToolHelper.getChanceToBehead(tool, toolTag);
		float shoddiness = ToolHelper.getStonebound(toolTag);
		String shoddinessType = StringHelper.getShoddinessTypeString(shoddiness);
		String shoddinessCode = StringHelper.getShoddinessTypeCode(shoddiness);
		String bonusOrLossCode = stoneboundDamage > 0 ? "bonus" : "loss";
		float knockback = ToolHelper.getKnockback(tool, toolTag);
		int sprintDamage = ToolHelper.hasDurability(tool) ? ToolHelper.getSprintDamage(tool, toolTag) : damage;

		String damageColor = ColorHelper.getRelativeColor(ToolHelper.getRawDamage(tool, toolTag) + stoneboundDamage, ToolPartHelper.minAttack, ToolPartHelper.maxAttack);
		toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + damageColor + StringHelper.getDamageString(damage));
		if (sprintDamage != damage)
		{
			String sprintDamageColor = ColorHelper.getRelativeColor(ToolHelper.getRawDamage(tool, toolTag) + stoneboundDamage + (sprintDamage - damage), ToolPartHelper.minAttack, ToolPartHelper.maxAttack);
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.tool.sprint.damage") + sprintDamageColor + StringHelper.getDamageString(sprintDamage));
		}
		if (stoneboundDamage != 0)
		{
			EnumChatFormatting textColor = stoneboundDamage > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
			String bonusOrLoss = (stoneboundDamage > 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5")) + textColor;
			String shoddinessBonusOrLossTitle = StatCollector.translateToLocalFormatted("tictooltips.tool." + shoddinessCode + "." + bonusOrLossCode, shoddinessType, bonusOrLoss);
			String maxString = "";
			if (stoneboundDamage == maxStoneboundDamage)
				bonusOrLoss += EnumChatFormatting.BOLD;
			else
				maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[" + StatCollector.translateToLocal("tictooltips.maximum") + ": " + StringHelper.getDamageNumberString((int) maxStoneboundDamage) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessBonusOrLossTitle + StringHelper.getDamageString((int) stoneboundDamage) + maxString);
		}
		else if (maxStoneboundDamage != 0 && stoneboundDamage != maxStoneboundDamage)
		{
			String bonusOrLoss = maxStoneboundDamage > 0 ? StringHelper.getLocalizedString("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StringHelper.getLocalizedString("gui.toolstation5") + EnumChatFormatting.DARK_RED;
			String maxShoddinessBonusOrLossTitle = StatCollector.translateToLocalFormatted("tictooltips.tool.max." + shoddinessCode + "." + bonusOrLossCode, StatCollector.translateToLocal("tictooltips.maximum"), shoddinessType, bonusOrLoss);
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + maxShoddinessBonusOrLossTitle + StringHelper.getDamageString((int) maxStoneboundDamage));
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
		if (knockback != 0)
		{
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocal("tictooltips.tool.knockback") + ColorHelper.getRelativeColor(knockback, 0.0f, 3.0f) + StringHelper.getKnockbackString(knockback));
		}

		return toolTip;
	}

	public List<String> getHarvestTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		int mineSpeed = ToolHelper.getTotalMiningSpeed(tool, toolTag);
		float stoneboundSpeed = ToolHelper.getShoddinessSpeedBonus(tool, toolTag);
		float maxStoneboundSpeed = ToolHelper.getMaxShoddinessSpeedBonus(tool, toolTag);
		float shoddiness = ToolHelper.getStonebound(toolTag);
		String shoddinessType = StringHelper.getShoddinessTypeString(shoddiness);
		String shoddinessCode = StringHelper.getShoddinessTypeCode(shoddiness);
		String bonusOrLossCode = stoneboundSpeed > 0 ? "bonus" : "loss";

		mineSpeed += stoneboundSpeed * 100f;

		toolTip.add(StringHelper.getLocalizedString("gui.toolstation14") + ToolPartHelper.getMiningSpeedString(mineSpeed));
		if (stoneboundSpeed != 0)
		{
			EnumChatFormatting textColor = stoneboundSpeed > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
			String bonusOrLoss = (stoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5")) + textColor;
			String shoddinessBonusOrLossTitle = StatCollector.translateToLocalFormatted("tictooltips.tool." + shoddinessCode + "." + bonusOrLossCode, shoddinessType, bonusOrLoss);
			String maxString = "";
			if (stoneboundSpeed == maxStoneboundSpeed)
				bonusOrLoss += EnumChatFormatting.BOLD;
			else
				maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[" + StatCollector.translateToLocal("tictooltips.maximum") + ": " + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessBonusOrLossTitle + StringHelper.getSpeedString((int) (stoneboundSpeed * 100f)) + maxString);
		}
		else if (maxStoneboundSpeed != 0 && stoneboundSpeed != maxStoneboundSpeed)
		{
			String bonusOrLoss = maxStoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StringHelper.getLocalizedString("gui.toolstation5") + EnumChatFormatting.DARK_RED;
			String maxShoddinessBonusOrLossTitle = StatCollector.translateToLocalFormatted("tictooltips.tool.max." + shoddinessCode + "." + bonusOrLossCode, StatCollector.translateToLocal("tictooltips.maximum"), shoddinessType, bonusOrLoss);
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + maxShoddinessBonusOrLossTitle + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)));
		}

		if (!ModTiCTooltips.hasIguanaTweaks)
		{
			int harvestLevel = ToolHelper.getPrimaryHarvestLevel(toolTag);

			toolTip.add(StringHelper.getLocalizedString("gui.toolstation15") + ToolPartHelper.getHarvestLevelString(harvestLevel));
		}

		return toolTip;
	}

	public List<String> getDualHarvestTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		int mineSpeed1 = ToolHelper.getPrimaryMiningSpeed(tool, toolTag);
		int mineSpeed2 = ToolHelper.getSecondaryMiningSpeed(tool, toolTag);
		float stoneboundSpeed = ToolHelper.getShoddinessSpeedBonus(tool, toolTag);
		float maxStoneboundSpeed = ToolHelper.getMaxShoddinessSpeedBonus(tool, toolTag);
		float shoddiness = ToolHelper.getStonebound(toolTag);
		String shoddinessType = StringHelper.getShoddinessTypeString(shoddiness);
		String shoddinessCode = StringHelper.getShoddinessTypeCode(shoddiness);
		String bonusOrLossCode = stoneboundSpeed > 0 ? "bonus" : "loss";

		mineSpeed1 += stoneboundSpeed * 100f;
		mineSpeed2 += stoneboundSpeed * 100f;

		int harvestLevel1 = ToolHelper.getPrimaryHarvestLevel(toolTag);
		int harvestLevel2 = ToolHelper.getSecondaryHarvestLevel(toolTag);

		toolTip.add(StringHelper.getLocalizedString("gui.toolstation12") + ToolPartHelper.getMiningSpeedString(mineSpeed1) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + ", " + ToolPartHelper.getMiningSpeedString(mineSpeed2));
		if (stoneboundSpeed != 0)
		{
			EnumChatFormatting textColor = stoneboundSpeed > 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
			String bonusOrLoss = (stoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5")) + textColor;
			String shoddinessBonusOrLossTitle = StatCollector.translateToLocalFormatted("tictooltips.tool." + shoddinessCode + "." + bonusOrLossCode, shoddinessType, bonusOrLoss);
			String maxString = "";
			if (stoneboundSpeed == maxStoneboundSpeed)
				bonusOrLoss += EnumChatFormatting.BOLD;
			else
				maxString = EnumChatFormatting.RESET + " " + EnumChatFormatting.DARK_GRAY + "[" + StatCollector.translateToLocal("tictooltips.maximum") + ": " + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)) + EnumChatFormatting.RESET + EnumChatFormatting.DARK_GRAY + "]";
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + shoddinessBonusOrLossTitle + StringHelper.getSpeedString((int) (stoneboundSpeed * 100f)) + maxString);
		}
		else if (maxStoneboundSpeed != 0 && stoneboundSpeed != maxStoneboundSpeed)
		{
			String bonusOrLoss = maxStoneboundSpeed > 0 ? StringHelper.getLocalizedString("gui.toolstation4") + EnumChatFormatting.DARK_GREEN : StringHelper.getLocalizedString("gui.toolstation5") + EnumChatFormatting.DARK_RED;
			String maxShoddinessBonusOrLossTitle = StatCollector.translateToLocalFormatted("tictooltips.tool.max." + shoddinessCode + "." + bonusOrLossCode, StatCollector.translateToLocal("tictooltips.maximum"), shoddinessType, bonusOrLoss);
			toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + maxShoddinessBonusOrLossTitle + StringHelper.getSpeedString((int) (maxStoneboundSpeed * 100f)));
		}

		toolTip.add(StringHelper.getLocalizedString("gui.toolstation13") + " " + ToolPartHelper.getHarvestLevelString(harvestLevel1) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + ", " + ToolPartHelper.getHarvestLevelString(harvestLevel2));

		return toolTip;
	}

	public List<String> getShoddinessTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		float shoddiness = ToolHelper.getStonebound(toolTag);
		boolean isShoddy = shoddiness != 0;

		if (isShoddy)
		{
			String shoddinessModifierTitle;
			String shoddinessCode = StringHelper.getShoddinessTypeCode(shoddiness);
			if (StatCollector.canTranslate("tictooltips.tool." + shoddinessCode + ".modifier"))
				shoddinessModifierTitle = StatCollector.translateToLocal("tictooltips.tool." + shoddinessCode + ".modifier");
			else
			{
				String shoddinessType = StringHelper.getShoddinessTypeString(shoddiness);
				shoddinessModifierTitle = StatCollector.translateToLocalFormatted("tictooltips.tool.shoddiness.modifier", shoddinessType);
			}
			toolTip.add(shoddinessModifierTitle + ToolPartHelper.getShoddinessString(shoddiness));
		}

		return toolTip;
	}

	public List<String> getBowTooltip(ToolCore tool, NBTTagCompound toolTag, ItemStack itemStack)
	{
		List<String> toolTip = new ArrayList<String>();

		if (itemStack != null && tool instanceof ProjectileWeapon)
		{
			ProjectileWeapon projectileWeapon = (ProjectileWeapon) tool;
			ItemStack currentAmmo = projectileWeapon.searchForAmmo(FMLClientHandler.instance().getClientPlayerEntity(), itemStack);
			if (currentAmmo != null)
			{
				if (ToolHelper.hasToolTag(currentAmmo) && currentAmmo.getItem() instanceof ToolCore)
				{
					toolTip.addAll(getWeaponTooltip((ToolCore) currentAmmo.getItem(), ToolHelper.getToolTag(currentAmmo), projectileWeapon.getProjectileSpeed(itemStack)));

					float[] critDamageRange = ToolHelper.getCriticalDamageRange((ToolCore) currentAmmo.getItem(), ToolHelper.getToolTag(currentAmmo), projectileWeapon.getProjectileSpeed(itemStack));
					if (critDamageRange[1] != 0)
					{
						EnumChatFormatting textColor = critDamageRange[0] >= 0 ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED;
						String bonusOrLoss = (critDamageRange[0] >= 0 ? StringHelper.getLocalizedString("gui.toolstation4") : StringHelper.getLocalizedString("gui.toolstation5"));
						bonusOrLoss = bonusOrLoss.substring(0, bonusOrLoss.length() - 2);
						toolTip.add(EnumChatFormatting.DARK_GRAY + "- " + StatCollector.translateToLocalFormatted("tictooltips.tool.crit.damage", bonusOrLoss) + textColor + StringHelper.getDamageNumberString(critDamageRange[0]) + "-" + StringHelper.getDamageString(critDamageRange[1]));
					}
				}
				else if (currentAmmo.getItem() == Items.arrow)
				{
					int damage = (int) (2 * projectileWeapon.getProjectileSpeed(itemStack));
					toolTip.add(StringHelper.getLocalizedString("gui.toolstation3") + ColorHelper.getRelativeColor(damage, ToolPartHelper.minAttack, ToolPartHelper.maxAttack) + StringHelper.getDamageString(damage));
				}
			}
		}

		int drawSpeed = ToolHelper.getDrawSpeed(tool, toolTag);
		float arrowSpeed = ToolHelper.getArrowSpeed(tool, toolTag);

		toolTip.add(StringHelper.getLocalizedString("gui.toolstation6") + ToolPartHelper.getBowDrawSpeedString(drawSpeed));
		if (tool instanceof Crossbow || tool instanceof LongBow)
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getArrowSpeedString(arrowSpeed, 1.5f));
		else
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation7") + ToolPartHelper.getArrowSpeedString(arrowSpeed));

		return toolTip;
	}

	public List<String> getAmmoTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();
		return toolTip;
	}

	public List<String> getUtilityTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		int mineSpeed = ToolHelper.getPrimaryMiningSpeed(tool, toolTag);

		toolTip.add(StringHelper.getLocalizedString("gui.toolstation16") + ToolPartHelper.getMiningSpeedString(mineSpeed));

		return toolTip;
	}

	public List<String> getThrownTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		return toolTip;
	}

	public List<String> getProjectileTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		float weight = ToolHelper.getWeight(toolTag);
		float accuracy = ToolHelper.getAccuracy(toolTag);
		float breakChance = ToolHelper.getBreakChance(toolTag);

		toolTip.add(StringHelper.getLocalizedString("gui.toolstation8") + ToolPartHelper.getWeightString(weight));
		toolTip.add(StringHelper.getLocalizedString("gui.toolstation9") + ToolPartHelper.getAccuracyString(accuracy));
		toolTip.add(StringHelper.getLocalizedString("gui.toolstation22") + ToolPartHelper.getBreakChanceString(breakChance));

		return toolTip;
	}

	public List<String> getModifiersTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		int modifiersAvailable = toolTag.getInteger("Modifiers");
		if (modifiersAvailable > 0)
		{
			toolTip.add(StringHelper.getLocalizedString("gui.toolstation18") + EnumChatFormatting.WHITE + modifiersAvailable);
		}

		List<String> modifierToolTips = new ArrayList<String>();

		// the following logic is taken from tconstruct/tools/gui/ToolStationGuiHelper.drawModifiers
		int tipNum = 1;
		while (toolTag.hasKey("ModifierTip" + tipNum))
		{
			String tipName = toolTag.getString("ModifierTip" + tipNum);
			String locString = "modifier.toolstation." + tipName;
			// strip out the '(X of Y)' in some for the localization strings.. sigh
			int bracket = tipName.indexOf("(");
			if (bracket > 0)
				locString = "modifier.toolstation." + tipName.substring(0, bracket);
			locString = EnumChatFormatting.getTextWithoutFormattingCodes(locString.replace(" ", ""));
			if (StatCollector.canTranslate(locString))
			{
				tipName = tipName.replace(EnumChatFormatting.getTextWithoutFormattingCodes(tipName), StatCollector.translateToLocal(locString));
				// re-add the X/Y
				if (bracket > 0)
					tipName += " " + toolTag.getString("ModifierTip" + tipNum).substring(bracket);
			}
			if (!tipName.trim().equals(""))
				modifierToolTips.add(EnumChatFormatting.DARK_GRAY + "- " + tipName);
			tipNum++;
		}

		if (!modifierToolTips.isEmpty())
		{
			toolTip.add(StatCollector.translateToLocal("gui.toolstation17") + ":");
			toolTip.addAll(modifierToolTips);
		}

		return toolTip;
	}

	public List<String> getRepairTooltip(ToolCore tool, NBTTagCompound toolTag)
	{
		List<String> toolTip = new ArrayList<String>();

		ToolMaterial repairMat = ToolHelper.getHeadMaterial(toolTag);
		if (repairMat != null)
		{
			toolTip.add(StatCollector.translateToLocal("tictooltips.tool.repair.material") + repairMat.style() + CompatibilityHelper.getLocalizedName(repairMat));
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

		toolTip.addAll(getRepairTooltip(tool, toolTag));

		if (ToolHelper.hasAmmoCount(tool))
			toolTip.addAll(getAmmoCountTooltip((IAmmo) tool, toolTag, itemStack));
		else if (ToolHelper.hasDurability(tool))
		{
			toolTip.addAll(getDurabilityTooltip(toolTag, itemStack));
			toolTip.addAll(getShoddinessTooltip(tool, toolTag));
		}

		if (ToolHelper.isBow(tool))
			toolTip.addAll(getBowTooltip(tool, toolTag, itemStack));
		else if (ToolHelper.isWeapon(tool))
			toolTip.addAll(getWeaponTooltip(tool, toolTag));

		if (ToolHelper.isThrown(tool))
			toolTip.addAll(getThrownTooltip(tool, toolTag));

		if (ToolHelper.isAmmo(tool))
			toolTip.addAll(getAmmoTooltip(tool, toolTag));

		if (ToolHelper.isProjectile(tool))
			toolTip.addAll(getProjectileTooltip(tool, toolTag));

		if (ToolHelper.isDualHarvestTool(tool))
			toolTip.addAll(getDualHarvestTooltip(tool, toolTag));
		else if (ToolHelper.isHarvestTool(tool))
			toolTip.addAll(getHarvestTooltip(tool, toolTag));
		else if (ToolHelper.isUtilityTool(tool))
			toolTip.addAll(getUtilityTooltip(tool, toolTag));

		toolTip.addAll(getModifiersTooltip(tool, toolTag));

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

		String matStyle = "";
		if (!ToolPartHelper.hasCustomMaterial(itemPart))
		{
			ToolMaterial mat = TConstructRegistry.getMaterial(matID);
			matStyle = mat != null ? mat.style() : "";
		}
		ItemStack tempStack = new ItemStack(itemPart, 1, matID);
		toolTip.add(matStyle + EnumChatFormatting.UNDERLINE + tempStack.getDisplayName());

		List<String> partStats = getMaterialTooltip(matID, itemPart, tool);
		toolTip.addAll(partStats);

		return toolTip;
	}

}
