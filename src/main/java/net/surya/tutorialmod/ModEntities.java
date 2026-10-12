package net.surya.tutorialmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?» ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.ENTITY_TYPES,
                    TutorialMod.MOD_ID
            );

    public static final RegistryObject<EntityType<KlushaEntity» KLUSHA =
            ENTITIES.register("klusha",
                    () -> EntityType.Builder.<KlushaEntity>of(
                                    KlushaEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.6F, 0.6F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("klusha"));
}
