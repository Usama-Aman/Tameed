package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;

import tameed.com.tameed.Adapter.Chat_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.ChatHistory_Entity;
import tameed.com.tameed.Entity.SupportEntity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.Util.URLogs;


public class Chat_window extends AppCompatActivity {
    RelativeLayout chat_window_header;
    ImageView chat_img;
    int i = 0;
    TextView chat_name_txt;
    ListView list_chat;
    String status_online_offline;
    String hiii, state = "", project_status, user_type;
    final Handler ha = new Handler();
    Chat_Adapter chatAdapter;
    ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    JSONObject jsonObject = new JSONObject();
    ArrayList<ChatHistory_Entity> chat_list = new ArrayList<ChatHistory_Entity>();
    ArrayList<SupportEntity> support_list = new ArrayList<SupportEntity>();
    int flagvalue;
    String action = "", friend_id = "", message, msg, firstname, lastname, friend_image = "", fullname, time;
    public static final String Prefs_remember_value = "UserTypeValues";
    SharedPreferences preferences2;
    EditText chat_edit;
    ImageView send_button, header_go;
    TextView error_msg, txt_online;
    RelativeLayout activity_chat_window;
    Timer timer;
    TextView header_txt;
    ImageView header_menu, header_back, header_logo;
    String status, email, aboutme, mobile, is_friend, friendstatus = "", userid_chat = "", admin_id = "";
    String auto_reply_message_doctor;
    String receiver_status_online_offline;
    RelativeLayout relative_online;
    public static String myid;
    public static String friendid;
    public static int checkview = 0;
    String formattedDate, order;
    int gmtvalue;
    String typingstatus;
    ImageView header_addappointment;
    RelativeLayout header;
    private SaveSharedPrefernces ssp;
    TextView chat_status;
    String otheruser_id, typing_status, user_id, user_name, profile_pic;
    TextView txtHeader, textTyping;
    ImageView imageChatProfile;
    private String TAG = "Chat_window";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        //Log.e(TAG, "****************************");
        ssp = new SaveSharedPrefernces();
        helper.setchatno = "";

        Intent i = getIntent();
        fullname = i.getStringExtra("firstname");
        profile_pic = i.getStringExtra("profile_pic");
        friend_id = i.getStringExtra("friend_id");
        state = i.getStringExtra("status");

//        helper.cat_name3.clear();

        txtHeader = (TextView) findViewById(R.id.txt_header);
        chat_status = (TextView) findViewById(R.id.textTyping);
        imageChatProfile = (ImageView) findViewById(R.id.imageChatProfile);
        imageChatProfile.setVisibility(View.VISIBLE);

//        //Log.e(TAG, "****************************");

        header_back = (ImageView) findViewById(R.id.header_back);


        txtHeader.setText(fullname);

        if (!profile_pic.equals("")) {
            Picasso.with(Chat_window.this)
                    .load(profile_pic)
                    .noFade()
                    .error(R.drawable.default_user_2x)
                    .into(imageChatProfile);
        } else {
            imageChatProfile.setImageResource(R.drawable.default_user_2x);
        }
//        lastname = i.getStringExtra("Lastname");
//        order = i.getStringExtra("order");


//        chat_window_header = (RelativeLayout) findViewById(R.id.chat_window_header);
        error_msg = (TextView) findViewById(R.id.error_msg);
//        chat_status= (TextView) findViewById(R.id.chat_status);
        activity_chat_window = (RelativeLayout) findViewById(R.id.activity_chat_window);
        relative_online = (RelativeLayout) findViewById(R.id.relative_online);
        txt_online = (TextView) findViewById(R.id.txt_online);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        send_button = (ImageView) findViewById(R.id.send_button);
//        header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        // chat_img = (ImageView) findViewById(R.id.chat_img);
//        header_addappointment = (ImageView) findViewById(R.id.header_addappointment);
        // chat_img.setVisibility(View.VISIBLE);
//        chat_name_txt = (TextView) findViewById(R.id.header_chattxt);

//        chat_name_txt.setText("Name");
        //chat_name_txt.setTextColor(getResources().getColor(R.color.white));
//        chat_name_txt.setVisibility(View.VISIBLE);
        // header_addappointment.setImageResource(R.drawable.ic_basket);


        list_chat = (ListView) findViewById(R.id.recent_chat_recycle);


//      header_go.setVisibility(View.VISIBLE);

        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                Get_TypingList();
                ha.postDelayed(this, 2000);
            }
        }, 2000);


        chat_edit.addTextChangedListener(new TextWatcher() {

                                             @SuppressLint("DefaultLocale")
                                             @Override
                                             public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                                                 if (chat_edit.getText().toString().length() > 0) {
                                                     typingstatus = "1";
                                                     ShowAddTyping();
                                                 } else {
                                                     typingstatus = "0";
                                                     ShowAddTyping();
                                                 }

                                             }

//                                            }

                                             @Override
                                             public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                                           int arg3) {

                                                 // TODO Auto-generated method stub
//                                                         chat_edit.setText("You will pay : $ "+"0");


                                             }

                                             @Override
                                             public void afterTextChanged(Editable arg0) {

                                                 // TODO Auto-generated method stub

                                             }
                                         }

        );


        TimeZone tz = TimeZone.getDefault();
        ////Log.e("shahhs", "" + tz.getDisplayName(false, TimeZone.SHORT));

        TimeZone tzzz = TimeZone.getDefault();
        Date now = new Date();
        gmtvalue = tzzz.getOffset(now.getTime()) / 1000;
        ////Log.e("111111", "" + gmtvalue);
        ////Log.e("gmt", String.valueOf(gmtvalue));

//        helper.myid=ssp.get_id(Chat_window.this);

        helper.friendid = friend_id;
        helper.checkview = 1;

//        chat_name_txt.setText(fullname);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        formattedDate = df.format(c.getTime());
        ////Log.e("Time", "" + state);


        Showchathistory();
//
//        if(state.equals("project")){
//            header_go.setVisibility(View.GONE);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.checkview = 0;
                typingstatus = "0";
                ha.removeCallbacksAndMessages(null);
                ShowAddTyping();
                chat_status.setVisibility(View.GONE);

                finish();
            }
        });
//        }
//        else  {
//            header_go.setVisibility(View.VISIBLE);
//            header_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    helper.checkview = 0;
//                    typingstatus="0";
//                    ShowAddTyping();
//                    ha.removeCallbacksAndMessages(null);
//                    chat_status.setVisibility(View.GONE);
//                   Intent intent = new Intent(Chat_window.this, Recent_Chat.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//        }

//        header_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    helper.checkview = 0;
////                    Intent intent = new Intent(Chat_window.this, Pr.class);
////                    startActivity(intent);
//                finish();
//            }
//        });
//


       /* if (friend_image.equals("")) {
            chat_img.setImageResource(R.drawable.default_user_2x);
        } else {
            Picasso.with(this)
                    .load(friend_image)
                    .noFade()
                    .error(R.drawable.default_user_2x)
                    .into(chat_img);
        }

        if (fullname.equals("")) {
            chat_name_txt.setText("Name");
        } else {
            chat_name_txt.setText(fullname);
        }*/

/*

        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.checkview = 0;
                helper.friendid="";
                if(order!=null){
                    Intent intent = new Intent(Chat_window.this, OrderActivity.class);
                    intent.putExtra("order","order");
                    startActivity(intent);
                    ////Log.e("hiii4444",""+hiii);
                    Readmessage();
                }
                else {
                    helper.checkview = 0;
                    Intent intent = new Intent(Chat_window.this, DashPage.class);
                    startActivity(intent);
                    ////Log.e("hiii5555",""+hiii);
                    Readmessage();
                }
                finish();
            }
        });
*/

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = chat_edit.getText().toString();
                helper.setchatno = "1";
                if (state == null) {
                    ////Log.e("11111", "11111");
                    ChatHistory_Entity ent = new ChatHistory_Entity();
                    ent.setMessage(message);
                    ent.setFromUsers_id(ssp.getUserId(Chat_window.this));
                    ent.setToUsers_id(friend_id);
                    ent.setStatus("sent");
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);//dd/MM/yyyy
                    Date now = new Date();
                    String strDate = sdfDate.format(now);
                    ent.setTimee(strDate);
                    ent.setTime(strDate);
                    ////Log.e("TIME111", "" + strDate);
                    chat_list.add(ent);
                    // chatAdapter.notifyDataSetChanged();
                    chat_edit.setText("");
                    sendMessage();
                }
                if (state != null) {
                    ////Log.e("222", "222");
                    if (state.equals("chat")) {
                        ////Log.e("3333333", "333333");
                        ChatHistory_Entity ent = new ChatHistory_Entity();
                        ent.setMessage(message);
                        ent.setFromUsers_id(ssp.getUserId(Chat_window.this));
                        ent.setToUsers_id(friend_id);
                        ent.setStatus("sent");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);//dd/MM/yyyy
                        Date now = new Date();
                        String strDate = sdfDate.format(now);
                        ent.setTimee(strDate);
                        ent.setTime(strDate);
                        ////Log.e("TIME2222", "" + strDate);
                        chat_list.add(ent);
                        // chatAdapter.notifyDataSetChanged();
                        chat_edit.setText("");
                        sendMessage();
                    } else {
                        ////Log.e("44444", "4444");
                        try {
                            if (!message.equals("")) {
                                ////Log.e("555555", "5555555");
                                ChatHistory_Entity ent = new ChatHistory_Entity();
                                ent.setMessage(message);
                                ent.setFromUsers_id(ssp.getUserId(Chat_window.this));
                                ent.setToUsers_id(friend_id);
                                ent.setStatus("sent");
                                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);//dd/MM/yyyy
                                Date now = new Date();
                                String strDate = sdfDate.format(now);
                                ent.setTimee(strDate);
                                ent.setTime(strDate);
                                ////Log.e("TIME33333", "" + strDate);
//                                String date_selected;
//                                Calendar metFromdate1 = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                try {
//                                    metFromdate1.setTime(dateFormat.parse(strDate));
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                                date_selected = df.format(metFromdate1.getTime());
//                                ent.setTime(date_selected);
                                chat_list.add(ent);
                                chatAdapter.notifyDataSetChanged();
                                chat_edit.setText("");
                                list_chat.smoothScrollToPosition(chatAdapter.getCount());
                                sendMessage();
                            }
                        } catch (Exception e) {

                        }
                    }
                }

            }
        });


    }

    private void ShowAddTyping() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ////Log.e("ShowAddTyping", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
//                            D/ShowAddTyping: {"msg":"Add successfully"}
                            if (msg.equals("Add successfully")) {
//                                friend_id=otheruser_id;


                            }
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Addtyping");
                params.put("user_id", ssp.getUserId(Chat_window.this));
                params.put("other_user_id", friend_id);
//                params.put("project_id",project_id);
                params.put("user_name", fullname);
                params.put("typing_status", typingstatus);
                // params.put("gmt",String.valueOf(gmtvalue));
                ////Log.e("Addtyping", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void Get_TypingList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        ////Log.e("Get_TypingList", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());

                            ArrayList<SupportEntity> list = new ArrayList<SupportEntity>();

                            JSONArray jsonParentNode = obj.getJSONArray("show");
//                            ////Log.e("SHOWWWWW", jsonParentNode.toString());

                            if (jsonParentNode.length() == 0) {
//                                ////Log.e("aaaaa","aaaa");
                                chat_status.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonParentNode.length(); i++) {
                                    JSONObject jsonChildNode = jsonParentNode.getJSONObject(i);

                                    SupportEntity chatHistory_entity = new SupportEntity();
                                    chatHistory_entity.setOtheruser_id(jsonChildNode.getString("other_user_id"));
                                    chatHistory_entity.setUser_id(jsonChildNode.getString("user_id"));
                                    chatHistory_entity.setUser_name(jsonChildNode.getString("user_name"));
                                    chatHistory_entity.setTyping_status(jsonChildNode.getString("typing_status"));
//                                    chatHistory_entity.setChat_from(jsonChildNode.getString("chat_from"));


                                    list.add(chatHistory_entity);

                                    if (jsonParentNode.length() != 0) {

                                        otheruser_id = jsonChildNode.getString("other_user_id");
                                        user_id = jsonChildNode.getString("user_id");
                                        user_name = jsonChildNode.getString("user_name");
                                        typing_status = jsonChildNode.getString("typing_status");
//                                        chat_from = jsonChildNode.getString("chat_from");
                                        ////Log.e("otherid", "" + otheruser_id);
                                        ////Log.e("username", "" + user_name);
                                        ////Log.e("TYPINGSTATUS", "" + typing_status);
//                                        ////Log.e("CHAT FROM", "" + chat_from);
                                        ////Log.e("USERID", "" + user_id);
                                        ////Log.e("USERID1111", "" + ssp.getUserId(Chat_window.this));

                                        if (typing_status.equals("1")) {
                                            if (otheruser_id.equals(ssp.getUserId(Chat_window.this))) {
                                                chat_status.setVisibility(View.VISIBLE);
                                            }


                                        } else if (typing_status.equals("0")) {
                                            chat_status.setVisibility(View.GONE);
                                        }


                                    }

                                }

                                support_list.addAll(list);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Gettypinglist");
                params.put("user_id", ssp.getUserId(Chat_window.this));
                params.put("other_user_id", friend_id);
//                params.put("project_id",project_id);
                // params.put("gmt",String.valueOf(gmtvalue));
//                ////Log.e("Gettypinglist", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void Showchathistory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ////Log.e("Jsonshowchat", response.toString());
                        JSONObject obj = null;
                        chat_list = new ArrayList<ChatHistory_Entity>();
                        try {
                            obj = new JSONObject(response.toString());
                            ArrayList<ChatHistory_Entity> list = new ArrayList<ChatHistory_Entity>();
//
                            JSONArray jsonParentNode = obj.getJSONArray("show");
                            status_online_offline = obj.getString("receiver_status_online_offline");
                            for (int i = 0; i < jsonParentNode.length(); i++) {
                                JSONObject jsonChildNode = jsonParentNode.getJSONObject(i);
                                ChatHistory_Entity chatHistory_entity = new ChatHistory_Entity();
                                chatHistory_entity.setDatee(jsonChildNode.getString("datee"));
                                chatHistory_entity.setFromUsers_id(jsonChildNode.getString("fromUsers_id"));
                                chatHistory_entity.setId(jsonChildNode.getString("id"));
                                chatHistory_entity.setMessage(jsonChildNode.getString("message"));
                                chatHistory_entity.setNew_msg(jsonChildNode.getString("new"));
                                chatHistory_entity.setStatus(jsonChildNode.getString("status"));
                                chatHistory_entity.setTime(jsonChildNode.getString("time"));
                                chatHistory_entity.setPic(jsonChildNode.getString("pic"));
                                chatHistory_entity.setTimee(jsonChildNode.getString("timee"));
                                chatHistory_entity.setToUsers_id(jsonChildNode.getString("toUsers_id"));

                                //TODO ADD JAY
                                chatHistory_entity.setStatus_new(jsonChildNode.getString("new"));

//                                TODO GET CHAT
                                //Log.e("SATATUS",jsonChildNode.getString("status")+" = done");
                                //Log.e("NEW",jsonChildNode.getString("new")+" = done");


//                                Calendar metFromdate1 = Calendar.getInstance();
//
//                                String date_selected = "";
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                try {
//                                    metFromdate1.setTime(dateFormat.parse(jsonChildNode.getString("time")));
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                                date_selected = df.format(metFromdate1.getTime());
//
//                                if (helper.cat_name3.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",").contains(date_selected)) {
//                                    ////Log.e("1", "1");
//                                    chatHistory_entity.setNo("1");
//                                } else {
                                chatHistory_entity.setNo("2");
//
//                                    helper.cat_name3.add(date_selected);
//                                }
                                list.add(chatHistory_entity);
                            }
                            chat_list.addAll(list);
                            Collections.reverse(chat_list);

                            if (chat_list.size() != 0) {
                                list_chat.setVisibility(View.VISIBLE);
//                                list_chat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                                list_chat.setStackFromBottom(true);
                                chatAdapter = new Chat_Adapter(Chat_window.this, chat_list);
                                list_chat.setAdapter(chatAdapter);
//                                list_chat.setSelection(chatAdapter.getCount());
                                list_chat.setSelection(chat_list.size());
                                list_chat.smoothScrollToPosition(chatAdapter.getCount());
                                //scrollMyListViewToBottom();

                            } else {

                                list_chat.setVisibility(View.GONE);
                                error_msg.setVisibility(View.VISIBLE);
                                error_msg.setText("لا توجد دردشات");

                            }

                            if (status_online_offline.equals("0")) {
//                            ////Log.e("HEPLR CHAT",""+helper.reciever_status);
                                txt_online.setText("غير متوفر");
                                relative_online.setBackgroundResource(R.color.dark_grey);

                            } else {

                                txt_online.setText("متوفر");
                                relative_online.setBackgroundResource(R.color.green);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Showchathistory");
                params.put("user_id", ssp.getUserId(Chat_window.this));
                params.put("friend_id", friend_id);
                ////Log.e("CHATSHOWMESSAGE", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void sendMessage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ////Log.e("JsonData", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            error_msg.setVisibility(View.GONE);
                            list_chat.setVisibility(View.VISIBLE);
                            msg = obj.getString("msg");
                            Showchathistory();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Chatmessage");
                params.put("user_id", ssp.getUserId(Chat_window.this));
                params.put("friend_id", friend_id);
                params.put("message", message);
                ////Log.e("CHATSENDMEASSAGE", String.valueOf(params));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private final BroadcastReceiver HandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            URLogs.m1("onReceive Message");
            String from_user_id = intent.getExtras().getString("friend_id");
            String user_name = intent.getExtras().getString("firstname");
            String project_id = intent.getExtras().getString("project_id");
            String time = intent.getExtras().getString("time");
            String user_photo = intent.getExtras().getString("friend_image");
            String message = intent.getExtras().getString("message");

            ////Log.e("from_user_id", "AA" + from_user_id);
            ////Log.e("user_name", "AA" + user_name);
            ////Log.e("message", "AA" + message);
            ////Log.e("project_id", "AA" + project_id);


            ChatHistory_Entity ent = new ChatHistory_Entity();
            ent.setMessage(message);
            ent.setFromUsers_id(from_user_id);
            ent.setStatus("received");
            ent.setNo("1");
            ent.setTimee(time);
            ent.setTime(time);
            chat_list.add(ent);
            chatAdapter.notifyDataSetChanged();

            list_chat.smoothScrollToPosition(chatAdapter.getCount());
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(HandleMessageReceiver);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentfilter = new IntentFilter(GCMIntentService.MESSAGE_ACTION);
        registerReceiver(HandleMessageReceiver, intentfilter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helper.checkview = 0;
        typingstatus = "0";
        ShowAddTyping();
        chat_status.setVisibility(View.GONE);
        ha.removeCallbacksAndMessages(null);
//        finish();
//        if(state.equals("project")){
//            header_go.setVisibility(View.GONE);
//            header_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    helper.checkview = 0;
//                    typingstatus="0";
//                    ha.removeCallbacksAndMessages(null);
//                    ShowAddTyping();
//                    chat_status.setVisibility(View.GONE);
//
        finish();
//                }
//            });
//        }
//        else  {
//            header_go.setVisibility(View.VISIBLE);
//            header_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    helper.checkview = 0;
//                    typingstatus="0";
//                    ShowAddTyping();
//                    ha.removeCallbacksAndMessages(null);
//                    chat_status.setVisibility(View.GONE);
//                    Intent intent = new Intent(Chat_window.this, Recent_Chat.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//        }
////        if(state.equals("chat")){
//            helper.checkview = 0;
//            Intent intent = new Intent(Chat_window.this, Recent_Chat.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
//         finish();
//        }
    }

    protected void onStop() {
        super.onStop();
        ha.removeCallbacksAndMessages(null);
    }


}
