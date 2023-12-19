package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import tameed.com.tameed.Adapter.Sub_category_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Service_Category_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by dev on 18-01-2018.
 */

public class My_services extends AppCompatActivity {
    TextView header_txt;
    ImageView header_back,cat_check,sub_cat_check;
    Boolean bool=true;
    RecyclerView recyclerView,sub_recycleview;
    ConstraintLayout constraint_service_type,constrain_sub_cat;
    ArrayList<String> Category_detail=new ArrayList<>();
    ArrayList<String >Sub_category_detail=new ArrayList<>();
    Sub_category_Adapter sub_category_adapter;
    Category_Adapter category_adapter;
    FilterAdapter category_service;
    SaveSharedPrefernces ssp;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    ArrayList<Service_Category_Entity>service_list=new ArrayList<>();
    int flagvalue;
    Button button2;
    String p_room;
    String name,email,mobile_number,combine_mobile,latitude,longitude,location,login_status,payment_preference,active_status,user_type,profile_pic_thumb_url,added_date,push_notification,online_ofiline_status,profile_pic_url,description,review_count,review_rating,order_count,country,mobile_visible,city_to_cover,distance,user_bank_name,user_bank_acct_num,user_iban_num;

    String user_id,calling_code,password,gmt_value,device_id,service_id,msg
            ,verify_code,is_verified,profile_pic_2xthumb_url,profile_pic_3xthumb_url
            , profile_cover_pic_1xthumb_url,profile_cover_pic_2xthumb_url,profile_cover_pic_3xthumb_url,
            online_offline_status
            ,user_device_type,user_bank_account_number,user_bank_iban_number;
    String  user_service_id,main_category_id,sub_service_id,category_name,service_name,sub_service_name,setting_register;
    LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    RecyclerView my_service_recycle;
    private String TAG = "My_services";


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
        //Log.e(TAG,"****************************");
        setContentView(R.layout.myservice_home);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("خدماتي");
        ssp=new SaveSharedPrefernces();
        button2=findViewById(R.id.button2);
        my_service_recycle=findViewById(R.id.my_service_recycle);

        Intent i=getIntent();
        p_room=i.getStringExtra("p_room");

        Intent j=getIntent();
        setting_register=j.getStringExtra("setting_register");

        linearLayoutManager=new LinearLayoutManager(My_services.this);
        my_service_recycle.setLayoutManager(linearLayoutManager);

//        helper.service_id.clear();
//        helper.service_list.clear();

        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        flagvalue=0;
        makeStringRequest();



/*
        if(p_room.equals("1"))
        {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }*/




            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(p_room.equals("1"))
                    {

                        if (helper.service_id.isEmpty()) {
                            final AlertDialog.Builder alert;
                            if (Build.VERSION.SDK_INT >= 11) {
                                alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                            } else {
                                alert = new AlertDialog.Builder(My_services.this);
                            }

                            alert.setMessage("اختر خدمة واحدة على الأقل للبحث ");
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

                        else {

                            flagvalue = 2;
                            makeStringRequest();
                        }

                    }

                      else if(p_room.equals("2")) {

                     if (helper.service_id.isEmpty()&&ssp.getUser_type(My_services.this).equals("Provider")) {
                         /*if (helper.service_id.isEmpty()) {

                             final AlertDialog.Builder alert;
                             if (Build.VERSION.SDK_INT >= 11) {
                                 alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                             } else {
                                 alert = new AlertDialog.Builder(My_services.this);
                             }

                             alert.setMessage("Please select atleast one category");
                             alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                 }
                             });
                             Dialog dialog = alert.create();
                             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                             dialog.show();

                         }*/
                             final AlertDialog.Builder alert;
                             if (Build.VERSION.SDK_INT >= 11) {
                                 alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                             } else {
                                 alert = new AlertDialog.Builder(My_services.this);
                             }
                             alert.setMessage("هل ترغب بتحويل حسابك لعميل");
                             alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     flagvalue = 3;

                                     makeStringRequest();

                                     dialog.dismiss();
                                 }
                             });

                             alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {

                                     dialog.dismiss();

                                 }
                             });
                             Dialog dialog = alert.create();
                             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                             dialog.show();


                        }
                        else if(!helper.service_id.isEmpty())
                     {
                        flagvalue=2;
                        makeStringRequest();
                     }

                        else if (ssp.getUser_type(My_services.this).equals("Customer")) {
                            if (helper.service_id.size() > 0) {
                                flagvalue = 2;
                                makeStringRequest();
                            } else {
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(My_services.this);
                                }

                                alert.setMessage("اختر خدمة واحدة على الأقل للتسجيل كمقدم خدمة");


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
                        }
                    }
                }

            });

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
                                    ////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                JSONArray jsonArray = obj.getJSONArray("categories");
                                ////Log.e("JSON_ARRAY", "===" + jsonArray);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ArrayList<Service_Category_Entity.Services_Entity> list = new ArrayList<>();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    ////Log.e("JSON_OBJECT", "===" + object);
                                    Service_Category_Entity entity = new Service_Category_Entity();
                                    entity.setCategory_id(object.getString("category_id"));
                                    entity.setCategory_name(object.getString("category_name"));
                                    entity.setAdded_date(object.getString("added_date"));
                                    entity.setIs_enable(object.getString("is_enable"));
                                    entity.setAdded_date(object.getString("added_date"));
                                    entity.setIs_subscribed(object.getString("is_subscribed"));

                                    JSONArray jsonArray1 = object.getJSONArray("services");
                                    ////Log.e("JSON_INNER_ARRAY", "====" + jsonArray1);
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
                                    category_service=new FilterAdapter(My_services.this,service_list);
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

                        else if(flagvalue==2&&p_room.equals("1")) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                msg=obj.getString("msg");




                                    if (obj.has("user_details")) {

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

                                    if (msg.equals("Services updated successfully")) {
                                        final AlertDialog.Builder alert;
                                        if (Build.VERSION.SDK_INT >= 11) {
                                            alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                                        } else {
                                            alert = new AlertDialog.Builder(My_services.this);
                                        }

                                        alert.setMessage("تم تحديث الخدمات بنجاح");


                                        alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                ssp.setName(My_services.this, name);
                                                ssp.setEmail(My_services.this, email);
                                                ssp.setMobile_number(My_services.this, mobile_number);
                                                ssp.setCombine_Mobile_number(My_services.this, combine_mobile);
                                                ssp.setLatitude(My_services.this, latitude);
                                                ssp.setLongitude(My_services.this, longitude);
                                                ssp.setLocation(My_services.this, location);
                                                ssp.setLogin_status(My_services.this, login_status);
                                                ssp.setPayment_preference(My_services.this, payment_preference);
                                                ssp.setActive_status(My_services.this, active_status);
                                                ssp.setUser_type(My_services.this, user_type);
                                                ssp.setKEY_profile_pic_thumb_url(My_services.this, profile_pic_thumb_url);
                                                ssp.setPush_notification(My_services.this, push_notification);
                                                ssp.setOnline_offline_status(My_services.this, online_ofiline_status);
                                                ssp.setDescription(My_services.this, description);
                                                ssp.setAdded_date(My_services.this, added_date);
                                                ssp.setReview_count(My_services.this, review_count);
                                                ssp.setReview_rating(My_services.this, review_rating);
                                                ssp.setOrder_count(My_services.this, order_count);
                                                ssp.setCountry(My_services.this, country);
                                                ssp.setMobile_visible(My_services.this, mobile_visible);
                                                ssp.setCity_to_cover(My_services.this, city_to_cover);
                                                ssp.setDistance(My_services.this, distance);
                                                ssp.setUser_bank_name(My_services.this, user_bank_name);
                                                ssp.setUser_bank_acct_num(My_services.this, user_bank_acct_num);
                                                ssp.setUser_bank_iban_num(My_services.this, user_iban_num);
                                                ssp.setKEY_profile_pic_thumb_url(My_services.this, profile_pic_thumb_url);


                                                    ////Log.e("home2222", "" + String.valueOf(helper.home));
                                                    Intent intent = new Intent(My_services.this, P_room.class);
                                                intent.putExtra("flag",10);
                                                    startActivity(intent);

//
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


                        else if(p_room.equals("2")&&flagvalue==2) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                msg=obj.getString("msg");




                                if (obj.has("user_details")) {

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

                                if (msg.equals("Services updated successfully")) {
                                    final AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(My_services.this);
                                    }

                                    alert.setMessage("تم تحديث الخدمات بنجاح");


                                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ssp.setName(My_services.this, name);
                                            ssp.setEmail(My_services.this, email);
                                            ssp.setMobile_number(My_services.this, mobile_number);
                                            ssp.setCombine_Mobile_number(My_services.this, combine_mobile);
                                            ssp.setLatitude(My_services.this, latitude);
                                            ssp.setLongitude(My_services.this, longitude);
                                            ssp.setLocation(My_services.this, location);
                                            ssp.setLogin_status(My_services.this, login_status);
                                            ssp.setPayment_preference(My_services.this, payment_preference);
                                            ssp.setActive_status(My_services.this, active_status);
                                            ssp.setUser_type(My_services.this, user_type);
                                            ssp.setKEY_profile_pic_thumb_url(My_services.this, profile_pic_thumb_url);
                                            ssp.setPush_notification(My_services.this, push_notification);
                                            ssp.setOnline_offline_status(My_services.this, online_ofiline_status);
                                            ssp.setDescription(My_services.this, description);
                                            ssp.setAdded_date(My_services.this, added_date);
                                            ssp.setReview_count(My_services.this, review_count);
                                            ssp.setReview_rating(My_services.this, review_rating);
                                            ssp.setOrder_count(My_services.this, order_count);
                                            ssp.setCountry(My_services.this, country);
                                            ssp.setMobile_visible(My_services.this, mobile_visible);
                                            ssp.setCity_to_cover(My_services.this, city_to_cover);
                                            ssp.setDistance(My_services.this, distance);
                                            ssp.setUser_bank_name(My_services.this, user_bank_name);
                                            ssp.setUser_bank_acct_num(My_services.this, user_bank_acct_num);
                                            ssp.setUser_bank_iban_num(My_services.this, user_iban_num);
                                            ssp.setKEY_profile_pic_thumb_url(My_services.this, profile_pic_thumb_url);


                                                ////Log.e("home2222", "" + String.valueOf(helper.home));
                                                Intent intent = new Intent(My_services.this, MainActivity.class);
                                                intent.putExtra("flag",11);
                                                startActivity(intent);
                                            }
//                                                   else {}

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



                        if(p_room.equals("2")&&flagvalue==3) {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////Log.e("Json Data", response.toString().substring(start1, end));
                                }
                                ////Log.e("Message6666",response);
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
                                        alert = new AlertDialog.Builder(My_services.this, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(My_services.this);
                                    }

                                    alert.setMessage("تم تحويل حسابك لعميل بنجاح");


                                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ssp.setName(My_services.this,name);
                                            ssp.setEmail(My_services.this,email);
                                            ssp.setMobile_number(My_services.this,mobile_number);
                                            ssp.setCombine_Mobile_number(My_services.this,combine_mobile);
                                            ssp.setLatitude(My_services.this,latitude);
                                            ssp.setLongitude(My_services.this,longitude);
                                            ssp.setLocation(My_services.this,location);
                                            ssp.setLogin_status(My_services.this,login_status);
                                            ssp.setPayment_preference(My_services.this,payment_preference);
                                            ssp.setActive_status(My_services.this,active_status);
                                            ssp.setUser_type(My_services.this,user_type);
                                            ssp.setKEY_profile_pic_thumb_url(My_services.this,profile_pic_thumb_url);
                                            ssp.setPush_notification(My_services.this,push_notification);
                                            ssp.setOnline_offline_status(My_services.this,online_ofiline_status);
                                            ssp.setDescription(My_services.this,description);
                                            ssp.setAdded_date(My_services.this,added_date);
                                            ssp.setReview_count(My_services.this,review_count);
                                            ssp.setReview_rating(My_services.this,review_rating);
                                            ssp.setOrder_count(My_services.this,order_count);
                                            ssp.setCountry(My_services.this,country);
                                            ssp.setMobile_visible(My_services.this,mobile_visible);
                                            ssp.setCity_to_cover(My_services.this,city_to_cover);
                                            ssp.setDistance(My_services.this,distance);
                                            ssp.setUser_bank_name(My_services.this,user_bank_name);
                                            ssp.setUser_bank_acct_num(My_services.this,user_bank_acct_num);
                                            ssp.setUser_bank_iban_num(My_services.this,user_iban_num);
                                            ssp.setKEY_profile_pic_thumb_url(My_services.this,profile_pic_thumb_url);

                                                ////Log.e("home2222",""+String.valueOf(helper.home));
                                                Intent intent=new Intent(My_services.this, MainActivity.class);
                                                 intent.putExtra("flag",11);
                                                startActivity(intent);

//                                                   else {}

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



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(My_services.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(My_services.this,getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(My_services.this,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(My_services.this,getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

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
                    ////Log.e("params", String.valueOf(params));
                }
                else if(flagvalue==2)
                {
                    params.put("action", "Registerassp");
                    params.put("user_id",ssp.getUserId(My_services.this));
                    params.put("services", helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    ////Log.e("params",String.valueOf(params));
                }
                else if(flagvalue==3)
                {
                    params.put("action","Unregistertocustomer");
                    params.put("user_id",ssp.getUserId(My_services.this));
                    // params.put("services",helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    ////Log.e("params",String.valueOf(params));
                }


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

                ////Log.e("PARAMS", "" + params.toString());
                ////Log.e("PARAMS SUBCATEGORY",""+helper.filter_subcategoryid.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));*/

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
                progress_dialog = new ProgressDialog(My_services.this, AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(My_services.this);
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