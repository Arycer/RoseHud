package me.arycer.rosehud.network.handler;

import me.arycer.rosehud.RoseHudClient;
import me.arycer.rosehud.hud.InfoHud;
import me.arycer.rosehud.network.packet.s2c.WeatherTimeS2C;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class WeatherTimeHandler {
    public static void receive(WeatherTimeS2C packet, ClientPlayerEntity player, PacketSender sender) {
        RoseHudClient.getInstance().getInfoHud().setWeatherTime(packet.weatherTime());
    }
}
