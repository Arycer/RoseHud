package me.arycer.rosehud.network.s2c;

import me.arycer.rosehud.RoseHudClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class RoseServerS2C implements S2CPacket {
    public static final PacketType<RoseServerS2C> TYPE = PacketType.create(
            new Identifier("rosehud", "rose_server"),
            RoseServerS2C::new
    );

    public RoseServerS2C(PacketByteBuf packetByteBuf) {
    }

    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static void recieve(RoseServerS2C packet, ClientPlayerEntity player, PacketSender sender) {
        RoseHudClient.getInstance().getInfoHud().setRoseServer(true);
    }
}
