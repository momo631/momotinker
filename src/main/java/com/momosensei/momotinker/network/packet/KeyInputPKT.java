package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Modifiers.modifiers.Berserk.berserker;
import static com.momosensei.momotinker.Modifiers.modifiers.DrinkingDemon.*;
import static com.momosensei.momotinker.Modifiers.modifiers.FallingStars.falling;
import static com.momosensei.momotinker.Modifiers.modifiers.OverCrystalline.crystallization;
import static com.momosensei.momotinker.Modifiers.modifiers.Red.ender;
import static net.minecraft.world.item.enchantment.EnchantmentCategory.*;
import static slimeknights.tconstruct.TConstruct.RANDOM;

public class KeyInputPKT {
    public int key;

    public KeyInputPKT(){
    }

    public KeyInputPKT(int key){
        this.key = key;
    }

    public static void encode(KeyInputPKT pkt, FriendlyByteBuf buf){
        buf.writeInt(pkt.key);
    }

    public static KeyInputPKT decode(FriendlyByteBuf buf){
        return new KeyInputPKT(buf.readInt());
    }

    public static void handlePacket(KeyInputPKT pkt, Supplier<NetworkEvent.Context> context$) {
        NetworkEvent.Context context = context$.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.red.getId()) > 0) {
                ModDataNBT enddata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                String[] array = new String[]{"层林尽染", "引渡徘徊", "终归尘土", "化为虚无", "在此永眠", "万籁俱寂", "如雨而逝"};
                Random random = new Random();
                int randomIndex = random.nextInt(array.length);
                if (enddata.getFloat(ender) == 0) {
                    enddata.putFloat(ender, enddata.getFloat(ender) + 90);
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.End.get(), 300));
                    player.sendSystemMessage(Component.literal(array[randomIndex]).withStyle(ChatFormatting.DARK_RED));
                }
            }
            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.fallingstars.getId()) > 0) {
                ModDataNBT falldata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (falldata.getFloat(falling) == 0) {
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.FallingPreparation.get(), 20));
                    falldata.putFloat(falling, falldata.getFloat(falling) + 30);
                }
                if (falldata.getFloat(falling) > 27 && falldata.getFloat(falling) < 30) {
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.FallingStar.get(), 20));
                    falldata.putFloat(falling, falldata.getFloat(falling) + 120);
                    if (player.getEffect(MomotinkerEffects.FallingPreparation.get()) != null && player.hasEffect(MomotinkerEffects.FallingPreparation.get())) {
                        player.removeEffect(MomotinkerEffects.FallingPreparation.get());
                    }
                }
            }

            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crystallization.getId()) > 0) {
                ModDataNBT crystallizationdata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(MomotinkerItem.dimensional_prism.get())) {
                    int a = (int) crystallizationdata.getFloat(crystallization);
                    int b = RANDOM.nextInt(a + 4);
                    player.getItemBySlot(EquipmentSlot.OFFHAND).setCount(player.getItemBySlot(EquipmentSlot.OFFHAND).getCount() - 1);
                    if (b < 4) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization) + 1);
                        player.sendSystemMessage(Component.translatable("强化成功！").withStyle(ChatFormatting.GREEN));
                    }
                    if (b >= 4 && a < 6) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization));
                        player.sendSystemMessage(Component.translatable("强化失败！").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a < 10 && a >= 6) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization) - 1);
                        player.sendSystemMessage(Component.translatable("强化失败！强化等级减1").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a < 14 && a >= 10) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization) - 4);
                        player.sendSystemMessage(Component.translatable("强化失败！强化等级减4").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a >= 14) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        player.sendSystemMessage(Component.translatable("强化失败！装备碎掉了。。。").withStyle(ChatFormatting.RED));
                    }
                }
            }
            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.berserk.getId()) > 0) {
                ModDataNBT berserkdata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (berserkdata.getFloat(berserker) == 1) {
                    berserkdata.putFloat(berserker, 0);
                } else if (berserkdata.getFloat(berserker) == 0) {
                    berserkdata.putFloat(berserker, 1);
                }
            }
            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.drinkingdemon.getId()) > 0) {
                ModDataNBT drinkingdemondata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                int a = drinkingdemondata.getInt(defenseenchant)+drinkingdemondata.getInt(meleeenchant)+drinkingdemondata.getInt(projectileenchant)+drinkingdemondata.getInt(toolsenchant)+drinkingdemondata.getInt(curseenchant);
                float f = (float) 5 /(drinkingdemondata.getInt(curseenchant)+5);
                if (!player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())&&player.getItemBySlot(EquipmentSlot.OFFHAND).isEnchanted()&&player.totalExperience>a*50*f) {
                    player.giveExperiencePoints((int) (-a*50*f));
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.OFFHAND));
                    for (Enchantment enchantment: enchantments.keySet()) {
                        if (enchantments.get(enchantment) > 0) {
                            if (!enchantment.isCurse()&&enchantment.category==ARMOR||enchantment.category==ARMOR_FEET||enchantment.category==ARMOR_CHEST||enchantment.category==ARMOR_HEAD||enchantment.category==ARMOR_LEGS) {
                                drinkingdemondata.putInt(defenseenchant,drinkingdemondata.getInt(defenseenchant)+enchantments.get(enchantment));
                            }
                            if (!enchantment.isCurse()&&enchantment.category==WEAPON) {
                                drinkingdemondata.putInt(meleeenchant,drinkingdemondata.getInt(meleeenchant)+enchantments.get(enchantment));
                            }
                            if (!enchantment.isCurse()&&enchantment.category==CROSSBOW||enchantment.category==BOW||enchantment.category==TRIDENT) {
                                drinkingdemondata.putInt(projectileenchant,drinkingdemondata.getInt(projectileenchant)+enchantments.get(enchantment));
                            }
                            if (!enchantment.isCurse()&&enchantment.category==WEARABLE||enchantment.category==DIGGER) {
                                drinkingdemondata.putInt(toolsenchant,drinkingdemondata.getInt(toolsenchant)+enchantments.get(enchantment));
                            }
                            if (enchantment.isCurse()) {
                                drinkingdemondata.putInt(curseenchant,drinkingdemondata.getInt(curseenchant)+enchantments.get(enchantment));
                            }
                        }
                    }
                    player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 1800);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
