package org.enrime.thebrokencore.mixin;

import net.minecraft.entity.mob.HostileEntity;
import org.enrime.thebrokencore.effect.interfaces.IMarkAffectable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/*
    Logically, it should be injected into HostileEntity#initDataTracker,
    but HostileEntity class doesn't override this method. TBH, I just
    don't know how to properly override it with mixins, so let it be
    injected into the parent MobEntity class.
 */
@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin implements IMarkAffectable {
    // TODO: Replace with UUID, add saving via NBT
    @Unique
    public int thebrokencore$markTargetId = -1;

    @Override
    public int thebrokencore$getMarkTargetId() {
        return thebrokencore$markTargetId;
    }

    @Override
    public void thebrokencore$setMarkTargetId(int markTargetId) {
        thebrokencore$markTargetId = markTargetId;
    }

    @Override
    public void thebrokencore$resetMarkTargetId() {
        thebrokencore$markTargetId = -1;
    }
}
