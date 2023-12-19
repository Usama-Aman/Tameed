package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Abstract_global;
import tameed.com.tameed.Util.Concreate_global;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by dev on 23-01-2018.
 */

public class Sort extends AppCompatActivity {
    TextView header_txt;
    ImageView header_back;
    Button button_sort_reset,button_sort_apply;
    Boolean bool=true;
    String sort_by="";
    Abstract_global abstract_global;
    ConstraintLayout constraintLayout_byRating;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    SaveSharedPrefernces ssp;
    String sort_type="";
    Activity act;

    private String TAG = "Sort";


    String user_id,name,email_address,calling_code,mobile_number,combine_mobile,password,gmt_value,latitude,longitude,location,payment_preference,
            device_id,active_status,verify_code,is_verified,login_status,user_type,profile_pic_thumb_url,profile_pic_2xthumb_url,profile_pic_3xthumb_url,
            profile_cover_pic_1xthumb_url,profile_cover_pic_2xthumb_url,profile_cover_pic_3xthumb_url,push_notification,online_offline_status,
            profile_pic_url,description ,review_count,review_rating,order_count,country,user_device_type,mobile_visible ,city_to_cover,distance ,user_bank_name ,user_bank_account_number ,user_bank_iban_number ,is_favourite,
            user_service_id ,sub_service_id ,main_category_id ,service_id ,added_date ,category_name ,service_name ,sub_service_name;
    ImageView asc_dsc, radio_rating, radio_popularity, radio_best_rating, nearest;
   private ConstraintLayout constraintLayout_rating,constraintLayout_popularity,constraintLayout_best_rating,constraintLayout_nearest;

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
        setContentView(R.layout.sort);
        ssp=new SaveSharedPrefernces();
        act=Sort.this;
        abstract_global = new Concreate_global();
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("ترتيب");
        //Log.e(TAG,"****************************");
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        asc_dsc=(ImageView)findViewById(R.id.asc_dsc_img);
        radio_rating=(ImageView)findViewById(R.id.radio_rating);
        radio_popularity=(ImageView)findViewById(R.id.radio_popularity);
        radio_best_rating=(ImageView)findViewById(R.id.radio_best_rating);
        nearest=(ImageView)findViewById(R.id.radio_nearest);
        constraintLayout_rating=(ConstraintLayout)findViewById(R.id.constraintLayout_byRating);
        constraintLayout_popularity=(ConstraintLayout)findViewById(R.id.constraintLayout_byPopularity);
        constraintLayout_best_rating=(ConstraintLayout)findViewById(R.id.constraintLayout_bestRating);
        constraintLayout_nearest=(ConstraintLayout)findViewById(R.id.constraintLayout_nearest);

        button_sort_reset=(Button)findViewById(R.id.button_sort_reset);
        button_sort_apply=(Button)findViewById(R.id.button_sort_apply);

        ////Log.e("status",helper.status);

        if(helper.status.equals("0"))
        {
            sort_type="ASC";
            asc_dsc.setImageResource(R.mipmap.dsc);
            bool=true;
            //radio_rating.setImageResource(R.mipmap.radio_w2x);
        }

        else
        {
            ////Log.e("1111","3333");
            bool=false;
            sort_type="DESC";
            asc_dsc.setImageResource(R.mipmap.asc);
           // radio_rating.setImageResource(R.mipmap.radio_checked);

        }


         if(helper.sort_by.equals("rating"))
        {
            sort_by="rating";
            radio_rating.setImageResource(R.mipmap.radio_checked);
            // radio_rating.setImageResource(R.mipmap.radio_checked);

        }
        else if(helper.sort_by.equals("popularity"))
        {
            sort_by="popularity";
            radio_popularity.setImageResource(R.mipmap.radio_checked);
            // radio_rating.setImageResource(R.mipmap.radio_checked);

        }
        else if(helper.sort_by.equals("name"))
        {
            sort_by="name";
            radio_best_rating.setImageResource(R.mipmap.radio_checked);
            // radio_rating.setImageResource(R.mipmap.radio_checked);

        }
        else if( helper.sort_by.equals("distance"))
        {
            sort_by="distance";
            nearest.setImageResource(R.mipmap.radio_checked);
            // radio_rating.setImageResource(R.mipmap.radio_checked);

        }







   /*     asc_dsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool){
                    asc_dsc.setImageResource(R.mipmap.asc);

                }
                else{
                    asc_dsc.setImageResource(R.mipmap.dsc);
                }
                bool=!bool;

            }
        });*/



        constraintLayout_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_by="rating";
                helper.radio_rating="rating";
                radio_rating.setImageResource(R.mipmap.radio_checked);
                radio_popularity.setImageResource(R.mipmap.radio_w2x);
                radio_best_rating.setImageResource(R.mipmap.radio_w2x);
                nearest.setImageResource(R.mipmap.radio_w2x);


            }
        });

        constraintLayout_popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_by="popularity";
                helper.radio_popularity="radio_popularity";
               // helper.status="1";
                radio_rating.setImageResource(R.mipmap.radio_w2x);
                radio_popularity.setImageResource(R.mipmap.radio_checked);
                radio_best_rating.setImageResource(R.mipmap.radio_w2x);
                nearest.setImageResource(R.mipmap.radio_w2x);

            }
        });

        constraintLayout_best_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_by="name";
               helper.radio_best_rating="name";
                //helper.status="1";
                radio_rating.setImageResource(R.mipmap.radio_w2x);
                radio_popularity.setImageResource(R.mipmap.radio_w2x);
                radio_best_rating.setImageResource(R.mipmap.radio_checked);
                nearest.setImageResource(R.mipmap.radio_w2x);
            }
        });

        constraintLayout_nearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_by="distance";
                helper.radio_nearest="distance";
                //helper.status="1";
                radio_rating.setImageResource(R.mipmap.radio_w2x);
                radio_popularity.setImageResource(R.mipmap.radio_w2x);
                radio_best_rating.setImageResource(R.mipmap.radio_w2x);
                nearest.setImageResource(R.mipmap.radio_checked);


            }
        });

        button_sort_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        radio_rating.setImageResource(R.mipmap.radio_w2x);
                        radio_popularity.setImageResource(R.mipmap.radio_w2x);
                        radio_best_rating.setImageResource(R.mipmap.radio_w2x);
                        nearest.setImageResource(R.mipmap.radio_w2x);
                        asc_dsc.setImageResource(R.mipmap.asc);

                    }
                });

        asc_dsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool){
                    asc_dsc.setImageResource(R.mipmap.asc);
                    sort_type="DESC";
                    helper.status="1";
//                    flagvalue=1;
//                    loadData();
                    /*if (AppController.isOnline(Sort.this)) {
                        makeStringReq();

                    }

                    else {
                        AppController.showAlert(Sort.this,
                                getString(R.string.networkError_Message));
                    }*/
                }
                else {
                    asc_dsc.setImageResource(R.mipmap.dsc);
                    sort_type="ASC";
                    helper.status="0";
//                    flagvalue=1;
//                    loadData();
                    /*if (AppController.isOnline(Sort.this)) {
                        makeStringReq();

                    }

                    else {
                        AppController.showAlert(Sort.this,
                                getString(R.string.networkError_Message));
                    }*/
                }
                bool=!bool;
            }
        });

        button_sort_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(sort_by)){
                    abstract_global.abstract_toast(act, "يرجى اختيار نوع الترتيب");

                }
                else  if (TextUtils.isEmpty(sort_type)){
                    abstract_global.abstract_toast(act, "يرجى اختيار نوع الترتيب");

                }

                else
                {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11 ) {
                        alert = new AlertDialog.Builder(Sort.this, AlertDialog.THEME_HOLO_LIGHT );
                    } else {
                        alert = new AlertDialog.Builder(Sort.this);
                    }
                    //alert.setTitle("Registration!");
                    alert.setMessage("هل تريد تطبيق اسم الفرز مع "+sort_type+"معاملة");
                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            helper.sort_type="";
                            helper.sort_by="";
                            helper.radio_rating="";
                            helper.radio_popularity="";
                            helper.radio_best_rating="";
                            helper.radio_nearest="";
                            Intent i=new Intent(Sort.this,MainActivity.class);
                            helper.sort_type=sort_type;
                            helper.sort_by=sort_by;
                            i.putExtra("flag",12);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });



                    try{
                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }



            }
        });



    }
   /* Handler mHandler = new Handler(new Handler.Callback() {

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

                            JSONArray jsonArray = obj.getJSONArray("providers");
                            ArrayList<Provider_list_Entity> list = new ArrayList<Provider_list_Entity>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    // Provider_list_Entity provider_list_entity = new Provider_list_Entity();
                                    user_id=  jsonObject.getString("user_id");
                                    name=jsonObject.getString("name");
                                   email_address= jsonObject.getString("email_address");
                                   calling_code= jsonObject.getString("calling_code");
                                   mobile_number=jsonObject.getString("mobile_number");
                                    combine_mobile=jsonObject.getString("combine_mobile");
                                    password= jsonObject.getString("password");
                                  gmt_value=  jsonObject.getString("gmt_value");
                                  latitude=  jsonObject.getString("latitude");
                                    longitude=jsonObject.getString("longitude");
                                    location=jsonObject.getString("location");
                                    payment_preference=jsonObject.getString("payment_preference");
                                    device_id=jsonObject.getString("device_id");

                                    active_status=jsonObject.getString("active_status");
                                   verify_code= jsonObject.getString("verify_code");
                                   is_verified= jsonObject.getString("is_verified");
                                    login_status=jsonObject.getString("login_status");
                                    user_type=jsonObject.getString("user_type");
                                   profile_pic_thumb_url= jsonObject.getString("profile_pic_thumb_url");
                                    profile_pic_2xthumb_url=jsonObject.getString("profile_pic_2xthumb_url");
                                    profile_pic_3xthumb_url=jsonObject.getString("profile_pic_3xthumb_url");
                                    profile_cover_pic_1xthumb_url= jsonObject.getString("profile_cover_pic_1xthumb_url");
                                    profile_cover_pic_2xthumb_url=jsonObject.getString("profile_cover_pic_2xthumb_url");
                                    profile_cover_pic_3xthumb_url=jsonObject.getString("profile_cover_pic_3xthumb_url");
                                    push_notification=jsonObject.getString("push_notification");
                                    online_offline_status=jsonObject.getString("online_offline_status");
                                    profile_pic_url= jsonObject.getString("profile_pic_url");
                                    description=jsonObject.getString("description");
                                    review_count= jsonObject.getString("review_count");
                                    review_rating= jsonObject.getString("review_rating");
                                    order_count= jsonObject.getString("order_count");
                                    country= jsonObject.getString("country");
                                    user_device_type=jsonObject.getString("user_device_type");
                                    mobile_visible=jsonObject.getString("mobile_visible");
                                    city_to_cover= jsonObject.getString("city_to_cover");
                                    distance= jsonObject.getString("distance");
                                   user_bank_name= jsonObject.getString("user_bank_name");
                                    user_bank_account_number=jsonObject.getString("user_bank_account_number");
                                    user_bank_iban_number=jsonObject.getString("user_bank_iban_number");
                                    is_favourite=jsonObject.getString("is_favourite");
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("services");
                                    // servicemap.put(jsonObject.getString("user_id"),jsonArray1);
                                    for (int j = 0; j <jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                        user_service_id=jsonObject1.getString("user_service_id");
                                       sub_service_id= jsonObject1.getString("sub_service_id");
                                        main_category_id=jsonObject1.getString("main_category_id");
                                       sub_service_id= jsonObject1.getString("sub_service_id");

                                       added_date= jsonObject1.getString("added_date");
                                        category_name=jsonObject1.getString("category_name");

                                       service_name= jsonObject1.getString("service_name");
                                       sub_service_name= jsonObject1.getString("sub_service_name");
                                    }



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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

                            Toast.makeText(Sort.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Sort.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Sort.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Sort.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Sortproviders");
                params.put("latitude", ssp.getLatitude(Sort.this));
                params.put("longitude", ssp.getLongitude(Sort.this));
                params.put("user_id", ssp.getUserId(Sort.this));
                params.put("sort_type",sort_type);
                params.put("sort_by",sort_by);
               //params.put("services", helper.services.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                //params.toString("page",String.valueOf(page));
                ////Log.e("params", params.toString());



                return params;
            }

        };



        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Sort.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(Sort.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Sort.this);
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
*/
}