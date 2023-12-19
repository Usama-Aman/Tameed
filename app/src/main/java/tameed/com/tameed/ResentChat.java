package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Recent_Chat_Adapter;
import tameed.com.tameed.Entity.RecentChatEntity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;
public class ResentChat extends AppCompatActivity {
    //ArrayList<String> personNames = new ArrayList(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
    private TextView txtHeader;
    ImageView header_back;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "";
    ArrayList<RecentChatEntity> recent_chatlist=new ArrayList<RecentChatEntity>();
    RecyclerView chat_list;
    Recent_Chat_Adapter recentChat_adapter;
    TextView error_msg;
    SaveSharedPrefernces ssp;
    private String TAG = "ResentChat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        ////Log.e(TAG,"****************************");
        setContentView(R.layout.resent_chat);
        ssp = new SaveSharedPrefernces();

        txtHeader=(TextView) findViewById(R.id.txt_header);
        error_msg=(TextView) findViewById(R.id.error_msg);
        txtHeader.setText("المحادثة الفورية");
        header_back=(ImageView)findViewById(R.id.header_back);



        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        chat_list = (RecyclerView) findViewById(R.id.recent_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chat_list.setLayoutManager(linearLayoutManager);

        showRecentChat();


//        Recent_Chat_Adapter recent_chat_adapter = new Recent_Chat_Adapter((this), personNames);
//        recyclerView.setAdapter(recent_chat_adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*////Log.e(TAG,"onResume>>>>>>>>>>");
        //ADD JAY BUT NOT REQ
        if(recentChat_adapter !=null)
        {
            recentChat_adapter.notifyDataSetChanged();
        }*/

    }

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;

                case LOAD_QUESTION_SUCCESS:

                    break;

                default:
                    break;
            }

            return false;
        }

    });
    @SuppressLint("InlinedApi") private void showProgDialog() {
        progress_dialog = null;
        try{
            if (Build.VERSION.SDK_INT >= 11 ) {
                progress_dialog = new ProgressDialog(ResentChat.this, AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(ResentChat.this);
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // hide progress
    private void hideProgDialog() {
        try{
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showRecentChat() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg =getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //////Log.e("Showrecentchatmesage>>>", response.toString());
                        JSONObject obj = null;
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jArray=obj.getJSONArray("show");
                            //////Log.e("Json_Array", ">>>>>" + jArray);
//                            if (obj.has("result_count")){
//                                result_count=obj.getString("result_count");
//                                //////Log.e("Result_Count","=="+result_count);
//                            }
                            ArrayList<RecentChatEntity> list=new ArrayList<RecentChatEntity>();
                            for (int i=0;i<jArray.length();i++){

                                JSONObject jsonObject=jArray.getJSONObject(i);
                                //////Log.e("Object", ">>>>" + jsonObject);
                                RecentChatEntity entity=new RecentChatEntity();

                                entity.setFull_name(jsonObject.getString("name"));
                                entity.setChat_id(jsonObject.getString("chat_id"));
                                entity.setFriend_id(jsonObject.getString("user_id"));
                                entity.setPicture(jsonObject.getString("picture"));
                                entity.setUnreade_count(jsonObject.getString("unreade_count"));
                                entity.setDate_added(jsonObject.getString("date_added"));
                                entity.setMessage(jsonObject.getString("message"));
                                entity.setTimee(jsonObject.getString("timee"));
                                entity.setProject_id(jsonObject.getString("login_status"));
                                entity.setProject_title(jsonObject.getString("is_friend"));



                                list.add(entity);
                            }
//                            if (page == 0) {



                                recent_chatlist.addAll(list);
                                //////Log.e("list",""+recent_chatlist.size());
                                ////////Log.e("list44555",""+project_list.toString());
                                try {

                                    if (recent_chatlist.size() > 0) {

                                        chat_list.setVisibility(View.VISIBLE);
                                        recentChat_adapter=new Recent_Chat_Adapter(ResentChat.this,recent_chatlist);
                                        chat_list.setAdapter(recentChat_adapter);

                                    } else {
                                        chat_list.setVisibility(View.GONE);
                                        error_msg.setVisibility(View.VISIBLE);
                                        error_msg.setText("لا دردشات المتاحة");
                                    }

//                                    curSize = recent_chatlist.size();
//                                    recentChat_adapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                            } else {
//
//                                curSize = recent_chatlist.size();
//                                recent_chatlist.addAll(curSize, list);
//                                recentChat_adapter.notifyItemInserted(curSize);
//                                recentChat_adapter.notifyItemRangeChanged(curSize, recent_chatlist.size());
//                                recentChat_adapter.notifyDataSetChanged();
//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        Toast.makeText(ResentChat.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Showrecentchatmesage");
                params.put("user_id", ssp.getUserId(ResentChat.this));
//                params.put("page",String.valueOf(page));
                //////Log.e("Showrecentchatmesage", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


}
