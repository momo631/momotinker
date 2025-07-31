package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;


public class TimeEchoes extends momomodifier {
    public TimeEchoes() {
    }

    public static final ResourceLocation echopoints = Momotinker.getResource("echopoints");
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(echopoints);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player&&player.tickCount%100==0&&tool.getStats().get(ToolStats.DURABILITY)>500) {
            ModDataNBT a = tool.getPersistentData();
            a.putInt(echopoints, a.getInt(echopoints) + 1);
            ToolStack.from(stack).rebuildStats();
        }
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ModDataNBT a = (ModDataNBT) context.getPersistentData();
        int b =a.getInt(echopoints);
        if (b > 0){
            ToolStats.DURABILITY.add(builder, -b*20);
            ToolStats.ATTACK_SPEED.add(builder, b*0.002f);
            ToolStats.ATTACK_DAMAGE.add(builder, b*0.1f);
            ToolStats.ACCURACY.add(builder, b*0.1f);
            if (builder.getStat(ToolStats.DRAW_SPEED)>b*0.005f) {
                ToolStats.DRAW_SPEED.add(builder, -b * 0.005f);
            }else {ToolStats.DRAW_SPEED.add(builder, -builder.getStat(ToolStats.DRAW_SPEED));}
            ToolStats.VELOCITY.add(builder, b*0.01f);
            ToolStats.MINING_SPEED.add(builder, b*0.01f);
            ToolStats.ARMOR.add(builder, b*0.2f);
            ToolStats.ARMOR_TOUGHNESS.add(builder, b*0.1f);
            ToolStats.PROJECTILE_DAMAGE.add(builder, b*0.05f);
            ToolStats.KNOCKBACK_RESISTANCE.add(builder, b*0.01f);
            ToolStats.BLOCK_AMOUNT.add(builder, b*0.005f);
            ToolStats.BLOCK_ANGLE.add(builder, b*0.005f);
        }
    }
}