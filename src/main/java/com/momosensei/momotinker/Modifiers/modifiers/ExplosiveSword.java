package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nonnull;
import java.util.List;


public class ExplosiveSword extends momomodifier {
    boolean config = MomotinkerConfig.explosion_destroys_limit.get();

    public ExplosiveSword() {
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        Player player =context.getPlayerAttacker();
        LivingEntity entity =context.getLivingTarget();
        if (player != null && entity != null){
            int a=getMainhandModifierlevel(player, MomotinkerModifiers.explosivesword.getId());
            ItemStack stack = player.getMainHandItem();
            if (a>0&&!context.isExtraAttack()) {
                Explode(ToolStack.from(stack), player, entity, player.level(), damage, 0.3f+0.1f*a);
            }
        }
        return damage;
    }
    public void Explode(ToolStack tool,Player player,LivingEntity living,Level level,float damage,float damagemultiplier){
        if (!level.isClientSide) {
            Level.ExplosionInteraction explosionInteraction;
            if (config) {
                explosionInteraction=Level.ExplosionInteraction.BLOCK;
            }else {
                explosionInteraction=Level.ExplosionInteraction.NONE;
            }
            Explosion explosion =level.explode(player, living.getX(), living.getY(), living.getZ(), 3, true, explosionInteraction);
            List<LivingEntity> lis = level.getEntitiesOfClass(LivingEntity.class, living.getBoundingBox().inflate(3));
            for (LivingEntity entity : lis) {
                if (entity != null&&entity!=player) {
                    entity.invulnerableTime = 0;
                    attackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, entity, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), damage, damagemultiplier,false, true, true,false);
                }
            }
        }
    }
}