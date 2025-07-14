package com.momosensei.momotinker.Items;


import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static com.momosensei.momotinker.util.PenetratingDamage.reflectionPenetratingDamage;

public class twilight_ego extends Item {
    private final Multimap<Attribute, AttributeModifier> attributes;
    public twilight_ego(Properties properties) {
        super(properties.rarity(Rarity.create("aleph",ChatFormatting.DARK_RED)).fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 17D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.5F, AttributeModifier.Operation.ADDITION));
        this.attributes = builder.build();
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 24;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return Items.DIAMOND_SWORD.isBookEnchantable(new ItemStack(Items.DIAMOND_SWORD), book);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return Items.DIAMOND_SWORD.canApplyAtEnchantingTable(new ItemStack(Items.DIAMOND_SWORD), enchantment);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&!player.level.isClientSide()&&a instanceof LivingEntity living) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty()&&stack.is(MomotinkerItem.twilight_ego.get())) {
                living.invulnerableTime=0;
                living.hurt(DamageSource.MAGIC,event.getAmount());
                living.invulnerableTime=0;
                living.hurt(DamageSource.OUT_OF_WORLD,event.getAmount());
                living.invulnerableTime=0;
                reflectionPenetratingDamage(a, player, event.getAmount()*living.getMaxHealth()*0.01f);
                living.invulnerableTime=0;
            }
        }
    }
}