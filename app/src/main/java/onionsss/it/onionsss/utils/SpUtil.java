package onionsss.it.onionsss.utils;

import android.content.Context;
import android.content.SharedPreferences;

import onionsss.it.onionsss.utils.constant.Constant;

/**
 * 作者：张琦 on 2016/5/19 04:52
 */
public class SpUtil {

    public static boolean putInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_URI,Context.MODE_PRIVATE);
        return sp.edit().putInt(key,value).commit();
    }
    public static int getInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_URI,Context.MODE_PRIVATE);
        return sp.getInt(key,0);
    }
}
