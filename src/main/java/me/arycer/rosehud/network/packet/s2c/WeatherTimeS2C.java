package me.arycer.rosehud.network.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record WeatherTimeS2C(long weatherTime) implements S2CPacket {
    public static final PacketType<WeatherTimeS2C> TYPE = PacketType.create(
            new Identifier("rosehud", "weather_time"),
            WeatherTimeS2C::new
    );

    public WeatherTimeS2C(PacketByteBuf packetByteBuf) {
        this(packetByteBuf.readLong());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(weatherTime);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
