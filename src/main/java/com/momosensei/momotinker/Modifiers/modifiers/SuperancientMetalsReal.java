package com.momosensei.momotinker.Modifiers.modifiers;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;

public class SuperancientMetalsReal extends momomodifier{
    public SuperancientMetalsReal() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.5);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.5);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.5);
            ToolStats.ACCURACY.multiply(builder, 1.5);
            ToolStats.DRAW_SPEED.multiply(builder, 1.5);
            ToolStats.VELOCITY.multiply(builder, 1.5);
            ToolStats.MINING_SPEED.multiply(builder, 1.5);
            ToolStats.ARMOR.multiply(builder, 1.5);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.5);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.5);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.5);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.5);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.5);
        }
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (modifier.getLevel()>0){
            return (int) (amount * 0.1f);
        }
        return amount;
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null){
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getFloat(sanctification)==500&&a instanceof Mob mob&&!mob.getTags().contains("BeConquered")) {
                mob.addTag("BeConquered");
            }
            if (a.getTags().contains("BeConquered")) {
                event.setAmount(event.getAmount() * 1.5F);
            }
            if (c.getFloat(degenerate)==100) {
                a.invulnerableTime=0;
                event.getSource().bypassArmor();
                a.invulnerableTime=0;
            }
        }
    }
}