package org.enrime.thebrokencore.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import org.enrime.thebrokencore.effect.ModEffects;
import org.enrime.thebrokencore.effect.interfaces.IMarkAffectable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onStatusEffectsRemoved", at = @At("HEAD"))
    private void removeMarkEffect(Collection<StatusEffectInstance> effects, CallbackInfo ci) {
        if (this instanceof IMarkAffectable mob
            && effects.stream().anyMatch(effectInstance -> effectInstance.getEffectType() == ModEffects.MARK_AFFECTED)) {
            mob.thebrokencore$resetMarkTargetId();
            ((HostileEntity) mob).setTarget(null);
            ((HostileEntity) mob).setAttacking(false);
        }
    }
}
