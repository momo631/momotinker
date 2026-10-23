package com.momosensei.momotinker.data.gungnir;

import com.mojang.blaze3d.vertex.PoseStack;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.GungnirTrajectoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GungnirTrajectoryClient {
    private static final List<GungnirTrajectoryData> TRAILS = new ArrayList<>();
    private static final int MAX_TRAILS = 64;

    private static final float SHRINK_START = 10;
    private static final float DURATION = 20;

    private GungnirTrajectoryClient() {}

    public static void add(Vec3 from, Vec3 to, float duration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        if (TRAILS.size() >= MAX_TRAILS) {
            TRAILS.remove(0);
        }

        TRAILS.add(new GungnirTrajectoryData(
                from, to,
                mc.level.getGameTime(),
                duration,
                mc.level.dimension()
        ));
    }

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            TRAILS.clear();
            return;
        }
        long now = mc.level.getGameTime();
        var currentDim = mc.level.dimension();
        TRAILS.removeIf(t -> t.expired(now) || !t.dimension.equals(currentDim));
    }

    public static void clear() {
        TRAILS.clear();
    }

    public static void render(PoseStack pose, MultiBufferSource provider, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || TRAILS.isEmpty()) return;

        long now = mc.level.getGameTime();
        var currentDim = mc.level.dimension();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        for (GungnirTrajectoryData t : TRAILS) {
            if (!t.dimension.equals(currentDim)) continue;

            float elapsed = (now - t.startGameTime) + partialTick;
            if (elapsed >= DURATION) continue;

            float shrinkProgress;
            if (elapsed <= SHRINK_START) {
                shrinkProgress = 0F;
            } else {
                shrinkProgress = (elapsed - SHRINK_START) / (DURATION - SHRINK_START);
                shrinkProgress = Mth.clamp(shrinkProgress, 0F, 1F);
            }

            float tLeft = 1F - shrinkProgress;
            float radius = 0.15F * tLeft * tLeft;
            int alpha = (int) (255 * tLeft);

            if (radius <= 1e-4F || alpha <= 0) continue;

            Vec3 dir = t.to.subtract(t.from);

            pose.pushPose();
            pose.translate(-cam.x, -cam.y, -cam.z);
            pose.translate(t.from.x, t.from.y, t.from.z);

            GungnirTrajectoryRenderer.renderBeam(dir, radius, alpha, now, partialTick, pose, provider);

            pose.popPose();
        }
    }

    public static void addGungnirTrajectory(Player player, LivingEntity living,InteractionHand hand) {
        if (player.level() instanceof ServerLevel serverLevel) {
            Vec3 from = getFrom(player, hand);
            Vec3 to = new Vec3(
                    living.getX(),
                    living.getY() + living.getBbHeight() * 0.5,
                    living.getZ());
            GungnirTrajectoryPacket pkt = new GungnirTrajectoryPacket(from, to, 30);
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                if (serverPlayer.distanceToSqr(player) < 200 * 200) {
                    Channel.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> serverPlayer),
                            pkt
                    );
                }
            }
        }
    }

    private static @NotNull Vec3 getFrom(Player player,InteractionHand hand) {
        Vec3 look = player.getLookAngle();
        Vec3 right = new Vec3(-look.z, 0, look.x);
        if (right.lengthSqr() < 1e-6) {
            right = new Vec3(1, 0, 0);
        } else {
            right = right.normalize();
        }

        final double sideOffset = 0.4;

        double side = (hand == InteractionHand.MAIN_HAND) ? 1.0 : -1.0;

        return new Vec3(
                player.getX() + right.x * sideOffset * side,
                player.getY() + player.getEyeHeight()*1.05,
                player.getZ() + right.z * sideOffset * side
        );
    }
}