package org.enrime.thebrokencore.entity.custom;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.enrime.thebrokencore.effect.ModEffects;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.EnumSet;

public class BateyeEntity extends FlyingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT =
            DataTracker.registerData(BateyeEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public BateyeEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlyingEyeMoveControl(this);
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 10)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.6f)
                /*.add(EntityAttributes.FLYING_SPEED, 0.6f)*/;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bateye.fly"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // Variant

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DATA_ID_TYPE_VARIANT, 0);
    }

    public BateyeVariant getVariant() {
        return BateyeVariant.byId(getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return dataTracker.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BateyeVariant variant) {
        dataTracker.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", getTypeVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        // TODO: Make percent based choice
        BateyeVariant variant = BateyeVariant.getRandomVariant(random);
        setVariant(variant);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    // TEST ONLY


    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            attacker.addStatusEffect(new StatusEffectInstance(ModEffects.MARK, 600));
        }
        return super.damage(world, source, amount);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new FlyAroundGoal(this, 1.0, 10.0));
        this.goalSelector.add(7, new LookAtFlightDirection(this));
    }

    // TODO: Extract new point choose radius and random height modifier as variables
    private static class FlyAroundGoal extends Goal {
        private final MobEntity mob;
        private final double speed;
        private final double hoverHeight;

        public FlyAroundGoal(MobEntity mob, double speed, double hoverHeight) {
            this.mob = mob;
            this.speed = speed;
            this.hoverHeight = hoverHeight;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            MoveControl moveControl = mob.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            } else {
                double dx = moveControl.getTargetX() - mob.getX();
                double dy = moveControl.getTargetY() - mob.getY();
                double dz = moveControl.getTargetZ() - mob.getZ();
                double distSq = dx * dx + dy * dy + dz * dz;
                return distSq < 1.0 || distSq > 1000.0;
            }
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            Random random = mob.getRandom();

            double targetX = mob.getX() + (random.nextDouble() * 2 - 1) * 12.0;
            double targetZ = mob.getZ() + (random.nextDouble() * 2 - 1) * 12.0;

            int groundY = mob.getWorld().getTopY(
                    Heightmap.Type.MOTION_BLOCKING,
                    (int) targetX, (int) targetZ
            );

            double targetY = groundY + hoverHeight + (random.nextDouble() * 2 - 1) * 5.0;

            mob.getMoveControl().moveTo(targetX, targetY, targetZ, speed);
        }
    }

    private static class LookAtFlightDirection extends Goal {
        private final MobEntity mob;

        public LookAtFlightDirection(MobEntity mob) {
            this.mob = mob;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return true;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            Vec3d vec3d = this.mob.getVelocity();
            this.mob.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180.0f / (float) Math.PI));
            this.mob.bodyYaw = this.mob.getYaw();
        }
    }

    private static class FlyingEyeMoveControl extends MoveControl {
        private final MobEntity mob;

        public FlyingEyeMoveControl(MobEntity mob) {
            super(mob);
            this.mob = mob;
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                double dx = this.targetX - mob.getX();
                double dy = this.targetY - mob.getY();
                double dz = this.targetZ - mob.getZ();
                double distanceSquared = dx * dx + dy * dy + dz * dz;

                if (distanceSquared < 0.1) {
                    this.state = State.WAIT;
                    return;
                }

                double distance = Math.sqrt(distanceSquared);
                dx /= distance;
                dy /= distance;
                dz /= distance;

                // Проверка столкновений
                // {
                    mob.setVelocity(mob.getVelocity().add(dx * 0.025, dy * 0.025, dz * 0.025));
                //} else {
                if (!willCollide(dx, dy, dz, MathHelper.ceil(distance))) {
                    this.state = State.WAIT;
                }
            }
        }

        private boolean willCollide(double dx, double dy, double dz, int steps) {
            Box box = mob.getBoundingBox();
            for (int i = 1; i < steps; i++) {
                box = box.offset(dx, dy, dz);
                if (!mob.getWorld().isSpaceEmpty(mob, box)) {
                    return false;
                }
            }
            return true;
        }
    }

}
