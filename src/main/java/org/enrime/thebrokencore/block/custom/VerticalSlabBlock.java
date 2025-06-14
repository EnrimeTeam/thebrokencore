package org.enrime.thebrokencore.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class VerticalSlabBlock extends Block implements Waterloggable {
    public static final MapCodec<SlabBlock> CODEC = createCodec(SlabBlock::new);

    public static final EnumProperty<Direction> FACING = EnumProperty.of("facing", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    public static final EnumProperty<VerticalSlabType> TYPE = EnumProperty.of("type", VerticalSlabType.class);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(0, 0, 0, 16, 16, 8);
    private static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(0, 0, 8, 16, 16, 16);
    private static final VoxelShape SHAPE_WEST = Block.createCuboidShape(0, 0, 0, 8, 16, 16);
    private static final VoxelShape SHAPE_EAST = Block.createCuboidShape(8, 0, 0, 16, 16, 16);

    public VerticalSlabBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(TYPE, VerticalSlabType.SINGLE)
                .with(WATERLOGGED, false));
    }

    public MapCodec<? extends SlabBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(TYPE) == VerticalSlabType.DOUBLE) {
            return VoxelShapes.fullCube();
        }

        return switch (state.get(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();

        // Turning single slab into double
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        if (blockState.isOf(this)) {
            return blockState
                    .with(FACING, blockState.get(FACING))
                    .with(TYPE, VerticalSlabType.DOUBLE)
                    .with(WATERLOGGED, false);
        }

        Vec3d hitPos = ctx.getHitPos();
        double relX = hitPos.x - blockPos.getX();
        double relZ = hitPos.z - blockPos.getZ();

        Direction facing = getFacingDirection(ctx, relX, relZ);

        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        boolean isPlacedInWater = fluidState.getFluid() == Fluids.WATER;

        return getDefaultState()
                .with(FACING, facing)
                .with(TYPE, VerticalSlabType.SINGLE)
                .with(WATERLOGGED, isPlacedInWater);
    }

    private static Direction getFacingDirection(ItemPlacementContext ctx, double relX, double relZ) {
        Direction facing = ctx.getHorizontalPlayerFacing();

        Direction side = ctx.getSide();
        if (side.getAxis().isVertical()) {
            double centerX = 0.5;
            double centerZ = 0.5;
            facing = switch (facing) {
                case NORTH, SOUTH -> (relZ < centerX) ? Direction.NORTH : Direction.SOUTH;
                case WEST, EAST -> (relX < centerZ) ? Direction.WEST : Direction.EAST;
                default -> facing;  // Should not ever trigger, but needed for switch
            };
        }
        return facing;
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext ctx) {
        ItemStack handStack = ctx.getStack();
        if (!handStack.isOf(this.asItem())) {
            return false;
        }

        if (state.get(TYPE) != VerticalSlabType.SINGLE) {
            return false;
        }

        Direction clickSide = ctx.getSide();
        if (clickSide.getAxis().isVertical()) {
            Vec3d hitPos = ctx.getHitPos();
            Vec3d blockCenterPos = ctx.getBlockPos().toCenterPos();
            double relY = hitPos.subtract(blockCenterPos).y;
            return (clickSide == Direction.UP && relY < 0) || (clickSide == Direction.DOWN && relY > 0);
        }

        Direction facing = state.get(FACING);
        return clickSide == facing.getOpposite();
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED)
                ? Fluids.WATER.getStill(false)
                : super.getFluidState(state);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return state.get(TYPE) != VerticalSlabType.DOUBLE
                ? Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState)
                : false;
    }

    @Override
    public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return state.get(TYPE) != VerticalSlabType.DOUBLE
                ? Waterloggable.super.canFillWithFluid(player, world, pos, state, fluid)
                : false;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    public enum VerticalSlabType implements StringIdentifiable {
        SINGLE("single"),
        DOUBLE("double");

        private final String name;

        VerticalSlabType(final String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }
}
