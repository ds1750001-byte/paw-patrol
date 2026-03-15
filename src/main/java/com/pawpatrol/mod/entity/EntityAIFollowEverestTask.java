package com.pawpatrol.mod.entity;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

public class EntityAIFollowEverestTask extends EntityAIBase {

    private final EntityEverest everest;
    private EntityPlayer targetPlayer;
    private final World world;
    private final PathNavigate navigator;
    private int updateTimer;

    private static final double FOLLOW_SPEED = 0.8D;
    private static final float STOP_DISTANCE = 3.0F;
    private static final float START_DISTANCE = 10.0F;

    public EntityAIFollowEverestTask(EntityEverest e) {
        this.everest = e; this.world = e.world; this.navigator = e.getNavigator();
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (everest.isSitting()) return false;
        EntityPlayer p = world.getClosestPlayerToEntity(everest, START_DISTANCE);
        if (p == null || p.getDistanceSq(everest) < STOP_DISTANCE * STOP_DISTANCE) return false;
        targetPlayer = p; return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (everest.isSitting() || targetPlayer == null || !targetPlayer.isEntityAlive()) return false;
        if (navigator.noPath()) return false;
        return everest.getDistanceSq(targetPlayer) > STOP_DISTANCE * STOP_DISTANCE;
    }

    @Override public void startExecuting() { updateTimer = 0; }
    @Override public void resetTask() { targetPlayer = null; navigator.clearPathEntity(); }

    @Override
    public void updateTask() {
        if (targetPlayer == null) return;
        everest.getLookHelper().setLookPositionWithEntity(targetPlayer, 10F, everest.getVerticalFaceSpeed());
        if (--updateTimer <= 0) { updateTimer = 10; navigator.tryMoveToEntityLiving(targetPlayer, FOLLOW_SPEED); }
    }
}
