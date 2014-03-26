package squeek.tictooltips;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import squeek.tictooltips.helpers.PatternHelper;
import squeek.tictooltips.helpers.ToolPartHelper;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.BowstringMaterial;
import tconstruct.library.tools.FletchingMaterial;
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
				event.toolTip.add(StatCollector.translateToLocal("gui.toolstation8")+ToolPartHelper.getWeightString(arrowMat.mass));
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
					event.toolTip.add(mat.style()+ToolPartHelper.getReinforcedString(mat.reinforced()));
	
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
	}
	
}
