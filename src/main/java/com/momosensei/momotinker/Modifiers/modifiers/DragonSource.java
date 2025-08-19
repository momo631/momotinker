package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;
import java.util.function.BiConsumer;


public class DragonSource extends momomodifier {
    public DragonSource() {

    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() != null) {
            if (getAllModifierlevel(event.getEntity(), MomotinkerModifiers.dragon_source.getId())> 0) {
                if (event.getSource().getEntity() == null) {
                    event.setAmount(0);
                }
            }
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player) {
            if (getAllModifierlevel(player, MomotinkerModifiers.dragon_source.getId()) > 0 && !player.getAbilities().mayfly && !player.getAbilities().flying && !player.isCreative() && !player.isSpectator()) {
                player.getAbilities().flying = true;
                player.getAbilities().mayfly = true;
            }
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (context.getEntity() instanceof Player player &&getAllModifierlevel(player, MomotinkerModifiers.dragon_source.getId())==0&& player.getAbilities().mayfly && player.getAbilities().flying && !player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
        }
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        float a=modifier.getLevel()*0.2f;
        switch (slot) {
            case HEAD -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("E2AA0000-0D0D-1339-20AF-61966A9A767F"), Attributes.MAX_HEALTH.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("6AEA5FB4-9F79-D3B6-BD6D-278D1E7C8C09"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("93C2E4FC-B1FD-E360-551F-66A63BEB7F70"), Attributes.ATTACK_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("08A192C2-17EB-E58A-6CF3-DE5665D42FB2"), Attributes.MOVEMENT_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.FLYING_SPEED, new AttributeModifier(UUID.fromString("F803AC95-F83B-F174-EE8F-272AABB445EC"), Attributes.FLYING_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("E852D8EE-D0F9-076F-8EB9-4E24F35A4FA4"), ForgeMod.ENTITY_GRAVITY.get().getDescriptionId(), -a, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case CHEST -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("EA3CEE79-A0ED-DC25-0EE7-BE3912830183"), Attributes.MAX_HEALTH.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("6F23EC71-1F92-D79A-ABF0-B53D979EE2C7"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("70A0C50F-E86A-6395-5382-0AA9DF24A784"), Attributes.ATTACK_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("13046DA0-D681-5D46-5B52-73003CA11A3E"), Attributes.MOVEMENT_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.FLYING_SPEED, new AttributeModifier(UUID.fromString("635CE7E6-A65D-429B-51E6-D7C719532BF6"), Attributes.FLYING_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("91FE1FEF-7C81-0AF8-021B-64683BBE698C"), ForgeMod.ENTITY_GRAVITY.get().getDescriptionId(), -a, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case LEGS -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("916C85B6-C028-CE01-1030-D3030E074FFC"), Attributes.MAX_HEALTH.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("93DB4FE7-5627-46CC-000A-4A08A84361A2"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("464B69AD-8C87-44D3-681D-641A58825687"), Attributes.ATTACK_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("5C04F77E-D836-37D0-9703-01F1B8A9A783"), Attributes.MOVEMENT_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.FLYING_SPEED, new AttributeModifier(UUID.fromString("BCF168A8-0CBF-5716-AA0F-50D31E656D84"), Attributes.FLYING_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("74141441-337D-3521-51B6-5F6F7A11AB86"), ForgeMod.ENTITY_GRAVITY.get().getDescriptionId(), -a, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case FEET -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("24EEFC7A-396E-BE8E-6B2B-FCF0AC9E2B1C"), Attributes.MAX_HEALTH.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("034B7E34-0E0F-3FBA-2FB1-2F4D74A6DB86"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("DA1A6375-3FDF-AA07-BB9D-93C3BEC10B53"), Attributes.ATTACK_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("BC3B54F4-415B-015D-056A-22212B4A1817"), Attributes.MOVEMENT_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.FLYING_SPEED, new AttributeModifier(UUID.fromString("A115E38F-E46C-75F5-D368-6583C8E55F4D"), Attributes.FLYING_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("F4C1744E-E83C-D0BA-D84B-E0548460EAFD"), ForgeMod.ENTITY_GRAVITY.get().getDescriptionId(), -a, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case MAINHAND -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("FBBC53E6-B59C-41B6-F591-4587837ABCF4"), Attributes.MAX_HEALTH.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("29C85281-D0E3-D401-397F-DDEDCD6C6E1C"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("CE7C962B-8EC9-88E2-6D35-BDDFEB2BACF2"), Attributes.ATTACK_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("0E789AF6-F886-2F4A-2BB1-37C59793E5CB"), Attributes.MOVEMENT_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.FLYING_SPEED, new AttributeModifier(UUID.fromString("8CDC0320-6BA1-AA99-C55B-E2BE8F272847"), Attributes.FLYING_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("6209D868-48A5-3921-25A9-86159389F7FC"), ForgeMod.ENTITY_GRAVITY.get().getDescriptionId(), -a, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case OFFHAND -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("E33C3C35-F38E-A758-A2FD-01655EAC76E6"), Attributes.MAX_HEALTH.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("93BD281B-B2DD-0C7A-FC6D-F727D7D72978"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("7B4C4543-B80B-E04E-AAEF-A0EDBBD37E7E"), Attributes.ATTACK_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("43499726-2416-56D0-B626-557F1C054582"), Attributes.MOVEMENT_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(Attributes.FLYING_SPEED, new AttributeModifier(UUID.fromString("4168C69F-1B39-47FE-1C9E-C1D02052EE01"), Attributes.FLYING_SPEED.getDescriptionId(), a, AttributeModifier.Operation.MULTIPLY_TOTAL));
                consumer.accept(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(UUID.fromString("BDCB201E-8039-EE24-A87E-75B0A92DD8EC"), ForgeMod.ENTITY_GRAVITY.get().getDescriptionId(), -a, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }
    }
}