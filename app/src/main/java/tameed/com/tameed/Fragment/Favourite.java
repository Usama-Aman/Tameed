package tameed.com.tameed.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Favourite_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Favourite_Entity;
import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 18-01-2018.
 */

public class Favourite extends AppCompatActivity {
    ImageView header_back;
    TextView header_txt;
    RecyclerView recyclerView;
    SaveSharedPrefernces ssp;
    int page = 0, curSize;
    String provider_id;
    TextView error2;
    HashMap<String, JSONArray> servicemap;
    public static SwipeRefreshLayout my_swipe_refresh_layout3;
    String result_count = "";
    Favourite_Adapter favourite_adapter;
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    LinearLayoutManager layoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount = 0;
    ArrayList<Favourite_Entity> favorite_list = new ArrayList<>();
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    private String TAG = "Favourite";

    ArrayList<String> favourite_detail = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        setContentView(R.layout.favourite);

        ssp = new SaveSharedPrefernces();
        my_swipe_refresh_layout3 = findViewById(R.id.my_swipe_refresh_layout3);

        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("المفضلة");

        error2 = findViewById(R.id.error2);

        //Log.e(TAG,"****************************");
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Favourite.this, MainActivity.class));

                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.favourite_recycle);
        layoutManager = new LinearLayoutManager(Favourite.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        my_swipe_refresh_layout3.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);

        my_swipe_refresh_layout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                ////Log.e("refreshh", "333" + refresh);
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                favorite_list.clear();
                //customerorderlist_orderdetail_second.clear();
                //list.clear();

                makeStringReq();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                //  ////Log.e("visibleItemCount", "" + visibleItemCount);
                ////Log.e("totalItemCount", "" + totalItemCount);
                //    ////Log.e("firstVisibleItem", "" + firstVisibleItem);
                ////Log.e("LogEntity1.size()", "" + favorite_list.size());
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
                        if (favorite_list.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);
                            ////Log.e("33333", "33333");
                            page = page + 1;

//                            flagvalue=0;
//                            makeStringReq();

                            if (!helper.sort_type.equals("")) {
                                makeStringReq();
                            } else {
                                makeStringReq();
                            }


                        } else {
                            ////Log.e("44444", "444444");
//                            Toast.makeText(Favourite.this,
//                                    getResources().getString(R.string.end_of_list), Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });

        makeStringReq();
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


    private void makeStringReq() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        servicemap = new HashMap<>();

                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }

                            ////Log.e("Message", "<<>>" + response);


                            result_count = obj.getString("result_count");

                            JSONArray jsonArray = obj.getJSONArray("providers");
                            ArrayList<Favourite_Entity> list = new ArrayList<Favourite_Entity>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    ArrayList<Provider_List_Entity.Service> all_service_list = new ArrayList<>();

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    helper.service_list.clear();
                                    ;
                                    helper.category_list.clear();
                                    helper.category_idlist.clear();
                                    helper.service_idlist.clear();

                                    Favourite_Entity provider_list_entity = new Favourite_Entity();
                                    provider_list_entity.setUser_id(jsonObject.getString("user_id"));
                                    provider_id = jsonObject.getString("user_id");
                                    ssp.setProvider_id(Favourite.this, provider_id);
                                    provider_list_entity.setName(jsonObject.getString("name"));
                                    provider_list_entity.setEmail_address(jsonObject.getString("email_address"));
                                    provider_list_entity.setCalling_code(jsonObject.getString("calling_code"));
                                    provider_list_entity.setMobile_number(jsonObject.getString("mobile_number"));
                                    provider_list_entity.setCombine_mobile(jsonObject.getString("combine_mobile"));
                                    provider_list_entity.setPassword(jsonObject.getString("password"));
                                    provider_list_entity.setGmt_value(jsonObject.getString("gmt_value"));
                                    provider_list_entity.setLatitude(jsonObject.getString("latitude"));
                                    provider_list_entity.setLongitude(jsonObject.getString("longitude"));
                                    provider_list_entity.setLocation(jsonObject.getString("location"));
                                    provider_list_entity.setPayment_preference(jsonObject.getString("payment_preference"));
                                    provider_list_entity.setDevice_id(jsonObject.getString("device_id"));

                                    provider_list_entity.setActive_status(jsonObject.getString("active_status"));
                                    provider_list_entity.setVerify_code(jsonObject.getString("verify_code"));
                                    provider_list_entity.setIs_verified(jsonObject.getString("is_verified"));
                                    provider_list_entity.setLogin_status(jsonObject.getString("login_status"));
                                    provider_list_entity.setUser_type(jsonObject.getString("user_type"));
                                    provider_list_entity.setProfile_pic_thumb_url(jsonObject.getString("profile_pic_thumb_url"));
                                    provider_list_entity.setProfile_pic_2xthumb_url(jsonObject.getString("profile_pic_2xthumb_url"));
                                    provider_list_entity.setProfile_pic_3xthumb_url(jsonObject.getString("profile_pic_3xthumb_url"));
                                    provider_list_entity.setProfile_cover_pic_1xthumb_url(jsonObject.getString("profile_cover_pic_1xthumb_url"));
                                    provider_list_entity.setProfile_cover_pic_2xthumb_url(jsonObject.getString("profile_cover_pic_2xthumb_url"));
                                    provider_list_entity.setProfile_cover_pic_3xthumb_url(jsonObject.getString("profile_cover_pic_3xthumb_url"));
                                    provider_list_entity.setPush_notification(jsonObject.getString("push_notification"));
                                    provider_list_entity.setOnline_offline_status(jsonObject.getString("online_offline_status"));
                                    provider_list_entity.setProfile_pic_url(jsonObject.getString("profile_pic_url"));
                                    provider_list_entity.setDescription(jsonObject.getString("description"));
                                    provider_list_entity.setReview_count(jsonObject.getString("review_count"));
                                    provider_list_entity.setReview_rating(jsonObject.getString("review_rating"));
                                    provider_list_entity.setOrder_count(jsonObject.getString("order_count"));
                                    provider_list_entity.setCountry(jsonObject.getString("country"));
                                    provider_list_entity.setUser_device_type(jsonObject.getString("user_device_type"));
                                    provider_list_entity.setMobile_visible(jsonObject.getString("mobile_visible"));
                                    provider_list_entity.setCity_to_cover(jsonObject.getString("city_to_cover"));
                                    provider_list_entity.setDistance(jsonObject.getString("distance"));
                                    provider_list_entity.setUser_bank_name(jsonObject.getString("user_bank_name"));
                                    provider_list_entity.setUser_bank_account_number(jsonObject.getString("user_bank_account_number"));
                                    provider_list_entity.setUser_bank_iban_number(jsonObject.getString("user_bank_iban_number"));
                                    //provider_list_entity.setIs_favourite(jsonObject.getString("is_favourite"));
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("services");
                                    ArrayList<String> service_name_list = new ArrayList<>();
                                    ArrayList<String> category_name_list = new ArrayList<>();
                                    ArrayList<String> service_id_list = new ArrayList<>();
                                    ArrayList<String> category_id_list = new ArrayList<>();
                                    //servicemap.put(jsonObject.getString("user_id"), jsonArray1);
                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        JSONObject json = jsonArray1.getJSONObject(j);
                                        Provider_List_Entity.Service service = new Provider_List_Entity.Service();
                                        service.setUser_service_id(json.getString("user_service_id"));
                                        //service.setSub_service_id(json.getString("sub_service_id"));
                                        service.setMain_category_id(json.getString("main_category_id"));
                                        category_id_list.add(json.getString("main_category_id"));
                                        service.setService_id(json.getString("service_id"));
                                        service_id_list.add(json.getString("service_id"));
                                        service.setAdded_date(json.getString("added_date"));
                                        service.setCategory_name(json.getString("category_name"));
                                        category_name_list.add(json.getString("category_name"));
                                        service.setService_name(json.getString("service_name"));
                                        service_name_list.add(json.getString("service_name"));
                                        //service.setSub_service_name(json.getString("sub_service_name"));
                                        all_service_list.add(service);

                                           /* JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            provider_list_entity.setUser_service_id(jsonObject1.getString("user_service_id"));
                                           // provider_list_entity.setSub_service_id(jsonObject1.getString("sub_service_id"));
                                            provider_list_entity.setMain_category_id(jsonObject1.getString("main_category_id"));
                                           // service_id_list.add(jsonObject1.getString("sub_service_id"));
                                            provider_list_entity.setService_id_list(service_id_list);
                                            provider_list_entity.setAdded_date(jsonObject1.getString("added_date"));
                                            category_name_list.add(jsonObject1.getString("category_name"));
                                            provider_list_entity.setCategory_name_list(category_name_list);
                                            service_name_list.add(jsonObject1.getString("service_name"));
                                            provider_list_entity.setService_name_list(service_name_list);*/
                                        // provider_list_entity.setSub_service_name(jsonObject1.getString("sub_service_name"));
                                    }
                                    provider_list_entity.setCategory_id_list(category_id_list);
                                    provider_list_entity.setCategory_name_list(category_name_list);
                                    provider_list_entity.setService_id_list(service_id_list);
                                    provider_list_entity.setService_name_list(service_name_list);
                                    provider_list_entity.setAll_service_list(all_service_list);
                                    list.add(provider_list_entity);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            if (page == 0) {
                                favorite_list.addAll(list);
                                if (favorite_list.size() > 0) {
                                    error2.setVisibility(View.GONE);
                                    my_swipe_refresh_layout3.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    my_swipe_refresh_layout3.setRefreshing(false);
                                    layoutManager = new LinearLayoutManager(Favourite.this);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(layoutManager);
                                    favourite_adapter = new Favourite_Adapter(Favourite.this, favorite_list, servicemap);
                                    recyclerView.setAdapter(favourite_adapter);
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    my_swipe_refresh_layout3.setVisibility(View.GONE);
                                    error2.setVisibility(View.VISIBLE);
                                    curSize = favorite_list.size();
                                    favourite_adapter.notifyDataSetChanged();
                                }
                            } else {

                                curSize = favorite_list.size();
                                favorite_list.addAll(curSize, list);
                                favourite_adapter.notifyItemInserted(curSize);
                                favourite_adapter.notifyItemRangeChanged(curSize, favorite_list.size());
                                favourite_adapter.notifyDataSetChanged();
                            }

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

                            Toast.makeText(Favourite.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Favourite.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Favourite.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Favourite.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Favouriteproviderslist");
                params.put("latitude", ssp.getLatitude(Favourite.this));
                params.put("longitude", ssp.getLongitude(Favourite.this));
                params.put("user_id", ssp.getUserId(Favourite.this));
                params.put("page", String.valueOf(page));
                ////Log.e("params", params.toString());


                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Favourite.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(Favourite.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Favourite.this);
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
