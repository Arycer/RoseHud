package me.arycer.rosehud;

import lombok.Getter;
import me.arycer.rosehud.hud.InfoHud;
import me.arycer.rosehud.network.PacketHandler;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class RoseHudClient implements ClientModInitializer {
    @Getter
    private static RoseHudClient instance;
    private final InfoHud infoHud = new InfoHud();
    private final Logger logger = LogManager.getLogger("RoseHud Client");

    @Override
    public void onInitializeClient() {
        instance = this;
        infoHud.register();
        PacketHandler.registerClient();
    }
}
