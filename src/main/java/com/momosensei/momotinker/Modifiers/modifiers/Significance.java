package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.SignifiCharge;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import static com.momosensei.momotinker.util.PenetratingDamage.reflectionPenetratingDamage;


public class Significance extends momomodifier {
    public Significance() {
    }

    public static final ResourceLocation signifincances = Momotinker.getResource("signifincances");
    public static final ResourceLocation signifincancecool = Momotinker.getResource("signifincancecool");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(signifincancecool);
        iToolStackView.getPersistentData().remove(signifincances);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player && player.level() instanceof ServerLevel serverLevel) {
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(signifincances)<0){
                a.putInt(signifincances,0);
            }
            if (a.getInt(signifincances)==1){
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
            }
            if (a.getInt(signifincancecool)<0){
                a.putInt(signifincancecool,0);
            }
            if (a.getInt(signifincancecool)>0&&player.tickCount%20==0){
                a.putInt(signifincancecool,a.getInt(signifincancecool)-1);
            }
            if (a.getInt(signifincances)>1){
                if (player.tickCount%20==0) {
                    a.putInt(signifincances, a.getInt(signifincances) - 1);
                }
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getMaxHealth()*(0.99f));
                if (player.getHealth()>player.getMaxHealth()){
                    player.setHealth(player.getMaxHealth());
                }
                Collection<MobEffectInstance> harmeffect = player.getActiveEffects();
                for (int i = 0; i < harmeffect.size(); ++i) {
                    MobEffectInstance effect = harmeffect.stream().toList().get(i);
                    player.removeEffect(effect.getEffect());
                }
                List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12));
                for (LivingEntity e : list) {
                    if (e != null && e != player) {
                        e.getAttribute(Attributes.MAX_HEALTH).setBaseValue(e.getMaxHealth()*(0.95f));
                        if (e.getHealth()>e.getMaxHealth()){
                            e.setHealth(e.getMaxHealth());
                        }
                        Collection<MobEffectInstance> harmeffect1 = e.getActiveEffects();
                        for (int i = 0; i < harmeffect1.size(); ++i) {
                            MobEffectInstance effect = harmeffect1.stream().toList().get(i);
                            e.removeEffect(effect.getEffect());
                        }
                        if (e.getMaxHealth()<player.getMaxHealth()*3f){
                            reflectionPenetratingDamage(e,player,e.getMaxHealth());
                            e.onRemovedFromWorld();
                            e.setPos(Double.NaN, Double.NaN, Double.NaN);
                        }
                    }
                    if (e instanceof ServerPlayer player1){
                        Channel.sendToPlayer(new SignifiCharge(3),player1);
                    }
                }
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int a = tool.getPersistentData().getInt(signifincancecool);
        if (player != null&&a!=0) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.significance1").append(a+"s"));
        }
    }
}