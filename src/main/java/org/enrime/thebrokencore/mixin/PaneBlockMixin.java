package org.enrime.thebrokencore.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.enrime.thebrokencore.block.custom.VerticalSlabBlock;
import org.enrime.thebrokencore.block.custom.VerticalSlabBlock.VerticalSlabType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PaneBlock.class)
public abstract class PaneBlockMixin extends HorizontalConnectingBlock {
    protected PaneBlockMixin(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
        super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    }

    @Unique
    private Direction thebrokencore$checkedDirection = null;
    @Unique
    private int thebrokencore$directionIdx = -1;
    @Unique
    private static final Direction[] thebrokencore$directions = new Direction[] {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST
    };

    @Inject(
            method = "getStateForNeighborUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PaneBlock;connectsTo(Lnet/minecraft/block/BlockState;Z)Z")
    )
    private void captureDirectionOnNeighbourUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random, CallbackInfoReturnable<BlockState> cir) {
        thebrokencore$checkedDirection = direction;
    }

    @Inject(
            method = "getPlacementState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PaneBlock;connectsTo(Lnet/minecraft/block/BlockState;Z)Z")
    )
    private void captureDirectionOnPlacement(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        thebrokencore$checkedDirection = thebrokencore$directions[++thebrokencore$directionIdx % thebrokencore$directions.length];
    }

    @Inject(method = "connectsTo", at = @At("HEAD"), cancellable = true)
    private void canConnectToVerticalSlab(BlockState state, boolean sideSolidFullSquare, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof VerticalSlabBlock && thebrokencore$checkedDirection != null) {
            Direction slabFacing = state.get(VerticalSlabBlock.FACING);
            VerticalSlabType slabType = state.get(VerticalSlabBlock.TYPE);

            boolean shouldConnect =
                    (slabFacing != thebrokencore$checkedDirection) ||
                    (slabType == VerticalSlabType.DOUBLE);

            cir.setReturnValue(shouldConnect);
        }
    }

    @Inject(method = "connectsTo", at = @At("RETURN"))
    private void resetLastCheckedDirection(BlockState state, boolean sideSolidFullSquare, CallbackInfoReturnable<Boolean> cir) {
        thebrokencore$checkedDirection = null;
    }
}
