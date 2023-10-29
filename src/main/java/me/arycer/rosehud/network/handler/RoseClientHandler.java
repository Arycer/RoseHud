package me.arycer.rosehud.network.handler;

import me.arycer.rosehud.RoseHudServer;
import me.arycer.rosehud.network.packet.c2s.RoseClientC2S;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.network.ServerPlayerEntity;

public class RoseClientHandler {
    public static void receive(RoseClientC2S packet, ServerPlayerEntity player, PacketSender sender) {
        RoseHudServer.getInstance().getLogger().info(String.format("Player %s joined with RoseHud installed!", player.getName().getString()));
        RoseHudServer.getInstance().getRosePlayers().add(player);
    }
}
