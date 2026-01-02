package com.momosensei.momotinker.Modifiers.hook;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface SweepAttackModifierHook {
    /**
     * 修改横扫攻击的伤害值
     *
     * @param tool 当前使用的工具（包含工具数据和修饰符信息）
     * @param modifier 调用此Hook的修饰符条目（包含等级等信息）
     * @param context 攻击上下文（包含攻击者、目标、环境等信息）
     * @param baseDamage 基础伤害值（对主目标的原始伤害）
     * @param sweepDamage 计算出的横扫伤害值（由SweepingEdgeModifier计算得出）
     * @return 修改后的横扫伤害值
     */
    default float modifySweepDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float sweepDamage) {
        return sweepDamage;
    }

    /**
     * 修改横扫攻击的范围
     *
     * @param tool 当前使用的工具
     * @param modifier 调用此Hook的修饰符条目
     * @param context 攻击上下文
     * @param range 基础横扫范围（已减去expanded修饰符的加成）
     * @return 修改后的横扫范围
     * &#064;note  最终范围 = 返回值 + tool.getModifierLevel(TinkerModifiers.expanded.getId())
     */
    default double modifySweepRange(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, double range) {
        return range;
    }

    /**
     * 修改横扫攻击的击退强度
     *
     * @param tool 当前使用的工具
     * @param modifier 调用此Hook的修饰符条目
     * @param context 攻击上下文
     * @param knockback 基础击退强度（默认为0.4）
     * @return 修改后的击退强度
     */
    default float modifySweepKnockback(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float knockback) {
        return knockback;
    }

    record AllMerger(Collection<SweepAttackModifierHook> modules) implements SweepAttackModifierHook {
        @Override
        public float modifySweepDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float sweepDamage) {
            float result = sweepDamage;
            for (SweepAttackModifierHook module : modules) {
                result = module.modifySweepDamage(tool, modifier, context, baseDamage, result);
            }
            return result;
        }

        @Override
        public double modifySweepRange(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, double range) {
            double result = range;
            for (SweepAttackModifierHook module : modules) {
                result = module.modifySweepRange(tool, modifier, context, result);
            }
            return result;
        }

        @Override
        public float modifySweepKnockback(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float knockback) {
            float result = knockback;
            for (SweepAttackModifierHook module : modules) {
                result = module.modifySweepKnockback(tool, modifier, context, result);
            }
            return result;
        }
    }
}
