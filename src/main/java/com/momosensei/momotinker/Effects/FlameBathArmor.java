package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlameBathArmor extends StaticEffect {
    public FlameBathArmor() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        super.addAttributeModifier(Attributes.ARMOR, "94168FB1-54A3-EE85-49C7-395B48E46C04", +0.6, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "77D9BF62-CD92-B29E-2208-C37A877FB151", +0.4, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.flamebatharmor";
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof ServerPlayer player && player.level instanceof ServerLevel serverLevel&&player.getEffect(MomotinkerEffects.FlameBathArmor.get())!=null) {
            for (int i = 0; i <= 120; i++) {
                if (player.getEffect(MomotinkerEffects.FlameBathArmor.get()).getDuration()>0&&player.tickCount%100==0){
                    serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 1.5);
                    List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4));
                    for (LivingEntity entity : list) {
                        if (entity != null) {
                            Vec3 vec = entity.position().subtract(player.position()).normalize().scale(0.004+0.003*amplifier);
                            entity.push(vec.x, vec.y, vec.z);
                        }
                    }
                }
            }
        }
    }
}
