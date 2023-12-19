package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.SearchProviderAdapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.GPSTracker;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by dell on 2/12/2018.
 */

public class Search_ProvideService extends AppCompatActivity {
    RecyclerView search_view;
    LinearLayoutManager layoutManager;
    SaveSharedPrefernces ssp;
    Activity act;
   ImageView header_back;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    int page = 0, curSize;
    String result_count = "";
    private String TAG = "Search_ProvideService";


    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    List<Address> addresses;
    Double latis,longis;
    String longitude,latitude;
    String no_people,date,time,city,address,comment,address1="",state="",device_id,msg="",service_id;
    Geocoder geocoder;
    Intent intent;
  SearchProviderAdapter adapterhome;
    ArrayList<Provider_List_Entity> list_provider=new ArrayList<>();
    TextView txt_header,error_msg;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        //Log.e(TAG,"****************************");
       setContentView(R.layout.search_providerservice);
        act=Search_ProvideService.this;
        ssp=new SaveSharedPrefernces();
        intent=getIntent();
        service_id=intent.getStringExtra("service_id");
        header_back=(ImageView) findViewById(R.id.header_back);
        txt_header=(TextView)findViewById(R.id.txt_header);
        txt_header.setText("اختر موفر");


        error_msg=(TextView) findViewById(R.id.error_msg);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Search_ProvideService.this,Order_detail.class);
                intent.putExtra("order_id", helper.order_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        //recList = (RecyclerView) rootView.findViewById(R.id.home_recycler_view);
       search_view=(RecyclerView) findViewById(R.id.search_view);

        GPSTracker gpsTracker= new GPSTracker(act);

        longitude=String.valueOf(gpsTracker.getLongitude());
        ////Log.e("longitude", ""+longitude);
        latitude=String.valueOf(gpsTracker.getLatitude());
        ////Log.e("latitude",""+latitude);
layoutManager=new LinearLayoutManager(act);
search_view.setLayoutManager(layoutManager);









        search_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                ////Log.e("visibleItemCount", "" + visibleItemCount);
                ////Log.e("totalItemCount", "" + totalItemCount);
                ////Log.e("firstVisibleItem", "" + firstVisibleItem);
                ////Log.e("LogEntity1.size()", "" + list_provider.size());
                ////Log.e("total_count", result_count);
                ////Log.e("page", "" + page);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    ////Log.e("page1", "" + page);
                    try {
                        if (list_provider.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);

                            page = page + 1;
                            makeStringReq();


                        } else {
                            Toast.makeText(act,
                                    "End of list", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }

            }
        });

        latis=Double.parseDouble(latitude);
        longis=Double.parseDouble(longitude);

        geocoder = new Geocoder(act, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latis, longis, 1);

           /* if (addresses.isEmpty()) {
                Toast.makeText(Member_login.this,"waiting for the location", Toast.LENGTH_SHORT).show();


            }

            else {*/
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
//	    							zip = addresses.get(0).getPostalCode();
//	    							////Log.e("zip","zip>>>" +zip);
                address1=address+","+city+","+state;
                ////Log.e("Address","==="+address1);
//	    							createaccount_zip_edtxt.setText(zip);
                //  txt_location.setText(address1);
            }
            //  }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(act, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(act);
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
    private void makeStringReq() {
//        if (page==0) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);
        //  }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);


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
                            if (obj.has("result_count")){
                                result_count=obj.getString("result_count");
                                if (TextUtils.isEmpty(result_count)){
                                    result_count="0";
                                }

                            }
                            JSONArray jsonArray=obj.getJSONArray("providers");
                            ArrayList<Provider_List_Entity> list=new ArrayList<>();
                            for (int i=0;i<jsonArray.length();i++){
                                ArrayList<Provider_List_Entity.Service> all_service_list=new ArrayList<>();
                                ArrayList<String> service_name_list=new ArrayList<>();
                                ArrayList<String> category_name_list=new ArrayList<>();
                                ArrayList<String> service_id_list=new ArrayList<>();
                                ArrayList<String> category_id_list=new ArrayList<>();
                                JSONObject object=jsonArray.getJSONObject(i);
                                Provider_List_Entity entity=new Provider_List_Entity();
                                entity.setUser_id(object.getString("user_id"));
                                entity.setName(object.getString("name"));
                                entity.setEmail_address(object.getString("email_address"));
                                entity.setCalling_code(object.getString("calling_code"));
                                entity.setMobile_number(object.getString("mobile_number"));
                                entity.setCombine_mobile(object.getString("combine_mobile"));
                                entity.setPassword(object.getString("password"));
                                entity.setGmt_value(object.getString("gmt_value"));
                                entity.setLatitude(object.getString("latitude"));
                                entity.setLongitude(object.getString("longitude"));
                                entity.setLocation(object.getString("location"));
                                entity.setPayment_preference(object.getString("payment_preference"));
                                entity.setDevice_id(object.getString("device_id"));
                                entity.setActive_status(object.getString("active_status"));
                                entity.setVerify_code(object.getString("verify_code"));
                                entity.setIs_verified(object.getString("is_verified"));
                                entity.setUser_type(object.getString("user_type"));
                                entity.setLogin_status(object.getString("login_status"));
                                entity.setProfile_pic_thumb_url(object.getString("profile_pic_thumb_url"));
                                entity.setProfile_pic_2xthumb_url(object.getString("profile_pic_2xthumb_url"));
                                entity.setProfile_pic_3xthumb_url(object.getString("profile_pic_3xthumb_url"));
                                entity.setProfile_cover_pic_1xthumb_url(object.getString("profile_cover_pic_1xthumb_url"));
                                entity.setProfile_cover_pic_2xthumb_url(object.getString("profile_cover_pic_2xthumb_url"));
                                entity.setProfile_cover_pic_3xthumb_url(object.getString("profile_cover_pic_3xthumb_url"));
                                entity.setAdded_date(object.getString("added_date"));
                                entity.setPush_notification(object.getString("push_notification"));
                                entity.setOnline_offline_status(object.getString("online_offline_status"));
                                entity.setProfile_pic_url(object.getString("profile_pic_url"));
                                entity.setDescription(object.getString("description"));
                                entity.setReview_count(object.getString("review_count"));
                                entity.setReview_rating(object.getString("review_rating"));
                                entity.setOrder_count(object.getString("order_count"));
                                entity.setCountry(object.getString("country"));
                                entity.setUser_device_type(object.getString("user_device_type"));
                                entity.setMobile_visible(object.getString("mobile_visible"));
                                entity.setCity_to_cover(object.getString("city_to_cover"));
                                entity.setDistance(object.getString("distance"));
                                entity.setUser_bank_name(object.getString("user_bank_name"));
                                entity.setUser_bank_account_number(object.getString("user_bank_account_number"));
                                entity.setUser_bank_iban_number(object.getString("user_bank_iban_number"));
                                entity.setIs_favourite(object.getString("is_favourite"));
                                JSONArray array=object.getJSONArray("services");
                                for (int j=0;j<array.length();j++){
                                    JSONObject json=array.getJSONObject(j);
                                    Provider_List_Entity.Service service=new Provider_List_Entity.Service();
                                    service.setUser_service_id(json.getString("user_service_id"));
                                  //  service.setSub_service_id(json.getString("sub_service_id"));
                                    service.setMain_category_id(json.getString("main_category_id"));
                                    category_id_list.add(json.getString("main_category_id"));
                                    service.setService_id(json.getString("service_id"));
                                    service_id_list.add(json.getString("service_id"));
                                    service.setAdded_date(json.getString("added_date"));
                                    service.setCategory_name(json.getString("category_name"));
                                    category_name_list.add(json.getString("category_name"));
                                    service.setService_name(json.getString("service_name"));
                                    service_name_list.add(json.getString("service_name"));
                                   // service.setSub_service_name(json.getString("sub_service_name"));
                                    all_service_list.add(service);
                                }

                                entity.setCategory_id_list(category_id_list);
                                entity.setCategory_name_list(category_name_list);
                                entity.setService_id_list(service_id_list);
                                entity.setService_name_list(service_name_list);
                                entity.setAll_service_list(all_service_list);
                                list.add(entity);


                            }
                            if (page == 0) {
                                curSize=0;
                                ////Log.e("6666", "66666");
                               list_provider.addAll(list);

                                ////Log.e("List_Size", "===" + list_provider.size());

                                try {

                                    if (list_provider.size() > 0) {
                                        ////Log.e("7777", "7777");
                                        ////Log.e("List_Size>0", "===" + list_provider.size());
                                        error_msg.setVisibility(View.GONE);
                                        search_view.setVisibility(View.VISIBLE);
                                        adapterhome=new SearchProviderAdapter(act,list_provider);
                                        search_view.setAdapter(adapterhome);
                                    } else {
                                        ////Log.e("88888", "8888");
                                        ////Log.e("List_Size", "===" + list_provider.size());
                                      //  my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        error_msg.setVisibility(View.VISIBLE);
                                        search_view.setVisibility(View.GONE);

                                    }

                                    curSize = list_provider.size();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                ////Log.e("9999", "9999");
                                curSize = list_provider.size();
                                list_provider.addAll(curSize, list);
                                adapterhome.notifyItemInserted(curSize);
                                adapterhome.notifyItemRangeChanged(curSize, list_provider.size());
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Search_ProvideService.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(Search_ProvideService.this,getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(Search_ProvideService.this,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(Search_ProvideService.this,getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Searchbyservices");
                params.put("user_id", ssp.getUserId(act));
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.put("distance","");
                params.put("sort_type","");
                params.put("sort_by","");
                params.put("services",service_id);

                params.put("page",String.valueOf(page));

                ////Log.e("params", params.toString());
                return params;
            }

        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Search_ProvideService.this,Order_detail.class);
        intent.putExtra("order_id", helper.order_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
