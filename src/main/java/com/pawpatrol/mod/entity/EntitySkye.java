package com.pawpatrol.mod.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntitySkye extends EntityCreature {

    private static final DataParameter<Boolean> IS_SITTING =
        EntityDataManager.createKey(EntitySkye.class, DataSerializers.BOOLEAN);

    public EntitySkye(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.55F);
        this.stepHeight = 0.6F;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_SITTING, false);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAISit(this) {
            @Override public boolean shouldExecute() { return EntitySkye.this.isSitting(); }
        });
        this.tasks.addTask(3, new EntityAIFollowSkyeTask(this));
        this.tasks.addTask(4, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(5, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            if (!this.world.isRemote) {
                boolean nowSitting = !this.isSitting();
                this.setSitting(nowSitting);
                if (nowSitting) {
                    player.sendMessage(new TextComponentString("§dСкай ждёт на месте."));
                } else {
                    player.sendMessage(new TextComponentString("§dСкай следует за тобой!"));
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    public boolean isSitting() { return this.dataManager.get(IS_SITTING); }
    public void setSitting(boolean s) { this.dataManager.set(IS_SITTING, s); }

    @Override public void writeEntityToNBT(NBTTagCompound c) { super.writeEntityToNBT(c); c.setBoolean("IsSitting", isSitting()); }
    @Override public void readEntityFromNBT(NBTTagCompound c) { super.readEntityFromNBT(c); setSitting(c.getBoolean("IsSitting")); }
    @Override protected boolean canDespawn() { return false; }
    @Override public boolean canBePushed() { return !isSitting(); }
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_WOLF_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource d) { return SoundEvents.ENTITY_WOLF_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_WOLF_DEATH; }
    @Override protected float getSoundVolume() { return 0.4F; }
}
