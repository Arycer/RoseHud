package me.arycer.rosehud;

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
}
