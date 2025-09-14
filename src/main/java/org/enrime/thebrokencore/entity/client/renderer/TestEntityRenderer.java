package org.enrime.thebrokencore.entity.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.client.model.TestEntityModel;
import org.enrime.thebrokencore.entity.custom.TestEntity;
import org.enrime.thebrokencore.entity.custom.TestEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class TestEntityRenderer extends GeoEntityRenderer<TestEntity> {
    public TestEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TestEntityModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(TestEntity animatable) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/entity/test_entity.png");
    }
}
