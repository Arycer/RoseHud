package me.arycer.rosehud.network.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class RoseServerS2C implements S2CPacket {
    public static final PacketType<RoseServerS2C> TYPE = PacketType.create(
            new Identifier("rosehud", "rose_server"),
            RoseServerS2C::new
    );

    public RoseServerS2C(PacketByteBuf packetByteBuf) {
    }

    public RoseServerS2C() {
    }

    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
