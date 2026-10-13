package net.surya.tutorialmod;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KlushaItemModel extends GeoModel<KlushaItem> {

    @Override
    public ResourceLocation getModelResource(KlushaItem item) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "geo/klusha.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(KlushaItem item) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "textures/entity/klusha.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(KlushaItem item) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "animations/klusha.animation.json"
        );
    }
}
