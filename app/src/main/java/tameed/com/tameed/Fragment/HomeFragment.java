package tameed.com.tameed.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.HomeAdapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.FilterActivity;
import tameed.com.tameed.P_room;
import tameed.com.tameed.R;
import tameed.com.tameed.Sort;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.GPSTracker;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import static tameed.com.tameed.MapView_Activity.RequestPermissionCode;

public class HomeFragment extends Fragment {
    HomeAdapter adapterhome;
    LinearLayoutManager layoutManager;
    Button direct, service_room, public_room;
    ConstraintLayout sort, filter;
    SaveSharedPrefernces ssp;
    LinearLayoutManager llm;
    RecyclerView recList;
    int page = 0, curSize;
    int flagvalue;
    public static SwipeRefreshLayout my_swipe_refresh_layout2;
    String result_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    String lat, lang;
    GPSTracker gpsTracker;
    String search_by_service;
    int firstVisibleItem, visibleItemCount, totalItemCount = 0;
    String user_id, name, email_address, provider_id, calling_code, mobile_number, combine_mobile, password, gmt_value, latitude, longitude, location, payment_preference,
            device_id, active_status, verify_code, is_verified, login_status, user_type, profile_pic_thumb_url, profile_pic_2xthumb_url, profile_pic_3xthumb_url,
            profile_cover_pic_1xthumb_url, profile_cover_pic_2xthumb_url, profile_cover_pic_3xthumb_url, push_notification, online_offline_status,
            profile_pic_url, description, review_count, review_rating, order_count, country, user_device_type, mobile_visible, city_to_cover, distance, user_bank_name, user_bank_account_number, user_bank_iban_number, is_favourite,
            user_service_id, sub_service_id, main_category_id, service_id, added_date, category_name, service_name, sub_service_name;
    TextView error;
    private String TAG = "HomeFragment";
    ArrayList<Provider_List_Entity> provider_list = new ArrayList<>();
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    HashMap<String, JSONArray> servicemap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ssp = new SaveSharedPrefernces();
        ////Log.e(TAG, "****************************" + ssp.getUserId(getActivity()));
        recList = (RecyclerView) rootView.findViewById(R.id.home_recycler_view);
        error = rootView.findViewById(R.id.error);
        helper.sevice_fragment = "2";
        my_swipe_refresh_layout2 = rootView.findViewById(R.id.my_swipe_refresh_layout2);

        helper.service_list.clear();
        ;
        helper.category_list.clear();
        helper.category_idlist.clear();
        helper.service_idlist.clear();

        // getContactList();
   /*     if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContactList() ;
           *//* if(helper.phone_list.size()>0) {
                makeStringReq();
            }*//*
            //////////Log.e("33333","3333333");
        } else {
            requestLocationPermission();
            //////////Log.e("444444","444444");
        }

*/


        //ADD JAY CALL FOR UPDATE CHAT COUNT
        ////Log.e(TAG, "getUserId>>>>>>>>" + ssp.getUserId(getActivity()));
        if (TextUtils.isEmpty(ssp.getUserId(getActivity()))) {
            helper.login = false;
        } else {
            helper.login = true;
            CallApiShowbadgecount();
            ////Log.e(TAG, "CallApiShowbadgecount>>>>>>>>");
        }


        //////////Log.e("filter_service", String.valueOf(helper.service_id_list));
        //////////Log.e("filter_distance", String.valueOf(helper.filter_distance));

        direct = (Button) rootView.findViewById(R.id.btn_direct);
        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.tab = 3;
                Intent intent = new Intent(getActivity(), P_room.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        service_room = (Button) rootView.findViewById(R.id.services);
        service_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.tab = 2;
                Intent intent = new Intent(getActivity(), P_room.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        public_room = (Button) rootView.findViewById(R.id.public_room);
        public_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.tab = 1;
                Intent intent = new Intent(getActivity(), P_room.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        sort = (ConstraintLayout) rootView.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sort.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);

            }
        });
        gpsTracker = new GPSTracker(getActivity());
        lat = String.valueOf(gpsTracker.getLatitude());

        //////////Log.e("latitude", "" + lat);
        lang = String.valueOf(gpsTracker.getLongitude());


        filter = (ConstraintLayout) rootView.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        my_swipe_refresh_layout2.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);

        my_swipe_refresh_layout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                //////////Log.e("refreshh", "333" + refresh);
                recList.removeAllViews();
                recList.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                provider_list.clear();
                //customerorderlist_orderdetail_second.clear();
                //list.clear();

                makeStringReq();
            }
        });

        recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recList.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                //  //////////Log.e("visibleItemCount", "" + visibleItemCount);
                //////////Log.e("totalItemCount", "" + totalItemCount);
                //    //////////Log.e("firstVisibleItem", "" + firstVisibleItem);
                //////////Log.e("LogEntity1.size()", "" + provider_list.size());
                //    //////////Log.e("result_count", result_count);
                //////////Log.e("page", "" + page);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        //////////Log.e("11111", "11111");
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    //////////Log.e("page1", "" + page);
                    //////////Log.e("222222", "22222");
                    try {
                        if (provider_list.size() < Integer.parseInt(result_count)) {
                            //////////Log.e("page2", "" + page);
                            //////////Log.e("33333", "33333");
                            page = page + 1;

//                            flagvalue=0;
//                            makeStringReq();

                            if (!helper.sort_type.equals("")) {
                                flagvalue = 1;
                                makeStringReq();
                            } else if (helper.filter_pro.equals("1")) {
                                flagvalue = 2;
                                makeStringReq();
                            } else if (helper.search_by_service.equals("1")) {
                                flagvalue = 3;
                                makeStringReq();
                            } else {
                                flagvalue = 0;
                                makeStringReq();
                            }


                        } else {
                            //////////Log.e("44444", "444444");
//                            Toast.makeText(getActivity(),
//                                    getActivity().getResources().getString(R.string.end_of_list), Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });

        if (helper.search_by_service.equals("1")) {
            flagvalue = 3;
            makeStringReq();
        }
    /*    else if(!helper.city_to_id_list.equals("")&&!helper.sort_by.equals(""))
        {
            helper.search_by_service="";
            //////////Log.e("status_filter","fddfdfg");
            flagvalue=2;
            makeStringReq();
        }*/
        else if (helper.filter_pro.equals("1")) {
            // helper.search_by_service="";
            ////////////Log.e("status_filter","fddfdfg");
            flagvalue = 2;
            makeStringReq();
        }
       /* else if(!helper.filter_pro.equals("1")&&!helper.city_to_cover_list.equals("")&&!helper.sort_by.equals(""))
        {
            helper.search_by_service="";
            //////////Log.e("status_filter","fddfdfg");
            flagvalue=2;
            makeStringReq();
        }*/
        else if (!helper.sort_by.equals("")) {
            //helper.search_by_service="";
            //////////Log.e("status_filter", "fddfdfg");
            flagvalue = 1;
            makeStringReq();
        } else {

            if (AppController.isOnline(getActivity())) {
                // helper.search_by_service="";
                flagvalue = 0;
                makeStringReq();
            } else {
                AppController.showAlert(getActivity(),
                        getString(R.string.networkError_Message));
            }
        }


        EnableRuntimePermission();
        // ReadWriteContact();


        return rootView;
    }


    private void CallApiShowbadgecount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                //////////Log.e("Json Data", response.toString().substring(start1, end));
                            }
                            String unread_chat_count = obj.getString("unread_chat_count");
                            String online_offline_status = obj.getString("online_offline_status");
                            String pending_orders_count_as_customer = obj.getString("pending_orders_count_as_customer");
                            String total_order_badge_count = obj.getString("total_order_badge_count");

                            ////Log.e(TAG, "unread_chat_count >>>>>>>>>>" + unread_chat_count);
                            ////Log.e(TAG, "total_order_badge_count >>>>" + total_order_badge_count);


                            //ORDER VIEW COUNT
                            if (!TextUtils.isEmpty(total_order_badge_count) && !total_order_badge_count.equals("0")) {
                                helper.badge_txt.setVisibility(View.VISIBLE);
                                helper.badge_txt.setText(total_order_badge_count);
                            }

                            //SETTING VIEW COUNT
                            if (!TextUtils.isEmpty(unread_chat_count) && !unread_chat_count.equals("0")) {
                                helper.badge_txt_setting.setVisibility(View.VISIBLE);
                                helper.badge_txt_setting.setText(unread_chat_count);
                            } else {
                                helper.badge_txt_setting.setVisibility(View.GONE);
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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {



                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Showbadgecount");
                params.put("user_id", ssp.getUserId(getActivity()));
                //////////Log.e("params", params.toString());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
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

                        if (flagvalue == 0) {

                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    //////////Log.e("Json Data", response.toString().substring(start1, end));
                                }

                                //////////Log.e("Message", "<<>>" + response);


                                result_count = obj.getString("result_count");

                                JSONArray jsonArray = obj.getJSONArray("providers");
                                ArrayList<Provider_List_Entity> list = new ArrayList<Provider_List_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        ArrayList<Provider_List_Entity.Service> all_service_list = new ArrayList<>();

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Provider_List_Entity provider_list_entity = new Provider_List_Entity();
                                        provider_list_entity.setUser_id(jsonObject.getString("user_id"));
                                        provider_id = jsonObject.getString("user_id");
                                        ssp.setProvider_id(getActivity(), provider_id);
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
                                        provider_list_entity.setIs_favourite(jsonObject.getString("is_favourite"));
                                        JSONArray jsonArray1 = jsonObject.getJSONArray("services");
                                        servicemap.put(jsonObject.getString("user_id"), jsonArray1);
                                        ArrayList<String> service_name_list = new ArrayList<>();
                                        ArrayList<String> category_name_list = new ArrayList<>();
                                        ArrayList<String> service_id_list = new ArrayList<>();
                                        ArrayList<String> category_id_list = new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            JSONObject json = jsonArray1.getJSONObject(j);
                                            Provider_List_Entity.Service service = new Provider_List_Entity.Service();
                                            service.setUser_service_id(json.getString("user_service_id"));
                                            service.setSub_service_id(json.getString("sub_service_id"));
                                            service.setMain_category_id(json.getString("main_category_id"));
                                            category_id_list.add(json.getString("main_category_id"));
                                            service.setService_id(json.getString("service_id"));
                                            service_id_list.add(json.getString("service_id"));
                                            service.setAdded_date(json.getString("added_date"));
                                            service.setCategory_name(json.getString("category_name"));
                                            category_name_list.add(json.getString("category_name"));
                                            service.setService_name(json.getString("service_name"));
                                            service_name_list.add(json.getString("service_name"));
                                            service.setSub_service_name(json.getString("sub_service_name"));
                                            all_service_list.add(service);

                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            provider_list_entity.setUser_service_id(jsonObject1.getString("user_service_id"));
                                            provider_list_entity.setSub_service_id(jsonObject1.getString("sub_service_id"));
                                            provider_list_entity.setMain_category_id(jsonObject1.getString("main_category_id"));
                                            // service_id_list.add(jsonObject1.getString("sub_service_id"));
                                            //provider_list_entity.setService_id_list(service_id_list);
                                            provider_list_entity.setAdded_date(jsonObject1.getString("added_date"));
                                            //category_name_list.add(jsonObject1.getString("category_name"));
                                            //provider_list_entity.setCategory_name_list(category_name_list);
                                            // service_name_list.add(jsonObject1.getString("service_name"));
                                            provider_list_entity.setService_name_list(service_name_list);
                                            provider_list_entity.setSub_service_name(jsonObject1.getString("sub_service_name"));
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
                                    provider_list.addAll(list);
                                    if (provider_list.size() > 0) {
                                        error.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                        recList.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setRefreshing(false);
                                        layoutManager = new LinearLayoutManager(getActivity());
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recList.setLayoutManager(layoutManager);
                                        adapterhome = new HomeAdapter(getActivity(), provider_list, servicemap);
                                        recList.setAdapter(adapterhome);
                                    } else {
                                        recList.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        error.setVisibility(View.VISIBLE);
                                        curSize = provider_list.size();
                                        if (adapterhome == null)
                                            adapterhome = new HomeAdapter(getActivity(), provider_list, servicemap);
                                        else adapterhome.notifyDataSetChanged();
                                    }
                                } else {

                                    curSize = provider_list.size();
                                    provider_list.addAll(curSize, list);
                                    adapterhome.notifyItemInserted(curSize);
                                    adapterhome.notifyItemRangeChanged(curSize, provider_list.size());
                                    adapterhome.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (flagvalue == 1) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    //////////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                //////////Log.e("Message", "<<>>" + response);

                                result_count = obj.getString("result_count");

                                JSONArray jsonArray = obj.getJSONArray("providers");
                                ArrayList<Provider_List_Entity.Service> all_service_list = new ArrayList<>();
                                ArrayList<Provider_List_Entity> list = new ArrayList<Provider_List_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Provider_List_Entity provider_list_entity = new Provider_List_Entity();
                                        provider_list_entity.setUser_id(jsonObject.getString("user_id"));
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
                                        provider_list_entity.setIs_favourite(jsonObject.getString("is_favourite"));
                                        JSONArray jsonArray1 = jsonObject.getJSONArray("services");

                                        servicemap.put(jsonObject.getString("user_id"), jsonArray1);
                                        ArrayList<String> service_name_list = new ArrayList<>();
                                        ArrayList<String> category_name_list = new ArrayList<>();
                                        ArrayList<String> service_id_list = new ArrayList<>();
                                        ArrayList<String> category_id_list = new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            JSONObject json = jsonArray1.getJSONObject(j);
                                            Provider_List_Entity.Service service = new Provider_List_Entity.Service();
                                            service.setUser_service_id(json.getString("user_service_id"));
                                            service.setSub_service_id(json.getString("sub_service_id"));
                                            service.setMain_category_id(json.getString("main_category_id"));
                                            category_id_list.add(json.getString("main_category_id"));
                                            service.setService_id(json.getString("service_id"));
                                            service_id_list.add(json.getString("service_id"));
                                            service.setAdded_date(json.getString("added_date"));
                                            service.setCategory_name(json.getString("category_name"));
                                            category_name_list.add(json.getString("category_name"));
                                            service.setService_name(json.getString("service_name"));
                                            service_name_list.add(json.getString("service_name"));
                                            service.setSub_service_name(json.getString("sub_service_name"));
                                            all_service_list.add(service);

                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            provider_list_entity.setUser_service_id(jsonObject1.getString("user_service_id"));
                                            provider_list_entity.setSub_service_id(jsonObject1.getString("sub_service_id"));
                                            provider_list_entity.setMain_category_id(jsonObject1.getString("main_category_id"));
                                            // service_id_list.add(jsonObject1.getString("sub_service_id"));
                                            //provider_list_entity.setService_id_list(service_id_list);
                                            provider_list_entity.setAdded_date(jsonObject1.getString("added_date"));
                                            //category_name_list.add(jsonObject1.getString("category_name"));
                                            //provider_list_entity.setCategory_name_list(category_name_list);
                                            // service_name_list.add(jsonObject1.getString("service_name"));
                                            provider_list_entity.setService_name_list(service_name_list);
                                            provider_list_entity.setSub_service_name(jsonObject1.getString("sub_service_name"));
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
                                    provider_list.addAll(list);
                                    if (provider_list.size() > 0) {
                                        error.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                        recList.setVisibility(View.VISIBLE);
                                        layoutManager = new LinearLayoutManager(getActivity());
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recList.setLayoutManager(layoutManager);
                                        adapterhome = new HomeAdapter(getActivity(), provider_list, servicemap);
                                        recList.setAdapter(adapterhome);
                                    } else {
                                        error.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        recList.setVisibility(View.GONE);
                                        curSize = provider_list.size();
                                        adapterhome.notifyDataSetChanged();
                                    }
                                } else {

                                    curSize = provider_list.size();
                                    provider_list.addAll(curSize, list);
                                    adapterhome.notifyItemInserted(curSize);
                                    adapterhome.notifyItemRangeChanged(curSize, provider_list.size());
                                    adapterhome.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (flagvalue == 2) {
                            ////////Log.e("55555", "22222");
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                ////////Log.e("Message", "<<>>" + response);

                                result_count = obj.getString("result_count");
                                helper.city_to_cover_list = "";
                                //  helper.sort_by="";


                                JSONArray jsonArray = obj.getJSONArray("providers");
                                ArrayList<Provider_List_Entity> list = new ArrayList<Provider_List_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Provider_List_Entity provider_list_entity = new Provider_List_Entity();
                                        provider_list_entity.setUser_id(jsonObject.getString("user_id"));
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
                                        provider_list_entity.setIs_favourite(jsonObject.getString("is_favourite"));
                                        JSONArray jsonArray1 = jsonObject.getJSONArray("services");
                                        servicemap.put(jsonObject.getString("user_id"), jsonArray1);
                                        ArrayList<String> service_name_list = new ArrayList<>();
                                        ArrayList<String> category_name_list = new ArrayList<>();
                                        ArrayList<String> service_id_list = new ArrayList<>();
                                        ArrayList<String> category_id_list = new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            provider_list_entity.setUser_service_id(jsonObject1.getString("user_service_id"));
                                            //provider_list_entity.setSub_service_id(jsonObject1.getString("sub_service_id"));
                                            provider_list_entity.setMain_category_id(jsonObject1.getString("main_category_id"));
                                            service_id_list.add(jsonObject1.getString("service_id"));
                                            provider_list_entity.setService_id_list(service_id_list);
                                            provider_list_entity.setAdded_date(jsonObject1.getString("added_date"));
                                            category_name_list.add(jsonObject1.getString("category_name"));
                                            provider_list_entity.setCategory_name_list(category_name_list);
                                            service_name_list.add(jsonObject1.getString("service_name"));
                                            provider_list_entity.setService_name_list(service_name_list);

                                            category_id_list.add(jsonObject1.getString("main_category_id"));
                                            // provider_list_entity.setSub_service_name(jsonObject1.getString("sub_service_name"));
                                        }
                                        provider_list_entity.setCategory_id_list(category_id_list);
                                        provider_list_entity.setCategory_name_list(category_name_list);
                                        provider_list_entity.setService_id_list(service_id_list);
                                        provider_list_entity.setService_name_list(service_name_list);
                                        list.add(provider_list_entity);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (page == 0) {
                                    provider_list.addAll(list);
                                    if (provider_list.size() > 0) {
                                        error.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                        recList.setVisibility(View.VISIBLE);
                                        layoutManager = new LinearLayoutManager(getActivity());
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recList.setLayoutManager(layoutManager);
                                        adapterhome = new HomeAdapter(getActivity(), provider_list, servicemap);
                                        recList.setAdapter(adapterhome);
                                    } else {
                                        error.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        recList.setVisibility(View.GONE);
                                        curSize = provider_list.size();
                                        ////////Log.e("sssss", "filter");
                                        // adapterhome.notifyDataSetChanged();
                                    }
                                } else {

                                    curSize = provider_list.size();
                                    provider_list.addAll(curSize, list);
                                    adapterhome.notifyItemInserted(curSize);
                                    adapterhome.notifyItemRangeChanged(curSize, provider_list.size());
                                    adapterhome.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (flagvalue == 3) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                ////////Log.e("Message", "<<>>" + response);

                                result_count = obj.getString("result_count");
                                //helper.city_to_cover_list="";
                                // helper.sort_by="";


                                JSONArray jsonArray = obj.getJSONArray("providers");
                                ArrayList<Provider_List_Entity> list = new ArrayList<Provider_List_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Provider_List_Entity provider_list_entity = new Provider_List_Entity();
                                        provider_list_entity.setUser_id(jsonObject.getString("user_id"));
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
                                        provider_list_entity.setIs_favourite(jsonObject.getString("is_favourite"));
                                        JSONArray jsonArray1 = jsonObject.getJSONArray("services");
                                        servicemap.put(jsonObject.getString("user_id"), jsonArray1);
                                        ArrayList<String> service_name_list = new ArrayList<>();
                                        ArrayList<String> category_name_list = new ArrayList<>();
                                        ArrayList<String> service_id_list = new ArrayList<>();
                                        ArrayList<String> category_id_list = new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            provider_list_entity.setUser_service_id(jsonObject1.getString("user_service_id"));
                                            //       provider_list_entity.setSub_service_id(jsonObject1.getString("sub_service_id"));
                                            provider_list_entity.setMain_category_id(jsonObject1.getString("main_category_id"));
                                            service_id_list.add(jsonObject1.getString("service_id"));
                                            provider_list_entity.setService_id_list(service_id_list);
                                            provider_list_entity.setAdded_date(jsonObject1.getString("added_date"));
                                            category_name_list.add(jsonObject1.getString("category_name"));
                                            provider_list_entity.setCategory_name_list(category_name_list);
                                            service_name_list.add(jsonObject1.getString("service_name"));
                                            provider_list_entity.setService_name_list(service_name_list);


                                            category_id_list.add(jsonObject1.getString("main_category_id"));
                                            // provider_list_entity.setSub_service_name(jsonObject1.getString("sub_service_name"));
                                        }
                                        provider_list_entity.setCategory_id_list(category_id_list);
                                        provider_list_entity.setCategory_name_list(category_name_list);
                                        provider_list_entity.setService_id_list(service_id_list);
                                        provider_list_entity.setService_name_list(service_name_list);
                                        list.add(provider_list_entity);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (page == 0) {
                                    provider_list.addAll(list);
                                    if (provider_list.size() > 0) {
                                        error.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                        recList.setVisibility(View.VISIBLE);
                                        layoutManager = new LinearLayoutManager(getActivity());
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recList.setLayoutManager(layoutManager);
                                        adapterhome = new HomeAdapter(getActivity(), provider_list, servicemap);
                                        recList.setAdapter(adapterhome);
                                    } else {
                                        error.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        recList.setVisibility(View.GONE);
                                        curSize = provider_list.size();
                                        // adapterhome.notifyDataSetChanged();
                                    }
                                } else {

                                    curSize = provider_list.size();
                                    provider_list.addAll(curSize, list);
                                    adapterhome.notifyItemInserted(curSize);
                                    adapterhome.notifyItemRangeChanged(curSize, provider_list.size());
                                    adapterhome.notifyDataSetChanged();
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


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (flagvalue == 0) {
                    params.put("action", "Providerslist");
                    params.put("latitude", lat);
                    params.put("longitude", lang);
                    params.put("user_id", ssp.getUserId(getActivity()));
                    params.put("page", String.valueOf(page));
                    ////////Log.e("params", params.toString());
                } else if (flagvalue == 1) {
                    params.put("action", "Sortproviders");
                    params.put("latitude", lat);
                    params.put("longitude", lang);
                    params.put("user_id", ssp.getUserId(getActivity()));
                    params.put("sort_type", helper.sort_type);
                    params.put("sort_by", helper.sort_by);
                    params.put("page", String.valueOf(page));
                    ////////Log.e("params22222", params.toString());
                } else if (flagvalue == 2) {
            /*        if(!TextUtils.isEmpty(helper.sort_type)&&!TextUtils.isEmpty(helper.sort_by)) {
                        params.put("action", "Filterproviders");
                        params.put("latitude", lat);
                        params.put("longitude", lang);
                        params.put("user_id", ssp.getUserId(getActivity()));
                       *//* params.put("sort_type", helper.sort_type);
                        params.put("sort_by", helper.sort_by);*//*
                        params.put("page", String.valueOf(page));
                        params.put("distance", String.valueOf(helper.filter_distance));
                        params.put("city_to_cover", helper.city_to_id_list);
                        params.put("services", helper.service_id_list);
                        params.put("online_offline_status", helper.online_offline_status);
                        ////////Log.e("params333333", params.toString());
                    }*/

                    ////////Log.e("Filter_City", "Filter_City");
                    params.put("action", "Filterproviders");
                    params.put("latitude", lat);
                    params.put("longitude", lang);
                    params.put("user_id", ssp.getUserId(getActivity()));
                      /*  params.put("sort_type", helper.sort_type);
                        params.put("sort_by", helper.sort_by);*/
                    params.put("page", String.valueOf(page));
                    params.put("distance", String.valueOf(helper.filter_distance));
                    params.put("city_to_cover", helper.city_to_id_list);
                    params.put("services", helper.service_id_list);
                    params.put("online_offline_status", helper.online_offline_status);
                    ////////Log.e("params333333", params.toString());

                } else if (flagvalue == 3) {
                    // helper.search_by_service.equals("1")
                    ////////Log.e("search_by_service", "search_by_service");
                    params.put("action", "Searchbyservices");
                    params.put("latitude", lat);
                    params.put("longitude", lang);
                    params.put("user_id", ssp.getUserId(getActivity()));
                    params.put("page", String.valueOf(page));
                    params.put("services", helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    params.put("sort_type", helper.sort_type);
                    params.put("sort_by", helper.sort_by);
                    //////Log.e("params55555", params.toString());
                }


                return params;
            }

        };



        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
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

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(getActivity(), getResources().getString(R.string.allow_access), Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionCode);

        }
    }
}
