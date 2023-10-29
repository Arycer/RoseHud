package me.arycer.rosehud.network.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class RoseClientC2S implements C2SPacket {
    public static final PacketType<RoseClientC2S> TYPE = PacketType.create(
            new Identifier("rosehud", "rose_client"),
            RoseClientC2S::new
    );

    public RoseClientC2S(PacketByteBuf packetByteBuf) {

    }

    public RoseClientC2S() {

    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
