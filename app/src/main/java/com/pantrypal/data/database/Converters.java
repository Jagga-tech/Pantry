package com.pantrypal.data.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Type converters for Room Database
 * Converts Date objects to Long (timestamp) for database storage
 */
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
