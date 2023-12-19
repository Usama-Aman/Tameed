package tameed.com.tameed.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Notification_Adapter;
import tameed.com.tameed.Entity.Notification_entity;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 16-01-2018.
 */

public class NotificationFragment extends Fragment {
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    TextView header_txt, clear_notification, error;
    ImageView header_back;
    String result_count;
    ArrayList<Notification_entity> Notification_detail = new ArrayList<>();
    SaveSharedPrefernces ssp;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Notification_Adapter notification_adapter;
    int page = 0, curSize;
    int firstVisibleItem, visibleItemCount, totalItemCount = 0;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "", tag_string_req = "string_req", notification_id, message;
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
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private String TAG = "Notification";
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        View v = inflater.inflate(R.layout.notification, container, false);

        ssp = new SaveSharedPrefernces();
        //Log.e(TAG, "****************************");
        header_txt = (TextView) v.findViewById(R.id.txt_header);
        header_txt.setText("الإشعارات");
        header_txt.setVisibility(View.VISIBLE);
        header_back = (ImageView) v.findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MainActivity.class));
            }
        });

        error = v.findViewById(R.id.error);

        clear_notification = (TextView) v.findViewById(R.id.notification_clear_all);
        clear_notification.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) v.findViewById(R.id.notification_recycle);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //TEMP
        /*recyclerView = (RecyclerView) findViewById(R.id.notification_recycle);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);*/


   /*     notification_adapter=new Notification_Adapter(this,Notification_detail);
        recyclerView.setAdapter(notification_adapter);*/

        clear_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(mContext);
                }

                alert.setMessage("مسح الكل");


                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Notification_detail.clear();
                        notification_adapter.notifyDataSetChanged();
                        notification_clear_all();


                    }
                });

                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
        show_notification();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                //  ////Log.e("visibleItemCount", "" + visibleItemCount);
                ////Log.e("totalItemCount", "" + totalItemCount);
                //    ////Log.e("firstVisibleItem", "" + firstVisibleItem);
                ////Log.e("LogEntity1.size()", "" + Notification_detail.size());
                //    ////Log.e("result_count", result_count);
                ////Log.e("page", "" + page);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        ////Log.e("11111", "11111");
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    ////Log.e("page1", "" + page);
                    ////Log.e("222222", "22222");
                    try {
                        if (Notification_detail.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);
                            ////Log.e("33333", "33333");
                            page = page + 1;

                            show_notification();


                        } else {
                            ////Log.e("44444", "444444");
//                            Toast.makeText(mContext,
//                                    "End of list", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });

        return v;
    }

    private void show_notification() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }
                            // msg=obj.getString("msg");
                            result_count = obj.getString("result_count");
                            ArrayList<Notification_entity> list = new ArrayList<Notification_entity>();

                            JSONArray jsonArray = obj.getJSONArray("all_notifications");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Notification_entity notification_entity = new Notification_entity();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                notification_id = jsonObject.getString("notification_id");

                                ssp.setNotification_id(mContext, notification_id);

                                notification_entity.setNotification_message(jsonObject.getString("notification_message"));

                                String date = jsonObject.getString("added_date");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt = formatter.parse(date);
                                date = formatter.format(dt);


                                notification_entity.setAdded_date(date);
                                list.add(notification_entity);

                            }
                         /*   Notification_detail.addAll(list);
                            ////Log.e("country_list444",list.toString());

                            if(Notification_detail.size()>0)
                            {
                                notification_adapter = new Notification_Adapter(mContext,Notification_detail);
                                recyclerView.setAdapter(notification_adapter);
                            } else {
                                recyclerView.setVisibility(View.GONE);
//                                error.setVisibility(View.VISIBLE);
                            }*/

                            if (page == 0) {
                                ////Log.e(TAG,"page>>>0>>"+page);
                                Notification_detail.addAll(list);
                                if (Notification_detail.size() > 0) {
                                    error.setVisibility(View.GONE);
                                    //my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    //my_swipe_refresh_layout2.setRefreshing(false);
                                    linearLayoutManager = new LinearLayoutManager(mContext);
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    notification_adapter = new Notification_Adapter(mContext, Notification_detail);
                                    recyclerView.setAdapter(notification_adapter);
                                    clear_notification.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    //my_swipe_refresh_layout2.setVisibility(View.GONE);
                                    error.setVisibility(View.VISIBLE);
                                    curSize = Notification_detail.size();
//                                    notification_adapter.notifyDataSetChanged();
                                    clear_notification.setVisibility(View.GONE);
                                }
                            } else {
                                ////Log.e(TAG,"page>>>1>>"+curSize);
                                curSize = Notification_detail.size();
                                Notification_detail.addAll(curSize, list);
                                notification_adapter.notifyItemInserted(curSize);
                                notification_adapter.notifyItemRangeChanged(curSize, Notification_detail.size());
                                clear_notification.setVisibility(View.VISIBLE);
                                notification_adapter.notifyDataSetChanged();
                            }


//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(mContext, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mContext, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(mContext, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mContext, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Shownotifications");
                params.put("user_id", ssp.getUserId(mContext));
                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    public void notification_clear_all() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");
                            ////Log.e("response", response.toString());

                            if (message.equals("No notification to clear")) {

                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(mContext);
                                }

                                alert.setMessage("No notification to clear");


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();

                            } else if (message.equals("Notification has been cleared successfully")) {


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(mContext);
                                }

                                alert.setMessage("تم حذف جميع الإشعارات");


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();


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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(mContext, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mContext, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(mContext, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("action", "Clearnotifications");
                params.put("delete_by", "all");
                params.put("user_id", ssp.getUserId(mContext));
                ////Log.e("notify clear all", params.toString());

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }


    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(mContext);
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide progress
    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}