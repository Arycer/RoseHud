package me.arycer.rosehud;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {
    private static TimeZone getSystemTimezone() {
        return TimeZone.getDefault();
    }

    public static String getCurrentTime() {
        TimeZone timeZone = getSystemTimezone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(timeZone);

        Date currentTime = new Date();
        return simpleDateFormat.format(currentTime);
    }

    public static ServerWorldProperties getServerWorldProperties(MinecraftServer server) {
        World world = server.getWorld(World.OVERWORLD);
        if (world == null) return null;

        RegistryKey<World> worldKey = world.getRegistryKey();
        ServerWorld serverWorld = server.getWorld(worldKey);
        if (serverWorld == null) return null;

        return (ServerWorldProperties) serverWorld.getLevelProperties();
    }

    public static String ticksToTime(long ticks) {
        int seconds = (int) (ticks / 20);
        int minutes = seconds / 60;
        int hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
