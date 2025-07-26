package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

import static com.momosensei.momotinker.util.PenetratingDamage.reflectionPenetratingDamage;

public class Origin extends momomodifier {
    public Origin() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
    }

    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof ServerPlayer player && a != null) {
            int c = getMainhandModifierlevel(player, MomotinkerModifiers.origin.getId());
            if (c > 0) {
                event.getSource().bypassArmor().bypassMagic().bypassInvul().bypassEnchantments();
                if (a.getHealth() > a.getMaxHealth()) {
                    a.setHealth(a.getMaxHealth());
                }
                a.getAttribute(Attributes.MAX_HEALTH).setBaseValue(a.getMaxHealth() * (1f - 0.1f * c));
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player&&getMainhandModifierlevel(player, MomotinkerModifiers.origin.getId())>0) {
            double a = player.getAttackRange();
            List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(a));
            for (LivingEntity e : list) {
                if (e != null && e != player) {
                    if (e.getMaxHealth() < player.getMaxHealth()) {
                        reflectionPenetratingDamage(e, player, e.getMaxHealth());
                        e.onRemovedFromWorld();
                        e.setPos(Double.NaN, Double.NaN, Double.NaN);
                    }
                }
            }
        }
    }
}