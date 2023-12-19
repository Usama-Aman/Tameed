package tameed.com.tameed;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import tameed.com.tameed.Util.Constant1;
import tameed.com.tameed.Adapter.helper;
;

public class GCMIntentService extends GCMBaseIntentService {
    public static String MESSAGE_ACTION = "com.ritesh.gcmchat";
    public static String REGISTER_ACTION = "GcmRegister";
    public static String EXTRA_MESSAGE = "message";
    private String TAG = "Ritesh";
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
    String message, gcm_alert_text, assign_id = "", process_type = "", price, user_name = "", file, user_image, user_id, cat_id = "", post_id = "", question_type = "", type, date, from_user_id, to_userid, id,
            status, image, new_msg;
    String full_name, datee, warranty_card_pic, timee, wallet_id, project_title, is_review_given_by_receiver;

    String msg_type, title, subtitle;

    String time = "", userdata, friend_image, friend_id, firstname, lastname;
    String alert = "";
    String forum_alert = "", forum_message = "";
    int newww = 0;


    @Override
    protected void onError(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
    }

    public GCMIntentService() {
        super(Constant1.SENDER_ID);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onMessage(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //Log.e("receive", "ttttt");
        sender_id = Constant1.SENDER_ID;
        //Log.e("receive", intent.getExtras().toString());


        price = intent.getExtras().getString("price");
        try {
            //	temp = jarray.getJSONObject(0);
            JSONObject temp = new JSONObject(price);
            type = temp.getString("type");
            if (type.equals("Project Post Notification")) {
                newww = 0;
                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");
//				helper.project_id=temp.getString("project_id");
//				helper.user_type="employee";
//                helper.type_list="active";
//                helper.project_status="Active";

            } else if (type.equals("admin_push")) {
                newww = 0;
                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");

                message = temp.getString("message");

            } else if (type.equals("Review Notification")) {
                newww = 0;
                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");
//				user_id=temp.getString("user_id");
                message = temp.getString("message");
                order_id = temp.getString("order_id");
            } else if (type.equals("Private Order Notification")) {
                newww = 0;
                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");
                user_id = temp.getString("user_id");
                message = temp.getString("message");
                order_id = temp.getString("order_id");
//				helper.project_id=temp.getString("project_id");
//				helper.user_type="employee";
//                helper.type_list="active";
//                helper.project_status="Active";

            } else if (type.equals("Payment alert Notification")) {
                newww = 0;
                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");
                //	user_id=temp.getString("user_id");
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
//				helper.project_id=temp.getString("project_id");
//				helper.user_type="employee";
//                helper.type_list="active";
//                helper.project_status="Active";

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
//				helper.project_id=temp.getString("project_id");
//				helper.user_type="employee";
//                helper.type_list="active";
//                helper.project_status="Active";

            } else if (type.equals("Order Cancelled By Provider Notification")) {
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
//				friend_image = temp.getString("picture");
//				time = temp.getString("timee");
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
//				friend_image = temp.getString("picture");
//				time = temp.getString("timee");
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
//				friend_image = temp.getString("picture");
//				time = temp.getString("timee");
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
//				newww=1;


                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");
//				project_id = temp.getString("project_id");
                friend_id = temp.getString("sender_id");
                friend_image = temp.getString("picture");
                time = temp.getString("time");
                message = temp.getString("message");
                if (helper.checkview == 1) {
                    if (helper.friendid.equals(friend_id)) {
                        newww = 1;
//						displayMessage(context, friend_id ,firstname,lastname ,friend_image,time,message);
                    } else {
                        newww = 0;
                    }

                } else {
                    newww = 0;
                }
            } else if (type.equals("chatsupport_message")) {
//				newww=0;
                gcm_alert_text = temp.getString("gcm_alert_text");
                full_name = temp.getString("full_name");
                message = temp.getString("message");
                friend_id = temp.getString("sender_id");
                time = temp.getString("time");

                if (helper.checkview == 1) {
                    if (helper.friendid.equals(friend_id)) {
                        newww = 1;
//						displayMessage_1(context, friend_id, time, message);
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
                        displayMessage_1(context, friend_id, time, message);
                    }
                }

            } else if (type.equals("chatmessage")) {
                if (componentInfo.getClassName().equalsIgnoreCase("tameed.com.tameed.Chat_window")) {
                    //Log.e(TAG, "iffffffffffff");
                    if (helper.checkview == 1) {
                        if (helper.friendid.equals(friend_id)) {
                            displayMessage(context, friend_id, full_name, friend_image, time, message);
                        }
                    }
                }

            }
        }

        if (newww == 0) {
            //Log.e("Message and full name:", message + ":" + full_name);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) //V1.0
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(full_name)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX);
            // mBuilder.getNotification().flags |= (Notification.FLAG_AUTO_CANCEL | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

            intentt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            final PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            intentt,
                            PendingIntent.FLAG_UPDATE_CURRENT
                                    | PendingIntent.FLAG_ONE_SHOT
                    );

            mBuilder.setContentIntent(resultPendingIntent);


            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            //Log.e("RANDOM", "Random" + m);
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(m, mBuilder.build());

            // Notification Channel is required for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Log.e("oreo notifications", "....");
                mBuilder.setChannelId("channel_id");

            }


//			displayMessages(context, from_user_id, user_name, message, type, pic);
        }
    }

    private void displayMessage_1(Context context,
                                  String friend_id, String time, String message) {
        //Log.e("Send", "BRODCAST FRM GCM: From usedid:" + from_user_id
            //    + "  Username:" + user_name + "   type:" + type + "   Msg:" + message);
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
//		intent.putExtra("project_id", project_id);
        intent.putExtra("profile_pic", friend_image);
        intent.putExtra("time", time);
        intent.putExtra("message", message);
        intent.putExtra("status", "chat");
        context.sendBroadcast(intent);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Constant1.REGISTER_ACTION);
        intent.putExtra(Constant1.REGISTRATION_ID, regId);
        context.sendBroadcast(intent);
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub

    }

}