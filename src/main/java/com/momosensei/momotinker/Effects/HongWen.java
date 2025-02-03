package com.momosensei.momotinker.Effects;

import net.minecraft.world.effect.MobEffectCategory;


//先继承那个预设好的超类
public class HongWen extends StaticEffect{
    protected HongWen(MobEffectCategory type, int color) {
        super(type, color);
    }
    //MobEffectCategory表示药水类型(有害，中性，有益等等)
    //color里面填写10进制颜色数，或者0x  + 十六进制颜色
    //最后记得注册表那边注册一下
    public HongWen() {
        super(MobEffectCategory.HARMFUL, 16769263);
    }


    //这个就是药水刻期间对实体的效果
/*
    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        //living 是生物形参
        //amplifier是药水等级形参
        //特别注意一下，药水等级1对应的amplifier其实是0，2对应的1
        //举个例子你要让这个生物每秒受伤(摔落伤害)3点 * 等级,你就可以这样
        if(living.tickCount%20==0){
            //tickcount是一个进游戏后就会累计的东西,20tick1秒
            //%是取余数，当这个可以整除说明过去了一秒

            //hurt是对生物造成伤害
            //hurt内两个形参，一个是伤害类型，一个是伤害大小
            //伤害类型有四个参数,
            //bypassArmor()代表是否穿透护甲
            //bypassMagic()代表是否无视抗性提升等
            //bypassInvul()代表是否无视无敌(如创造模式等)
            //bypassEnchantments()代表是否无视附魔
            //值得注意的是这四个值在造成伤害时候再次修改就可以更改原有的固有属性
            //比如摔落伤害本来是不无视抗性提升和创造模式的
            //在你像这样修改之后这个伤害就变成了能够无视抗性提升,保护和创造模式的摔落伤害
            living.hurt(DamageSource.FALL.bypassArmor().bypassMagic().bypassInvul().bypassEnchantments(),3 * amplifier+1);//因为amplifier0才是1级药水效果，记得加1

            //第二种是玩家伤害那种
            //这时候需要在playerAttack()内额外填上一个玩家参数
            //(Player)这样就可以把这个参转化为玩家类型,尽管有时候他并不是玩家
            //其他部分和上面一样



//            living.hurt(DamageSource.playerAttack((Player) living).bypassArmor().bypassMagic().bypassInvul().bypassEnchantments(),3 * amplifier);
        }
    }*/
}
