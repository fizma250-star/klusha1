package net.surya.tutorialmod;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PashtetItemModel extends GeoModel<PashtetItem> {

    @Override
    public ResourceLocation getModelResource(PashtetItem item) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "geo/pashtet.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(PashtetItem item) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "textures/entity/pashtet.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(PashtetItem item) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "animations/pashtet.animation.json"
        );
    }
}
