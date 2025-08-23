package org.enrime.thebrokencore.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.render.entity.state.PaintingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaintingEntityRenderer.class)
public abstract class KnightPaintingEyesMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/PaintingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN")
    )
    private void renderEyes(PaintingEntityRenderState paintingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        PaintingVariant variant = paintingEntityRenderState.variant;
        if (variant == null) {
            return;
        }

        if (variant.assetId().equals(Identifier.of("thebrokencore:knight"))) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(paintingEntityRenderState.facing.getHorizontalQuarterTurns() * -90));
            // 1 block = 0.5f => 1 px = 0.0375f (painting border width)
            // + 0.0001f to draw slightly in front of the painting
            matrixStack.translate(0.0f,  0.0f, 0.0375f + 0.0001f);

            float eyesWidth = 0.07f;
            float eyesHeight = 0.03f;
            float eyesDistance = 0.125f;
            float eyesXOffset = 0.015f;
            float eyesYOffset = 0.285f;

            eyesHeight *= getBlinkFactor(paintingEntityRenderState);
            eyesXOffset += getFollowingEyesOffset(paintingEntityRenderState);

            Matrix4f matrix = matrixStack.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            // First eye
            buffer.vertex(matrix, eyesXOffset - eyesDistance / 2 + eyesWidth / 2, eyesYOffset - eyesHeight / 2, 0.0f).color(0xFFFF0000);
            buffer.vertex(matrix, eyesXOffset - eyesDistance / 2 + eyesWidth / 2, eyesYOffset + eyesHeight / 2, 0.0f).color(0xFFFF0000);
            buffer.vertex(matrix, eyesXOffset - eyesDistance / 2 - eyesWidth / 2, eyesYOffset + eyesHeight / 2, 0.0f).color(0xFFFF0000);
            buffer.vertex(matrix, eyesXOffset - eyesDistance / 2 - eyesWidth / 2, eyesYOffset - eyesHeight / 2, 0.0f).color(0xFFFF0000);

            // Second eye
            buffer.vertex(matrix, eyesXOffset + eyesDistance / 2 + eyesWidth / 2, eyesYOffset - eyesHeight / 2, 0.0f).color(0xFFFF0000);
            buffer.vertex(matrix, eyesXOffset + eyesDistance / 2 + eyesWidth / 2, eyesYOffset + eyesHeight / 2, 0.0f).color(0xFFFF0000);
            buffer.vertex(matrix, eyesXOffset + eyesDistance / 2 - eyesWidth / 2, eyesYOffset + eyesHeight / 2, 0.0f).color(0xFFFF0000);
            buffer.vertex(matrix, eyesXOffset + eyesDistance / 2 - eyesWidth / 2, eyesYOffset - eyesHeight / 2, 0.0f).color(0xFFFF0000);

            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            RenderSystem.enableDepthTest();
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.disableDepthTest();

            matrixStack.pop();
        }
    }

    @Unique
    private static float getBlinkFactor(PaintingEntityRenderState paintingEntityRenderState) {
        int blinkPeriod = 200;
        int blinkDuration = 8;

        int phase = ((int) paintingEntityRenderState.age % blinkPeriod);

        float blinkFactor;
        if (phase < blinkDuration) {  // Closing
            blinkFactor = 1.0f - (phase / (float) blinkDuration);
        } else if (phase < blinkDuration * 2) {  // Opening
            blinkFactor = (phase - blinkDuration) / (float) blinkDuration;
        } else {  // Opened
            blinkFactor = 1.0f;
        }

        return blinkFactor;
    }

    @Unique
    private static float getFollowingEyesOffset(PaintingEntityRenderState paintingEntityRenderState) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return 0.0f;
        }

        double paintingX = paintingEntityRenderState.x;
        double paintingY = paintingEntityRenderState.y;
        double paintingZ = paintingEntityRenderState.z;

        Vec3d toPlayer = player.getPos().subtract(paintingX, paintingY, paintingZ);

        float lookX = (float) switch (paintingEntityRenderState.facing) {
            case NORTH -> -toPlayer.x;
            case SOUTH -> toPlayer.x;
            case WEST -> toPlayer.z;
            case EAST -> -toPlayer.z;
            default -> 0.0f;
        };

        float maxOffset = 0.045f;
        if (MathHelper.abs(lookX) > 0.0001f) {
            lookX = MathHelper.clamp(lookX / 50, -maxOffset, maxOffset);
        }
        return lookX;
    }
}
