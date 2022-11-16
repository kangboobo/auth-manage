package com.byd.auth.manage.common.util;

import com.byd.auth.manage.common.constants.Constants;
import com.byd.auth.manage.common.enums.DateTypeEnum;
import com.byd.auth.manage.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {


    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_TYPE = "yyyy-MM-dd";

    public static final String DATE_TYPE_1 = "yyyy-MM-dd HH:mm";
    public static final String DATE_TYPE_2 = "MM-dd HH:mm:ss";
    public static final String DATE_TYPE_3 = "yyyy/MM/dd";
    public static final String DATE_TYPE_4 = "yyyy.MM.dd";
    public static final String DATE_TYPE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TYPE_MINS = "yyyy-MM-dd HH:mm";
    public static final String DATE_TYPE_SLASH = "yyyy/MM/dd HH:mm";
    public static final String DATE_TYPE_MMDDHHMM = "MMddHHmm";
    public static final String DEFAULT_MIN_DATE_STRING_SEC = "1980-01-01 00:00:00";
    public static final String DEFAULT_MAX_DATE_STRING_SEC = "2050-01-01 00:00:00";

    /**
     * 时长格式化HH:mm:ss
     */
    public static final String DURATION_TYPE_HOUR = "HH:mm:ss";
    /**
     * 时长格式化mm:ss
     */
    public static final String DURATION_TYPE_MINUTE = "mm:ss";
    /**
     * 时分秒单位转换
     */
    public static final long TIME_UNIT_CONVERSION = 60L;
    /**
     * 一小时秒数
     */
    public static final long HOUR_SECOND = TIME_UNIT_CONVERSION * TIME_UNIT_CONVERSION;
    /**
     * 秒转毫秒
     */
    public static final int MILLISECOND = 1000;
    /**
     * 毫秒标志
     */
    public static final String MILLISECOND_FLAG = "000";

    public static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Date formatStr2Date(String type, String dateStr) {
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String formatDate2Str(String type, Date date) {
        if(Objects.isNull(date)){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        return simpleDateFormat.format(date);
    }

    public static String getTodayStr() {
        Date date = new Date();
        return formatDate2Str("yyyy-MM-dd", date);
    }

    public static String getTStr() {
        Date date = new Date();
        return formatDate2Str(DATE_TYPE_4, date);
    }

    /**
     * 当前日期加上月数后的日志
     * @param date
     * @param num
     * @return
     */
    public static String plusDayOrMonth(String date, int num, String type, String dateType) {
        Date srcDate = formatStr2Date(dateType, date);
        SimpleDateFormat format = new SimpleDateFormat(dateType);
        Calendar cal = Calendar.getInstance();
        cal.setTime(srcDate);
        // num为增加的 天/月 数，可以改变的
        if (type.equals(DateTypeEnum.DAY.getValue())) {
            cal.add(Calendar.DATE, num);
        } else if (type.equals(DateTypeEnum.MONTH.getValue())) {
            cal.add(Calendar.MONTH, num);
        }
        return format.format(cal.getTime());
    }

    /**
     * 日期加上月数后的日期
     *
     * @param srcDate
     * @param months
     * @return
     */
    public static Date addMonths(Date srcDate, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(srcDate);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 只保留年月日
     * @return Date
     */
    public static Date convertToYmd(Date sourceDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TYPE);
        String s = sdf.format(sourceDate);
        try {
            return sdf.parse(s);
        } catch (Exception e) {
            logger.error("去掉时分秒转换出错:", e);
        }
        throw new ServiceException("去掉时分秒转换出错");
    }


    /**
     * 当前日期加上天数后的日期
     *
     * @param num 为增加的天数
     * @return
     */
    public static Date plusS(Date srcDate, int num) {

        return new Date(srcDate.getTime() + num * 1000L);
    }

    public static void main(String[] args) throws ParseException {

        System.out.println(plusDayOrMonth(formatDate2Str(DATE_TYPE_4, new Date()), 12, DateTypeEnum.MONTH.getValue(), DATE_TYPE_4));

    }

    /**
     * 根据两个时间计算天数
     * @param fDate
     * @param oDate
     * @return
     */
    public static long getDay(Date oDate, Date fDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TYPE);
        try {
            return (sdf.parse(sdf.format(oDate)).getTime() - sdf.parse(sdf.format(fDate)).getTime()) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            logger.info("day error:{}", e);
        }
        return Constants.ZERO_VALUE_LANG.longValue();
    }

    /**
     * String  转 localDateTime ， String的日期型是df
     *
     * @param sdate
     * @return
     */
    public static LocalDateTime convertStringToLocalDateTime(String sdate) {
        LocalDateTime localDateTime = LocalDateTime.parse(sdate, df);
        return localDateTime;
    }

    /**
     * 距离目前多少秒
     *
     * @param * @param localDateTime
     * @return long
     * @description
     * @author 王婷
     * @date 2019/4/4 17:11
     */
    public static long untilNow(LocalDateTime localDateTime) {
        Duration duration = Duration.between(localDateTime, LocalDateTime.now());
        long seconds = duration.toMinutes() * 60;
        return seconds;
    }

    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 带时区的时间转化成本地时间 该方法用于转换oss获取的音视频时间，该时间与北京时间相差8小时
     *
     * @param sZoneTime
     * @return
     */
    public static String parseZonedTime(String sZoneTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(sZoneTime, formatter).plusHours(8);
        return zonedDateTime.format(df);
    }


    public static LocalDateTime convertZonedTimeToLocalDateTime(String sdate) {
        String sLocal = parseZonedTime(sdate);
        LocalDateTime localDateTime = LocalDateTime.parse(sLocal, df);
        return localDateTime;
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    /**
     * 格式化输出date型
     *
     * @param date
     * @return
     */
    public static String printDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 判断是否过期：传入的参数是否小于当前时间
     *
     * @param expireDate
     * @return
     */
    public static Boolean isBefore(Date expireDate) {
        LocalDate expireLocalDate = expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (expireLocalDate.isBefore(LocalDate.now())) {
            return true;
        }
        return false;
    }

    /**
     * 获取本年第一天
     *
     * @return
     */
    public static Date getFirstDayOfYear() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);


        return calendar.getTime();
    }

    public static String getFirstDayOfYearStr() {

        Date firstDayOfYear = getFirstDayOfYear();
        return formatDate2Str(DATE_TYPE_SECOND, firstDayOfYear);
    }

    /**
     * @param duration 时长
     * @return
     * @Title conversionDuration
     * @Description 时长单位转换，支持小时转换为分钟/分钟转换为秒
     */
    public static Long conversionDuration(Integer duration) {
        if (Objects.isNull(duration)) {
            return Constants.ZERO_VALUE_LANG;
        }
        return duration * TIME_UNIT_CONVERSION;
    }

    /**
     * @param duration 时长（单位：秒）
     * @return HH:mm:ss
     * @Title formatDuration
     * @Description 格式化时长
     */
    public static String formatDuration(Long duration) {
        if (Objects.isNull(duration) || Objects.equals(duration, Constants.ZERO_VALUE_LANG)) {
            return Constants.LINE_THROUGH;
        }
        return DurationFormatUtils.formatDuration(duration * MILLISECOND, DURATION_TYPE_HOUR);
    }

    /**
     * @param  timestamp  String 时间戳（单位：毫秒）
     * @return String
     * @Title formatLongStr2DateStr
     * @Description 格式化时间
     */
    public static String formatLongStr2DateStr(String timestamp) {

        Long time;

        try {
            time = Long.parseLong(timestamp);
        } catch (Exception e) {
            return null;
        }
        return formatLong2DateStr(time, DATE_TYPE_SECOND);
    }

    public static boolean isValidDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TYPE_3);
        try {
            Date date = formatter.parse(str);
            return str.startsWith(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param timestamp 时间戳（单位：毫秒）
     * @return String
     * @Title formatLong2DateStr
     * @Description 格式化时间
     */
    public static String formatLong2DateStr(Long timestamp, String dateFormat) {
        if (Objects.isNull(timestamp) || timestamp <= Constants.ZERO_VALUE_LANG) {
            timestamp = Constants.ZERO_VALUE_LANG;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        return calendar.getTime();
    }

    /**
     * 获取月日时分秒字符串
     *
     * @return
     */
    public static String getCurrentMonthDayStr() {
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int time = calendar.get(Calendar.HOUR_OF_DAY);       //获取当前小时
        int min = calendar.get(Calendar.MINUTE);          //获取当前分钟
        int sec = calendar.get(Calendar.SECOND);          //获取当前秒

        StringBuilder builder = new StringBuilder();
        builder.append(month).append(day).append(time).append(min).append(sec);
        return builder.toString();
    }

    /**
     * @Title millisecondTransforMinute
     * @Description 毫秒转换为分钟
     * @param millisecond 毫秒时长
     * @return Long
     */
    public static Long millisecondTransforMinute(Long millisecond) {
        if (Objects.isNull(millisecond)) {
            return Constants.ZERO_VALUE_LANG;
        }
        millisecond = millisecond / 1000;
        Long minute = millisecond / 60;
        if (millisecond % 60 > 0) {
            minute++;
        }
        return minute;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 查询指定日期间的日期列表
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findDates(Date dBegin, Date dEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TYPE);
        List<String> lDate = new ArrayList();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdf.format(calBegin.getTime()));
        }
        return lDate;
    }

    public static Date moveSeconds4Date(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_TYPE_MINS);
        return sdf.parse(sdf.format(date));
    }

    /**
     * 获取今天结束时间
     * @return
     */
    public static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * @Title: getTodayStartTime
     * @Description: 获取当日开始时间
     * @param
     * @return java.util.Date
     * @throws
     */
    public static Date getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @Title: getTodayEndTime
     * @Description: 获取当日结束时间
     * @param
     * @return java.util.Date
     * @throws
     */
    public static Date getTodayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date timeStampToDate(Long timeStamp) {
        return new Date(Long.parseLong(String.valueOf(timeStamp)));
    }

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String convertTimeToString(Long time) {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String convertTimeToString(Long time,DateTimeFormatter dateTimeFormatter ) {
        return dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * @Title timeStampToFormatDate
     * @Description 时间戳转换为格式化的日期字符串
     * @param dateStr 时间戳或格式化日期字符串
     * @return String
     */
    public static String timeStampToFormatDate(String dateStr) {
        return DateUtil.timeStampToFormatDate(dateStr, DateUtil.DATE_TYPE_SECOND);
    }

    /**
     * @Title timeStampToFormatDate
     * @Description 时间戳转换为格式化的日期字符串
     * @param dateStr 时间戳或格式化日期字符串
     * @param format 格式化
     * @return String
     */
    public static String timeStampToFormatDate(String dateStr, String format) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        try {
            Long timeStamp = Long.valueOf(dateStr);
            // 前端传参时间戳时，毫秒一定为000，所以末尾没有三个0，则需要补充毫秒
            if (!dateStr.endsWith(MILLISECOND_FLAG)) {
                timeStamp = timeStamp * MILLISECOND;
            }
            // 格式化后返回
            String date = DateUtil.formatDate2Str(format, new Date(timeStamp));
            logger.info("timeStampToFormatDate finish, dateStr:{}, date:{}", dateStr, date);
            return date;
        } catch (Exception e) {
            logger.error("timeStampToFormatDate failed, dateStr:{}", dateStr, e);
            return dateStr;
        }
    }
}
