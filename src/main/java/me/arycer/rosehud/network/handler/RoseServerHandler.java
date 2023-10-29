package me.arycer.rosehud.network.handler;

import me.arycer.rosehud.hud.InfoHud;
import me.arycer.rosehud.network.packet.s2c.RoseServerS2C;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class RoseServerHandler {
    public static void receive(RoseServerS2C packet, ClientPlayerEntity player, PacketSender sender) {
        InfoHud.setRoseServer(true);
    }
}
