package Exmpl.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dateUtils {
    public static String DATE_FORMATTER = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    public final static String DATE_HOUR_MINUTE_SECOND_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_HOUR_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_HOUR_MINUTE_SECOND_FORMATTER);

    private static final LocalDateTime NOW_TIME = LocalDateTime.now();
    /**
     * 获取系统本地日期
     **/
    public static String getLocalDateNow() {
        return LocalDate.now().format(DATE_TIME_FORMATTER);
    }

    /**
     * 获取系统本地时间
     **/
    public static String getLocalTimeNow() {
        return LocalDateTime.now().format(DATE_TIME_HOUR_TIME_FORMATTER);
    }


}
