package com.pawpatrol.mod.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityChase extends EntityCreature {

    // DataParameter для синхронизации режима между клиентом и сервером
    private static final DataParameter<Boolean> IS_SITTING =
        EntityDataManager.createKey(EntityChase.class, DataSerializers.BOOLEAN);

    // AI задача следования за игроком
    private EntityAIFollowOwner followOwnerAI;
    private EntityPlayer owner;

    public EntityChase(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 0.7F);
        this.stepHeight = 0.6F;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_SITTING, false);
    }

    @Override
    protected void initEntityAI() {
        // Задача 0: стоять на месте если sitting
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAISit(this) {
            @Override
            public boolean shouldExecute() {
                return EntityChase.this.isSitting();
            }
        });
        // Задача следования за ближайшим игроком
        this.tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false) {
            // Переопределяем как задачу следования, не атаки
        });
        this.tasks.addTask(3, new EntityAIFollowOwnerTask(this));
        this.tasks.addTask(4, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(5, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    // Shift + ПКМ - переключение режима
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            if (!this.world.isRemote) {
                boolean nowSitting = !this.isSitting();
                this.setSitting(nowSitting);
                this.owner = player;

                if (nowSitting) {
                    player.sendMessage(new TextComponentString("§bЧейз ждёт на месте."));
                } else {
                    player.sendMessage(new TextComponentString("§bЧейз следует за тобой!"));
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    public boolean isSitting() {
        return this.dataManager.get(IS_SITTING);
    }

    public void setSitting(boolean sitting) {
        this.dataManager.set(IS_SITTING, sitting);
    }

    public EntityPlayer getOwner() {
        return owner;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsSitting", this.isSitting());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSitting(compound.getBoolean("IsSitting"));
    }

    @Override
    protected boolean canDespawn() {
        return false; // Чейз не исчезает
    }

    @Override
    public boolean canBePushed() {
        return !this.isSitting();
    }

    // Звуки
    @Override
    protected net.minecraft.util.SoundEvent getAmbientSound() {
        return net.minecraft.init.SoundEvents.ENTITY_WOLF_AMBIENT;
    }

    @Override
    protected net.minecraft.util.SoundEvent getHurtSound(net.minecraft.util.DamageSource damageSourceIn) {
        return net.minecraft.init.SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    protected net.minecraft.util.SoundEvent getDeathSound() {
        return net.minecraft.init.SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }
}
