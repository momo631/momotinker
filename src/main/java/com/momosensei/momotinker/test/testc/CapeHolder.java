package com.momosensei.momotinker.test.testc;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;

import static com.momosensei.momotinker.test.testc.CapeConfig.*;

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
            for(int i = 0; i < 5; i++) {
                simulate(abstractClientPlayer);
            }
        }
    }

    public default boolean incorrectSimulation(BasicSimulation sim) {
        return sim.getClass() != StickSimulation3d.class;
    }

    public default BasicSimulation createSimulation() {
        return new StickSimulation3d();
    }

    public default void simulate(AbstractClientPlayer abstractClientPlayer) {
        BasicSimulation simulation = momotinker$getSimulation();
        if(simulation == null || simulation.empty()) {
            return;
        }

        double d = abstractClientPlayer.xCloak - abstractClientPlayer.getX();
        double m = abstractClientPlayer.zCloak - abstractClientPlayer.getZ();
        float n = abstractClientPlayer.yBodyRotO + abstractClientPlayer.yBodyRot - abstractClientPlayer.yBodyRotO;
        double o = Mth.sin(n * 0.017453292F);
        double p = -Mth.cos(n * 0.017453292F);

        double fallHack = Mth.clamp((abstractClientPlayer.yo - abstractClientPlayer.getY()) * 10, 0, 1);

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
        double changeY = ((abstractClientPlayer.getY() - abstractClientPlayer.yo) * HEIGHT_MULTIPLIER) + (abstractClientPlayer.isCrouching() && !simulation.isSneaking() ? 1 : 0);
        double changeZ = -strafe.x * STRAFE_MULTIPLIER;

        simulation.setSneaking(abstractClientPlayer.isCrouching());
        Vector3 change = new Vector3((float)changeX, (float)changeY, (float)changeZ);

        if(abstractClientPlayer.isVisuallySwimming()) {
            float rotation = abstractClientPlayer.getXRot();
            rotation += 90;
            gravity.rotateDegrees(rotation);
            change.rotateDegrees(rotation);
        }

        simulation.setGravityDirection(gravity);
        simulation.applyMovement(change);
        simulation.simulate();
    }
}
