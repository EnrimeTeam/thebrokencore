package org.enrime.thebrokencore.effect.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.enrime.thebrokencore.effect.interfaces.IMarkAffectable;

public class MarkAffectedEffect extends StatusEffect {
    public MarkAffectedEffect() {
        super(StatusEffectCategory.HARMFUL, 0x880000);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient()) {
            return false;
        }

        if (!(entity instanceof HostileEntity hostileEntity)) {
            return false;
        }

        Integer targetId = null;
        if (hostileEntity instanceof IMarkAffectable markAffectableEntity) {
            targetId = markAffectableEntity.thebrokencore$getMarkTargetId();
        }

        // Target was not specified by the mark effect
        if (targetId == null || targetId == -1) {
            return false;
        }

        Entity targetEntity = hostileEntity.getWorld().getEntityById(targetId);

        // Cannot target itself
        if (targetEntity == entity) {
            return false;
        }

        // Cannot target creative/spectator players
        if (targetEntity instanceof PlayerEntity playerEntity
            && (playerEntity.isCreative() || playerEntity.isSpectator())) {
            return false;
        }

        if (targetEntity instanceof LivingEntity livingTargetEntity && livingTargetEntity.isAlive()) {
            hostileEntity.setTarget(livingTargetEntity);
            return true;
        }

        return false;
    }
}
