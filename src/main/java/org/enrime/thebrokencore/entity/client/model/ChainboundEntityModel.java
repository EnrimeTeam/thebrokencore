package org.enrime.thebrokencore.entity.client.model;

import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.custom.ChainboundEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ChainboundEntityModel extends GeoModel<ChainboundEntity> {
    @Override
    public Identifier getModelResource(ChainboundEntity chainboundEntity, @Nullable GeoRenderer<ChainboundEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "geo/entity/chainbound.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChainboundEntity chainboundEntity, @Nullable GeoRenderer<ChainboundEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/entity/chainbound.png");
    }

    @Override
    public Identifier getAnimationResource(ChainboundEntity chainboundEntity) {
        return null;
    }
}
