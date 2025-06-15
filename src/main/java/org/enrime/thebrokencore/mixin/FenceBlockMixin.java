package org.enrime.thebrokencore.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.Direction;
import org.enrime.thebrokencore.block.custom.VerticalSlabBlock;
import org.enrime.thebrokencore.block.custom.VerticalSlabBlock.VerticalSlabType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin {
    @Inject(method = "canConnect", at = @At("HEAD"), cancellable = true)
    private void canConnectToVerticalSlab(BlockState state, boolean neighborIsFullSquare, Direction dir, CallbackInfoReturnable<Boolean> cir){
        if (state.getBlock() instanceof VerticalSlabBlock) {
            Direction slabFacing = state.get(VerticalSlabBlock.FACING);
            VerticalSlabType slabType = state.get(VerticalSlabBlock.TYPE);

            boolean shouldConnect =
                    (slabFacing.getOpposite() != dir) ||
                    (slabType == VerticalSlabType.DOUBLE);

            cir.setReturnValue(shouldConnect);
        }
    }
}
