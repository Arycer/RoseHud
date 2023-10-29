package me.arycer.rosehud;

import lombok.Getter;
import me.arycer.rosehud.hud.InfoHud;
import me.arycer.rosehud.network.PacketHandler;
import net.fabricmc.api.ClientModInitializer;

public class RoseHudClient implements ClientModInitializer {
    @Getter
    private static RoseHudClient instance;

    @Getter
    private final InfoHud infoHud = new InfoHud();

    @Override
    public void onInitializeClient() {
        instance = this;
        infoHud.register();
        PacketHandler.registerClient();
    }
}
