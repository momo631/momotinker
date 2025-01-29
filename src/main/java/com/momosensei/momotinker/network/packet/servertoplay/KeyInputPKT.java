package com.momosensei.momotinker.network.packet.servertoplay;

import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Random;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Modifiers.modifiers.FallingStars.falling;
import static com.momosensei.momotinker.Modifiers.modifiers.Red.ender;

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
                    falldate.putFloat(falling,falldate.getFloat(falling)+30);
                }
                if (falldate.getFloat(falling) >27&&falldate.getFloat(falling)<30){
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.FallingStar.get(), 20));
                    falldate.putFloat(falling,falldate.getFloat(falling)+90);
                }
            }
            });
        context.setPacketHandled(true);
    }
}
