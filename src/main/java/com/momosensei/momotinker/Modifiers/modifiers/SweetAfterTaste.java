package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.Modifiers.modifiers.DrinkingDemon.*;

public class SweetAfterTaste extends momomodifier {
    public SweetAfterTaste() {

    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
    private int getDefenseenchant(ToolStack tool){
        if (tool!=null) {
            return tool.getPersistentData().getInt(defenseenchant);
        }
        return 0;
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        if (a instanceof ServerPlayer player) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this) > 0 ) {
                        int b1=  getDefenseenchant(getToolStack(player.getItemBySlot(EquipmentSlot.HEAD)))
                                +getDefenseenchant(getToolStack(player.getItemBySlot(EquipmentSlot.CHEST)))
                                +getDefenseenchant(getToolStack(player.getItemBySlot(EquipmentSlot.LEGS)))
                                +getDefenseenchant(getToolStack(player.getItemBySlot(EquipmentSlot.FEET)));
                        float b = (float) 80 / (b1 + 80);
                        event.setAmount(event.getAmount()*b);
                    }
                }
            }
        }
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof ServerPlayer player){
            int a =tool.getPersistentData().getInt(meleeenchant);
            return damage*(1F+a*0.01F);
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker instanceof ServerPlayer player && projectile instanceof AbstractArrow arrow){
            int a =persistentData.getInt(projectileenchant);
            arrow.setBaseDamage(arrow.getBaseDamage()*(1f+a*0.005f));
        }
        return false;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        int a = tool.getPersistentData().getInt(toolsenchant);
        if (!(tool instanceof ModifiableArmorItem)) {
            switch (slot) {
                case MAINHAND, OFFHAND ->
                        consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("1F38AE2E-C876-5B70-79A6-BBCFCBF7CC2E"), Attributes.ATTACK_SPEED.getDescriptionId(), a*0.01, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }
}