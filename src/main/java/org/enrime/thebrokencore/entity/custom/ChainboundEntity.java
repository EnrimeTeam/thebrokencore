package org.enrime.thebrokencore.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChainboundEntity extends PathAwareEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ChainboundEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new ChainboundMoveControl(this);
        this.lookControl = new NoLookControl(this);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        // TODO: REPLACE HP
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20)
                .add(EntityAttributes.MOVEMENT_SPEED, 1.2f);
    }

    @Override
    protected void initGoals() {
//        this.targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
//        this.goalSelector.add(0, new ChainboundRamGoal(this));
    }

    // TODO:
    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        var optBox = getNearestAnchorBox(world, this.getBlockPos(), 15, 10);
        if (optBox.isEmpty()) {
            this.discard();
            return null;
        }

        Box box = optBox.get();
        for (double x : new double[]{box.minX, box.maxX}) {
            for (double y : new double[]{box.minY, box.maxY}) {
                for (double z : new double[]{box.minZ, box.maxZ}) {
                    BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
                    world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
                }
            }
        }

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    private static Optional<Box> getNearestAnchorBox(ServerWorldAccess world, BlockPos bossPos, int radiusHorizontal, int radiusVertical) {
        List<BlockPos> anchors = new ArrayList<>();
        BlockPos.Mutable checkedPos = new BlockPos.Mutable();

        for (int dx = -radiusHorizontal; dx <= radiusHorizontal; dx++) {
            for (int dy = -radiusVertical; dy <= radiusVertical; dy++) {
                for (int dz = -radiusHorizontal; dz <= radiusHorizontal; dz++) {
                    checkedPos.set(bossPos.getX() + dx, bossPos.getY() + dy, bossPos.getZ() + dz);
                    // TODO: Replace with TEST_BLOCK or some anchor block when ready
                    if (world.getBlockState(checkedPos).isOf(Blocks.IRON_BLOCK)) {
                        anchors.add(checkedPos.toImmutable());
                    }
                }
            }
        }

        int bestPerimeter = Integer.MAX_VALUE;
        double bestDistance = Double.MAX_VALUE;
        Box bestBox = null;

        for (int i = 0; i < anchors.size(); i++) {
            for (int j = i + 1; j < anchors.size(); j++) {
                BlockPos a = anchors.get(i);
                BlockPos b = anchors.get(j);

                if (a.getX() == b.getX() || a.getY() == b.getY() || a.getZ() == b.getZ()) {
                    continue;
                }

                int x1 = Math.min(a.getX(), b.getX());
                int x2 = Math.max(a.getX(), b.getX());
                int y1 = Math.min(a.getY(), b.getY());
                int y2 = Math.max(a.getY(), b.getY());
                int z1 = Math.min(a.getZ(), b.getZ());
                int z2 = Math.max(a.getZ(), b.getZ());

                if (bossPos.getX() < x1 || bossPos.getX() > x2 ||
                    bossPos.getY() < y1 || bossPos.getY() > y2 ||
                    bossPos.getZ() < z1 || bossPos.getZ() > z2) {
                    continue;
                }

                boolean allCornersExist = true;
                for (int x : new int[]{x1, x2}) {
                    for (int y : new int[]{y1, y2}) {
                        for (int z : new int[]{z1, z2}) {
                            if (!world.getBlockState(new BlockPos(x, y, z)).isOf(Blocks.IRON_BLOCK)) {
                                allCornersExist = false;
                                break;
                            }
                        }
                        if (!allCornersExist) {
                            break;
                        }
                    }
                    if (!allCornersExist) {
                        break;
                    }
                }

                if (allCornersExist) {
                    int dx = x2 - x1;
                    int dy = y2 - y1;
                    int dz = z2 - z1;
                    int perimeter = dx + dy + dz;

                    double centerX = (x1 + x2) / 2.0;
                    double centerY = (y1 + y2) / 2.0;
                    double centerZ = (z1 + z2) / 2.0;
                    double distance = bossPos.getSquaredDistance(centerX, centerY, centerZ);

                    if (perimeter < bestPerimeter || (perimeter == bestPerimeter && distance < bestDistance)) {
                        bestPerimeter = perimeter;
                        bestDistance = distance;
                        bestBox = new Box(x1, y1, z1, x2, y2, z2);
                    }
                }
            }
        }

        return Optional.ofNullable(bestBox);
    }

    // TEST
    // === BEGIN ===
    public class ChainboundRamGoal extends Goal {
        private final ChainboundEntity boss;
        private int cooldown;

        public ChainboundRamGoal(ChainboundEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canStart() {
            if (cooldown > 0) {
                cooldown--;
                return false;
            }
            return boss.getTarget() != null;
        }

        @Override
        public void start() {
            LivingEntity target = boss.getTarget();
            if (target != null) {
                boss.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 2.0); // быстрый рывок
            }
        }

        @Override
        public void tick() {
            // Проверка на "удар о стену"
            if (boss.horizontalCollision || boss.verticalCollision) {
                boss.getWorld().createExplosion(boss, boss.getX(), boss.getY(), boss.getZ(), 2.0f, World.ExplosionSourceType.MOB);
                cooldown = 100; // откат атаки
            }
        }
    }
    // === END ===

    @Override
    public void tickMovement() {
        super.tickMovement();

        // Зафиксировать поворот (например, строго на север = 180f)
        this.setYaw(180f);
        this.setHeadYaw(180f);
        this.prevYaw = 180f;
        this.prevHeadYaw = 180f;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public class NoLookControl extends LookControl {
        public NoLookControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
            // ничего не делаем — моб не поворачивает голову
        }
    }


    public class ChainboundMoveControl extends MoveControl {
        private final ChainboundEntity boss;

        public ChainboundMoveControl(ChainboundEntity boss) {
            super(boss);
            this.boss = boss;
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                double dx = this.targetX - boss.getX();
                double dy = this.targetY - boss.getY();
                double dz = this.targetZ - boss.getZ();

                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < 0.1) {
                    this.state = State.WAIT;
                    return;
                }

                double speed = this.speed * boss.getAttributeValue(EntityAttributes.MOVEMENT_SPEED);
                boss.setVelocity(
                        boss.getVelocity().add(
                                dx / distSq * speed * 0.1,
                                dy / distSq * speed * 0.1,
                                dz / distSq * speed * 0.1
                        )
                );
            }
        }
    }
}
