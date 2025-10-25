package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.entity.LegionEntity.LegionEntity;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.tool.divine_punishment_spear;
import com.momosensei.momotinker.tool.entropy_burning_cannon;
import com.momosensei.momotinker.tool.trigger_blade;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.momosensei.momotinker.Modifiers.modifiers.BreakthroughStars.breakthroughstar;
import static com.momosensei.momotinker.tool.entropy_burning_cube.crystallized;
import static slimeknights.tconstruct.TConstruct.RANDOM;
import static slimeknights.tconstruct.library.tools.stat.ToolStats.ACCURACY;

public class MomotinkerEntitiesCreate {
    public static void createSlash(ServerPlayer player) {
        if (!(player.getMainHandItem().getItem() instanceof trigger_blade) || player.getAttackStrengthScale(0) != 1 || !checkOffHand(player)) {
            return;
        }
        ToolStack tool = ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) {
            return;
        }
        ItemStack color = getSlash();
        Level level = player.getLevel();
        EntityType<TriggerSlashEntity> entityType = getSlashType();
        TriggerSlashEntity slash = new TriggerSlashEntity(entityType, level, color);
        double x = player.getLookAngle().x;
        double y = player.getLookAngle().y;
        double z = player.getLookAngle().z;
        int a = tool.getModifierLevel(MomotinkerModifiers.yamato.getId());
        slash.damage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        if (a>0){
            slash.damagemultiplier = getSlashDamageMultiplier(tool)*0.8f;
        }else {
            slash.damagemultiplier = getSlashDamageMultiplier(tool);
        }
        slash.setOwner(player);
        slash.setToolstack(tool);
        slash.noPhysics = false;
        slash.setint(a);
        slash.setDeltaMovement(player.getLookAngle());
        slash.setPos(player.getX()+x*2,player.getY()+0.7*player.getBbHeight()+y*1.5,player.getZ()+z*2);
        level.addFreshEntity(slash);
        ToolDamageUtil.damageAnimated(tool,1,player, InteractionHand.MAIN_HAND);
    }
    public static ItemStack getSlash(){
        return new ItemStack(MomotinkerItem.trigger_slash_a.get());
    }
    public static EntityType<TriggerSlashEntity> getSlashType() {
        return MomotinkerEntities.trigger_slash_a.get();
    }
    public static float getSlashDamageMultiplier(ToolStack tool) {
        float b = RANDOM.nextInt((int) (tool.getStats().get(ACCURACY) * 100));
        return (1F + 0.005F * b + 0.2F * tool.getStats().get(ToolStats.VELOCITY));
    }
    public static void createSpear(ServerPlayer player) {
        if (!(player.getMainHandItem().getItem() instanceof divine_punishment_spear) || player.getAttackStrengthScale(0) != 1 || !checkOffHand(player)) {
            return;
        }
        ToolStack tool = ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) {
            return;
        }
        float damage = getSpearDamageMultiplier(tool);
        ItemStack color = getSpear();
        Level level = player.getLevel();
        EntityType<SpearEntity> entityType = getSpearType();
        SpearEntity spear = new SpearEntity(entityType, level, color);
        double x = player.getLookAngle().x;
        double y = player.getLookAngle().y;
        double z = player.getLookAngle().z;
        spear.damage = damage;
        spear.setOwner(player);
        spear.setToolstack(tool);
        spear.noPhysics = false;
        spear.setDeltaMovement(player.getLookAngle());
        spear.setPos(player.getX() + x * 2, player.getY() + 0.7 * player.getBbHeight() + y * 1.5, player.getZ() + z * 2);
        level.addFreshEntity(spear);
        ToolDamageUtil.damageAnimated(tool, 1, player, InteractionHand.MAIN_HAND);
    }

    public static ItemStack getSpear() {
        return new ItemStack(MomotinkerItem.spear_entity.get());
    }

    public static EntityType<SpearEntity> getSpearType() {
        return MomotinkerEntities.spear_entity.get();
    }
    public static float getSpearDamageMultiplier(ToolStack tool) {
        int a = tool.getModifierLevel(MomotinkerModifiers.breakthroughstars.getId());
        if (a == 0) {
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        }else
        if (a > 0) {
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE) * (1F + tool.getPersistentData().getInt(breakthroughstar) * 0.01F);
        }
        return getSpearDamageMultiplier(tool);
    }

    public static boolean checkOffHand(ServerPlayer player) {
        return player != null && !player.hasItemInSlot(EquipmentSlot.OFFHAND);
    }
    public static float getRayExplosionDamage(ToolStack tool,ServerPlayer player) {
        int a = (int) (player.totalExperience*0.02f);
        float b = RANDOM.nextInt((int) (tool.getStats().get(ACCURACY) * 100));
        int d = MomotinkerConfig.entropy_burning_cannon_limit.get();
        if (a<d){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE)*(1f+a*0.01f + 0.005F * b + 0.2F * tool.getStats().get(ToolStats.VELOCITY));
        }else
        if (a>d){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE)*(1f+d*0.01f + 0.005F * b + 0.2F * tool.getStats().get(ToolStats.VELOCITY));
        }
        return getRayExplosionDamage(tool,player);
    }
    public static void createRayExplosion(ServerPlayer player) {
        if (!(player.getMainHandItem().getItem() instanceof entropy_burning_cannon) || !checkOffHand(player)) {
            return;
        }
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        ToolStack tool = ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) {
            return;
        }
        for (int a = 0;a <= 1;a++) {
            Level level = player.getLevel();
            RayEntity entity = new RayEntity(MomotinkerEntities.ray_entity.get(), level);

            int b =45;
            int c = 6;
            if (tool.getPersistentData().getFloat(crystallized)==crystallized_limit) {
                b*=2;
                c+=8;
            }
            entity.rayVec3 = player.getLookAngle().scale(b);
            entity.settimes(c);
            entity.damage = getRayExplosionDamage(tool,player)*0.05F;
            entity.tool = tool;
            entity.setPos(player.getEyePosition().x, player.getEyePosition().y - 0.5 * entity.getBbHeight(), player.getEyePosition().z);
            entity.setOwner(player);
            level.addFreshEntity(entity);
            ToolDamageUtil.damageAnimated(tool, 1, player, InteractionHand.MAIN_HAND);
        }
    }
    public static void createPull(ServerPlayer player) {
        Level level = player.getLevel();
        if (level.isClientSide) return;
        ToolStack tool1 = ToolStack.from(player.getMainHandItem());
        ToolStack tool2 = ToolStack.from(player.getOffhandItem());
        if ((!player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get()) && !player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())) || player.getAttackStrengthScale(0) != 1) {
            return;
        }
        int a = 15;
        double x = player.getLookAngle().x*0.8;
        double z = player.getLookAngle().z*0.8;
        if (player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())) {
            if (tool1.isBroken()) {
                return;
            }
            PullAEntity pull = new PullAEntity(MomotinkerEntities.pull_a_entity.get(), level);
            pull.setOwner(player);
            pull.setToolstack(tool1);
            pull.noPhysics = false;
            pull.setDeltaMovement(player.getLookAngle());
            pull.setPos(player.getX()+Math.cos(a)*x-Math.sin(a)*z, player.getY() + 0.5 * player.getBbHeight() , player.getZ()+Math.sin(a)*x+Math.cos(a)*z);
            level.addFreshEntity(pull);
            ToolDamageUtil.damageAnimated(tool1, 1, player, InteractionHand.MAIN_HAND);
        }
        if (player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())) {
            if (tool2.isBroken()) {
                return;
            }
            PullBEntity pull = new PullBEntity(MomotinkerEntities.pull_b_entity.get(), level);
            pull.setOwner(player);
            pull.setToolstack(tool2);
            pull.noPhysics = false;
            pull.setDeltaMovement(player.getLookAngle());
            pull.setPos(player.getX()+Math.cos(a)*x+Math.sin(a)*z, player.getY() + 0.5 * player.getBbHeight(), player.getZ()-Math.sin(a)*x+Math.cos(a)*z);
            level.addFreshEntity(pull);
            ToolDamageUtil.damageAnimated(tool2, 1, player, InteractionHand.OFF_HAND);
        }
    }

    public static EntityType<LegionEntity> getboxType() {
        return MomotinkerEntities.box_entity.get();
    }

}
