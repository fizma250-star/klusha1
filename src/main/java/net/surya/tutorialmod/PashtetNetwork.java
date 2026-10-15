package net.surya.tutorialmod;

import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PashtetNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(TutorialMod.MOD_ID, "pashtet"),
                    () -> PROTOCOL,
                    PROTOCOL::equals,
                    PROTOCOL::equals
            );

    public static void register() {
        CHANNEL.registerMessage(
                0,
                PashtetAirAttackPacket.class,
                PashtetAirAttackPacket::encode,
                PashtetAirAttackPacket::decode,
                PashtetAirAttackPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}
