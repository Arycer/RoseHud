package me.arycer.rosehud;

import lombok.Getter;
import me.arycer.rosehud.network.PacketHandler;
import me.arycer.rosehud.network.packet.s2c.RoseServerS2C;
import me.arycer.rosehud.network.packet.s2c.WeatherTimeS2C;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.level.ServerWorldProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
@Getter
public class RoseHudServer implements DedicatedServerModInitializer {
    @Getter
    private static RoseHudServer instance;
    private MinecraftServer server;
    private final List<ServerPlayerEntity> rosePlayers = new ArrayList<>();
    private final Logger logger = LogManager.getLogger("RoseHud Server");

    @Override
    public void onInitializeServer() {
        instance = this;
        PacketHandler.registerServer();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> this.server = server);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sender.sendPacket(new RoseServerS2C()));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> rosePlayers.remove(handler.player));
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (server == null) return;
            ServerWorldProperties properties = Util.getServerWorldProperties(server);
            if (properties == null) return;

            long time;
            if (properties.isThundering()) {
                time = properties.getThunderTime();
            } else if (properties.isRaining()) {
                time = properties.getRainTime();
            } else {
                return;
            }

            if (time % 20 == 0 && time != 0) {
                for (ServerPlayerEntity player : rosePlayers) {
                    PacketHandler.sendToPlayer(player, new WeatherTimeS2C(time));
                }
            }
        });
    }
}
