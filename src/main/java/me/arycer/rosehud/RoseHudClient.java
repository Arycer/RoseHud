package me.arycer.rosehud;

import net.fabricmc.api.ClientModInitializer;

public class RoseHudClient implements ClientModInitializer {
    private final InfoHud infoHud = new InfoHud();

    @Override
    public void onInitializeClient() {
        infoHud.register();
    }
}
