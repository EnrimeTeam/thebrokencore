package org.enrime.thebrokencore.entity.client.model;

import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.custom.StingrayEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class StingrayEntityModel extends GeoModel<StingrayEntity> {
    @Override
    public Identifier getModelResource(StingrayEntity stingrayEntity, @Nullable GeoRenderer<StingrayEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "geo/entity/stingray.geo.json");
    }

    @Override
    public Identifier getTextureResource(StingrayEntity stingrayEntity, @Nullable GeoRenderer<StingrayEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/entity/stingray.png");
    }

    @Override
    public Identifier getAnimationResource(StingrayEntity stingrayEntity) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "animations/entity/stingray.animation.json");
    }
}
