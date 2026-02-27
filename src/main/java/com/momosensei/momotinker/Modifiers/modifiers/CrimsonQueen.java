package com.momosensei.momotinker.Modifiers.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.ToolsTimeCharge;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;

import static com.momosensei.momotinker.register.MomotinkerTools.trigger_blade;

public class CrimsonQueen extends momomodifier {
    public CrimsonQueen() {
    }
    public static final ResourceLocation crimsontime = Momotinker.getResource("crimsontime");
    public static final ResourceLocation crimsonlayers = Momotinker.getResource("crimsonlayers");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.crimsonqueen");
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.yamato.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(crimsontime);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player&&player.getItemBySlot(EquipmentSlot.MAINHAND).is(trigger_blade.get())&&player.level() instanceof ServerLevel serverLevel) {
            ModDataNBT a = tool.getPersistentData();
            if (tool.getModifierLevel(MomotinkerModifiers.crimsonqueen.getId()) > 0) {
                if (a.getFloat(crimsonlayers) > 0) {
                    if (a.getFloat(crimsontime) > 0 && player.tickCount % 20 == 0) {
                        a.putFloat(crimsontime, a.getFloat(crimsontime) - 1);
                        serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, player.getX(), player.getY(), player.getZ(), 10, 0, 0, 0, 0.5);
                    }
                    if (a.getFloat(crimsonlayers) >= 1 && a.getFloat(crimsontime) == 0) {
                        a.putFloat(crimsonlayers, a.getFloat(crimsonlayers) - 1);
                        a.putFloat(crimsontime, 20);
                    }
                }
                float perc = Mth.clamp(a.getFloat(crimsontime) / 20, 0, 1);
                Channel.sendToPlayer(new ToolsTimeCharge(perc),player);
            }
            if (tool.getModifierLevel(MomotinkerModifiers.crimsonqueen.getId()) == 0) {
                a.putFloat(crimsonlayers, 0);
            }
            if (a.getFloat(crimsonlayers)==0){
                a.putFloat(crimsontime,0);
            }
        }
    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null&&getMainHandTool(player,trigger_blade.get())) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crimsonqueen.getId()) > 0) {
                if (c.getFloat(crimsonlayers) >= 1) {
                    a.invulnerableTime = 0;
                    event.setAmount(event.getAmount() * 2.4F);
                    a.invulnerableTime = 0;
                    c.putFloat(crimsonlayers, c.getFloat(crimsonlayers) - 1);
                    c.putFloat(crimsontime, 20);
                }
            }
        }
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        ModDataNBT c = tool.getPersistentData();
        if (attacker!=null&&c.getFloat(crimsonlayers)>0) {
            return source.setBypassArmor();
        }
        return source;
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @org.jetbrains.annotations.Nullable LivingEntity attacker, @NotNull Entity target, LegacyDamageSource source) {
        if (attacker != null&&persistentData.getFloat(crimsonlayers)>0) {
            return source.setBypassArmor();
        }
        return source;
    }
}