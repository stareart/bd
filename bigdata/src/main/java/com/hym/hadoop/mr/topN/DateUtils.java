package com.hym.hadoop.mr.topN;

public class DateUtils {
    public static String getYearMonthString(String dateTime){
        String[] split = dateTime.split("-");
        return split[0]+split[1];
    }
}
