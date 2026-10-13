package net.surya.tutorialmod;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(
        modid = TutorialMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class KlushaNetwork {

    private static final String PROTOCOL = "1";

    private static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(
                            TutorialMod.MOD_ID,
                            "klusha_network"
                    ),
                    () -> PROTOCOL,
                    PROTOCOL::equals,
                    PROTOCOL::equals
            );

    private static boolean registered = false;

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (!registered) {
                CHANNEL.registerMessage(
                        0,
                        AirAttackPacket.class,
                        (message, buffer) -> {
                        },
                        buffer -> new AirAttackPacket(),
                        AirAttackPacket::handle
                );

                registered = true;
            }
        });
    }

    public static void sendAirAttack() {
        CHANNEL.sendToServer(new AirAttackPacket());
    }

    public static class AirAttackPacket {

        public AirAttackPacket() {
        }

        public static void handle(
                AirAttackPacket message,
                Supplier<NetworkEvent.Context> contextSupplier) {

            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();

                if (player != null) {
                    KlushaItem.playAttackAnimation(player);
                }
            });

            context.setPacketHandled(true);
        }
    }
}





