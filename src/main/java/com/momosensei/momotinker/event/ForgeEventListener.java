package com.momosensei.momotinker.event;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.servertoplay.KeyInputEndPKT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID,value = Dist.CLIENT)
public class ForgeEventListener {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (key.KeyBinding.KEY.consumeClick()) {
            Channel.sendToServer(new KeyInputEndPKT());
        }
    }
}
