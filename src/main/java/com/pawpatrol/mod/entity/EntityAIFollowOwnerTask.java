package com.pawpatrol.mod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

/**
 * Задача ИИ: Чейз следует за ближайшим игроком, если не сидит.
 * Останавливается на расстоянии 2-3 блоков от игрока.
 */
public class EntityAIFollowOwnerTask extends EntityAIBase {

    private final EntityChase chase;
    private EntityPlayer targetPlayer;
    private final World world;
    private final PathNavigate navigator;

    private int updateTimer;
    private static final double FOLLOW_SPEED = 0.8D;
    private static final float STOP_DISTANCE = 3.0F;
    private static final float START_DISTANCE = 10.0F;

    public EntityAIFollowOwnerTask(EntityChase chaseIn) {
        this.chase = chaseIn;
        this.world = chaseIn.world;
        this.navigator = chaseIn.getNavigator();
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.chase.isSitting()) return false;

        // Ищем ближайшего игрока
        EntityPlayer nearest = this.world.getClosestPlayerToEntity(this.chase, START_DISTANCE);
        if (nearest == null) return false;

        // Если игрок слишком близко — не нужно следовать
        if (nearest.getDistanceSq(this.chase) < STOP_DISTANCE * STOP_DISTANCE) return false;

        this.targetPlayer = nearest;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.chase.isSitting()) return false;
        if (this.targetPlayer == null || !this.targetPlayer.isEntityAlive()) return false;
        if (this.navigator.noPath()) return false;
        return this.chase.getDistanceSq(this.targetPlayer) > STOP_DISTANCE * STOP_DISTANCE;
    }

    @Override
    public void startExecuting() {
        this.updateTimer = 0;
    }

    @Override
    public void resetTask() {
        this.targetPlayer = null;
        this.navigator.clearPathEntity();
    }

    @Override
    public void updateTask() {
        if (this.targetPlayer == null) return;

        // Смотрим на игрока
        this.chase.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 10.0F, this.chase.getVerticalFaceSpeed());

        // Обновляем путь каждые 10 тиков
        if (--this.updateTimer <= 0) {
            this.updateTimer = 10;
            this.navigator.tryMoveToEntityLiving(this.targetPlayer, FOLLOW_SPEED);
        }
    }
}
