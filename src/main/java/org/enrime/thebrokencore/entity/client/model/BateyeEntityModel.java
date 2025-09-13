package org.enrime.thebrokencore.entity.client.model;

import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.custom.BateyeEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BateyeEntityModel extends GeoModel<BateyeEntity> {
    @Override
    public Identifier getModelResource(BateyeEntity batEyeEntity, @Nullable GeoRenderer<BateyeEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "geo/entity/bateye.geo.json");
    }

    @Override
    public Identifier getTextureResource(BateyeEntity batEyeEntity, @Nullable GeoRenderer<BateyeEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/entity/bateye_red.png");
    }

    @Override
    public Identifier getAnimationResource(BateyeEntity batEyeEntity) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "animations/entity/bateye.animation.json");
    }
}
