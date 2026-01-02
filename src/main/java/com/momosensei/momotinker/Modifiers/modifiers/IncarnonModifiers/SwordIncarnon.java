package com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers;


import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.IncarnonOpenMenuPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.util.AttackUtil.getCooldownFunctionFloat;

public class SwordIncarnon extends IncarnonModifier {
    public SwordIncarnon() {
        super();
        this.incarnon_task_a=100;
        this.incarnon_task_b=1;
        this.incarnon_task_c=2;
        this.incarnon_task_d=4;
        this.incarnon_energy_max=120;
        this.use_time=40;
    }
    static {
        Map<String, Component> IncarnonTexts = new HashMap<>();
        IncarnonTexts.put("phase_1_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon1"));
        IncarnonTexts.put("phase_1_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon2"));

        IncarnonTexts.put("phase_2_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon3"));
        IncarnonTexts.put("phase_2_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon4"));

        IncarnonTexts.put("phase_3_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon5"));
        IncarnonTexts.put("phase_3_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon6"));

        IncarnonTexts.put("phase_4_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon7"));
        IncarnonTexts.put("phase_4_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon8"));
        IncarnonTexts.put("phase_4_ability_3", Component.translatable("modifier.momotinker.tooltip.sword_incarnon9"));

        IncarnonTexts.put("phase_task_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon_task1"));
        IncarnonTexts.put("phase_task_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon_task2"));
        IncarnonTexts.put("phase_task_3", Component.translatable("modifier.momotinker.tooltip.sword_incarnon_task3"));
        IncarnonTexts.put("phase_task_4", Component.translatable("modifier.momotinker.tooltip.sword_incarnon_task4"));

        IncarnonTexts.put("incarnon_on", Component.translatable("modifier.momotinker.tooltip.sword_incarnon_on"));

        registerModifierTexts(SwordIncarnon.class, IncarnonTexts);
    }

    private static final String catalogue = "incarnon_sword";
    private static final Component title = Component.translatable("modifier.momotinker.tooltip.sword_incarnon0");

    @Override
    public boolean overrideOtherStackedOnMe(IToolStackView tool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        if (player.level().isClientSide) {
            Channel.sendToServer(new IncarnonOpenMenuPacket(slot.getSlotIndex(),catalogue,getTextsForClass(SwordIncarnon.class),title));
        }
        return true;
    }

    @Override
    public void OnEntityDeath(LivingDeathEvent event) {
        super.OnEntityDeath(event);
        if (event.getSource().getEntity() instanceof Player player&& event.getEntity() != null
                && isToolStack(player.getMainHandItem())&&getMainhandModifierlevel(player, this.getId())>0) {
            ToolStack stack = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = stack.getPersistentData();
            if (data.getInt(incarnon_phase)==0){
                if (event.getEntity() instanceof Monster){
                    data.putInt(incarnon_task_phase,data.getInt(incarnon_task_phase)+1);
                }
            }else
            if (data.getInt(incarnon_phase)==1){
                if (event.getEntity() instanceof EnderDragon){
                    data.putInt(incarnon_task_phase,data.getInt(incarnon_task_phase)+1);
                }
            }else
            if (data.getInt(incarnon_phase)==2){
                if (event.getEntity() instanceof WitherBoss&&data.getBoolean(is_incarnon)){
                    data.putInt(incarnon_task_phase,data.getInt(incarnon_task_phase)+1);
                }
            }
        }
    }
    @Override
    public void onFinishUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity) {
        super.onFinishUsing(tool, modifier, entity);
        if (tool.getModifierLevel(this)>0&&entity instanceof Player){
            if (tool.getPersistentData().getInt(incarnon_phase)==3&&!tool.getPersistentData().getBoolean(is_incarnon)){
                tool.getPersistentData().putInt(incarnon_task_phase,tool.getPersistentData().getInt(incarnon_task_phase)+1);
            }
        }
    }
    @Override
    public void OnLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() != null && isToolStack(player.getMainHandItem())&&getMainhandModifierlevel(player, this.getId())>0) {
            ModDataNBT data = ToolStack.from(player.getMainHandItem()).getPersistentData();
            int a = (int) Math.floor(getCooldownFunctionFloat(player, InteractionHand.MAIN_HAND)*6+4);
            if (data.getInt(incarnon_phase)>=1&&data.getFloat(incarnon_energy)<this.incarnon_energy_max&&!data.getBoolean(is_incarnon)){
                data.putFloat(incarnon_energy,data.getFloat(incarnon_energy)+a);
            }
        }
    }



    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        if (a.getInt(incarnon_a)==1&&a.getBoolean(is_incarnon)){
            biConsumer.accept(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("95002BB7-A0D1-6F8D-CF59-9427BDCC700D"), ForgeMod.ENTITY_REACH.get().getDescriptionId(), 1.5, AttributeModifier.Operation.ADDITION));
        }
        if (a.getInt(incarnon_c)==1){
            biConsumer.accept(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("BBD39973-12EA-6A42-AA92-2D69DC8DF3AA"), ForgeMod.ENTITY_REACH.get().getDescriptionId(), 1, AttributeModifier.Operation.ADDITION));
        }
        if (a.getInt(incarnon_c)==2){
            biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("48EE3061-388A-3E4E-3221-B94026BFB701"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (a.getBoolean(is_incarnon)){
            switch (a.getInt(incarnon_a)){
                case 1-> biConsumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("49D03A35-ABCE-C288-1BEF-42E18675BE80"), Attributes.ATTACK_DAMAGE.getDescriptionId(), 1.8f, AttributeModifier.Operation.MULTIPLY_TOTAL));
                case 2-> biConsumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("3D3A509C-742A-840A-C579-A6B5169A9E85"), Attributes.ATTACK_DAMAGE.getDescriptionId(), 1f, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ModDataNBT a = (ModDataNBT) context.getPersistentData();
        switch (a.getInt(incarnon_d)){
            case 1 -> ToolStats.ATTACK_DAMAGE.multiply(builder, 1.5);
            case 2 -> {
                ToolStats.ATTACK_DAMAGE.multiply(builder, 1.25);
                ToolStats.ATTACK_SPEED.multiply(builder, 1.25);
            }
            case 3 -> ToolStats.ATTACK_SPEED.multiply(builder, 1.5);
        }
    }
    @Override
    public float getCriticalModifier(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float originalModifier, float modifier) {
        ModDataNBT data = tool.getPersistentData();
        if (data.getInt(incarnon_b)==1){
            modifier*=1.6f;
        }
        return modifier;
    }

    private static final ThreadLocal<Boolean> incarnon_a_attack = new ThreadLocal<>();

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        super.OnLivingHurt(event);
        if (incarnon_a_attack.get() != null && incarnon_a_attack.get()) {
            return;
        }
        if (event.getSource().getEntity() instanceof ServerPlayer player && event.getEntity() != null
                && isToolStack(player.getMainHandItem()) && getMainhandModifierlevel(player, this.getId()) > 0) {
            ToolStack stack = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = stack.getPersistentData();
            if (data.getInt(incarnon_b)==2&&event.getAmount()>0){
                player.heal(event.getAmount()*0.2f);
            }
            if (data.getBoolean(is_incarnon)&&data.getInt(incarnon_a)==2){
                incarnon_a_attack.set(true);
                try {
                    event.getEntity().invulnerableTime=0;
                    event.getEntity().hurt(LegacyDamageSource.playerAttack(player).setPercentageBypassArmor(0.6f), event.getAmount()*0.4f);
                } finally {
                    incarnon_a_attack.set(false);
                }
            }
        }
    }

    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        float a = 0;
        if (tool.getPersistentData().getBoolean(is_incarnon)&&tool.getPersistentData().getInt(incarnon_a)==2) {
            a = 0.6f;
        }
        return source.addPercentageBypassArmor(a);
    }

    @Override
    public float modifySweepDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float sweepDamage) {
        if (tool.getPersistentData().getBoolean(is_incarnon)&&tool.getPersistentData().getInt(incarnon_a)==1){
            sweepDamage+=baseDamage;
        }
        return sweepDamage;
    }
    @Override
    public double modifySweepRange(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, double range) {
        LivingEntity attacker = context.getAttacker();
        if (tool.getPersistentData().getBoolean(is_incarnon)&&tool.getPersistentData().getInt(incarnon_a)==1&&attacker instanceof Player player){
            range *= 1.8F;
        }
        return range;
    }
}