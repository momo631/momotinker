package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.CoolTimeB;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Modifiers.modifiers.Berserk.berserker;
import static com.momosensei.momotinker.Modifiers.modifiers.DrinkingDemon.*;
import static com.momosensei.momotinker.Modifiers.modifiers.FallingStars.falling;
import static com.momosensei.momotinker.Modifiers.modifiers.FlameBath.flamebathcooldown;
import static com.momosensei.momotinker.Modifiers.modifiers.OverCrystalline.crystallization;
import static com.momosensei.momotinker.Modifiers.modifiers.Red.ender;
import static com.momosensei.momotinker.Modifiers.modifiers.Significance.signifincancecool;
import static com.momosensei.momotinker.Modifiers.modifiers.Significance.signifincances;
import static com.momosensei.momotinker.Momotinker.*;
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
            if (player==null)return;
            if (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.red.getId()) > 0) {
                ModDataNBT enddata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                String[] array = new String[]{"msg.ender1", "msg.ender2", "msg.ender3", "msg.ender4", "msg.ender5", "msg.ender6", "msg.ender7"};
                Random random = new Random();
                int randomIndex = random.nextInt(array.length);
                if (enddata.getFloat(ender) == 0) {
                    enddata.putFloat(ender, 90);
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.End.get(), 300));
                    player.sendSystemMessage(Component.translatable(array[randomIndex]).withStyle(ChatFormatting.DARK_RED));
                }
            }

            if (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.fallingstars.getId()) > 0) {
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

            if (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crystallization.getId()) > 0) {
                ModDataNBT crystallizationdata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(MomotinkerItem.dimensional_prism.get())) {
                    int a = (int) crystallizationdata.getFloat(crystallization);
                    int b = RANDOM.nextInt(a + 4);
                    player.getItemBySlot(EquipmentSlot.OFFHAND).setCount(player.getItemBySlot(EquipmentSlot.OFFHAND).getCount() - 1);
                    if (b < 4) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization) + 1);
                        player.sendSystemMessage(Component.translatable("msg.crystallization1").withStyle(ChatFormatting.GREEN));
                    }
                    if (b >= 4 && a < 6) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization));
                        player.sendSystemMessage(Component.translatable("msg.crystallization2").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a < 10 && a >= 6) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization) - 1);
                        player.sendSystemMessage(Component.translatable("msg.crystallization3").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a < 14 && a >= 10) {
                        crystallizationdata.putFloat(crystallization, crystallizationdata.getFloat(crystallization) - 4);
                        player.sendSystemMessage(Component.translatable("msg.crystallization4").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a >= 14) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        player.sendSystemMessage(Component.translatable("msg.crystallization5").withStyle(ChatFormatting.RED));
                    }
                }
            }
            if (getAllModifierlevel(player,MomotinkerModifiers.berserk.getId()) > 0) {
                for (ItemStack stack : player.getInventory().armor) {
                    if (stack.getItem() instanceof ModifiableArmorItem&&ToolStack.from(stack).getModifierLevel(MomotinkerModifiers.berserk.getId())>0) {
                        ModDataNBT berserkdata = ToolStack.from(stack).getPersistentData();
                        if (berserkdata.getFloat(berserker) == 1) {
                            berserkdata.putFloat(berserker, 0);
                        } else if (berserkdata.getFloat(berserker) == 0) {
                            berserkdata.putFloat(berserker, 1);
                        }
                    }
                }
                if (ToolStack.from(player.getMainHandItem()).getModifierLevel(MomotinkerModifiers.berserk.getId())>0){
                    ModDataNBT berserkdata = ToolStack.from(player.getMainHandItem()).getPersistentData();
                    if (berserkdata.getFloat(berserker) == 1) {
                        berserkdata.putFloat(berserker, 0);
                    } else if (berserkdata.getFloat(berserker) == 0) {
                        berserkdata.putFloat(berserker, 1);
                    }
                }
                if (ToolStack.from(player.getOffhandItem()).getModifierLevel(MomotinkerModifiers.berserk.getId())>0){
                    ModDataNBT berserkdata = ToolStack.from(player.getOffhandItem()).getPersistentData();
                    if (berserkdata.getFloat(berserker) == 1) {
                        berserkdata.putFloat(berserker, 0);
                    } else if (berserkdata.getFloat(berserker) == 0) {
                        berserkdata.putFloat(berserker, 1);
                    }
                }
            }
            if (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.drinkingdemon.getId()) > 0) {
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
            if (getMainhandModifierlevel(player, MomotinkerModifiers.significance.getId()) > 0 && !player.getItemBySlot(EquipmentSlot.OFFHAND).is(MomotinkerItem.nihilism.get())) {
                ModDataNBT significancedata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (significancedata.getInt(signifincancecool)==0){
                    significancedata.putInt(signifincances,10);
                    significancedata.putInt(signifincancecool,90);
                }
            }
            if (getMainhandModifierlevel(player, MomotinkerModifiers.blank.getId()) > 0 && CoolTimeB.getCoolTime() == 0) {
                if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(MomotinkerItem.nihilism.get())){
                    Channel.sendToClient(new CoolTimeChargeB(600));
                    player.getItemBySlot(EquipmentSlot.OFFHAND).setCount(player.getItemBySlot(EquipmentSlot.OFFHAND).getCount() - 1);
                }
            }
            if (getAllModifierlevel(player,MomotinkerModifiers.flamebath.getId()) > 0) {
                for (ItemStack stack : player.getInventory().armor) {
                    if (stack.getItem() instanceof ModifiableArmorItem) {
                        ModDataNBT flamebathdata = ToolStack.from(stack).getPersistentData();
                        if (flamebathdata.getInt(flamebathcooldown) == 0) {
                            player.addEffect(new MobEffectInstance(MomotinkerEffects.FlameBathArmor.get(),2400,getArmorModifierlevel(player,MomotinkerModifiers.flamebath.getId())-1));
                            if (player.getEffect(MomotinkerEffects.FlameBathArmor.get())!=null&&player.hasEffect(MomotinkerEffects.FlameBathArmor.get())){
                                player.removeEffect(MomotinkerEffects.FlameBathArmor.get());
                                player.addEffect(new MobEffectInstance(MomotinkerEffects.FlameBathArmor.get(),2400,getArmorModifierlevel(player,MomotinkerModifiers.flamebath.getId())-1));
                            }
                            flamebathdata.putInt(flamebathcooldown, 240);
                        }
                    }
                }
            }
            if ((getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId()) > 0)||(getMainhandModifierlevel(player,MomotinkerModifiers.longterminvestments.getId()) > 0)) {
                if (player.getMainHandItem().getItem() instanceof ModifiableItem){
                    ToolStack tool = ToolStack.from(player.getMainHandItem());
                    ModDataNBT data = tool.getPersistentData();
                    if (player.hasItemInSlot(EquipmentSlot.OFFHAND)) {
                        Item item =player.getOffhandItem().getItem();
                        boolean config = MomotinkerConfig.shortterminvestments_only_minecraft.get();
                        boolean i= !config || Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getNamespace().contains("minecraft");
                        if (i&&!player.getOffhandItem().hasTag()) {
                            if (data.getString(getResourceLocation("termname")).isEmpty()) {
                                data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex")) + 1);
                                data.putString(getResourceLocation("termname"), ItemString(item));
                                player.getItemBySlot(EquipmentSlot.OFFHAND).setCount(player.getItemBySlot(EquipmentSlot.OFFHAND).getCount() - 1);
                            }
                        }
                    }else if (player.getOffhandItem().isEmpty()&&!data.getString(getResourceLocation("termname")).isEmpty()){
                        int a = (int) Math.floor(data.getFloat(getResource("termindex")));
                        if (a!=0) {
                            ItemStack items = new ItemStack(ForgeRegistries.ITEMS.getValue(getResourceLocation(data.getString(getResourceLocation("termname")))), a);
                            ModifierUtil.dropItem(player, items);
                            data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex")) - a);
                        }else {
                            data.remove(getResource("termindex"));
                            data.remove(getResourceLocation("termname"));
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}