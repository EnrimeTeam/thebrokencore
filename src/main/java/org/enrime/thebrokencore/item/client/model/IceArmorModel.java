package org.enrime.thebrokencore.item.client.model;

import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.item.custom.IceArmorItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class IceArmorModel extends GeoModel<IceArmorItem> {
    @Override
    public Identifier getModelResource(IceArmorItem animatable, @Nullable GeoRenderer<IceArmorItem> renderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "geo/armor/ice_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(IceArmorItem animatable, @Nullable GeoRenderer<IceArmorItem> renderer) {
        return Identifier.of(TheBrokenCoreMod.MOD_ID, "textures/armor/ice_armor.png");
    }

    @Override
    public Identifier getAnimationResource(IceArmorItem animatable) {
        return null;
    }
}
