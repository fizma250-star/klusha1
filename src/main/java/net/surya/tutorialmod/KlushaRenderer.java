package net.surya.tutorialmod;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KlushaRenderer extends GeoEntityRenderer<KlushaEntity> {

    public KlushaRenderer(EntityRendererProvider.Context context) {
        super(context, new KlushaModel());
    }
}
