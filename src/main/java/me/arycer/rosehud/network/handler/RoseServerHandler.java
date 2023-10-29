package me.arycer.rosehud.network.handler;

import me.arycer.rosehud.RoseHudClient;
import me.arycer.rosehud.hud.InfoHud;
import me.arycer.rosehud.network.PacketHandler;
import me.arycer.rosehud.network.packet.c2s.RoseClientC2S;
import me.arycer.rosehud.network.packet.s2c.RoseServerS2C;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class RoseServerHandler {
    public static void receive(RoseServerS2C packet, ClientPlayerEntity player, PacketSender sender) {
        RoseHudClient.getInstance().getLogger().info("Joined server with RoseHud installed!");
        PacketHandler.sendToServer(new RoseClientC2S());
        InfoHud.setRoseServer(true);
    }
}
