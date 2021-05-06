package com.boxnet;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    /**
     * get Data String
     * */
    public static String getDate() {
        Date time = new Date();
        // Date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    /**
     * Default Error Result
     */
    public static String errorResult(JsonObject result) {
        result.addProperty("resultMessage", "ERROR");
        result.addProperty("resultCode", "2000");
        return result.toString();
    }
}
