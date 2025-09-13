package org.enrime.thebrokencore.entity.client.renderer;

import com.google.common.collect.Maps;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.client.model.BateyeEntityModel;
import org.enrime.thebrokencore.entity.custom.BateyeEntity;
import org.enrime.thebrokencore.entity.custom.BateyeVariant;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

public class BateyeEntityRenderer extends GeoEntityRenderer<BateyeEntity> {
    private static final Map<BateyeVariant, String> TEXTURE_PATH_BY_VARIANT =
            Util.make(Maps.newEnumMap(BateyeVariant.class), map -> {
                map.put(BateyeVariant.RED, "textures/entity/bateye_red.png");
                map.put(BateyeVariant.GREEN, "textures/entity/bateye_green.png");
                map.put(BateyeVariant.BLUE, "textures/entity/bateye_blue.png");
                map.put(BateyeVariant.PURPLE, "textures/entity/bateye_purple.png");
                map.put(BateyeVariant.LARTS_AND_MARRALD, "textures/entity/bateye_larts_and_marrald.png");
            });

    public BateyeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BateyeEntityModel());
    }

    @Override
    public Identifier getTextureLocation(BateyeEntity entity) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, TEXTURE_PATH_BY_VARIANT.get(entity.getVariant()));
    }
}
