package com.momosensei.momotinker.event;

import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.TriggerDamagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;
import org.joml.Vector4f;


//@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemSightEventHandler {

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;
            // 获取 GLFW 窗口句柄（如果不需要可以去掉）
            long glfwWindow = mc.getWindow().getWindow();
            // 获取摄像机数据
            Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
            float xRot = mc.gameRenderer.getMainCamera().getXRot();
            float yRot = mc.gameRenderer.getMainCamera().getYRot();
            float fov = mc.options.fov().get() * mc.player.getFieldOfViewModifier();

            // 获取窗口尺寸
            int width = mc.getWindow().getWidth();
            int height = mc.getWindow().getHeight();

            // 调用 renderCallback
            renderCallback(glfwWindow, camPos, xRot, yRot, fov, width, height);
        }
    }
    public static void renderCallback(
            long glfwWindow,      // GLFW 窗口句柄（可选）
            Vec3 camPos,          // 摄像机位置
            float xRot,           // 摄像机 X 旋转（俯仰角）
            float yRot,           // 摄像机 Y 旋转（偏航角）
            float fov,            // 视野角度
            int width,            // 屏幕宽度
            int height            // 屏幕高度
    ) {

        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null) return;

            // 调用 renderEntities（检测视野内的物品并造成伤害）
            renderEntities(
                    glfwWindow,
                    (float) camPos.x, (float) camPos.y, (float) camPos.z,
                    xRot, yRot, fov,
                    width, height
            );
        } catch (Throwable e) {
            //e.printStackTrace();
        }
    }
//    public static void renderCallback() {
//        try {
//            Minecraft mc = Minecraft.getInstance();
//            Objects.requireNonNull(mc.level);
//            Vec3 pos = mc.gameRenderer.getMainCamera().getPosition();
//            float fov = 0;
//            if (mc.player != null) {
//                fov = mc.options.fov().get() * mc.player.getFieldOfViewModifier();
//            }
//            renderEntities((float)pos.x, (float)pos.y, (float)pos.z, mc.gameRenderer.getMainCamera().getXRot(), mc.gameRenderer.getMainCamera().getYRot(), fov, mc.getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
//        } catch (Throwable e) {
//            //e.printStackTrace();
//        }
//    }
    private static void renderEntities(
            long hdc,float camX, float camY, float camZ,
            float xRot, float yRot, float fov, int width, int height
    ) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        // 获取玩家视线方向（用于锥形视野检测）
        Vec3 viewVec = player.getViewVector(1.0f); // 现在被使用了！
        Vec3 camPos = new Vec3(camX, camY, camZ);
        //LOGGER.warn("Player or level is null");
        // 遍历附近的物品实体
        if (mc.level != null) {
            for (ItemEntity item : mc.level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(20.0))) {
                // 调试：打印所有物品

                // 只处理钻石
                if (item.getItem().getItem() != Items.DIAMOND) continue;

                // 获取钻石位置和屏幕坐标
                Vec3 itemPos = item.position();
                Vector4f screenPos = projectToScreen(
                        itemPos.x, itemPos.y, itemPos.z,
                        (float) camPos.x, (float) camPos.y, (float) camPos.z,
                        xRot, yRot, fov,
                        width, height
                );
                // 检查是否在屏幕内
                if (screenPos.x() >= 0 && screenPos.x() <= 1 && screenPos.y() >= 0 && screenPos.y() <= 1) {
                    // 检查是否未被遮挡
                    if (!isOccluded(mc, camPos, itemPos)) {
                        applyDamage(player);
                        break;
                    }
                }
            }
        }
    }
    private static void applyDamage(Player player) {
        if (player.level().isClientSide) {
            // 客户端：发送数据包给服务端
            Channel.INSTANCE.sendToServer(new TriggerDamagePacket());
        } else {
            // 服务端（或单人模式）：直接造成伤害
            player.hurt(player.damageSources().fellOutOfWorld(), 1.0f);
        }
    }
    /**
     * 将 3D 世界坐标投影到 2D 屏幕坐标
     */
    private static Vector4f projectToScreen(
            double itemX, double itemY, double itemZ,
            float camX, float camY, float camZ,
            float xRot, float yRot, float fov,
            int width, int height
    ) {
        // 1. 构建 ModelView 矩阵（摄像机视角）
        Matrix4f modelView = new Matrix4f()
                .identity()
                .rotateY((float) Math.toRadians(-yRot)) // Yaw
                .rotateX((float) Math.toRadians(xRot))   // Pitch
                .translate(-camX, -camY, -camZ);

        // 2. 构建 Projection 矩阵（透视投影）
        Matrix4f projection = new Matrix4f()
                .identity()
                .perspective(
                        (float) Math.toRadians(fov),
                        (float) width / height,
                        0.05f,
                        1000.0f
                );

        // 3. 计算 MVP 矩阵
        Matrix4f mvp = new Matrix4f(projection).mul(modelView);

        // 4. 转换世界坐标到裁剪空间
        Vector4f clipCoords = new Vector4f(
                (float) (itemX - camX),
                (float) (itemY - camY),
                (float) (itemZ - camZ),
                1.0f
        ).mul(mvp);

        // 5. 透视除法（归一化到 NDC [-1, 1]）
        if (clipCoords.w != 0) {
            clipCoords.x /= clipCoords.w;
            clipCoords.y /= clipCoords.w;
            clipCoords.z /= clipCoords.w;
        }
        // ... (矩阵计算过程)
        // 透视除法后
        float ndcX = clipCoords.x() / clipCoords.w();
        float ndcY = clipCoords.y() / clipCoords.w();

        // 转换到屏幕坐标
        float screenX = (ndcX + 1.0f) / 2.0f;
        float screenY = (1.0f - ndcY) / 2.0f;

        return new Vector4f(screenX, screenY, 0, 1);
    }

    /**
     * 检查目标是否被方块遮挡（避免隔着墙也能触发）
     */
    private static boolean isOccluded(Minecraft mc, Vec3 from, Vec3 to) {
        ClipContext context = new ClipContext(
                from,
                to,
                ClipContext.Block.COLLIDER, // 检测碰撞方块
                ClipContext.Fluid.NONE,     // 不检测流体
                null                        // 无额外条件
        );
        BlockHitResult hit = null;
        if (mc.level != null) {
            hit = mc.level.clip(context);
        }
        if (hit != null) {
            return hit.getType() == HitResult.Type.BLOCK; // 如果碰撞到方块，说明被遮挡
        }
        return false;
    }
}
