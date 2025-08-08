package org.enrime.thebrokencore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.enrime.thebrokencore.entity.ModEntities;
import org.enrime.thebrokencore.entity.client.renderer.StingrayEntityRenderer;

public class TheBrokenCoreModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.STINGRAY, StingrayEntityRenderer::new);
    }
}
