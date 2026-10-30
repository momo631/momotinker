package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.Items.tool.divine_punishment_spear;
import com.momosensei.momotinker.Items.tool.entropy_burning_cannon;
import com.momosensei.momotinker.Items.tool.trigger_blade;
import com.momosensei.momotinker.entity.NyarlathotepEntity.NyarlathotepEntity;
import com.momosensei.momotinker.particle.SlashStrikeOptions;
import com.momosensei.momotinker.register.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.momosensei.momotinker.Items.tool.entropy_burning_cube.crystallized;
import static com.momosensei.momotinker.Modifiers.modifiers.BreakthroughStars.breakthroughstar;
import static slimeknights.tconstruct.library.modifiers.Modifier.RANDOM;
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
        Level level = player.level();
        EntityType<TriggerSlashEntity> entityType = getSlashType();
        TriggerSlashEntity slash = new TriggerSlashEntity(entityType, level);
        double x = player.getLookAngle().x;
        double y = player.getLookAngle().y;
        double z = player.getLookAngle().z;
        int yamatoLevel = tool.getModifierLevel(MomotinkerModifiers.yamato.getId());
        slash.damage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        if (yamatoLevel>0){
            slash.damagemultiplier = getSlashDamageMultiplier(tool)*0.6f;
        } else {
            slash.damagemultiplier = getSlashDamageMultiplier(tool);
        }
        slash.range = (float) player.getEntityReach();
        slash.setOwner(player);
        slash.setToolstack(tool);
        slash.noPhysics = false;
        slash.setYamatoLevel(yamatoLevel);
        slash.setDeltaMovement(player.getLookAngle());
        slash.setPos(player.getX()+x,player.getY()+0.7*player.getBbHeight()+y*0.75,player.getZ()+z);
        level.addFreshEntity(slash);

        spawnSlashParticles(player, slash, yamatoLevel);
        ToolDamageUtil.damageAnimated(tool,1,player, InteractionHand.MAIN_HAND);
    }
    private static void spawnSlashParticles(ServerPlayer player, TriggerSlashEntity slash, int yamatoLevel) {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        float yaw = player.getYRot();
        float pitch = player.getXRot();
        Vec3 look = player.getLookAngle();
        float range = (float) player.getEntityReach();
        int lifetime = 1;
        int perTick = 20;
        float r = 1f, g = 1f, b = 1f, a = 0.9f;
        if (yamatoLevel > 0) {
            lifetime = 6;
            perTick = 8;
            r = 0.1f; g = 0.2f; b = 0.9f; a = 0.8f;
        }
        float forwardOffset = player.getBbWidth() * 2.5f;

        double spawnX = slash.getX() + look.x * forwardOffset;
        double spawnY = slash.getY() + look.y * forwardOffset;
        double spawnZ = slash.getZ() + look.z * forwardOffset;

        serverLevel.sendParticles(
                new SlashStrikeOptions(r, g, b, a,
                        slash.angle, yaw, pitch,
                        (float) look.x, (float) look.y, (float) look.z,
                        lifetime, perTick,
                        range),
                spawnX, spawnY, spawnZ,
                1,
                0, 0, 0,
                0.0);
//        }
//        else {
//            double baseX = slash.getX();
//            double baseY = slash.getY();
//            double baseZ = slash.getZ();
//
//            float yawRad = (float) Math.toRadians(yaw);
//            float pitchRad = (float) Math.toRadians(pitch);
//
//            float fx = -Mth.sin(yawRad) * Mth.cos(pitchRad);
//            float fy = -Mth.sin(pitchRad);
//            float fz = Mth.cos(yawRad) * Mth.cos(pitchRad);
//
//            float rx = Mth.cos(yawRad);
//            float ry = 0f;
//            float rz = Mth.sin(yawRad);
//
//            float ux = ry * fz - rz * fy;
//            float uy = rz * fx - rx * fz;
//            float uz = rx * fy - ry * fx;
//
//            float startLeft = player.getBbWidth() * 0.8f;
//            float startDown = player.getBbHeight() * 0.2f;
//            float startForward = player.getBbWidth() * 2.4f;
//
//            double spawnX = baseX + rx * startLeft + ux * startDown - fx * startForward;
//            double spawnY = baseY + ry * startLeft + uy * startDown - fy * startForward;
//            double spawnZ = baseZ + rz * startLeft + uz * startDown - fz * startForward;
//
//            float endRight = player.getBbWidth() * 8f * Mth.nextFloat(random, 0.6f, 1.4f);
//            float endUp = player.getBbHeight() * 2f * Mth.nextFloat(random, 0.6f, 1.4f);
//
//            double dirX = look.x * range - rx * endRight - ux * endUp;
//            double dirY = look.y * range - ry * endRight - uy * endUp;
//            double dirZ = look.z * range - rz * endRight - uz * endUp;
//
//            serverLevel.sendParticles(
//                    new SlashTrailOptions(1f, 0f, 0f, 0.8f,
//                            (float) dirX, (float) dirY, (float) dirZ),
//                    spawnX, spawnY, spawnZ,
//                    1,
//                    0, 0, 0,
//                    0.0);
//        }
    }
    public static EntityType<TriggerSlashEntity> getSlashType() {
        return MomotinkerEntities.trigger_slash.get();
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
        Level level = player.level();
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
        spear.setPos(player.getX()+x*2,player.getY()+0.7*player.getBbHeight()+y*1.5,player.getZ()+z*2);
        level.addFreshEntity(spear);
        ToolDamageUtil.damageAnimated(tool,1,player, InteractionHand.MAIN_HAND);
    }

    public static ItemStack getSpear() {
        return new ItemStack(MomotinkerItem.spear_entity.get());
    }

    public static EntityType<SpearEntity> getSpearType() {
        return MomotinkerEntities.spear_entity.get();
    }
    public static float getSpearDamageMultiplier(ToolStack tool) {
        int a = tool.getModifierLevel(MomotinkerModifiers.breakthroughstars.getId());
        if (a==0){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        }else
        if (a>0){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE)* (1F + tool.getPersistentData().getInt(breakthroughstar)*0.01F);
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
            Level level = player.level();
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
        Level level = player.level();
        if (level.isClientSide) return;
        ToolStack tool1 = ToolStack.from(player.getMainHandItem());
        ToolStack tool2 = ToolStack.from(player.getOffhandItem());
        if ((!player.getMainHandItem().is(MomotinkerTools.pneumatic_sword.get()) && !player.getOffhandItem().is(MomotinkerTools.pneumatic_sword.get())) || player.getAttackStrengthScale(0) != 1) {
            return;
        }
        int a = 15;
        double x = player.getLookAngle().x*0.8;
        double z = player.getLookAngle().z*0.8;
        if (player.getMainHandItem().is(MomotinkerTools.pneumatic_sword.get())) {
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
        if (player.getOffhandItem().is(MomotinkerTools.pneumatic_sword.get())) {
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

    public static EntityType<NyarlathotepEntity> getNyarlathotepType() {
        return MomotinkerEntities.nyarlathotep_entity.get();
    }

}
