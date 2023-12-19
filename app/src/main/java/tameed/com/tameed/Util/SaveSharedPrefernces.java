package tameed.com.tameed.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveSharedPrefernces {

    SharedPreferences sharedPreferences;
    int latlng;
    public static final String PREFS_NAME = "MyPref";


    public static final String USER_ID = "user_id";


    public static final String City_id = "city_id";

    public String getCity_id(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String city_id = sharedPreferences.getString(City_id, "");
        return city_id;
    }

    public void setCity_id(Context context, String city_id) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(City_id, city_id);
        editor.commit();
    }


    public String getUserId(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString(USER_ID, "");
        return user_id;
    }

    public void setUserId(Context context, String user_id) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, user_id);
        editor.commit();
    }


    public static final String KEY_profile_pic_thumb_url = " profile_pic_thumb_url";

    public String getKEY_profile_pic_thumb_url(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String profile_pic_thumb_url = sharedPreferences.getString(KEY_profile_pic_thumb_url, "");
        return profile_pic_thumb_url;
    }

    public void setKEY_profile_pic_thumb_url(Context context, String profile_pic_thumb_url) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_profile_pic_thumb_url, profile_pic_thumb_url);
        editor.commit();
    }


    public static final String Name = "name";

    public String getName(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Name, "");
        return name;
    }

    public void setName(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Name, name);
        editor.commit();
    }


    public static final String Email = "email";

    public String getEmail(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Email, "");
        return email;
    }

    public void setEmail(Context context, String email) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Email, email);
        editor.commit();
    }

    public static final String Mobile_number = "mobile_number";

    public String getMobile_number(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String mobile_number = sharedPreferences.getString(Mobile_number, "");
        return mobile_number;
    }

    public void setMobile_number(Context context, String mobile_number) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Mobile_number, mobile_number);
        editor.commit();
    }


    public static final String Combine_Mobile_number = "combine_mobile_number";

    public String getCombine_Mobile_number(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String combine_mobile_number = sharedPreferences.getString(Combine_Mobile_number, "");
        return combine_mobile_number;
    }

    public void setCombine_Mobile_number(Context context, String combine_Mobile_number) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Combine_Mobile_number, combine_Mobile_number);
        editor.commit();
    }

    public static final String Provider_id = "provider_id";

    public String getProvider_id(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String provider_id = sharedPreferences.getString(Provider_id, "");
        return provider_id;
    }

    public void setProvider_id(Context context, String provider_id) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Provider_id, provider_id);
        editor.commit();
    }


    public static final String Latitude = "latitude";

    public String getLatitude(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String latitude = sharedPreferences.getString(Latitude, "");
        return latitude;
    }

    public void setLatitude(Context context, String latitude) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Latitude, latitude);
        editor.commit();
    }

    public static final String Longitude = "longitude";

    public String getLongitude(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String longitude = sharedPreferences.getString(Longitude, "");
        return longitude;
    }

    public void setLongitude(Context context, String longitude) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Longitude, longitude);
        editor.commit();
    }

    public static final String Location = "location";

    public String getLocation(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String location = sharedPreferences.getString(Location, "");
        return location;
    }

    public void setLocation(Context context, String location) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Location, location);
        editor.commit();
    }


    public static final String Login_status = "login_status";


    public String getLogin_status(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String login_status = sharedPreferences.getString(Login_status, "");
        return login_status;
    }

    public void setLogin_status(Context context, String login_status) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Login_status, login_status);
        editor.commit();
    }


    public static final String Payment_preference = "payment_preference";

    public String getPayment_preference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String payment_preference = sharedPreferences.getString(Payment_preference, "");
        return payment_preference;
    }

    public void setPayment_preference(Context context, String payment_preference) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Payment_preference, payment_preference);
        editor.commit();
    }


    public static final String Active_status = "active_status";

    public String getActive_status(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String active_status = sharedPreferences.getString(Active_status, "");
        return active_status;
    }

    public void setActive_status(Context context, String active_status) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Active_status, active_status);
        editor.commit();
    }


    public static final String User_type = "user_type";

    public String getUser_type(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_type = sharedPreferences.getString(User_type, "");
        return user_type;
    }

    public void setUser_type(Context context, String user_type) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(User_type, user_type);
        editor.commit();
    }


    public static final String Added_date = "added_date";

    public String getAdded_date(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String added_date = sharedPreferences.getString(Added_date, "");
        return added_date;
    }

    public void setAdded_date(Context context, String added_date) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Added_date, added_date);
        editor.commit();
    }


    public static final String Online_offline_status = "online_offline_status";

    public String getOnline_offline_status(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String online_offline_status = sharedPreferences.getString(Online_offline_status, "");
        return online_offline_status;
    }

    public void setOnline_offline_status(Context context, String online_offline_status) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Online_offline_status, online_offline_status);
        editor.commit();
    }


    public static final String Description = "description";

    public String getDescription(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String description = sharedPreferences.getString(Description, "");
        return description;
    }

    public void setDescription(Context context, String description) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Description, description);
        editor.commit();
    }


    public static final String Review_count = "review_count";

    public String getReview_count(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String review_count = sharedPreferences.getString(Review_count, "");
        return Review_count;
    }

    public void setReview_count(Context context, String review_count) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Review_count, review_count);
        editor.commit();
    }


    public static final String Review_rating = "review_rating";

    public String getReview_rating(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String review_rating = sharedPreferences.getString(Review_rating, "");
        return review_rating;
    }

    public void setReview_rating(Context context, String review_rating) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Review_rating, review_rating);
        editor.commit();
    }


    public static final String Order_count = "order_count";

    public String getOrder_count(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String order_count = sharedPreferences.getString(Order_count, "");
        return order_count;
    }

    public void setOrder_count(Context context, String order_count) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Order_count, order_count);
        editor.commit();
    }


    public static final String Country = "country";

    public String getCountry(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String country = sharedPreferences.getString(Country, "");
        return country;
    }

    public void setCountry(Context context, String country) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Country, country);
        editor.commit();
    }


    public static final String Mobile_visible = "mobile_visible";

    public String getMobile_visible(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String mobile_visible = sharedPreferences.getString(Mobile_visible, "");
        return mobile_visible;
    }

    public void setMobile_visible(Context context, String mobile_visible) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Mobile_visible, mobile_visible);
        editor.commit();
    }


    public static final String City_to_cover = "city_to_cover";


    public String getCity_to_cover(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String city_to_cover = sharedPreferences.getString(City_to_cover, "");
        return city_to_cover;
    }

    public void setCity_to_cover(Context context, String city_to_cover) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(City_to_cover, city_to_cover);
        editor.commit();
    }


    public static final String Distance = "distance";


    public String getDistance(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String distance = sharedPreferences.getString(Distance, "");
        return distance;
    }

    public void setDistance(Context context, String distance) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Distance, distance);
        editor.commit();
    }


    public static final String User_bank_name = "user_bank_name";

    public String getUser_bank_name(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_bank_name = sharedPreferences.getString(User_bank_name, "");
        return user_bank_name;
    }

    public void setUser_bank_name(Context context, String user_bank_name) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(User_bank_name, user_bank_name);
        editor.commit();
    }

    public static final String User_bank_id = "user_bank_id";

    public String getUser_bank_id(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_bank_id = sharedPreferences.getString(User_bank_id, "");
        return user_bank_id;
    }

    public void setUser_bank_id(Context context, String user_bank_id) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(User_bank_id, user_bank_id);
        editor.commit();
    }


    public static final String User_bank_acct_num = "user_bank_acct_num";

    public String getUser_bank_acct_num(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_bank_acct_num = sharedPreferences.getString(User_bank_acct_num, "");
        return user_bank_acct_num;
    }

    public void setUser_bank_acct_num(Context context, String user_bank_acct_num) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(User_bank_acct_num, user_bank_acct_num);
        editor.commit();
    }


    public static final String User_bank_iban_num = "user_bank_iban_num";


    public String getUser_bank_aban_num(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_bank_iban_num = sharedPreferences.getString(User_bank_iban_num, "");
        return user_bank_iban_num;
    }

    public void setUser_bank_iban_num(Context context, String user_bank_iban_num) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(User_bank_iban_num, user_bank_iban_num);
        editor.commit();
    }


    public static final String Push_notification = "push_notification";

    public String getPush_notification(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String push_notification = sharedPreferences.getString(Push_notification, "");
        return push_notification;
    }

    public void setPush_notification(Context context, String push_notification) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Push_notification, push_notification);
        editor.commit();
    }

    public static final String Notification_id = "notification_id";

    public String getNotification_id(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String notification_id = sharedPreferences.getString(Notification_id, "");
        return notification_id;
    }

    public void setNotification_id(Context context, String notification_id) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(Notification_id, notification_id);
        editor.commit();
    }


    public static final String City_name = "city_name";

    public String getCity_name(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String city_name = sharedPreferences.getString(City_name, "");
        return city_name;
    }

    public void setCity_name(Context context, String city_name) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(City_name, city_name);
        editor.commit();
    }

    public static final String KEY_REGID = "regId";

    public String getRegId(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String regId = sharedPreferences.getString(KEY_REGID, "");

        return regId;
    }

    public void setRegId(Context context, String regId) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REGID, regId);
        editor.commit();
    }


//    public String getKEY_Emalid(Context context) {
//        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        String added_date = sharedPreferences.getString(KEY_Emalid, "");
//        return added_date;
//    }
//    public void setKEY_Emalid(Context context, String email ) {
//        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//
//        Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_Emalid,email);
//        editor.commit();
//    }
//
//    public String getKEY_added_date(Context context) {
//        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        String added_date = sharedPreferences.getString(KEY_added_date, "");
//        return added_date;
//    }
//    public void setKEY_added_date(Context context, String added_date ) {
//        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//
//        Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_added_date,added_date);
//        editor.commit();
//    }


}