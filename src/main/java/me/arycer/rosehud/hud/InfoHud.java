package me.arycer.rosehud.hud;

import lombok.Getter;
import lombok.Setter;
import me.arycer.rosehud.Util;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;

public class InfoHud {
    private static final int POS_X = 8;
    private static final int POS_Y = 8;

    @Setter
    private static boolean isRoseServer = false;

    private static final PingBox PING_BOX = new PingBox();
    private static final CoordinatesBox COORDINATES_BOX = new CoordinatesBox();
    private static final TimeBox TIME_BOX = new TimeBox();
    private static final WeatherBox WEATHER_BOX = new WeatherBox();
    private static final SessionTimeBox SESSION_TIME_BOX = new SessionTimeBox();
    private static final DayBox DAY_BOX = new DayBox();
    private static final TimeOfDayBox TIME_OF_DAY_BOX = new TimeOfDayBox();

    public void register() {
        ClientLoginConnectionEvents.DISCONNECT.register((handler, client) -> setRoseServer(false));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SESSION_TIME_BOX.setSessionTime(0));

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden || client.options.debugEnabled) {
                return;
            }

            COORDINATES_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
            PING_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
            TIME_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
            WEATHER_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
            SESSION_TIME_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
            DAY_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
            TIME_OF_DAY_BOX.render(drawContext, MinecraftClient.getInstance().textRenderer);
        });

        ClientTickEvents.START_WORLD_TICK.register(world -> {
            if (world == null) {
                return;
            }

            SESSION_TIME_BOX.tickSessionTime();
        });
    }

    @Getter
    private static class PingBox {
        private int width;
        private int height;

        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden) {
                return;
            }

            if (client.isIntegratedServerRunning()) {
                width = 0;
                height = 0;
                return;
            }

            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
            if (networkHandler == null) {
                return;
            }

            if (client.player == null) {
                return;
            }

            PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(client.player.getUuid());
            if (playerListEntry == null) {
                return;
            }

            int ping = playerListEntry.getLatency();

            String renderString = String.format("%s ms", ping);

            width = client.textRenderer.getWidth(renderString) + 5;
            height = client.textRenderer.fontHeight + 3;

            final int posX = POS_X + COORDINATES_BOX.getWidth() + 8;

            drawContext.fill(posX - 5, POS_Y - 5, posX + width, POS_Y + height, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, posX, POS_Y, 0xffffff);
        }
    }

    @Getter
    private static class CoordinatesBox {
        private int width;

        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) {
                return;
            }

            int x = (int) player.getX();
            int y = (int) player.getY();
            int z = (int) player.getZ();

            String renderString = String.format("X: %s  Y: %s  Z: %s", x, y, z);

            width = MinecraftClient.getInstance().textRenderer.getWidth(renderString) + 5;
            final int boxHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 3;

            drawContext.fill(POS_X - 5, POS_Y - 5, POS_X + width, POS_Y + boxHeight, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, POS_X, POS_Y, 0xffffff);
        }
    }

    @Getter
    private static class TimeBox {
        private int width;
        private int height;

        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden) {
                return;
            }

            String renderString = "⌚ " + Util.getCurrentTime();

            width = client.textRenderer.getWidth(renderString) + 5;
            height = client.textRenderer.fontHeight + 3;

            final int posX = client.getWindow().getScaledWidth() - width - 3;

            drawContext.fill(posX - 5, POS_Y - 5, posX + width, POS_Y + height, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, posX, POS_Y, 0xffffff);
        }
    }

    @Getter
    private static class WeatherBox {
        private int width;
        private int height;

        @Setter
        private long weatherTime;

        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden) {
                return;
            }

            World world = client.world;
            if (world == null) {
                return;
            }

            String renderString;
            if (world.isRaining()) {
                if (world.isThundering()) {
                    renderString = "⚡";
                } else {
                    renderString = "☂";
                }
            } else {
                long time = world.getTimeOfDay();
                while (time > 24000) {
                    time -= 24000;
                }

                if (time >= 13000) {
                    renderString = "☽";
                } else {
                    renderString = "☀";
                }
            }

            if (isRoseServer() && !renderString.equals("☀") && !renderString.equals("☽")) {
                if (client.isIntegratedServerRunning() && client.getServer() != null) {
                    ServerWorldProperties serverWorldProperties = Util.getServerWorldProperties(client.getServer());
                    if (serverWorldProperties != null) {
                        if (world.isThundering()) {
                            weatherTime = serverWorldProperties.getThunderTime();
                        } else if (world.isRaining()) {
                            weatherTime = serverWorldProperties.getRainTime();
                        } else {
                            weatherTime = 0;
                        }
                    }
                }

                renderString = renderString.concat(" ").concat(Util.ticksToTime(weatherTime));
            }

            width = client.textRenderer.getWidth(renderString) + 5;
            height = client.textRenderer.fontHeight + 3;

            final int posX = client.getWindow().getScaledWidth() - (POS_X + TIME_BOX.getWidth() + width + 3);

            drawContext.fill(posX - 5, POS_Y - 5, posX + width, POS_Y + height, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, posX, POS_Y, 0xffffff);
        }
    }

    private static class DayBox {
        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden) {
                return;
            }

            World world = client.world;
            if (world == null) {
                return;
            }

            long time = world.getTimeOfDay();
            while (time > 24000) {
                time -= 24000;
            }

            String renderString;
            if (time >= 13000) {
                renderString = String.format("Night %s", world.getTimeOfDay() / 24000);
            } else {
                renderString = String.format("Day %s", world.getTimeOfDay() / 24000);
            }

            final int BOX_WIDTH = client.textRenderer.getWidth(renderString) + 5;
            final int BOX_HEIGHT = client.textRenderer.fontHeight + 3;

            final int posX = client.getWindow().getScaledWidth() - (POS_X + TIME_BOX.getWidth() + BOX_WIDTH + 3) - WEATHER_BOX.getWidth() - 8 - TIME_OF_DAY_BOX.getWidth() - 8;

            drawContext.fill(posX - 5, POS_Y - 5, posX + BOX_WIDTH, POS_Y + BOX_HEIGHT, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, posX, POS_Y, 0xffffff);
        }
    }

    @Getter
    private static class TimeOfDayBox {
        private int width;
        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden) {
                return;
            }

            World world = client.world;
            if (world == null) {
                return;
            }

            long time = world.getTimeOfDay();
            while (time > 24000) {
                time -= 24000;
            }

            String renderString = Util.ticksToTime(time);

            width = client.textRenderer.getWidth(renderString) + 5;
            final int BOX_HEIGHT = client.textRenderer.fontHeight + 3;

            final int posX = client.getWindow().getScaledWidth() - (POS_X + TIME_BOX.getWidth() + width + 3) - WEATHER_BOX.getWidth() - 8;

            drawContext.fill(posX - 5, POS_Y - 5, posX + width, POS_Y + BOX_HEIGHT, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, posX, POS_Y, 0xffffff);
        }
    }

    private static class SessionTimeBox {
        @Setter
        private int sessionTime;

        public void render(DrawContext drawContext, TextRenderer textRenderer) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options.hudHidden || client.currentScreen instanceof ChatScreen) {
                return;
            }

            String renderString = "⭐ " + Util.ticksToTime(sessionTime);

            final int BOX_WIDTH = client.textRenderer.getWidth(renderString) + 5;
            final int BOX_HEIGHT = client.textRenderer.fontHeight + 3;

            final int POX_X = client.getWindow().getScaledWidth() - BOX_WIDTH - 3;
            final int POS_Y = client.getWindow().getScaledHeight() - BOX_HEIGHT - 3;

            drawContext.fill(POX_X - 5, POS_Y - 5, POX_X + BOX_WIDTH, POS_Y + BOX_HEIGHT, 0x80000000);
            drawContext.drawTextWithShadow(textRenderer, renderString, POX_X, POS_Y, 0xffffff);
        }

        public void tickSessionTime() {
            sessionTime++;
        }
    }

    private static boolean isRoseServer() {
        return isRoseServer || MinecraftClient.getInstance().isIntegratedServerRunning();
    }

    public void setWeatherTime(long weatherTime) {
        WEATHER_BOX.setWeatherTime(weatherTime);
    }
}
