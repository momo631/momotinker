package com.momosensei.momotinker.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class MomotinkerEntitiesMove {
    public static Vec3 calculateMovementVector(float yaw, float pitch) {
        double yawRadians = Math.toRadians(-yaw);
        double pitchRadians = Math.toRadians(-pitch);
        double horizontalMagnitude = Math.cos(pitchRadians);

        double motionX = horizontalMagnitude * Math.sin(yawRadians);
        double motionY = Math.sin(pitchRadians);
        double motionZ = horizontalMagnitude * Math.cos(yawRadians);
        return new Vec3(motionX, motionY, motionZ);
    }

    public static boolean isValidTarget(LivingEntity living,Entity owner) {
        if (living == owner) {
            return false;
        }
        if (owner instanceof Player playerOwner) {
            if (living instanceof Player && ((Player) living).isCreative()) {
                return false;
            }
            if (playerOwner.isAlliedTo(living)) {
                return false;
            }
        }
        return living.isAlive();
    }
    private static final Map<Entity, Map<LivingEntity, Integer>> ignoredTargets = new HashMap<>();

    public static void moveTowardsTargetWithTransfer(Entity entity, LivingEntity target, double speed, double transferDistance) {
        Vec3 currentPos = entity.position();
        Vec3 targetPos = target.getEyePosition();
        double distance = currentPos.distanceTo(targetPos);
        if (distance <= transferDistance) {
            // 将当前目标加入忽略列表
            addToIgnoredTargets(entity, target, 10);
            entity.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 direction = targetPos.subtract(currentPos).normalize();
        entity.setDeltaMovement(direction.scale(speed));
    }

    public static LivingEntity findNearestTargetWithTransfer(Entity entity, Entity owner, double detectionRange) {
        List<LivingEntity> nearbyEntities = entity.level.getEntitiesOfClass(LivingEntity.class,
                entity.getBoundingBox().inflate(detectionRange));

        LivingEntity nearestTarget = null;
        double nearestDistance = Double.MAX_VALUE;

        for (LivingEntity living : nearbyEntities) {
            if (isValidTarget(living, owner) && !isTargetIgnored(entity, living)) {
                double distance = entity.distanceToSqr(living);
                if (distance < nearestDistance) {
                    nearestTarget = living;
                    nearestDistance = distance;
                }
            }
        }
        return nearestTarget;
    }

    private static boolean isTargetIgnored(Entity entity, LivingEntity target) {
        Map<LivingEntity, Integer> entityIgnoredTargets = ignoredTargets.get(entity);
        if (entityIgnoredTargets == null) {
            return false;
        }

        Integer ignoreTicks = entityIgnoredTargets.get(target);
        if (ignoreTicks == null) {
            return false;
        }

        if (ignoreTicks <= 0) {
            entityIgnoredTargets.remove(target);
            if (entityIgnoredTargets.isEmpty()) {
                ignoredTargets.remove(entity);
            }
            return false;
        }

        return true;
    }

    private static void addToIgnoredTargets(Entity entity, LivingEntity target, int ignoreTicks) {
        Map<LivingEntity, Integer> entityIgnoredTargets = ignoredTargets.computeIfAbsent(entity, k -> new HashMap<>());
        entityIgnoredTargets.put(target, ignoreTicks);
    }

    public static void updateEntityIgnoredTargets(Entity entity) {
        Map<LivingEntity, Integer> entityTargets = ignoredTargets.get(entity);
        if (entityTargets != null) {
            entityTargets.replaceAll((target, ticks) -> ticks - 1);
            entityTargets.entrySet().removeIf(entry -> entry.getValue() <= 0);

            if (entityTargets.isEmpty()) {
                ignoredTargets.remove(entity);
            }
        }
    }

    public static void cleanupEntityData(Entity entity) {
        ignoredTargets.remove(entity);
    }
    public static void circularMotion(Entity entity,Entity owner,double radius,double angularSpeed) {
        double centerY = owner.getY() + owner.getEyeHeight() * 0.5;
        int orbitIndex = entity.getPersistentData().getInt("OrbitIndex");
        int totalOrbiters = entity.getPersistentData().getInt("TotalOrbiters");
        double baseAngle = entity.getPersistentData().getDouble("BaseOrbitAngle") + angularSpeed;

        double angleStep = 2 * Math.PI / totalOrbiters;
        double currentAngle = baseAngle + (orbitIndex * angleStep);

        entity.getPersistentData().putDouble("BaseOrbitAngle", baseAngle);

        double targetRelX = Math.cos(currentAngle) * radius;
        double targetRelZ = Math.sin(currentAngle) * radius;

        double currentRelX = entity.getX() - owner.getX();
        double currentRelZ = entity.getZ() - owner.getZ();

        double speedFactor = 0.6;
        double moveX = (targetRelX - currentRelX) * speedFactor;
        double moveZ = (targetRelZ - currentRelZ) * speedFactor;

        entity.setDeltaMovement(moveX, 0, moveZ);
        entity.setPos(
                owner.getX() + currentRelX + moveX,
                centerY,
                owner.getZ() + currentRelZ + moveZ
        );

        if (Math.abs(moveX) > 0.01 || Math.abs(moveZ) > 0.01) {
            float yaw = (float) Math.toDegrees(Math.atan2(-moveX, moveZ));
            entity.setYRot(yaw);
            entity.yRotO = yaw;
        }
    }

    public static void circularMotionNew(Class<? extends Entity> entityClass, Entity entity, Entity owner, Entity owner2, int form, int form2, double radius, double angularSpeed) {
        double centerY = owner.getY() + owner.getEyeHeight() * 0.5;

        // 获取同形式同圆心的实体列表
        List<Entity> sameOrbitEntities = getSameOrbitEntities(entity, entityClass, owner, owner2, form, form2);
        int totalEntities = sameOrbitEntities.size();
        int currentIndex = sameOrbitEntities.indexOf(entity);

        // 计算均匀分布的角度偏移
        double angleOffset = 0;
        if (totalEntities > 1) {
            double idealAngleStep = 2 * Math.PI / totalEntities;
            angleOffset = currentIndex * idealAngleStep;
        }

        // 计算当前角度
        if (!entity.getPersistentData().contains("OrbitAngle")) {
            double initialAngle = Math.atan2(entity.getZ() - owner.getZ(), entity.getX() - owner.getX());
            entity.getPersistentData().putDouble("OrbitAngle", initialAngle);
        }

        double baseAngle = entity.getPersistentData().getDouble("OrbitAngle") + angularSpeed;
        entity.getPersistentData().putDouble("OrbitAngle", baseAngle);

        double currentAngle = baseAngle + angleOffset;

        // 计算相对于玩家的目标位置
        double targetRelX = Math.cos(currentAngle) * radius;
        double targetRelZ = Math.sin(currentAngle) * radius;

        // 计算当前位置相对于玩家的向量
        double currentRelX = entity.getX() - owner.getX();
        double currentRelZ = entity.getZ() - owner.getZ();

        // 添加间距保持和防重叠逻辑
        if (totalEntities > 1) {
            adjustForEqualSpacingAndAvoidOverlap(entity, owner, sameOrbitEntities, currentIndex, radius,
                    targetRelX, targetRelZ, currentAngle);
        }

        // 计算向目标位置移动的速度
        double speedFactor = 0.4;
        double moveX = (targetRelX - currentRelX) * speedFactor;
        double moveZ = (targetRelZ - currentRelZ) * speedFactor;

        entity.setDeltaMovement(moveX, 0, moveZ);
        entity.setPos(
                owner.getX() + currentRelX + moveX,
                centerY,
                owner.getZ() + currentRelZ + moveZ
        );

        // 设置面向方向
        if (Math.abs(moveX) > 0.01 || Math.abs(moveZ) > 0.01) {
            float yaw = (float) Math.toDegrees(Math.atan2(-moveX, moveZ));
            entity.setYRot(yaw);
            entity.yRotO = yaw;
        }
    }

    // 获取同形式同圆心的实体列表
    private static List<Entity> getSameOrbitEntities(Entity entity, Class<? extends Entity> entityClass, Entity owner1, Entity owner2, int form, int form2) {
        List<Entity> sameOrbitEntities = new ArrayList<>();
        List<Entity> nearbyEntities = entity.level.getEntitiesOfClass(Entity.class,
                entity.getBoundingBox().inflate(10));

        for (Entity other : nearbyEntities) {
            if (other != entity && entityClass.isInstance(other) &&
                    form2 == form &&
                    owner1 == owner2) {
                sameOrbitEntities.add(other);
            }
        }
        sameOrbitEntities.add(entity);
        sameOrbitEntities.sort(Comparator.comparingInt(Entity::getId));
        return sameOrbitEntities;
    }

    // 增强的间距调整和防重叠方法
    private static void adjustForEqualSpacingAndAvoidOverlap(Entity entity, Entity owner,
                                                             List<Entity> sameOrbitEntities, int currentIndex,
                                                             double radius, double targetRelX, double targetRelZ,
                                                             double currentAngle) {
        int totalEntities = sameOrbitEntities.size();

        // 获取左右相邻实体
        Entity leftEntity = sameOrbitEntities.get((currentIndex - 1 + totalEntities) % totalEntities);
        Entity rightEntity = sameOrbitEntities.get((currentIndex + 1) % totalEntities);

        // 计算相邻实体的角度
        double leftAngle = getEntityOrbitAngle(leftEntity, owner);
        double rightAngle = getEntityOrbitAngle(rightEntity, owner);

        // 规范化角度
        leftAngle = normalizeAngle(leftAngle);
        rightAngle = normalizeAngle(rightAngle);
        double myAngle = normalizeAngle(currentAngle);

        // 计算与左右实体的角度差
        double leftDiff = calculateAngleDifference(myAngle, leftAngle);
        double rightDiff = calculateAngleDifference(rightAngle, myAngle);

        // 计算理想角度间距
        double idealDiff = 2 * Math.PI / totalEntities;
        double adjustmentFactor = 0.1;

        // 计算最小安全角度
        double minSafeAngle = calculateMinSafeAngle(entity, leftEntity, radius);
        boolean tooCloseToLeft = leftDiff < minSafeAngle;
        boolean tooCloseToRight = rightDiff < minSafeAngle;

        // 调整逻辑
        double adjustment = 0;

        if (tooCloseToLeft || tooCloseToRight) {
            // 防重叠优先：如果太近就远离
            if (tooCloseToLeft && tooCloseToRight) {
                // 如果两边都太近，选择空隙较大的一侧移动
                if (leftDiff > rightDiff) {
                    adjustment += adjustmentFactor * 0.5;
                } else {
                    adjustment -= adjustmentFactor * 0.5;
                }
            } else if (tooCloseToLeft) {
                adjustment += adjustmentFactor;
            } else {
                adjustment -= adjustmentFactor;
            }
        } else {
            // 正常间距调整
            if (leftDiff < idealDiff) {
                adjustment += adjustmentFactor * (idealDiff - leftDiff);
            }
            if (rightDiff < idealDiff) {
                adjustment -= adjustmentFactor * (idealDiff - rightDiff);
            }
        }

        // 应用调整
        if (Math.abs(adjustment) > 0.001) {
            currentAngle += adjustment;

            // 更新目标位置
            targetRelX = Math.cos(currentAngle) * radius;
            targetRelZ = Math.sin(currentAngle) * radius;

            // 更新存储的角度
            entity.getPersistentData().putDouble("OrbitAngle",
                    entity.getPersistentData().getDouble("OrbitAngle") + adjustment);
        }
    }

    // 计算最小安全角度（防止实体重叠）
    private static double calculateMinSafeAngle(Entity entity1, Entity entity2, double radius) {
        // 获取实体的碰撞箱宽度
        double entity1Width = entity1.getBbWidth();
        double entity2Width = entity2.getBbWidth();

        // 计算两个实体的总半径
        double totalRadius = (entity1Width + entity2Width) / 2.0;

        // 计算最小安全角度（弧度）
        return 2 * Math.asin(totalRadius / (2 * radius));
    }

    private static double getEntityOrbitAngle(Entity entity, Entity owner) {
        return Math.atan2(entity.getZ() - owner.getZ(), entity.getX() - owner.getX());
    }

    private static double normalizeAngle(double angle) {
        angle = angle % (2 * Math.PI);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    // 计算两个角度之间的最小差值
    private static double calculateAngleDifference(double angle1, double angle2) {
        double diff = Math.abs(angle1 - angle2);
        return Math.min(diff, 2 * Math.PI - diff);
    }


    public static boolean isInLoadedChunk(Entity entity) {
        Level level = entity.level;
        BlockPos pos = entity.blockPosition();

        if (!level.isLoaded(pos)) {
            return false;
        }
        AABB boundingBox = entity.getBoundingBox();
        BlockPos minPos = new BlockPos(
                (int) Math.floor(boundingBox.minX),
                (int) Math.floor(boundingBox.minY),
                (int) Math.floor(boundingBox.minZ)
        );
        BlockPos maxPos = new BlockPos(
                (int) Math.floor(boundingBox.maxX),
                (int) Math.floor(boundingBox.maxY),
                (int) Math.floor(boundingBox.maxZ)
        );

        return level.isLoaded(minPos) && level.isLoaded(maxPos);
    }
}
