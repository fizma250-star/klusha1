package net.surya.tutorialmod;

import java.util.UUID;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemAttributeModifiers;
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
            ITEMS.register("klusha", () ->
                    new KlushaItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .attributes(
                                            ItemAttributeModifiers.builder()
                                                    .add(
                                                            Attributes.ATTACK_DAMAGE,
                                                            new AttributeModifier(
                                                                    UUID.fromString(
                                                                            "b5a30b11-4a5a-4f6b-a721-2b2c8c091234"
                                                                    ),
                                                                    "Klusha damage",
                                                                    14.0,
                                                                    AttributeModifier.Operation.ADDITION
                                                            ),
                                                            EquipmentSlot.MAINHAND
                                                    )
                                                    .add(
                                                            Attributes.ATTACK_SPEED,
                                                            new AttributeModifier(
                                                                    UUID.fromString(
                                                                            "b5a30b11-4a5a-4f6b-a721-2b2c8c091235"
                                                                    ),
                                                                    "Klusha attack speed",
                                                                    -2.4,
                                                                    AttributeModifier.Operation.ADDITION
                                                            ),
                                                            EquipmentSlot.MAINHAND
                                                    )
                                                    .build()
                                    )
                    )
            );
}
