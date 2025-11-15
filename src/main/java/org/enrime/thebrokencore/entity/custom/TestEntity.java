package org.enrime.thebrokencore.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class TestEntity extends HostileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    protected TargetPredicate targetPredicate;
    protected LivingEntity targetEntity;
    private int cd;

    public TestEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        cd = 30;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, true));
        //this.goalSelector.add(4, new LookAroundGoal(this));

        //this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void tick(){
        super.tick();
        cd--;
        if(cd <= 0) {
            cd = 30;
            this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(600).setPredicate(null);
            this.targetEntity = getWorld().getClosestPlayer(this, 600);
            Path path = this.getNavigation().findPathTo(targetEntity, 0);
            this.getNavigation().startMovingAlong(path, 0.4D);
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 15)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.ATTACK_DAMAGE, 3)
                .add(EntityAttributes.SCALE, 3);
    }


    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if(event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.test_entity.walk", Animation.LoopType.LOOP));
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().then("animation.test_entity.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
