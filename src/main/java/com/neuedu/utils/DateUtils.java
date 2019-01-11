package com.neuedu.utils;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtils {
    private static final String STANDRAD_FORMAT="yyyy-MM-dd-mm:ss";

    /*
    * date转换成String
    * */

    //todo 完成DateUtil
    public static String dateToString(Date date, String formate){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formate);
    }

}
