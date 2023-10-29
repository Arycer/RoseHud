package me.arycer.rosehud.network;

import me.arycer.rosehud.network.handler.RoseClientHandler;
import me.arycer.rosehud.network.handler.RoseServerHandler;
import me.arycer.rosehud.network.handler.WeatherTimeHandler;
import me.arycer.rosehud.network.packet.c2s.C2SPacket;
import me.arycer.rosehud.network.packet.c2s.RoseClientC2S;
import me.arycer.rosehud.network.packet.s2c.RoseServerS2C;
import me.arycer.rosehud.network.packet.s2c.S2CPacket;
import me.arycer.rosehud.network.packet.s2c.WeatherTimeS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class PacketHandler {
    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(RoseServerS2C.TYPE, RoseServerHandler::receive);
        ClientPlayNetworking.registerGlobalReceiver(WeatherTimeS2C.TYPE, WeatherTimeHandler::receive);
    }

    @Environment(EnvType.SERVER)
    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(RoseClientC2S.TYPE, RoseClientHandler::receive);
    }

    public static void sendToServer(C2SPacket packet){
        ClientPlayNetworking.send(packet);
    }

    public static void sendToPlayer(ServerPlayerEntity player, S2CPacket packet){
        ServerPlayNetworking.send(player, packet);
    }

    public static void sendToAll(MinecraftServer server, S2CPacket packet){
        server.getPlayerManager().getPlayerList().forEach(player -> sendToPlayer(player, packet));
    }
}
