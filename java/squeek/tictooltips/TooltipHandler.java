package squeek.tictooltips;

import java.util.List;

import net.minecraft.item.Item;
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
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.BowstringMaterial;
import tconstruct.library.tools.FletchingMaterial;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.util.IPattern;
import tconstruct.library.util.IToolPart;

public class TooltipHandler {
	
	@ForgeSubscribe
	public void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.entityPlayer == null)
			return;
		
		Item item = event.itemStack.getItem();
		if (item instanceof IToolPart)
		{
			if (ToolPartHelper.isShard(item))
				return;
			
			int matID = ((IToolPart) item).getMaterialID(event.itemStack);
			ToolMaterial mat = TConstructRegistry.getMaterial(matID);
			
			if (!mat.ability().equals(""))
				event.toolTip.add(mat.style()+mat.ability());
			
			if (ToolPartHelper.isArrowHead(item))
			{
				ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);

				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation3")+ToolPartHelper.getAttackString(mat.attack()));
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation9")+ToolPartHelper.getAccuracyString(arrowMat.accuracy));
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation8")+ToolPartHelper.getWeightString(arrowMat.mass / 5f));
			}
			else if (ToolPartHelper.isArrowFletching(item))
			{
				FletchingMaterial fletchingMat = (FletchingMaterial) TConstructRegistry.getCustomMaterial(matID, FletchingMaterial.class);
				
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation9")+ToolPartHelper.getAccuracyString(fletchingMat.accuracy));
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation8")+ToolPartHelper.getWeightString(fletchingMat.mass));
			}
			else if (ToolPartHelper.isBowString(item))
			{
				BowstringMaterial bowstringMat = (BowstringMaterial) TConstructRegistry.getCustomMaterial(matID, BowstringMaterial.class);
				
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation6")+ToolPartHelper.getBowStringDrawspeedModifierString(bowstringMat.drawspeedModifier));
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getBowStringDurabilityModifierString(bowstringMat.durabilityModifier));
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation7")+ToolPartHelper.getBowStringArrowSpeedModifierString(bowstringMat.flightSpeedModifier));
			}
			else
			{
				if (mat.reinforced() > 0)
					event.toolTip.add(mat.style()+StringHelper.getReinforcedString(mat.reinforced()));
	
				if (ToolPartHelper.isRod(item))
				{
					boolean isArrowMat = ToolPartHelper.isArrowRod(item);
					if (isArrowMat && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)))
					{
						if (TConstructRegistry.validBowMaterial(matID))
						{
							BowMaterial bowMat = (BowMaterial) TConstructRegistry.getBowMaterial(matID);
							event.toolTip.add(StatCollector.translateToLocal("gui.toolstation3")+ToolPartHelper.getAttackString(mat.attack()));
							event.toolTip.add("Bow "+StatCollector.translateToLocal("gui.toolstation6")+ToolPartHelper.getBowDrawSpeedString(bowMat.drawspeed));
							event.toolTip.add("Bow "+StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getBowDurabilityString(bowMat.durability));
							event.toolTip.add("Bow "+StatCollector.translateToLocal("gui.toolstation7")+ToolPartHelper.getBowArrowSpeedModifierString(bowMat.flightSpeedMax));
						}
						if (TConstructRegistry.validArrowMaterial(matID))
						{
							ArrowMaterial arrowMat = TConstructRegistry.getArrowMaterial(matID);
							event.toolTip.add(StatCollector.translateToLocal("item.InfiTool.Arrow.name")+" "+StatCollector.translateToLocal("gui.toolstation8")+ToolPartHelper.getWeightString(arrowMat.mass));
						}
					}
					else	
					{
						event.toolTip.add(StatCollector.translateToLocal("gui.partcrafter5")+ToolPartHelper.getHandleModifierString(mat.handleModifier));
						if (isArrowMat)
							event.toolTip.add("Hold "+EnumChatFormatting.YELLOW+EnumChatFormatting.ITALIC+"SHIFT"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" to show bow/arrow stats");
					}
				}
				else if (ToolPartHelper.isToolHead(item))
				{
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getDurabilityString(mat.durability()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation14")+ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation15")+ToolPartHelper.getHarvestLevelString(mat.harvestLevel()));
				}
				else if (ToolPartHelper.isWeaponToolHead(item))
				{
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getDurabilityString(mat.durability()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation16")+ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation3")+ToolPartHelper.getAttackString(mat.attack()));
				}
				else if (ToolPartHelper.isChiselHead(item))
				{
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getDurabilityString(mat.durability()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation16")+ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
				}
				else if (ToolPartHelper.isWeaponHead(item))
				{
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation3")+ToolPartHelper.getAttackString(mat.attack()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getDurabilityString(mat.durability()));
				}
				else if (ToolPartHelper.isPlate(item))
				{
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getDurabilityString(mat.durability()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation14")+ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation3")+ToolPartHelper.getAttackString(mat.attack()));
				}
			}
		}
		else if (item instanceof IPattern)
		{
			List<String> validMats = null;
			
			if (PatternHelper.isBowstringPattern(item, event.itemStack.getItemDamage()))
			{
				validMats = PatternHelper.getValidCustomMaterialsOfType(BowstringMaterial.class);
			}
			else if (PatternHelper.isFletchingPattern(item, event.itemStack.getItemDamage()))
			{
				validMats = PatternHelper.getValidCustomMaterialsOfType(FletchingMaterial.class);
			}

			if (validMats != null && !validMats.isEmpty())
			{
				event.toolTip.add("Valid Materials:");
				for (String matName : validMats)
				{
					if (event.toolTip.size() < 7 || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
						event.toolTip.add(" - "+matName);
					else
					{
						event.toolTip.add("Hold "+EnumChatFormatting.YELLOW+EnumChatFormatting.ITALIC+"SHIFT"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" for more");
						break;
					}
				}
			}
		}
		else if (item instanceof ToolCore && ToolHelper.hasToolTag(event.itemStack))
		{
			int toolTipIndex = event.toolTip.size() > 0 ? Math.max(0, event.toolTip.size() - 2) : 0;
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				ToolCore tool = (ToolCore) item;
				NBTTagCompound toolTag = ToolHelper.getToolTag(event.itemStack);
				
				ToolMaterial repairMat = ToolHelper.getHeadMaterial(toolTag);
				event.toolTip.add(toolTipIndex++, "Repair Material: "+repairMat.style()+repairMat.displayName);
				
				int maxDurability = ToolHelper.getMaxDurability(toolTag);

				if (maxDurability > 0)
				{
					int curDurability = maxDurability - ToolHelper.getUsedDurability(toolTag);
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation2")+ColorHelper.getRelativeColor(curDurability, 0, maxDurability)+StringHelper.getDurabilityString(curDurability)+" / "+StringHelper.getDurabilityString(maxDurability));
				}
				
				if (ToolHelper.isWeaponTool(tool))
				{
					int damage = ToolHelper.getDamage(tool, toolTag);
					float stoneboundDamage = ToolHelper.getStoneboundDamage(toolTag);
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation3")+ColorHelper.getRelativeColor(ToolHelper.getRawDamage(tool, toolTag)+stoneboundDamage, ToolPartHelper.minAttack, ToolPartHelper.maxAttack)+StringHelper.getDamageString(damage));
					if (stoneboundDamage != 0)
					{
						String bonusOrLoss = stoneboundDamage > 0 ? StatCollector.translateToLocal("gui.toolstation4")+EnumChatFormatting.DARK_GREEN : StatCollector.translateToLocal("gui.toolstation5")+EnumChatFormatting.DARK_RED;
						event.toolTip.add(toolTipIndex++, "- "+bonusOrLoss+StringHelper.getDamageString((int) stoneboundDamage));
					}
				}

				if (ToolHelper.isBowTool(tool))
				{
					int drawSpeed = ToolHelper.getDrawSpeed(toolTag);
					float arrowSpeedModifier = ToolHelper.getArrowSpeedModifier(toolTag);
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation6")+ToolPartHelper.getBowDrawSpeedString(drawSpeed));
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation7")+ToolPartHelper.getBowArrowSpeedModifierString(arrowSpeedModifier));
				}
				
				if (ToolHelper.isAmmoTool(tool))
				{
					int damage = ToolHelper.getAmmoDamage(toolTag);
					float weight = ToolHelper.getWeight(toolTag);
					float accuracy = ToolHelper.getAccuracy(toolTag);

					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation10")+" "+StringHelper.getDamageString(damage));
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation11")+" "+StringHelper.getAmmoDamageRangeString(damage));
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation8")+ToolPartHelper.getWeightString(weight));
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation9")+ToolPartHelper.getAccuracyString(accuracy));
				}
				
				if (ToolHelper.isDualHarvestTool(tool))
				{
					int mineSpeed1 = ToolHelper.getPrimaryMiningSpeed(toolTag);
					int mineSpeed2 = ToolHelper.getSecondaryMiningSpeed(toolTag);
					float stoneboundSpeed = ToolHelper.getStoneboundSpeed(toolTag);
					
					mineSpeed1 += stoneboundSpeed*100f;
					mineSpeed2 += stoneboundSpeed*100f;
					
					int harvestLevel1 = ToolHelper.getPrimaryHarvestLevel(toolTag);
					int harvestLevel2 = ToolHelper.getSecondaryHarvestLevel(toolTag);
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation12")+ToolPartHelper.getMiningSpeedString(mineSpeed1) + EnumChatFormatting.RESET+EnumChatFormatting.GRAY + ", " + ToolPartHelper.getMiningSpeedString(mineSpeed2));
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation13")+" "+ToolPartHelper.getHarvestLevelString(harvestLevel1) + EnumChatFormatting.RESET+EnumChatFormatting.GRAY + ", " + ToolPartHelper.getHarvestLevelString(harvestLevel2));
				}
				else if (ToolHelper.isHarvestTool(tool))
				{
					int mineSpeed = ToolHelper.getTotalMiningSpeed(toolTag);
					float stoneboundSpeed = ToolHelper.getStoneboundSpeed(toolTag);
					
					mineSpeed += stoneboundSpeed*100f;
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation14")+ToolPartHelper.getMiningSpeedString(mineSpeed));
					if (stoneboundSpeed != 0)
					{
						String bonusOrLoss = stoneboundSpeed > 0 ? StatCollector.translateToLocal("gui.toolstation4")+EnumChatFormatting.DARK_GREEN : StatCollector.translateToLocal("gui.toolstation5")+EnumChatFormatting.DARK_RED;
						event.toolTip.add(toolTipIndex++, "- "+bonusOrLoss+StringHelper.getSpeedString((int) (stoneboundSpeed*100f)));
					}
					
					int harvestLevel = ToolHelper.getPrimaryHarvestLevel(toolTag);
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation15")+ToolPartHelper.getHarvestLevelString(harvestLevel));
				}
				else if (ToolHelper.isUtilityTool(tool))
				{
					int mineSpeed = ToolHelper.getPrimaryMiningSpeed(toolTag);
					
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation16")+ToolPartHelper.getMiningSpeedString(mineSpeed));
				}
				
				int modifiers = toolTag.getInteger("Modifiers");
				if (modifiers > 0)
				{
					event.toolTip.add(toolTipIndex++, StatCollector.translateToLocal("gui.toolstation18")+EnumChatFormatting.WHITE+modifiers);
				}
				
				if (toolTag.hasKey("Tooltip1"))
					event.toolTip.add(toolTipIndex++, "Modifiers:");

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
							event.toolTip.add(toolTipIndex++, "- " + tipName);
					}
					else
						displayToolTips = false;
				}
			}
			else
			{
				event.toolTip.add(toolTipIndex, "Hold "+EnumChatFormatting.YELLOW+EnumChatFormatting.ITALIC+"SHIFT"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" for stats");
			}
		}
	}
	
}
