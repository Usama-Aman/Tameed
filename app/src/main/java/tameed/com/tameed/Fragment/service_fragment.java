package tameed.com.tameed.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Category_Adapter;
import tameed.com.tameed.Adapter.FilterAdapter;
import tameed.com.tameed.Adapter.Service_Adapter;
import tameed.com.tameed.Adapter.Sub_category_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Service_Category_Entity;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.P_room;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Abstract_global;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.Concreate_global;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by root on 24/1/18.
 */

public class service_fragment extends Fragment {
    TextView header_txt;
    ImageView header_back,cat_check,sub_cat_check,img_back2;
    Boolean bool=true;
    int flagvalue;
    String user_id,calling_code,password,gmt_value,device_id,service_id,msg
            ,verify_code,is_verified,profile_pic_2xthumb_url,profile_pic_3xthumb_url
           , profile_cover_pic_1xthumb_url,profile_cover_pic_2xthumb_url,profile_cover_pic_3xthumb_url,
          online_offline_status
    ,user_device_type,user_bank_account_number,user_bank_iban_number;
    String  user_service_id,main_category_id,sub_service_id,category_name,service_name,sub_service_name;
    SaveSharedPrefernces ssp;
    FilterAdapter category_service;
    ConstraintLayout constraint_service_type,constrain_sub_cat,linearLayout2;
    ArrayList<String> Category_detail=new ArrayList<>();
    ArrayList<String >Sub_category_detail=new ArrayList<>();
    ArrayList<Service_Category_Entity>service_list=new ArrayList<>();
    Sub_category_Adapter sub_category_adapter;
    Category_Adapter category_adapter;

    String name,email,mobile_number,combine_mobile,latitude,longitude,location,login_status,payment_preference,active_status,user_type,profile_pic_thumb_url,added_date,push_notification,online_ofiline_status,profile_pic_url,description,review_count,review_rating,order_count,country,mobile_visible,city_to_cover,distance,user_bank_name,user_bank_acct_num,user_iban_num;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    Service_Adapter service_adapter;
    Button button2;
    RecyclerView my_service_recycle;
    Abstract_global abstract_global;
    Activity act;

    private String TAG = "service_fragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
        Log.e(TAG,"****************************");
        View rootView = inflater.inflate(R.layout.my_services, container, false);
        act=getActivity();
        abstract_global = new Concreate_global();
        ssp=new SaveSharedPrefernces();
        button2=rootView.findViewById(R.id.button2);
        /*helper.service_id.clear();
        helper.service_list.clear();*/
        header_txt = (TextView) rootView.findViewById(R.id.txt_header);
        header_txt.setText("خدماتي");
        my_service_recycle=rootView.findViewById(R.id.my_service_recycle);
       // header_back = (ImageView) rootView.findViewById(R.id.header_back);
        img_back2=rootView.findViewById(R.id.img_back2);
        img_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(getActivity(),MainActivity.class);
               i.putExtra("flag",18);
               startActivity(i);
            }
        });
        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayout2=rootView.findViewById(R.id.linearLayout2);


        my_service_recycle.setLayoutManager(linearLayoutManager);
        Log.e("JSON", "====" + helper.home);
        Log.e("service_status", "====" + helper.service_status);
        Log.e("setting_service", "====" + helper.setting_service);
        Log.e("tab", "====" + helper.tab);
        if(helper.service_status.equals("1"))
        {
            button2.setText("بحث عن الخدمة");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(helper.service_id.isEmpty())
                    {
                        final AlertDialog.Builder alert;
                        if (Build.VERSION.SDK_INT >= 11) {
                            alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                        } else {
                            alert = new AlertDialog.Builder(getActivity());
                        }

                        alert.setMessage("اختر خدمة واحدة على الأقل للبحث");
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
                    else
                    {


//                    helper.service_id.clear();
                        helper.search_by_service="";
                        helper.filter_pro="";
                        Intent i=new Intent(getActivity(),MainActivity.class);
                        helper.search_by_service="1";
                        helper.home=1;
                        helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",");
                        startActivity(i);
                        flagvalue = 1;
                        makeStringRequest();
                    }
                }
            });
        }
/*
          if( helper.setting_service.equals("5")) {
            //Log.e("fragment","2222");
                button2.setText("CONTINUE");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(helper.service_id.isEmpty())
                    {
                        final AlertDialog.Builder alert;
                        if (Build.VERSION.SDK_INT >= 11) {
                            alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                        } else {
                            alert = new AlertDialog.Builder(getActivity());
                        }

                        alert.setMessage("Please select atleat one category");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    }

                    else if (ssp.getUser_type(getActivity()).equals("Provider")) {

                        final AlertDialog.Builder alert;
                        if (Build.VERSION.SDK_INT >= 11) {
                            alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                        } else {
                            alert = new AlertDialog.Builder(getActivity());
                        }

                        alert.setMessage("Do you really want to Downgrade as Customer?");


                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flagvalue = 3;

                                makeStringRequest();

                                dialog.dismiss();
                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });
                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();

                    } else if (ssp.getUser_type(getActivity()).equals("Customer")) {
                        if (helper.service_id.size() > 0) {
                            flagvalue = 2;
                            makeStringRequest();
                        } else {
                            final AlertDialog.Builder alert;
                            if (Build.VERSION.SDK_INT >= 11) {
                                alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                            } else {
                                alert = new AlertDialog.Builder(getActivity());
                            }

                            alert.setMessage("Please select atleast one service to search Service Providers");


                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                            Dialog dialog = alert.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();
                        }
                    } else {

                    }
                }

            });
        }
*/

        else if(helper.home==5)
        {
            button2.setText("متابعة");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flagvalue=5;
                    makeStringRequest();
                }
            });

        }

        flagvalue=0;
        makeStringRequest();

        return rootView;
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

    private void makeStringRequest() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj=null;

                        if(flagvalue==0) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    //Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                JSONArray jsonArray = obj.getJSONArray("categories");
                                //Log.e("JSON_ARRAY", "===" + jsonArray);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ArrayList<Service_Category_Entity.Services_Entity> list = new ArrayList<>();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    //Log.e("JSON_OBJECT", "===" + object);
                                    Service_Category_Entity entity = new Service_Category_Entity();
                                    entity.setCategory_id(object.getString("category_id"));
                                    entity.setCategory_name(object.getString("category_name"));
                                    entity.setAdded_date(object.getString("added_date"));
                                    entity.setIs_enable(object.getString("is_enable"));
                                    entity.setAdded_date(object.getString("added_date"));
                                    entity.setIs_subscribed(object.getString("is_subscribed"));

                                    JSONArray jsonArray1 = object.getJSONArray("services");
                                    //Log.e("JSON_INNER_ARRAY", "====" + jsonArray1);
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object1 = jsonArray1.getJSONObject(j);
                                        Service_Category_Entity.Services_Entity services_entity = new Service_Category_Entity.Services_Entity();
                                        services_entity.setService_id(object1.getString("service_id"));
                                        services_entity.setCategory_id(object1.getString("category_id"));
                                        services_entity.setService_name(object1.getString("service_name"));
                                        services_entity.setIs_enable(object1.getString("is_enable"));
                                        services_entity.setType(object1.getString("service_name"));
                                        services_entity.setAdded_date(object1.getString("added_date"));
                                        if (helper.service_id.contains(object1.getString("service_id"))) {
                                            services_entity.setNo("2");
                                        } else {
                                            services_entity.setNo("1");
                                        }
                                        list.add(services_entity);
                                    }
                                    entity.setNo("1");
                                    entity.setList(list);

                                    service_list.add(entity);
                                }

                                if (service_list.size() > 0) {
//                                    category_service = new Category_Service(getActivity(), service_list);
//                                    my_service_recycle.setAdapter(category_service);

                                    category_service=new FilterAdapter(getActivity(),service_list);
                                    my_service_recycle.setAdapter(category_service);

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
                        else if(flagvalue==1)
                        {

                                try {
                                    obj = new JSONObject(response.toString());
                                    int maxLogSize = 1000;
                                    for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                        int start1 = i * maxLogSize;
                                        int end = (i + 1) * maxLogSize;
                                        end = end > response.length() ? response.toString().length() : end;
                                        //Log.e("Json Data", response.toString().substring(start1, end));
                                    }

                                    //Log.e("Service",response);

                                    msg=obj.getString("msg");

                                    if(obj.has("user_details")) {

                                        JSONObject jarray = obj.getJSONObject("user_details");
                                        name = jarray.getString("name");
                                        calling_code = jarray.getString("calling_code");
                                        user_device_type = jarray.getString("user_device_type");


                                        email = jarray.getString("email_address");
                                        mobile_number = jarray.getString("mobile_number");
                                        combine_mobile = jarray.getString("combine_mobile");
                                        latitude = jarray.getString("latitude");
                                        longitude = jarray.getString("longitude");
                                        location = jarray.getString("location");
                                        login_status = jarray.getString("login_status");
                                        payment_preference = jarray.getString("payment_preference");
                                        active_status = jarray.getString("active_status");
                                        user_type = jarray.getString("user_type");
                                        profile_pic_thumb_url = jarray.getString("profile_pic_url");
                                        added_date = jarray.getString("added_date");
                                        push_notification = jarray.getString("push_notification");
                                        online_ofiline_status = jarray.getString("online_offline_status");
                                        profile_pic_url = jarray.getString("profile_pic_url");
                                        description = jarray.getString("description");
                                        review_count = jarray.getString("review_count");
                                        review_rating = jarray.getString("review_rating");
                                        order_count = jarray.getString("order_count");
                                        country = jarray.getString("country");
                                        mobile_visible = jarray.getString("mobile_visible");
                                        city_to_cover = jarray.getString("city_to_cover");
                                        distance = jarray.getString("distance");
                                        user_bank_name = jarray.getString("user_bank_name");
                                        user_bank_acct_num = jarray.getString("user_bank_account_number");
                                        user_iban_num = jarray.getString("user_bank_iban_number");

                                        JSONArray jsonArray = jarray.getJSONArray("services");
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                            user_service_id = jsonObject1.getString("user_service_id");

                                            main_category_id = jsonObject1.getString("main_category_id");
                                            service_id = jsonObject1.getString("service_id");
                                            added_date = jsonObject1.getString("added_date");
                                            category_name = jsonObject1.getString("category_name");
                                            service_name = jsonObject1.getString("service_name");

                                        }
                                    }

                                        if(msg.equals("Services updated successfully"))
                                        {
                                            final AlertDialog.Builder alert;
                                            if (Build.VERSION.SDK_INT >= 11) {
                                                alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                            } else {
                                                alert = new AlertDialog.Builder(getActivity());
                                            }

                                            alert.setMessage("تم تحديث الخدمات بنجاح");


                                            alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    ssp.setName(getActivity(),name);
                                                    ssp.setEmail(getActivity(),email);
                                                    ssp.setMobile_number(getActivity(),mobile_number);
                                                    ssp.setCombine_Mobile_number(getActivity(),combine_mobile);
                                                    ssp.setLatitude(getActivity(),latitude);
                                                    ssp.setLongitude(getActivity(),longitude);
                                                    ssp.setLocation(getActivity(),location);
                                                    ssp.setLogin_status(getActivity(),login_status);
                                                    ssp.setPayment_preference(getActivity(),payment_preference);
                                                    ssp.setActive_status(getActivity(),active_status);
                                                    ssp.setUser_type(getActivity(),user_type);
                                                    ssp.setKEY_profile_pic_thumb_url(getActivity(),profile_pic_thumb_url);
                                                    ssp.setPush_notification(getActivity(),push_notification);
                                                    ssp.setOnline_offline_status(getActivity(),online_ofiline_status);
                                                    ssp.setDescription(getActivity(),description);
                                                    ssp.setAdded_date(getActivity(),added_date);
                                                    ssp.setReview_count(getActivity(),review_count);
                                                    ssp.setReview_rating(getActivity(),review_rating);
                                                    ssp.setOrder_count(getActivity(),order_count);
                                                    ssp.setCountry(getActivity(),country);
                                                    ssp.setMobile_visible(getActivity(),mobile_visible);
                                                    ssp.setCity_to_cover(getActivity(),city_to_cover);
                                                    ssp.setDistance(getActivity(),distance);
                                                    ssp.setUser_bank_name(getActivity(),user_bank_name);
                                                    ssp.setUser_bank_acct_num(getActivity(),user_bank_acct_num);
                                                    ssp.setUser_bank_iban_num(getActivity(),user_iban_num);
                                                    ssp.setKEY_profile_pic_thumb_url(getActivity(),profile_pic_thumb_url);
                                                    helper.tab=0;
                                                    helper.setting_service="";
                                                   if(helper.home==1)
                                                  {
                                                        //Log.e("home2222",""+String.valueOf(helper.home));
                                                        Intent intent=new Intent(getActivity(), MainActivity.class);
                                                        helper.setting_service="";

                                                        helper.p_room="";
                                                        getActivity().startActivity(intent);
                                                   }
//                                                   else {}
                                                    else if(helper.tab==1) {

                                                        helper.p_room="";
                                                        Intent intent = new Intent(getActivity(), P_room.class);
                                                        helper.tab = 1;
                                                        helper.setting_service="";
                                                        getActivity().startActivity(intent);
                                                    }
//                                                    else
//                                                    {}
                                                 /* else if(helper.home==9) {
                                                        helper.setting_service="";
                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        helper.setting=1;
//                                                        helper.p_room="";
//                                                        helper.home=0;
                                                        getActivity().startActivity(intent);
                                                    }*/
//                                                    else{}
                                                }
                                            });
                                            Dialog dialog = alert.create();
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.show();


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

                      /*  if(flagvalue==2) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    //Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                msg=obj.getString("msg");

                                if(obj.has("user_details")) {

                                    JSONObject jarray = obj.getJSONObject("user_details");
                                    name = jarray.getString("name");
                                    calling_code = jarray.getString("calling_code");
                                    user_device_type = jarray.getString("user_device_type");


                                    email = jarray.getString("email_address");
                                    mobile_number = jarray.getString("mobile_number");
                                    combine_mobile = jarray.getString("combine_mobile");
                                    latitude = jarray.getString("latitude");
                                    longitude = jarray.getString("longitude");
                                    location = jarray.getString("location");
                                    login_status = jarray.getString("login_status");
                                    payment_preference = jarray.getString("payment_preference");
                                    active_status = jarray.getString("active_status");
                                    user_type = jarray.getString("user_type");
                                    profile_pic_thumb_url = jarray.getString("profile_pic_url");
                                    added_date = jarray.getString("added_date");
                                    push_notification = jarray.getString("push_notification");
                                    online_ofiline_status = jarray.getString("online_offline_status");
                                    profile_pic_url = jarray.getString("profile_pic_url");
                                    description = jarray.getString("description");
                                    review_count = jarray.getString("review_count");
                                    review_rating = jarray.getString("review_rating");
                                    order_count = jarray.getString("order_count");
                                    country = jarray.getString("country");
                                    mobile_visible = jarray.getString("mobile_visible");
                                    city_to_cover = jarray.getString("city_to_cover");
                                    distance = jarray.getString("distance");
                                    user_bank_name = jarray.getString("user_bank_name");
                                    user_bank_acct_num = jarray.getString("user_bank_account_number");
                                    user_iban_num = jarray.getString("user_bank_iban_number");

                                    JSONArray jsonArray = jarray.getJSONArray("services");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                        user_service_id = jsonObject1.getString("user_service_id");

                                        main_category_id = jsonObject1.getString("main_category_id");
                                        service_id = jsonObject1.getString("service_id");
                                        added_date = jsonObject1.getString("added_date");
                                        category_name = jsonObject1.getString("category_name");
                                        service_name = jsonObject1.getString("service_name");

                                    }
                                }

                                if(msg.equals("Services updated successfully"))
                                {
                                    final AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(getActivity());
                                    }

                                    alert.setMessage("Services updated successfully");


                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ssp.setName(getActivity(),name);
                                            ssp.setEmail(getActivity(),email);
                                            ssp.setMobile_number(getActivity(),mobile_number);
                                            ssp.setCombine_Mobile_number(getActivity(),combine_mobile);
                                            ssp.setLatitude(getActivity(),latitude);
                                            ssp.setLongitude(getActivity(),longitude);
                                            ssp.setLocation(getActivity(),location);
                                            ssp.setLogin_status(getActivity(),login_status);
                                            ssp.setPayment_preference(getActivity(),payment_preference);
                                            ssp.setActive_status(getActivity(),active_status);
                                            ssp.setUser_type(getActivity(),user_type);
                                            ssp.setKEY_profile_pic_thumb_url(getActivity(),profile_pic_thumb_url);
                                            ssp.setPush_notification(getActivity(),push_notification);
                                            ssp.setOnline_offline_status(getActivity(),online_ofiline_status);
                                            ssp.setDescription(getActivity(),description);
                                            ssp.setAdded_date(getActivity(),added_date);
                                            ssp.setReview_count(getActivity(),review_count);
                                            ssp.setReview_rating(getActivity(),review_rating);
                                            ssp.setOrder_count(getActivity(),order_count);
                                            ssp.setCountry(getActivity(),country);
                                            ssp.setMobile_visible(getActivity(),mobile_visible);
                                            ssp.setCity_to_cover(getActivity(),city_to_cover);
                                            ssp.setDistance(getActivity(),distance);
                                            ssp.setUser_bank_name(getActivity(),user_bank_name);
                                            ssp.setUser_bank_acct_num(getActivity(),user_bank_acct_num);
                                            ssp.setUser_bank_iban_num(getActivity(),user_iban_num);
                                            ssp.setKEY_profile_pic_thumb_url(getActivity(),profile_pic_thumb_url);
                                        *//*    helper.tab=0;
                                            helper.setting_service="";
                                           if(helper.home ==5) {

                                                helper.p_room="";
                                                Intent intent = new Intent(getActivity(), P_room.class);
                                                helper.tab = 1;
                                                helper.setting_service="";
                                                getActivity().startActivity(intent);
                                            }
//
                                            else if(helper.home==9) {
                                                helper.setting_service="";*//*
                                                Intent intent = new Intent(getActivity(), My_services.class);

                                                helper.setting=1;
//                                                        helper.p_room="";
//                                                        helper.home=0;
                                                getActivity().startActivity(intent);

//                                                    else{}
                                        }
                                    });
                                    Dialog dialog = alert.create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();


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


                        if(flagvalue==3) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    //Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                //Log.e("Message6666",response);
                                msg=obj.getString("msg");

                                if(obj.has("user_details")) {

                                    JSONObject jarray = obj.getJSONObject("user_details");
                                    name = jarray.getString("name");
                                    calling_code = jarray.getString("calling_code");
                                    user_device_type = jarray.getString("user_device_type");


                                    email = jarray.getString("email_address");
                                    mobile_number = jarray.getString("mobile_number");
                                    combine_mobile = jarray.getString("combine_mobile");
                                    latitude = jarray.getString("latitude");
                                    longitude = jarray.getString("longitude");
                                    location = jarray.getString("location");
                                    login_status = jarray.getString("login_status");
                                    payment_preference = jarray.getString("payment_preference");
                                    active_status = jarray.getString("active_status");
                                    user_type = jarray.getString("user_type");
                                    profile_pic_thumb_url = jarray.getString("profile_pic_url");
                                    added_date = jarray.getString("added_date");
                                    push_notification = jarray.getString("push_notification");
                                    online_ofiline_status = jarray.getString("online_offline_status");
                                    profile_pic_url = jarray.getString("profile_pic_url");
                                    description = jarray.getString("description");
                                    review_count = jarray.getString("review_count");
                                    review_rating = jarray.getString("review_rating");
                                    order_count = jarray.getString("order_count");
                                    country = jarray.getString("country");
                                    mobile_visible = jarray.getString("mobile_visible");
                                    city_to_cover = jarray.getString("city_to_cover");
                                    distance = jarray.getString("distance");
                                    user_bank_name = jarray.getString("user_bank_name");
                                    user_bank_acct_num = jarray.getString("user_bank_account_number");
                                    user_iban_num = jarray.getString("user_bank_iban_number");

                                    JSONArray jsonArray = jarray.getJSONArray("services");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                        user_service_id = jsonObject1.getString("user_service_id");

                                        main_category_id = jsonObject1.getString("main_category_id");
                                        service_id = jsonObject1.getString("service_id");
                                        added_date = jsonObject1.getString("added_date");
                                        category_name = jsonObject1.getString("category_name");
                                        service_name = jsonObject1.getString("service_name");

                                    }
                                }

                                if(msg.equals("You have downgraded to Customer successfully"))
                                {
                                    final AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(getActivity());
                                    }

                                    alert.setMessage("You have downgraded to Customer successfully");


                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ssp.setName(getActivity(),name);
                                            ssp.setEmail(getActivity(),email);
                                            ssp.setMobile_number(getActivity(),mobile_number);
                                            ssp.setCombine_Mobile_number(getActivity(),combine_mobile);
                                            ssp.setLatitude(getActivity(),latitude);
                                            ssp.setLongitude(getActivity(),longitude);
                                            ssp.setLocation(getActivity(),location);
                                            ssp.setLogin_status(getActivity(),login_status);
                                            ssp.setPayment_preference(getActivity(),payment_preference);
                                            ssp.setActive_status(getActivity(),active_status);
                                            ssp.setUser_type(getActivity(),user_type);
                                            ssp.setKEY_profile_pic_thumb_url(getActivity(),profile_pic_thumb_url);
                                            ssp.setPush_notification(getActivity(),push_notification);
                                            ssp.setOnline_offline_status(getActivity(),online_ofiline_status);
                                            ssp.setDescription(getActivity(),description);
                                            ssp.setAdded_date(getActivity(),added_date);
                                            ssp.setReview_count(getActivity(),review_count);
                                            ssp.setReview_rating(getActivity(),review_rating);
                                            ssp.setOrder_count(getActivity(),order_count);
                                            ssp.setCountry(getActivity(),country);
                                            ssp.setMobile_visible(getActivity(),mobile_visible);
                                            ssp.setCity_to_cover(getActivity(),city_to_cover);
                                            ssp.setDistance(getActivity(),distance);
                                            ssp.setUser_bank_name(getActivity(),user_bank_name);
                                            ssp.setUser_bank_acct_num(getActivity(),user_bank_acct_num);
                                            ssp.setUser_bank_iban_num(getActivity(),user_iban_num);
                                            ssp.setKEY_profile_pic_thumb_url(getActivity(),profile_pic_thumb_url);
                                         *//*   helper.tab=0;
                                            helper.setting_service="";
                                            if(helper.home==1)
                                            {
                                                //Log.e("home2222",""+String.valueOf(helper.home));
                                                Intent intent=new Intent(getActivity(), MainActivity.class);
                                                helper.setting_service="";
                                                helper.p_room="";
                                                helper.setting_service="5";
                                                getActivity().startActivity(intent);
                                            }
//                                                   else {}
                                            else if(helper.home ==5) {

                                                helper.p_room="";
                                                Intent intent = new Intent(getActivity(), P_room.class);
                                                helper.tab = 1;
                                                helper.setting_service="";
                                                getActivity().startActivity(intent);
                                            }
//                                                    else
//                                                    {}
                                            else if(helper.home==9) {*//*
                                               // helper.setting_service="";
                                                Intent intent = new Intent(getActivity(), My_services.class);

                                               // helper.setting=1;
//                                                        helper.p_room="";
//                                                        helper.home=0;
                                                getActivity().startActivity(intent);
                                            }
//                                                    else{}

                                    });
                                    Dialog dialog = alert.create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();


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


*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(getActivity(),getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

                            //TODO

                        }
                        else if (error instanceof ParseError) {


                            //TODO
                        }
                        else if (error instanceof ParseError) {


                            //TODO
                        }


                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                if(flagvalue==0) {
                    // params.put("action", "Allcategorieswithservices");
                    params.put("action", "Categoryhierarchy");
                    //Log.e("params", String.valueOf(params));
                }
                else if(flagvalue==1)
                {
                    params.put("action", "Registerassp");
                    params.put("user_id",ssp.getUserId(getActivity()));
                    params.put("services", helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    //Log.e("params",String.valueOf(params));
                }
               /* else if(flagvalue==2)
                {
                    params.put("action", "Registerassp");
                    params.put("user_id",ssp.getUserId(getActivity()));
                    params.put("services", helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    //Log.e("params",String.valueOf(params));
                }
                else if(flagvalue==3)
                {
                    params.put("action","Unregistertocustomer");
                    params.put("user_id",ssp.getUserId(getActivity()));
                   // params.put("services",helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    //Log.e("params",String.valueOf(params));
                }
*/

//                    params.put("action", "Filterproviders");
//                    params.put("manufacture_id", helper.manufacture_id);
//                    params.put("car_type_id", helper.car_type_id);
//                    params.put("car_year",helper.car_year);
//                    params.put("latitude",lat);
//                    params.put("longitude",lang);
//                    params.put("series_id", helper.series_id);
//                    params.put("model_id", helper.model_id);
//                    params.put("brand_id", helper.brand_id);
//                   params.put("distance",String.valueOf(seekBarProgress));
//                params.put("page","0");
               /* params.put("sub_categories_ids", helper.filter_subcategoryid.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));

                //Log.e("PARAMS", "" + params.toString());
                //Log.e("PARAMS SUBCATEGORY",""+helper.filter_subcategoryid.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));*/

//                latitude , longitude (both required) , distance , manufacture_id , brand_id , car_type_id , car_year , series_id , model_id ,
//                sub_categories_ids (selected sub categories as comma separate) , page (All optional)

                return params;
            }

//            latitude , longitude (Both required) , distance , page (both optional)

        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @SuppressLint("InlinedApi") private void showProgDialog() {
        progress_dialog = null;
        try{
            if (Build.VERSION.SDK_INT >= 11 ) {
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(getActivity());
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}