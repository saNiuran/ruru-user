package com.ruru.plastic.user.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.regex.Pattern;

import static com.xiaoleilu.hutool.util.StrUtil.isEmpty;

public class MyStringUtils {

    /**
     * 隐藏电话号码
     * @param val
     * @return
     */
    public static String getHidePhone(String val) {
        if (isEmpty(val)) {
            return "";
        }

        if (val.length() <= 3) {
            return "***********";
        }
        if (val.length() <= 7) {
            return val.substring(0, 3) + "****";
        }

        return val.substring(0, 3) + "****" + val.substring(7);
    }

    public static String getMonthDaysStr(){
        Calendar c = Calendar.getInstance();
        //获取月份，0表示1月份
        String month = c.get(Calendar.MONTH) + 1>9?c.get(Calendar.MONTH) + 1+ "":"0"+(c.get(Calendar.MONTH) + 1);
        //获取当前天数
        String day = c.get(Calendar.DAY_OF_MONTH)>9?c.get(Calendar.DAY_OF_MONTH)+"":"0"+c.get(Calendar.DAY_OF_MONTH);
        return month + day;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumericAndLengthCheck(String search){
        if (StringUtils.isNotEmpty(search) && search.length() < 4 && isNumeric(search)) {
            long s = Long.parseLong(search);
            //手机号码 前2位，这样查出来的用户太多了
            return s >= 200;
        }
        return true;
    }
}
