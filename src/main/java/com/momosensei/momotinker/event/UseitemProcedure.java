package com.momosensei.momotinker.mobs;

import com.momosensei.momotinker.network.Channel;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.register.MomotinkerTools.trigger_blade;

@Mod.EventBusSubscriber
public class UseitemProcedure {
	@SubscribeEvent
	public static void onUseItemStart(LivingEntityUseItemEvent.Start event) {
		if (event != null && event.getEntity() != null&& event.getEntity().getItemBySlot(EquipmentSlot.MAINHAND).is(trigger_blade.get())) {
			execute(event, event.getEntity().level(), event.getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.isClientSide()) {
			SetupanimationProcedure.setAnimationClientside((Player) entity, "slashanimation", true);
		}
		if (!world.isClientSide()) {
			if (entity instanceof Player && world instanceof ServerLevel srvLvl_) {
				List<Connection> connections = srvLvl_.getServer().getConnection().getConnections();
				synchronized (connections) {
					Iterator<Connection> iterator = connections.iterator();
					while (iterator.hasNext()) {
						Connection connection = iterator.next();
						if (!connection.isConnecting() && connection.isConnected())
							Channel.PACKET_HANDLER.sendTo(new SetupanimationProcedure.CModAnimationMessage(Component.literal("slashanimation"), entity.getId(), true), connection, NetworkDirection.PLAY_TO_CLIENT);
					}
				}
			}
		}
	}
}
