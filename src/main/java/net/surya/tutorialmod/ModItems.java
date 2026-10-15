package net.surya.tutorialmod;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(
                    ForgeRegistries.ITEMS,
                    TutorialMod.MOD_ID
            );

    public static final RegistryObject<Item> KLUSHA =
            ITEMS.register("klusha",
                    () -> new KlushaItem(
                            new Item.Properties().stacksTo(1)
                    ));

    public static final RegistryObject<Item> PAS_5TE =
            ITEMS.register("pas_5te",
                    () -> new PashtetItem(
                            new Item.Properties()
                                    .stacksTo(1)
                    ));
}
