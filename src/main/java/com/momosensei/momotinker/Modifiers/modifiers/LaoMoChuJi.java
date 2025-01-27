package com.momosensei.momotinker.Modifiers.modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


//extend为继承的类,需要至少保证为匠魂的那个Modifier
//你也可以换成你自己的超类那样更方便
//implements后面跟上接口，比如近战伤害就用这个MeleeDamageModifierHook
public class LaoMoChuJi extends Modifier implements MeleeDamageModifierHook {

    //这里需要注册钩子，这样才能生效匠魂内的方法
    //比如近战就需要跟上ModifierHooks.MELEE_DAMAGE
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    //这个方法就是作用的时候了
    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float v, float v1) {
        //这里v是basedamage，v1是damage(基础伤害和被其他词条增幅后伤害的区别)
        //modifierEntry内有词条等级等数值
        //iToolStackView这个类型是匠魂工具特有的，可以获取到匠魂的Nbt等等
        //由于实在太多这里做个简单例子

        //!=null这一步必须要做，不然会很大概率产生Nullpointer崩端
        //条件和  &&,  等于==  条件或 ||   非!
        if (toolAttackContext.getLivingTarget() != null) {
            //转换。由于已经是LivingEntity类了所以实际上这个句子只会更改命名，后面就可以用entity来指代toolAttackContext.getLivingTarget()
            LivingEntity entity =toolAttackContext.getLivingTarget();
            //直接伤害实体114514点伤害
            entity.hurt(DamageSource.MAGIC,114514);
            //instanceof 关键字用于判断左边对象是否为右边实例，如果你后面额外跟了一个即可以再次改名
            //说人话就是是不是属于xxx,并且命名
            if(entity instanceof Zombie zombie){
                //这个就是将僵尸设置为死亡，此时会直接掉掉落物并且目标被认为死亡
                //括号内是死亡原因
                zombie.die(DamageSource.DROWN);
                //这个就是直接击杀
                zombie.kill();
                //这个是直接移除这个实体，不会有任何动画，类似于一种处理
                zombie.remove(Entity.RemovalReason.KILLED);
            }
        }
        //这个就是修改返回的伤害，必须要浮点数
        return v + 114514F;
    }
}
