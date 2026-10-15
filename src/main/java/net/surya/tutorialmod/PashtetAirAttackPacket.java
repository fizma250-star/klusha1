package net.surya.tutorialmod;

import java.util.function.Supplier;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PashtetAirAttackPacket {

    public PashtetAirAttackPacket() {
    }

    public static void encode(
            PashtetAirAttackPacket packet,
            FriendlyByteBuf buffer) {
    }

    public static PashtetAirAttackPacket decode(
            FriendlyByteBuf buffer) {
        return new PashtetAirAttackPacket();
    }

    public static void handle(
            PashtetAirAttackPacket packet,
            Supplier<NetworkEvent.Context> supplier) {

        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null
                    && player.getMainHandItem().getItem()
                    instanceof PashtetItem) {

                PashtetItem.playNextAttackAnimation(player);
            }
        });

        context.setPacketHandled(true);
    }
}
