package com.momosensei.momotinker.network.packet.servertoplay;

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
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Random;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Modifiers.modifiers.FallingStars.falling;
import static com.momosensei.momotinker.Modifiers.modifiers.OverCrystalline.crystallization;
import static com.momosensei.momotinker.Modifiers.modifiers.Red.ender;
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
                ModDataNBT enddate = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                String[] array = new String[]{"层林尽染","引渡徘徊","终归尘土","化为虚无","在此永眠","万籁俱寂","如雨而逝"};
                Random random = new Random();
                int randomIndex = random.nextInt(array.length);
                if (enddate.getFloat(ender)==0) {
                    enddate.putFloat(ender, enddate.getFloat(ender)+90);
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.End.get(), 300));
                    player.sendSystemMessage(Component.literal(array[randomIndex]).withStyle(ChatFormatting.DARK_RED));
                }
            }
            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.fallingstars.getId()) > 0) {
                ModDataNBT falldate = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (falldate.getFloat(falling) == 0) {
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.FallingPreparation.get(), 20));
                    falldate.putFloat(falling, falldate.getFloat(falling) + 30);
                }
                if (falldate.getFloat(falling) > 27 && falldate.getFloat(falling) < 30) {
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.FallingStar.get(), 20));
                    falldate.putFloat(falling, falldate.getFloat(falling) + 120);
                    if (player.getEffect(MomotinkerEffects.FallingPreparation.get())!=null&&player.hasEffect(MomotinkerEffects.FallingPreparation.get())){
                        player.removeEffect(MomotinkerEffects.FallingPreparation.get());
                    }
                }
            }

            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crystallization.getId()) > 0) {
                ModDataNBT crystallizationdate = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(MomotinkerItem.dimensional_prism.get())&&!player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) {
                    int a = (int) crystallizationdate.getFloat(crystallization);
                    int b = RANDOM.nextInt(a + 4);
                    player.getItemBySlot(EquipmentSlot.OFFHAND).setCount(player.getItemBySlot(EquipmentSlot.OFFHAND).getCount() - 1);
                    if (b < 4) {
                        crystallizationdate.putFloat(crystallization, crystallizationdate.getFloat(crystallization) + 1);
                        player.sendSystemMessage(Component.translatable("强化成功！").withStyle(ChatFormatting.GREEN));
                    }
                    if (b >= 4 && a < 6) {
                        crystallizationdate.putFloat(crystallization, crystallizationdate.getFloat(crystallization));
                        player.sendSystemMessage(Component.translatable("强化失败！").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a < 10 && a>=6) {
                        crystallizationdate.putFloat(crystallization, crystallizationdate.getFloat(crystallization) - 1);
                        player.sendSystemMessage(Component.translatable("强化失败！强化等级减1").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a < 14 && a>=10) {
                        crystallizationdate.putFloat(crystallization, crystallizationdate.getFloat(crystallization) - 4);
                        player.sendSystemMessage(Component.translatable("强化失败！强化等级减4").withStyle(ChatFormatting.RED));
                    }
                    if (b >= 4 && a >=14) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        player.sendSystemMessage(Component.translatable("强化失败！装备碎掉了。。。").withStyle(ChatFormatting.RED));
                    }
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 10);
                }
            }
            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.test.getId()) > 0) {
                player.addEffect(new MobEffectInstance(MomotinkerEffects.A.get(), 100));
            }
        });
        context.setPacketHandled(true);
    }
}
