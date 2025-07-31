package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;


public class SacrificeToSpirit extends momomodifier  {
    public SacrificeToSpirit() {
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerInteract);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerAttack);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLeftClick);
        MinecraftForge.EVENT_BUS.addListener(this::onLeftClickEmpty);
        MinecraftForge.EVENT_BUS.addListener(this::onLeftClickBlock);
    }
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && getMainhandModifierlevel(event.getEntity(), MomotinkerModifiers.sacrificetospirit.getId())>0) {
            event.setCanceled(true);
        }
    }
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && getMainhandModifierlevel(event.getEntity(), MomotinkerModifiers.sacrificetospirit.getId())>0) {
            event.setCanceled(true);
        }
    }
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (event.isCancelable() && getMainhandModifierlevel(player, MomotinkerModifiers.sacrificetospirit.getId())>0) {
            event.setCanceled(true);
        }
    }
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.isCancelable() && getMainhandModifierlevel(event.getEntity(), MomotinkerModifiers.sacrificetospirit.getId())>0) {
            event.setCanceled(true);
        }
    }
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && getMainhandModifierlevel(event.getEntity(), MomotinkerModifiers.sacrificetospirit.getId())>0) {
            event.setCanceled(true);
        }
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player) {
            int a = getMainhandModifierlevel(player, MomotinkerModifiers.sacrificetospirit.getId());
            if (a>0&&player.tickCount%40==0){
                double d = (player.getAttributeValue(Attributes.ATTACK_SPEED)*0.6)+3;
                if (d>200)d=200;
                float e = (float) (player.getAttributeValue(ForgeMod.ENTITY_REACH.get())/3);
                List<LivingEntity> ls0 = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(d));
                for (LivingEntity living : ls0) {
                    if (living!=null&&living!=player){
                        living.invulnerableTime = 0;
                        AttackUtil.attackEntity(ToolStack.from(player.getMainHandItem()), player, InteractionHand.MAIN_HAND, living, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), ToolStack.from(player.getMainHandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), e+a*0.25f, false, true, true, false);
                        living.invulnerableTime = 0;
                    }
                }
            }
        }
    }
}