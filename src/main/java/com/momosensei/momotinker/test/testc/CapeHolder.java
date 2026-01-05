package com.momosensei.momotinker.test.testc;

/*
public interface CapeHolder {

    public BasicSimulation momotinker$getSimulation();

    public void momotinker$setSimulation(BasicSimulation sim);

    public default void updateSimulation(AbstractClientPlayer abstractClientPlayer, int partCount) {
        BasicSimulation simulation = momotinker$getSimulation();
        if(simulation == null || incorrectSimulation(simulation)) {
            simulation = createSimulation();
            momotinker$setSimulation(simulation);
        }
        if(simulation == null) {
            return;
        }
        boolean dirty = simulation.init(partCount);
        if(dirty) {
            simulation.applyMovement(new Vector3(1f, 1f, 0));
            for(int i = 0; i < 5; i++) { // 快速执行几次模拟步骤，使披风达到稳定状态
                simulate(abstractClientPlayer);
            }
        }
    }

    public default boolean incorrectSimulation(BasicSimulation sim) {
        // 只检查是否为 BASIC_SIMULATION_3D 类型
        return sim.getClass() != StickSimulation3d.class;
    }

    public default BasicSimulation createSimulation() {
        // 始终创建 BASIC_SIMULATION_3D 类型的模拟器
        return new StickSimulation3d();
    }

    public default void simulate(AbstractClientPlayer abstractClientPlayer) {
        BasicSimulation simulation = momotinker$getSimulation();
        if(simulation == null || simulation.empty()) {
            return; // 没有披风，无需更新
        }

        double d = abstractClientPlayer.xCloak - abstractClientPlayer.getX();
        double m = abstractClientPlayer.zCloak - abstractClientPlayer.getZ();
        float n = abstractClientPlayer.yBodyRotO + abstractClientPlayer.yBodyRot - abstractClientPlayer.yBodyRotO;
        double o = Mth.sin(n * 0.017453292F);
        double p = -Mth.cos(n * 0.017453292F);

        float heightMul = HEIGHT_MULTIPLIER;
        float strafeMul = STRAFE_MULTIPLIER;

        if(abstractClientPlayer.isUnderWater()) {
            heightMul *= 2; // 水下增加披风阻力
        }

        // 跳跃/下落时给披风一个小的摆动，防止自相交或模拟空气流动
        double fallHack = Mth.clamp((abstractClientPlayer.yo - abstractClientPlayer.getY()) * 10, 0, 1);

        // 设置重力
        if(abstractClientPlayer.isUnderWater()) {
            simulation.setGravity(GRAVITY / 10f);
        } else {
            simulation.setGravity(GRAVITY);
        }

        Vector3 gravity = new Vector3(0, -1, 0);
        Vector2 strafe = new Vector2(
                (float)(abstractClientPlayer.getX() - abstractClientPlayer.xo),
                (float)(abstractClientPlayer.getZ() - abstractClientPlayer.zo)
        );
        strafe.rotateDegrees(-abstractClientPlayer.getYRot());

        double changeX = (d * o + m * p) + fallHack + (abstractClientPlayer.isCrouching() && !simulation.isSneaking() ? 3 : 0);
        double changeY = ((abstractClientPlayer.getY() - abstractClientPlayer.yo) * heightMul) + (abstractClientPlayer.isCrouching() && !simulation.isSneaking() ? 1 : 0);
        double changeZ = -strafe.x * strafeMul;

        simulation.setSneaking(abstractClientPlayer.isCrouching());
        Vector3 change = new Vector3((float)changeX, (float)changeY, (float)changeZ);

        if(abstractClientPlayer.isVisuallySwimming()) {
            float rotation = abstractClientPlayer.getXRot(); // -90 = 向上游, 0 = 水平, 90 = 向下游
            // 偏移旋转，使向上游时不旋转向量
            rotation += 90;
            // 应用旋转
            gravity.rotateDegrees(rotation);
            change.rotateDegrees(rotation);
        }

        simulation.setGravityDirection(gravity);
        simulation.applyMovement(change);
        simulation.simulate();
    }
}

 */
