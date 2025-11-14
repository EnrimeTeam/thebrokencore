package org.enrime.thebrokencore.entity.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.enrime.thebrokencore.entity.client.model.ChainboundEntityModel;
import org.enrime.thebrokencore.entity.custom.ChainboundEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ChainboundEntityRenderer extends GeoEntityRenderer<ChainboundEntity> {
    public ChainboundEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ChainboundEntityModel());
    }
}
