package com.pawpatrol.mod.entity;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

public class EntityAIFollowRubbleTask extends EntityAIBase {

    private final EntityRubble rubble;
    private EntityPlayer targetPlayer;
    private final World world;
    private final PathNavigate navigator;
    private int updateTimer;

    private static final double FOLLOW_SPEED = 0.8D;
    private static final float STOP_DISTANCE = 3.0F;
    private static final float START_DISTANCE = 10.0F;

    public EntityAIFollowRubbleTask(EntityRubble rubbleIn) {
        this.rubble = rubbleIn;
        this.world = rubbleIn.world;
        this.navigator = rubbleIn.getNavigator();
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.rubble.isSitting()) return false;
        EntityPlayer nearest = this.world.getClosestPlayerToEntity(this.rubble, START_DISTANCE);
        if (nearest == null) return false;
        if (nearest.getDistanceSq(this.rubble) < STOP_DISTANCE * STOP_DISTANCE) return false;
        this.targetPlayer = nearest;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.rubble.isSitting()) return false;
        if (this.targetPlayer == null || !this.targetPlayer.isEntityAlive()) return false;
        if (this.navigator.noPath()) return false;
        return this.rubble.getDistanceSq(this.targetPlayer) > STOP_DISTANCE * STOP_DISTANCE;
    }

    @Override public void startExecuting() { this.updateTimer = 0; }
    @Override public void resetTask() { this.targetPlayer = null; this.navigator.clearPathEntity(); }

    @Override
    public void updateTask() {
        if (this.targetPlayer == null) return;
        this.rubble.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 10.0F, this.rubble.getVerticalFaceSpeed());
        if (--this.updateTimer <= 0) {
            this.updateTimer = 10;
            this.navigator.tryMoveToEntityLiving(this.targetPlayer, FOLLOW_SPEED);
        }
    }
}
