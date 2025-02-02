package com.momosensei.momotinker.event;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.servertoplay.KeyInputPKT;
import com.momosensei.momotinker.renderer.Layer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.momosensei.momotinker.renderer.Layer.CHARGE_TEXTURE;


@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID,value = Dist.CLIENT)
public class ForgeEventListener {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (key.KeyBinding.KEY.consumeClick()) {
            Channel.sendToServer(new KeyInputPKT());
        }
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, "default");
        addLayerToPlayerSkin(event, "slim");
    }

    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new Layer.Vanilla(livingRenderer, CHARGE_TEXTURE, Layer.CHARGED));
        }
    }
}
