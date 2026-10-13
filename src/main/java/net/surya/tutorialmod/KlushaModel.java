package net.surya.tutorialmod;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KlushaModel extends GeoModel<KlushaEntity> {

    @Override
    public ResourceLocation getModelResource(KlushaEntity entity) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "geo/klusha.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(KlushaEntity entity) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "textures/entity/klusha.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(KlushaEntity entity) {
        return new ResourceLocation(
                TutorialMod.MOD_ID,
                "animations/klusha.animation.json"
        );
    }
}
