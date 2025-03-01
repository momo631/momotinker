package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import com.momosensei.momotinker.tool.divine_punishment_spear;
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

public class SpearCreate {
    public static void createSpear(ServerPlayer player){
        if (!(player.getMainHandItem().getItem() instanceof divine_punishment_spear)||player.getAttackStrengthScale(0)!=1||!checkOffHand(player)){
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
        }
        if (a>0){
            return tool.getStats().get(ToolStats.ATTACK_DAMAGE)* (1F + tool.getPersistentData().getInt(breakthroughstar)*0.01F);
        }
        return getDamageMultiplier(tool);
    }
    public static boolean checkOffHand(Player player){
        return player!=null&& !player.hasItemInSlot(EquipmentSlot.OFFHAND);
    }
}
