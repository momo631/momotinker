package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nonnull;


public class Resonance extends momomodifier {
    public Resonance() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity living = context.getLivingTarget();
        if (attacker instanceof Player player&& living!=null &&getMainhandModifierlevel(player, this.getId())>0&&getOffhandModifierlevel(player, this.getId())>0&&!player.isAutoSpinAttack()&&!context.isExtraAttack()) {
            if (!context.isCritical()){
                living.invulnerableTime=0;
                AttackUtil.attackEntity(ToolStack.from(player.getOffhandItem()), player, InteractionHand.OFF_HAND, living, () -> 1, true, Util.getSlotType(InteractionHand.OFF_HAND), ToolStack.from(player.getOffhandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), 1f, false, true, true, false);
                living.invulnerableTime=0;
            }else {
                living.invulnerableTime=0;
                AttackUtil.attackEntity(ToolStack.from(player.getOffhandItem()), player, InteractionHand.OFF_HAND, living, () -> 1, true, Util.getSlotType(InteractionHand.OFF_HAND), ToolStack.from(player.getOffhandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), 1f, true, true, true, false);
                living.invulnerableTime=0;
            }
        }
        return damage;
    }
}