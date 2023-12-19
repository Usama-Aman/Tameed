package tameed.com.tameed.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Random;


public class SharedPreference {
    private static final String SHARED_PREFERENCES_KEY = "UserSharedPrefs";

    public static int count=0;
    public  static  int tempInt = 0;


    public static void saveSharedPrefValue(Context mContext, String key, String value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putString(key, scrambleText(value));
        edit.commit();
    }
    public static void savePrefValue(Context mContext, String key, String value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static void saveBoolSharedPrefValue(Context mContext, String key, boolean value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void saveIntegerSharedPrefValue(Context mContext, String key, int value) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        Editor edit = userSharedPrefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    //Read from Shared Preferance
    public static int readSharedPreferenceInt(Context mContext,  String spName, String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return tempInt = sharedPreferences.getInt(key, 0);
    }

    //write shared preferences in integer
    public static void writeSharedPreference(Context mContext, int ammount, String spName, String key) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();

        editor.putInt(key, ammount);
        editor.commit();
    }
    /****************************
     * @param cxt
     * @param key
     * @return
     ****************************/
    public static boolean getBoolSharedPrefValue(Context cxt, String key, boolean defaultValue) {
        SharedPreferences userSharedPrefs = cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        return userSharedPrefs.getBoolean(key, defaultValue);
    }

    public static String getSharedPrefValue(Context mContext, String key) {
        SharedPreferences userSharedPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        String value = userSharedPrefs.getString(key, null);
        return value;
    }

    public static SharedPreferences getSharedPref(Context cxt) {
        return cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
    }

    public static int getIntSharedPrefValue(Context cxt, String shared_pref_key, int defaultValue) {
        SharedPreferences userSharedPrefs = cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
        return userSharedPrefs.getInt(shared_pref_key, defaultValue);
    }


    private static String scrambleText(String text) {
        try {
            Random r = new Random();
            String prefix = String.valueOf(r.nextInt(90000) + 10000);
            String suffix = String.valueOf(r.nextInt(90000) + 10000);
            text = prefix + text + suffix;

            byte[] bytes = text.getBytes("UTF-8");
            byte[] newBytes = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte) (bytes[i] - 1);
            }
            return new String(newBytes, "UTF-8");
        } catch (Exception e) {return text;}
    }

    public static String unScrambleText(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            byte[] newBytes = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte) (bytes[i] + 1);
            }
            String textVal = new String(newBytes, "UTF-8");
            return textVal.substring(5, textVal.length() - 5);
        } catch (Exception e) {return text;}
    }
}
