package me.arycer.rosehud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.World;

public class InfoHud {
    public void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden || client.player == null) {
                return;
            }

            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
            if (networkHandler == null) {
                return;
            }

            int fps = client.getCurrentFps();

            PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(client.player.getUuid());
            if (playerListEntry == null) {
                return;
            }

            int ping = playerListEntry.getLatency();

            String renderString = String.format("FPS: %s  Ping: %s", fps, ping);

            int color = 0xffffff;

            int BOX_WIDTH = client.textRenderer.getWidth(renderString) + 5;
            int BOX_HEIGHT = client.textRenderer.fontHeight + 3;

            TextRenderer textRenderer = client.textRenderer;

            int pos_x = 8;
            int pos_y = 8;

            drawContext.fill(pos_x - 5, pos_y, 5, pos_x + BOX_WIDTH, pos_y + BOX_HEIGHT, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, pos_x, pos_y, color);

            World world = client.world;
            if (world == null) {
                return;
            }

            long time = world.getTimeOfDay();
            long day = time / 24000L + 1;

            renderString = String.format("Día: %s", day);
        });
    }
}
