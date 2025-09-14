package org.enrime.thebrokencore.entity.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.client.model.StingrayEntityModel;
import org.enrime.thebrokencore.entity.custom.StingrayEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class StingrayEntityRenderer extends GeoEntityRenderer<StingrayEntity> {
    public StingrayEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new StingrayEntityModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(StingrayEntity animatable) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/entity/stingray.png");
    }
}
