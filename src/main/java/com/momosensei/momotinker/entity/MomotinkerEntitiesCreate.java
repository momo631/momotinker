package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.register.*;
import com.momosensei.momotinker.tool.divine_punishment_spear;
import com.momosensei.momotinker.tool.entropy_burning_cannon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.momosensei.momotinker.Modifiers.modifiers.BreakthroughStars.breakthroughstar;
import static com.momosensei.momotinker.tool.entropy_burning_cube.crystallized;
import static slimeknights.tconstruct.library.modifiers.Modifier.RANDOM;
import static slimeknights.tconstruct.library.tools.stat.ToolStats.ACCURACY;

public class MomotinkerEntitiesCreate {
    public static void createSpear(ServerPlayer player) {
        if (!(player.getMainHandItem().getItem() instanceof divine_punishment_spear) || player.getAttackStrengthScale(0) != 1 || !checkOffHand(player)) {
            return;
        }
        ToolStack tool = ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()){
            return;
        }
        float damage = getDamageMultiplier(tool);
        ItemStack color = getSpear(tool.getStats().getInt(MomotinkerToolDefinitions.SLASH_COLOR));
        Level level =player.level();
        EntityType<SpearEntity> entityType = getSpearType(tool.getStats().getInt(MomotinkerToolDefinitions.SLASH_COLOR));
        SpearEntity spear =new SpearEntity(entityType,level,color);
        for (int dx=-1;dx<=1;dx++){
            for (int dz=-1;dz<=1;dz++){
                if (!level.hasChunk(spear.chunkPosition().x+dx,spear.chunkPosition().z+dz)){
                    spear.discard();
                    return;
                }
            }
        }
        double x =player.getLookAngle().x;
        double y =player.getLookAngle().y;
        double z =player.getLookAngle().z;
        spear.damage=damage;
        spear.setOwner(player);
        spear.setToolstack(tool);
        spear.noPhysics = false;
        spear.setDeltaMovement(player.getLookAngle());
        spear.setPos(player.getX()+x*2,player.getY()+0.7*player.getBbHeight()+y*1.5,player.getZ()+z*2);
        level.addFreshEntity(spear);
        ToolDamageUtil.damageAnimated(tool,1,player, InteractionHand.MAIN_HAND);
    }
    public static ItemStack getSpear(int index){
        return new ItemStack(MomotinkerItem.spear_entity.get());
    }

    public static EntityType<SpearEntity> getSpearType(int index) {
        return MomotinkerEntities.spear_entity.get();
    }

    public static float getDamageMultiplier(ToolStack tool) {
        int a = tool.getModifierLevel(MomotinkerModifiers.breakthroughstars.getId());
        if (a==0){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        }else
        if (a>0){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE)* (1F + tool.getPersistentData().getInt(breakthroughstar)*0.01F);
        }
        return getDamageMultiplier(tool);
    }
    public static boolean checkOffHand(Player player){
        return player!=null&& !player.hasItemInSlot(EquipmentSlot.OFFHAND);
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
            entity.scale = tool.getStats().get(MomotinkerToolDefinitions.SCALE);
            entity.setPos(player.getEyePosition().x, player.getEyePosition().y - 0.5 * entity.getBbHeight(), player.getEyePosition().z);
            entity.setOwner(player);
            level.addFreshEntity(entity);
            ToolDamageUtil.damageAnimated(tool, 1, player, InteractionHand.MAIN_HAND);
        }
    }
}
