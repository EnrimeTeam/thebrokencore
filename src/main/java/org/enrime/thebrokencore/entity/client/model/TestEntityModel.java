package org.enrime.thebrokencore.entity.client.model;

import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.custom.TestEntity;
import org.enrime.thebrokencore.entity.custom.TestEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class TestEntityModel extends GeoModel<TestEntity> {
    @Override
    public Identifier getModelResource(TestEntity TestEntity, @Nullable GeoRenderer<TestEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "geo/entity/test_entity.geo.json");
    }

    @Override
    public Identifier getTextureResource(TestEntity TestEntity, @Nullable GeoRenderer<TestEntity> geoRenderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/entity/test_entity.png");
    }

    @Override
    public Identifier getAnimationResource(TestEntity TestEntity) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "animations/entity/test_entity.animation.json");
    }
}
