package org.enrime.thebrokencore.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import org.enrime.thebrokencore.effect.ModEffects;
import org.enrime.thebrokencore.effect.interfaces.IMarkAffectable;

import java.util.List;

public class MarkEffect extends StatusEffect {
    private static final int BASE_RADIUS = 16;
    private static final int RADIUS_PER_LEVEL = 4;
    private static final int TRIGGER_INTERVAL = 600;  // 30 seconds in ticks
    private static final int TARGET_DURATION = 1200;  // 60 seconds in ticks

    public MarkEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFF0000);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % TRIGGER_INTERVAL == 0 || duration == 1;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient()) {
            return false;
        }

        double attractionRadius = BASE_RADIUS + RADIUS_PER_LEVEL * amplifier;

        int targetId = entity.getId();

        List<HostileEntity> hostileEntities = entity.getWorld().getEntitiesByClass(
                HostileEntity.class,
                entity.getBoundingBox().expand(attractionRadius),
                LivingEntity::isAlive
        );
        
        for (HostileEntity hostileEntity : hostileEntities) {
            if (hostileEntity instanceof IMarkAffectable markAffectableEntity) {
                int thatEntityTargetId = markAffectableEntity.thebrokencore$getMarkTargetId();

                if (thatEntityTargetId == -1) {  // Add effect on non-triggered [by another mark] mob
                    hostileEntity.addStatusEffect(
                            new StatusEffectInstance(ModEffects.MARK_AFFECTED, TARGET_DURATION, 0),
                            entity
                    );
                    markAffectableEntity.thebrokencore$setMarkTargetId(targetId);
                } else if (thatEntityTargetId == targetId) {  // Renew effect on already triggered mob
                    hostileEntity.addStatusEffect(
                            new StatusEffectInstance(ModEffects.MARK_AFFECTED, TARGET_DURATION, 0),
                            entity
                    );
                }
            }
        }
        
        return true;
    }
}
