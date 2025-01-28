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

import static com.momosensei.momotinker.Modifiers.modifiers.Red.ender;

public class KeyInputEndPKT {
    public int key;

    public KeyInputEndPKT(){
    }

    public KeyInputEndPKT(int key){
        this.key = key;
    }

    public static void encode(KeyInputEndPKT pkt, FriendlyByteBuf buf){
        buf.writeInt(pkt.key);
    }

    public static KeyInputEndPKT decode(FriendlyByteBuf buf){
        return new KeyInputEndPKT(buf.readInt());
    }

    public static void handlePacket(KeyInputEndPKT pkt, Supplier<NetworkEvent.Context> context$) {
        NetworkEvent.Context context = context$.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            String[] array = new String[]{"层林尽染","引渡徘徊","终归尘土","化为虚无","在此永眠","万籁俱寂","如雨而逝"};
            Random random = new Random();
            int randomIndex = random.nextInt(array.length);
            if (player != null && ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.red.getId()) > 0) {
                ModDataNBT tooldata = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (tooldata.getFloat(ender)==0) {
                    tooldata.putFloat(ender, tooldata.getFloat(ender)+90);
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.End.get(), 300));
                    player.sendSystemMessage(Component.literal(array[randomIndex]).withStyle(ChatFormatting.DARK_RED));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
