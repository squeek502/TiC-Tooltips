package squeek.tictooltips;

import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import squeek.tictooltips.helpers.ToolPartHelper;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.BowstringMaterial;
import tconstruct.library.tools.FletchingMaterial;
import tconstruct.library.tools.ToolMaterial;
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
					if (isArrowMat && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
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
							event.toolTip.add("Hold "+EnumChatFormatting.ITALIC+EnumChatFormatting.YELLOW+"SHIFT"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" to show bow/arrow stats");
					}
				}
				else if (ToolPartHelper.isToolHead(item))
				{
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation2")+ToolPartHelper.getDurabilityString(mat.durability()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation14")+ToolPartHelper.getMiningSpeedString(mat.toolSpeed()));
					event.toolTip.add(StatCollector.translateToLocal("gui.toolstation15")+ToolPartHelper.getHarvestLevelString(mat.harvestLevel()));
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
	}
	
}
