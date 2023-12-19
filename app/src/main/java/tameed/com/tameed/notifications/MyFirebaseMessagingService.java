package tameed.com.tameed.notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Chat_window;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.Oder_detail_provider;
import tameed.com.tameed.Order_detail;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Constant1;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.Util.SharedPreference;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static String MESSAGE_ACTION = "com.ritesh.gcmchat";
    public static String REGISTER_ACTION = "GcmRegister";
    public static String EXTRA_MESSAGE = "message";
    static String sender_id, project_id;
    //	String image;
    Intent intentt = null;
    String from, order_id, grp_id, msg_time;
    Button msgdialog_btn_ok;
    public static final String MY_PUSH_VALUES = "MyPushValues";
    SharedPreferences push_pref;
    SharedPreferences.Editor edit_push;

    //  String list_id,buyer_id,user_id,store_id,amount;
    //  String from_user_id,to_user_id,from_name,to_name,message,from_user_type,to_user_type;
    String message, gcm_alert_text, assign_id = "", process_type = "", price, user_name = "", file, user_image,
            user_id, cat_id = "", post_id = "", question_type = "", type, date, from_user_id, to_userid, id,
            status, image, new_msg;
    String full_name, datee, warranty_card_pic, timee, wallet_id, project_title, is_review_given_by_receiver;

    String msg_type, title, subtitle;

    String time = "", userdata, friend_image, friend_id, firstname, lastname;
    String alert = "";
    String forum_alert = "", forum_message = "";
    int newww = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {

                sender_id = Constant1.SENDER_ID;

                try {

                    JSONObject temp1 = new JSONObject(remoteMessage.getData().toString());
                    JSONObject temp = new JSONObject(temp1.getString("price"));
                    type = temp.getString("type");


                    //Log.e("Type of notification:", "......" + type);

                    if (type.equals("Project Post Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");

                    } else if (type.equals("admin_push")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");

                        message = temp.getString("message");

                    } else if (type.equals("Review Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Private Order Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Payment alert Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Order Accepted Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Order Rejected Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Quotation Approved Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Order Completed Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Public Order Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");

                    } else if (type.equals("Give Feedback Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Quotation Rejected Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Quotation Submitted Notification")) {

                        newww = 0;
                        if (temp.has("gcm_alert_text"))
                            gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");


                    } else if (type.equals("Extend Warranty Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");

                    } else if (type.equals("Online Payment Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");

                    } else if (type.equals("Offline Payment Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");

                    } else if (type.equals("customer complain")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");

                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("provider complain")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");

                        message = temp.getString("message");
                        order_id = temp.getString("order_id");
                    } else if (type.equals("Approve Payment Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");

                    } else if (type.equals("Order Cancelled By Customer Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");

                    } else if (type.equals("Order Cancelled By Provider Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");


                    } else if (type.equals("Order Completed By Provider Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");


                    } else if (type.equals("Order Rejected By Provider Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");


                    } else if (type.equals("Order Approved By Provider Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        user_id = temp.getString("user_id");
                        message = temp.getString("message");
                        order_id = temp.getString("order_id");


                    } else if (type.equals("Payment Request Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        project_id = temp.getString("project_id");
                        friend_id = temp.getString("sender_id");
                        message = temp.getString("message");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employer";
                        helper.type_list = "awaiting";
                        helper.project_status = "InProgress";

                    } else if (type.equals("Payment Hold Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        project_id = temp.getString("project_id");
                        friend_id = temp.getString("sender_id");
                        message = temp.getString("message");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employee";
                        helper.type_list = "awaiting";
                        helper.project_status = "InProgress";

                    } else if (type.equals("Payment Release Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        project_id = temp.getString("project_id");
                        friend_id = temp.getString("sender_id");
                        message = temp.getString("message");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employee";
                        helper.type_list = "awaiting";
                        helper.project_status = "InProgress";

                    } else if (type.equals("Hire Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employee";
                        helper.type_list = "awaiting";
                        //Log.e("HIRE_ID", "===" + temp.getString("hire_id"));
                        helper.hire_id = temp.getString("hire_id");

                        helper.project_status = "Awaiting";
                    } else if (type.equals("Hire Request Cancelled Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        //Log.e("HIRE_ID111", "===" + temp.getString("hire_id"));
                        helper.hire_id = temp.getString("hire_id");

                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employee";
                        helper.type_list = "active";

                        helper.project_status = "Awarded";
                    } else if (type.equals("Accepted Offer Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        //Log.e("HIRE_ID", "===" + temp.getString("hire_id"));
                        helper.hire_id = temp.getString("hire_id");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employer";
                        helper.type_list = "active";
                        helper.project_status = "InProgress";
                    } else if (type.equals("Rejected Offer Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employer";
                        helper.type_list = "active";
                        helper.project_status = "InProgress";
                    } else if (type.equals("Project Completed Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        title = temp.getString("project_title");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employee";
                        helper.type_list = "completed";
                        helper.project_status = "Completed";
                    } else if (type.equals("Project InComplete Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        helper.project_id = temp.getString("project_id");
                        title = temp.getString("project_title");
                        helper.user_type = "employee";
                        helper.type_list = "incompleted";
                        helper.project_status = "InComplete";
                    } else if (type.equals("Project Cancelled Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = "employee";
                        helper.type_list = "cancel";
                        helper.project_status = "Cancelled";
                    } else if (type.equals("Review Notification")) {
                        newww = 0;
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        title = temp.getString("project_title");
                        is_review_given_by_receiver = temp.getString("is_review_given_by_receiver");
                        helper.project_id = temp.getString("project_id");
                        helper.user_type = temp.getString("user_type");
                        if (temp.getString("project_status").equals("InComplete")) {


                            helper.type_list = "incompleted";

                        } else {
                            helper.type_list = "completed";
                        }
                        helper.project_status = temp.getString("project_status");


                    } else if (type.equals("chatmessage")) {
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        friend_id = temp.getString("sender_id");
                        friend_image = temp.getString("picture");
                        time = temp.getString("time");
                        message = temp.getString("message");
                        if (helper.checkview == 1) {
                            if (helper.friendid.equals(friend_id)) {
                                newww = 1;
                            } else {
                                newww = 0;
                            }

                        } else {
                            newww = 0;
                        }
                    } else if (type.equals("chatsupport_message")) {
                        gcm_alert_text = temp.getString("gcm_alert_text");
                        full_name = temp.getString("full_name");
                        message = temp.getString("message");
                        friend_id = temp.getString("sender_id");
                        time = temp.getString("time");

                        if (helper.checkview == 1) {
                            if (helper.friendid.equals(friend_id)) {
                                newww = 1;
                            } else {
                                newww = 0;
                            }

                        } else {
                            newww = 0;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

                //Log.e("current task :", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClass().getSimpleName() + "......" + newww);

                ComponentName componentInfo = taskInfo.get(0).topActivity;
                //Log.e("act name", "MM" + componentInfo.getPackageName().getClass().getName());
                if (componentInfo.getPackageName().equalsIgnoreCase("tameed.com.tameed")) {
                    if (type.equals("chatsupport_message")) {
                        if (componentInfo.getClassName().equalsIgnoreCase("tameed.com.tameed.Online_ChatWindow")) {
                            //Log.e(TAG, "iffffffffffff");
                            if (helper.checkview == 1) {
                                displayMessage_1(this, friend_id, time, message);
                            }
                        }

                    } else if (type.equals("chatmessage")) {
                        if (componentInfo.getClassName().equalsIgnoreCase("tameed.com.tameed.Chat_window")) {
                            //Log.e(TAG, "iffffffffffff");
                            if (helper.checkview == 1) {
                                if (helper.friendid.equals(friend_id)) {
                                    displayMessage(this, friend_id, full_name, friend_image, time, message);
                                }
                            }
                        }

                    }
                }

                if (newww == 0) {
                    //Log.e("Message and full name:", message + ":" + full_name);

                    if (type.equals("Project Post Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Hire Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Hire Request Cancelled Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Accepted Offer Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Rejected Offer Notification")) {

                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Review Notification")) {
                        helper.pblc = 1;
                        intentt = new Intent(this, Oder_detail_provider.class);

                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Private Order Notification")) {
                        helper.pblc = 1;
                        intentt = new Intent(this, Oder_detail_provider.class);

                        intentt.putExtra("order_id", order_id);


                    } else if (type.equals("Quotation Submitted Notification")) {
                        intentt = new Intent(this, Order_detail.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 0;
                    } else if (type.equals("Order Rejected Notification")) {
                        intentt = new Intent(this, Order_detail.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 0;
                    } else if (type.equals("Quotation Approved Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 1;
                    } else if (type.equals("Quotation Rejected Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 1;
                    } else if (type.equals("Give Feedback Notification")) {
                        intentt = new Intent(this, Order_detail.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 0;
                    } else if (type.equals("Order Completed Notification")) {
                        intentt = new Intent(this, Order_detail.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 0;
                    } else if (type.equals("Public Order Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 1;
                    } else if (type.equals("Order Accepted Notification")) {
                        intentt = new Intent(this, Order_detail.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 0;
                    } else if (type.equals("Payment alert Notification")) {
                        intentt = new Intent(this, Order_detail.class);

                        intentt.putExtra("order_id", order_id);
                        helper.pblc = 0;
                    } else if (type.equals("admin_push")) {
                        intentt = new Intent(this, tameed.com.tameed.Notification.class);

                    } else if (type.equals("Quotation Submitted Notification")) {
                        intentt = new Intent(this, Order_detail.class);
                        helper.pblc = 0;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Extend Warranty Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);
                        helper.pblc = 1;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Online Payment Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);
                        helper.pblc = 1;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Offline Payment Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);
                        helper.pblc = 1;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Approve Payment Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);
                        helper.pblc = 1;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("customer complain")) {
                        intentt = new Intent(this, Order_detail.class);
                        helper.pblc = 0;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("provider complain")) {
                        intentt = new Intent(this, Oder_detail_provider.class);
                        helper.pblc = 1;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Order Cancelled By Customer Notification")) {
                        intentt = new Intent(this, Oder_detail_provider.class);
                        helper.pblc = 1;
                        intentt.putExtra("order_id", order_id);

                    } else if (type.equals("Order Cancelled By Provider Notification")) {
                        intentt = new Intent(this, Order_detail.class);
                        helper.pblc = 0;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Order Rejected By Provider Notification")) {
                        intentt = new Intent(this, Order_detail.class);
                        helper.pblc = 0;
                        intentt.putExtra("order_id", order_id);
                    }else if (type.equals("Order Approved By Provider Notification")) {
                        intentt = new Intent(this, Order_detail.class);
                        helper.pblc = 0;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Order Completed By Provider Notification")) {
                        intentt = new Intent(this, Order_detail.class);
                        helper.pblc = 0;
                        intentt.putExtra("order_id", order_id);
                    } else if (type.equals("Payment Request Notification")) {

                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Payment Hold Notification")) {

                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Payment Release Notification")) {

                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Project Completed Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                        intentt.putExtra("project_title", title);
                    } else if (type.equals("Project InComplete Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                        intentt.putExtra("project_title", title);
                    } else if (type.equals("Project Cancelled Notification")) {
                        intentt = new Intent(this, MainActivity.class);
                    } else if (type.equals("Review Notification")) {
                        if (!is_review_given_by_receiver.equals("yes")) {
                            intentt = new Intent(this, MainActivity.class);
                            intentt.putExtra("project_title", title);
                        } else {
                            intentt = new Intent(this, MainActivity.class);
                            intentt.putExtra("project_title", title);
                        }
                    } else if (type.equals("chatmessage")) {


                        intentt = new Intent(this, Chat_window.class);
                        intentt.putExtra("friend_id", friend_id);
                        intentt.putExtra("firstname", full_name);
                        intentt.putExtra("profile_pic", friend_image);
                        intentt.putExtra("time", time);
                        intentt.putExtra("message", message);
                        intentt.putExtra("status", "chat");
                    } else if (type.equals("chatsupport_message")) {
                        intentt = new Intent(this, MainActivity.class);
                        intentt.putExtra("friend_id", friend_id);
                        intentt.putExtra("time", time);
                        intentt.putExtra("message", message);

                    } else {
                        intentt = new Intent(this, MainActivity.class);
                    }

                    if (full_name.equals("null"))
                        full_name = "";


                    intentt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    final PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    this,
                                    0,
                                    intentt,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                                            | PendingIntent.FLAG_ONE_SHOT
                            );
                    final int icon = R.mipmap.ic_launcher;
                    Random random = new Random();
                    int m = random.nextInt(9999 - 1000) + 1000;
                    //Log.e("RANDOM", "Random" + m);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                            .setContentTitle((full_name))
                            .setContentText((message))
                            .setAutoCancel(false)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(resultPendingIntent)
                            .setContentInfo(message)
                            .setTicker(message)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                            .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                            .setColor(Color.GREEN)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message))
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setLights(Color.RED, 1000, 300)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setSmallIcon(R.mipmap.ic_launcher_round).setTicker(full_name).setWhen(0);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(m, notificationBuilder.build());

                    // Notification Channel is required for Android O and above
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(
                                "channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH
                        );
                        channel.setDescription(message);
                        channel.setShowBadge(true);
                        channel.canShowBadge();
                        channel.enableLights(true);
                        channel.setLightColor(Color.RED);
                        channel.enableVibration(true);
                        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
                        notificationManager.createNotificationChannel(channel);

                    }

                }

            } catch (Exception e) {
                //Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void displayMessage_1(Context context,
                                  String friend_id, String time, String message) {
        //Log.e("Send", "BRODCAST FRM GCM: From usedid:" + from_user_id
        //   + "  Username:" + user_name + "   type:" + type + "   Msg:" + message);
        Intent intent = new Intent(MESSAGE_ACTION);

        intent.putExtra("friend_id", friend_id);
        intent.putExtra("time", time);
        intent.putExtra("message", message);

        context.sendBroadcast(intent);
    }

    private void displayMessage(Context context,
                                String friend_id, String full_name, String friend_image
            , String time, String message) {
        //Log.e("Send", "BRODCAST FRM GCM: From usedid:" + from_user_id
        //   + "  Username:" + user_name + "   type:" + type + "   Msg:" + message);
        Intent intent = new Intent(MESSAGE_ACTION);
        intent.putExtra("friend_id", friend_id);
        intent.putExtra("firstname", full_name);
        intent.putExtra("profile_pic", friend_image);
        intent.putExtra("time", time);
        intent.putExtra("message", message);
        intent.putExtra("status", "chat");
        context.sendBroadcast(intent);
    }

    private SaveSharedPrefernces ssp;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //Log.e("NEW_TOKEN", s);
        String refreshedToken = s;

        // Saving reg id to shared preferences
        SharedPreference.saveSharedPrefValue(this, NotificationConfig.FCM_ID, refreshedToken);
        SharedPreference.saveBoolSharedPrefValue(this, NotificationConfig.FCM_ID_FLAG, true);
        //Log.e(TAG, "save RegistrationToServer FCM: ");
        ssp = new SaveSharedPrefernces();
        ssp.setRegId(this, refreshedToken);
        // sending reg id to your server


        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(NotificationConfig.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);

        Intent intent = new Intent(Constant1.REGISTER_ACTION);
        intent.putExtra(Constant1.REGISTRATION_ID, refreshedToken);
        sendBroadcast(intent);
    }
}
