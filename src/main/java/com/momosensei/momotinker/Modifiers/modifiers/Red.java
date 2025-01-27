package com.momosensei.momotinker.Modifiers.modifiers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class Red extends momomodifier {
    public Red() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {

        if (entity instanceof ServerPlayer) {
            for (LivingEntity e : entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate((double) 20.0F), (ex) -> ex instanceof Enemy)) {
                e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120));
            }
        }
    }
}




