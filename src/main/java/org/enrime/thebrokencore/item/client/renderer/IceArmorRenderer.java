package org.enrime.thebrokencore.item.client.renderer;

import org.enrime.thebrokencore.item.client.model.IceArmorModel;
import org.enrime.thebrokencore.item.custom.IceArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class IceArmorRenderer extends GeoArmorRenderer<IceArmorItem> {
    public IceArmorRenderer() {
        super(new IceArmorModel());
    }
}
