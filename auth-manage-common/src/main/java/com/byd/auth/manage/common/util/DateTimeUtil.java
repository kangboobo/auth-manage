package com.byd.auth.manage.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/11/16 11:11
 * @description
 */
public class DateTimeUtil {

    public static final String TIME_FORMAT_WITH_SECOND = "YYYY-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_WITH_MILLI_SECOND = "YYYY-MM-dd HH:mm:ss.SSS";
    public static final String TIME_FORMAT_WITHOUT_SECOND = "YYYY-MM-dd HH:mm";
    public static final int TIMESTAMP_LENGTH = 13;

    public DateTimeUtil() {
    }

    public static final Date convertTimestamp2Date(Long timestamp) {
        if (null == timestamp) {
            throw new IllegalArgumentException("timestamp must not be null");
        } else {
            String value = String.valueOf(timestamp);
            if (value.length() != 13) {
                throw new IllegalArgumentException("timestamp must be millisecond seconds");
            } else {
                return new Date(timestamp);
            }
        }
    }

    public static final String convertTimestamp2String(Long timestamp) {
        return convertTimestamp2String(timestamp, "YYYY-MM-dd HH:mm:ss");
    }

    public static final String convertTimestamp2String(Long timestamp, String format) {
        if (null == timestamp) {
            throw new IllegalArgumentException("timestamp must not be null");
        } else {
            String value = String.valueOf(timestamp);
            if (value.length() != 13) {
                throw new IllegalArgumentException("timestamp must be millisecond seconds");
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
                String formatTime = dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
                return formatTime;
            }
        }
    }

    public static final String convertDate2String(Date date) {
        return convertDate2String(date, "YYYY-MM-dd HH:mm:ss");
    }

    public static final String convertDate2String(Date date, String format) {
        if (null == date) {
            throw new IllegalArgumentException("date must not be null");
        } else {
            Instant instant = date.toInstant();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            String formatTime = dateTimeFormatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
            return formatTime;
        }
    }
}
