package me.hgj.jetpackmvvm.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class DateUtil {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd HH:mm");

    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }

    /**
     * 返回当前时间字符串。 格式：yyyy-MM-dd
     *
     * @return String 指定格式的日期字符串.
     */
    public static String getCurrentDate() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd");
    }

    /**
     * 返回当前时间字符串。 格式：yyyyMMdd
     *
     * @return String 指定格式的日期字符串.
     */
    public static String getCurrentDate1() {
        return getFormatDateTime(new Date(), "yyyyMMdd");
    }


    /**
     * 返回当前指定的时间戳。格式为yyyy-MM-dd HH:mm
     *
     * @return 格式为yyyy-MM-dd HH:mm。
     */
    public static String getCurrentYMDHMDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm");
    }

    /**
     * 返回当前指定的时间戳。格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 格式为yyyy-MM-dd HH:mm:ss，总共19位。
     */
    public static String getCurrentDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentDateTime1() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm");
    }

    public static String getCurrentDateTime2() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd_HH-mm-ss");
    }

    /**
     * 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。<br>
     *
     * @param date   指定的日期
     * @param format 日期格式字符串
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 给指定的时间字符串格式化，返回时间格式为 yyyy-MM-dd的时间串
     *
     * @param time 原时间串
     * @return String 格式化后的字符串
     */
    public static String getFormatDateYMDString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(time); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
        }
        return sdf.format(d);
    }

    /**
     * 离当前时间分钟数
     *
     * @param beginDate
     * @return
     * @throws Exception
     */
    public static int getBetweenMinute(String beginDate) {
        Log.d("", beginDate);
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date d1 = sim.parse(beginDate);
            Date d2 = new Date(System.currentTimeMillis());
            int aa = (int) ((d2.getTime() - d1.getTime()) / (60L * 1000));
            Log.d("", aa + " d1" + d1.getTime() + "  d2" + d2.getTime());
            return aa;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将字符串 转化成 日期
     *
     * @param dateStr
     * @return
     */
    public static Date StringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串转化为时间格式
     */
    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String format(long now) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        // System.out.println(now + " = " +
        // formatter.format(calendar.getTime()));
        // 日期转换为毫秒 两个日期想减得到天数
        return formatter.format(calendar.getTime());

    }

    /**
     * 时间戳转换成字符窜
     */
    public static String getDateToString(long time, int type) {
        Date d;
//        if (Long.toString(time).length() == 13) {
//            d = new Date(time);
//        } else if (Long.toString(time).length() == 10) {
//            d = new Date(time * 1000L);
//        } else {
//            return "传入时间格式错误";
//        }
        d = new Date(time);
        DateFormat sf;
        if (1 == type) {
            sf = new SimpleDateFormat("yyyy-MM-dd");
        } else if (2 == type) {
            sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        } else if (3 == type) {
            sf = new SimpleDateFormat("HH:mm:ss");
        } else if (4 == type) {
            sf = new SimpleDateFormat("yyyyMMdd");
        } else {
            sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return sf.format(d);
    }


    /**
     * 毫秒数转化成00:00:00的格式
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(long timeMs) {
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        return new Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    /**
     * 计算时差 根据 long 返回时间点
     *
     * @param millisecond
     * @return string 11分55秒
     */
    public static String parseMillisecone(long millisecond) {
        String time = "";
        try {
            long yushu_day = millisecond % (1000 * 60 * 60 * 24);
            long yushu_hour = (millisecond % (1000 * 60 * 60 * 24))
                    % (1000 * 60 * 60);
            long yushu_minute = millisecond % (1000 * 60 * 60 * 24)
                    % (1000 * 60 * 60) % (1000 * 60);
            long yushu_second = millisecond % (1000 * 60 * 60 * 24)
                    % (1000 * 60 * 60) % (1000 * 60) % 1000;

            if (yushu_day == 0) {
                return (millisecond / (1000 * 60 * 60 * 24)) + "天";
            } else {
                if (yushu_hour == 0) {
                    return (millisecond / (1000 * 60 * 60 * 24)) + "天"
                            + (yushu_day / (1000 * 60 * 60)) + "时";
                } else {
                    if (yushu_minute == 0) {
                        return (millisecond / (1000 * 60 * 60 * 24)) + "天"
                                + (yushu_day / (1000 * 60 * 60)) + "时"
                                + (yushu_hour / (1000 * 60)) + "分";
                    } else {
                        return (millisecond / (1000 * 60 * 60 * 24)) + "天"
                                + (yushu_day / (1000 * 60 * 60)) + "时"
                                + (yushu_hour / (1000 * 60)) + "分"
                                + (yushu_minute / 1000) + "秒";
                    }
                }
            }

//            long result_day = millisecond / (1000 * 60 * 60 * 24);
//            long result_hour = yushu_day / (1000 * 60 * 60);
//            long result_minute = yushu_hour / (1000 * 60);
//            long result_second = yushu_minute / 1000;
//            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return 毫秒级别
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据时间日期得出现在是星期几
     *
     * @param dateTime yyyy-MM-dd
     * @return
     */
    private static int getDayofWeek(String dateTime) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(dateTime);
            cal.setTime(new Date(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String dayofWeek(String dateTime) {
        String week = "";
        switch (getDayofWeek(dateTime)) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return week;
    }
}

