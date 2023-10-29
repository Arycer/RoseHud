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
import net.minecraft.world.level.ServerWorldProperties;

public class RoseHudServer implements DedicatedServerModInitializer {
    @Getter
    private static RoseHudServer instance;
    @Getter
    private MinecraftServer server;

    @Override
    public void onInitializeServer() {
        instance = this;
        ServerLifecycleEvents.SERVER_STARTED.register(server -> this.server = server);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sender.sendPacket(new RoseServerS2C()));
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

            if (time % 20 == 0) {
                PacketHandler.sendToAll(server, new WeatherTimeS2C(time));
            }
        });
    }
}
