package tameed.com.tameed.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Sp_contact_list_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Direct_T_Entity;
import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.GPSTracker;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import static tameed.com.tameed.MapView_Activity.RequestPermissionCode;
/**
 * Created by root on 24/1/18.
 */

public class Direct_T_fragment extends Fragment {

    TextView header_txt;
    ImageView header_back;
    RecyclerView recyclerView;
    String result_count = "";
    String refresh = "";
    int page = 0, curSize;
    public static final int REQUEST_READ_CONTACTS = 79;
    TextView error2;
    int flagvalue;
    ArrayList<Direct_T_Entity>direct_list=new ArrayList<>();
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private String TAG = "Direct_T_fragment";
    String lat, lang;
    GPSTracker gpsTracker;
    Sp_contact_list_Adapter sp_contact_list_adapter;
    int firstVisibleItem, visibleItemCount, totalItemCount=0;
    //ArrayList<String>phone_list;
    HashMap<String,JSONArray> servicemap;
    LinearLayoutManager linearLayoutManager;
    public static SwipeRefreshLayout my_swipe_refresh_layout3;
    ArrayList<String> contact_list=new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    SaveSharedPrefernces ssp;


    // TODO Contact list Nardeep
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
    
        View rootView=inflater.inflate(R.layout.sp_contact_list,container,false);
        ssp = new SaveSharedPrefernces();

        //Log.e(TAG,"****************************");
        header_txt = (TextView) rootView.findViewById(R.id.txt_header);
        header_txt.setText("مقدمي خدمة من قائمة الاتصال بهاتفك");
        header_txt.setVisibility(View.VISIBLE);
        header_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,25);
        header_back = (ImageView) rootView.findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        error2=rootView.findViewById(R.id.error2);

        recyclerView=(RecyclerView)rootView.findViewById(R.id.sp_contact_recycle);
        my_swipe_refresh_layout3=rootView.findViewById(R.id.my_swipe_refresh_layout3);
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        gpsTracker = new GPSTracker(getActivity());
        lat = String.valueOf(gpsTracker.getLatitude());

        ////Log.e("latitude", "" + lat);
        lang = String.valueOf(gpsTracker.getLongitude());
     /*   EnableRuntimePermission();
        getContactList() ;
        makeStringReq();*/
        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContactList() ;
           *//* if(helper.phone_list.size()>0) {
                makeStringReq();
            }*//*
            //Log.e("33333","3333333");
        } else {
            requestLocationPermission();
            //Log.e("444444","444444");
        }*/



        my_swipe_refresh_layout3.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);


        my_swipe_refresh_layout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                //Log.e("refreshh", "333" + refresh);
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
               direct_list.clear();
                //customerorderlist_orderdetail_second.clear();
                //list.clear();

                makeStringReq();
            }
        });
        makeStringReq();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                //  //Log.e("visibleItemCount", "" + visibleItemCount);
                //Log.e("totalItemCount", "" + totalItemCount);
                //    //Log.e("firstVisibleItem", "" + firstVisibleItem);
                //Log.e("LogEntity1.size()", "" + direct_list.size());
                //    //Log.e("result_count", result_count);
                //Log.e("page", "" + page);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        //Log.e("11111", "11111");
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    //Log.e("page1", "" + page);
                    //Log.e("222222", "22222");
                    try {
                        if (direct_list.size() < Integer.parseInt(result_count)) {
                            //Log.e("page2", "" + page);
                            //Log.e("33333", "33333");
                            page = page + 1;

//                            flagvalue=0;
//                            makeStringReq();

                            if(!helper.sort_type.equals(""))
                            {
                                flagvalue=1;
                                makeStringReq();
                            }
                            else
                            {
                                flagvalue=0;
                                makeStringReq();
                            }


                        } else {
                            //Log.e("44444", "444444");
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



/*
        sp_contact_list_adapter=new Sp_contact_list_Adapter(getActivity(),contact_list);

        recyclerView.setAdapter(sp_contact_list_adapter);*/
        return  rootView;
    }

/*    protected void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   getContactList();

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }*/

 /*   private void getContactList() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    String final_phone="";
                    while (pCur.moveToNext()) {

                       String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                       ////Log.e("phoneNo",phoneNo);


                       if(phoneNo.startsWith("0"))
                       {
                          final_phone=phoneNo.replace("0"," ").trim();
                           //Log.e("final_phone5555",final_phone);

                       }
                       else
                       {
                           final_phone=phoneNo;
                           //Log.e("final_phone",final_phone);


                       }

                        helper.phone_list.add(final_phone);
                        //Log.e("phoneNo",helper.phone_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    }

                    pCur.close();
                }
            }
        }
        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
        makeStringReq();
        if(cur!=null){
            cur.close();
        }
    }*/

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

//TODO HERe
    private void makeStringReq()
    {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        my_swipe_refresh_layout3.setRefreshing(false);
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                       // servicemap = new HashMap<>();
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                //Log.e("Json Data", response.toString().substring(start1, end));
                            }

                            //Log.e("Message", "<<>>" + response);
                        }catch (Exception e){e.printStackTrace();}
                        if (flagvalue == 0) {

                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    Log.e("DIRECT_T", response.toString().substring(start1, end));
                                }

                                Log.e("Message", "<<>>" + response);

                                servicemap = new HashMap<>();
                                result_count = obj.getString("result_count");
                                Log.e(TAG,"OBJECT"+obj);
                                JSONArray jsonArray = obj.getJSONArray("providers");
                                Log.e(TAG,"JSONARRAY"+jsonArray);
                                ArrayList<Direct_T_Entity> list = new ArrayList<Direct_T_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        ArrayList<Provider_List_Entity.Service> all_service_list=new ArrayList<>();

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        helper.service_list.clear();;
                                        helper.category_list.clear();
                                        helper.category_idlist.clear();
                                        helper.service_idlist.clear();

                                        Direct_T_Entity direct_t_entity=new Direct_T_Entity();
                                        direct_t_entity.setUser_id(jsonObject.getString("user_id"));
                                /*        provider_id=jsonObject.getString("user_id");
                                        ssp.setProvider_id(getActivity(),provider_id);*/
                                        direct_t_entity.setName(jsonObject.getString("name"));
                                        direct_t_entity.setEmail_address(jsonObject.getString("email_address"));
                                        direct_t_entity.setCalling_code(jsonObject.getString("calling_code"));
                                        direct_t_entity.setMobile_number(jsonObject.getString("mobile_number"));
                                        direct_t_entity.setCombine_mobile(jsonObject.getString("combine_mobile"));
                                        direct_t_entity.setPassword(jsonObject.getString("password"));
                                        direct_t_entity.setGmt_value(jsonObject.getString("gmt_value"));
                                        direct_t_entity.setLatitude(jsonObject.getString("latitude"));
                                        direct_t_entity.setLongitude(jsonObject.getString("longitude"));
                                        direct_t_entity.setLocation(jsonObject.getString("location"));
                                        direct_t_entity.setPayment_preference(jsonObject.getString("payment_preference"));
                                        direct_t_entity.setDevice_id(jsonObject.getString("device_id"));

                                        direct_t_entity.setActive_status(jsonObject.getString("active_status"));
                                        direct_t_entity.setVerify_code(jsonObject.getString("verify_code"));
                                        direct_t_entity.setIs_verified(jsonObject.getString("is_verified"));
                                        direct_t_entity.setLogin_status(jsonObject.getString("login_status"));
                                        direct_t_entity.setUser_type(jsonObject.getString("user_type"));
                                        direct_t_entity.setProfile_pic_thumb_url(jsonObject.getString("profile_pic_thumb_url"));
                                        direct_t_entity.setProfile_pic_2xthumb_url(jsonObject.getString("profile_pic_2xthumb_url"));
                                        direct_t_entity.setProfile_pic_3xthumb_url(jsonObject.getString("profile_pic_3xthumb_url"));
                                        direct_t_entity.setProfile_cover_pic_1xthumb_url(jsonObject.getString("profile_cover_pic_1xthumb_url"));
                                        direct_t_entity.setProfile_cover_pic_2xthumb_url(jsonObject.getString("profile_cover_pic_2xthumb_url"));
                                        direct_t_entity.setProfile_cover_pic_3xthumb_url(jsonObject.getString("profile_cover_pic_3xthumb_url"));
                                        direct_t_entity.setPush_notification(jsonObject.getString("push_notification"));
                                        direct_t_entity.setOnline_offline_status(jsonObject.getString("online_offline_status"));
                                        direct_t_entity.setProfile_pic_url(jsonObject.getString("profile_pic_url"));
                                        direct_t_entity.setDescription(jsonObject.getString("description"));
                                        direct_t_entity.setReview_count(jsonObject.getString("review_count"));
                                        direct_t_entity.setReview_rating(jsonObject.getString("review_rating"));
                                        direct_t_entity.setOrder_count(jsonObject.getString("order_count"));
                                        direct_t_entity.setCountry(jsonObject.getString("country"));
                                        direct_t_entity.setUser_device_type(jsonObject.getString("user_device_type"));
                                        direct_t_entity.setMobile_visible(jsonObject.getString("mobile_visible"));
                                        direct_t_entity.setCity_to_cover(jsonObject.getString("city_to_cover"));
                                        direct_t_entity.setDistance(jsonObject.getString("distance"));
                                        direct_t_entity.setUser_bank_name(jsonObject.getString("user_bank_name"));
                                        direct_t_entity.setUser_bank_account_number(jsonObject.getString("user_bank_account_number"));
                                        direct_t_entity.setUser_bank_iban_number(jsonObject.getString("user_bank_iban_number"));
                                        direct_t_entity.setIs_favourite(jsonObject.getString("is_favourite"));
                                        JSONArray jsonArray1 = jsonObject.getJSONArray("services");
                                        servicemap.put(jsonObject.getString("user_id"), jsonArray1);
                                        ArrayList<String> service_name_list=new ArrayList<>();
                                        ArrayList<String> category_name_list=new ArrayList<>();
                                        ArrayList<String> service_id_list=new ArrayList<>();
                                        ArrayList<String> category_id_list=new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            JSONObject json=jsonArray1.getJSONObject(j);
                                            Provider_List_Entity.Service service=new Provider_List_Entity.Service();
                                            service.setUser_service_id(json.getString("user_service_id"));
                                           // service.setSub_service_id(json.getString("sub_service_id"));
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


                                         /*   JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            direct_t_entity.setUser_service_id(jsonObject1.getString("user_service_id"));
                                            direct_t_entity.setSub_service_id(jsonObject1.getString("service_id"));
                                            direct_t_entity.setMain_category_id(jsonObject1.getString("main_category_id"));
                                            //service_id_list.add(jsonObject1.getString("sub_service_id"));
                                           // service.setMain_category_id(json.getString("main_category_id"));
                                            direct_t_entity.setService_id_list(service_id_list);
                                            direct_t_entity.setAdded_date(jsonObject1.getString("added_date"));
                                            category_name_list.add(jsonObject1.getString("category_name"));
                                            direct_t_entity.setCategory_name_list(category_name_list);
                                            service_name_list.add(jsonObject1.getString("service_name"));
                                            direct_t_entity.setService_name_list(service_name_list);
                                            //direct_t_entity.setSub_service_name(jsonObject1.getString"sub_service_name"));*/
                                        }
                                        direct_t_entity.setCategory_id_list(category_id_list);
                                        direct_t_entity.setCategory_name_list(category_name_list);
                                        direct_t_entity.setService_id_list(service_id_list);
                                        direct_t_entity.setService_name_list(service_name_list);
                                        direct_t_entity.setAll_service_list(all_service_list);
                                        list.add(direct_t_entity);
                                        Log.e(TAG,"LIST_SIZE"+list.size());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (page == 0) {
                                    direct_list.addAll(list);
                                    if (direct_list.size() > 0) {
                                        error2.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);


                                        Log.e(TAG,"MOBILE_NUMBER_DIRECT");
                                        sp_contact_list_adapter=new Sp_contact_list_Adapter(getActivity(),direct_list,servicemap);

                                        recyclerView.setAdapter(sp_contact_list_adapter);
                                    } else {

                                        Log.e(TAG,"ERROR MESSAGE");
                                        recyclerView.setVisibility(View.GONE);
                                        error2.setVisibility(View.VISIBLE);
                                        curSize = direct_list.size();
//                                        sp_contact_list_adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    curSize = direct_list.size();
                                    direct_list.addAll(curSize, list);
                                    sp_contact_list_adapter.notifyItemInserted(curSize);
                                    sp_contact_list_adapter.notifyItemRangeChanged(curSize, direct_list.size());
                                    sp_contact_list_adapter.notifyDataSetChanged();
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

                Log.e(TAG,"flagvalue........>"+flagvalue);

                if(flagvalue==0)
                {
                    params.put("action", "Directserviceproviderslist");
                    params.put("latitude", lat);
                    params.put("longitude", lang);
                    params.put("mobile_numbers",helper.phone_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",").replaceAll(" ", ""));
                    //params.put("mobile_numbers","0561699345,8885555512");
                    //params.put("mobile_numbers","18003001947,8885555512,12354687388,,4155553695,7184302106,7137984842,5555228243");
                    params.put("page", String.valueOf(page));
                    params.put("user_id", ssp.getUserId(getActivity())); //ADD JAY


                   // Log.e(TAG,"helper.phone_list........>"+ helper.phone_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",").replaceAll(" ", ""));
                   // Log.e(TAG,helper.phone_list+".......helper.phone_list........"+ helper.phone_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",").replaceAll(" ", ""));

                    //TEMP ONLYE
                    /*String phone_list = helper.phone_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",").replaceAll(" ", "");
                    if(!phone_list.equalsIgnoreCase(""))
                    {
                        if(phone_list.contains("+"))
                        {
                            String[] separated = phone_list.split(""+"");
                            Log.e(TAG,"separated>>>>>>"+separated[0]);
                        }
                    }*/



                    Log.e("params........>", params.toString());
                }

                /*else if(flagvalue==1) // CLAIENT ALRADY COMMENTED THIS CODE
                {
                    params.put("action", "Sortproviders");
                    params.put("latitude", lat);
                    params.put("longitude", lang);
                    params.put("user_id", ssp.getUserId(getActivity()));
                    params.put("sort_type", helper.sort_type);
                    params.put("sort_by",helper.sort_by);
                    //Log.e("params22222", params.toString());
                }
                else if(flagvalue==2)
                {
                    if(!TextUtils.isEmpty(helper.sort_type)&&!TextUtils.isEmpty(helper.sort_by)) {

                        params.put("action", "Filterproviders");
                        params.put("latitude", lat);
                        params.put("longitude", lang);
                        params.put("user_id", ssp.getUserId(getActivity()));
                       *//* params.put("sort_type", helper.sort_type);
                        params.put("sort_by", helper.sort_by);*//*
                        params.put("distance", String.valueOf(helper.filter_distance));
                        params.put("city_to_cover", helper.city_to_id_list);
                        params.put("services", helper.service_id_list);
                        params.put("online_offline_status", helper.online_offline_status);
                        //Log.e("params333333", params.toString());

                    }
                    else if(!helper.city_to_id_list.equals("")&&helper.sort_by.equals(""))
                    {
                        params.put("action", "Filterproviders");
                        params.put("latitude", lat);
                        params.put("longitude",lang);
                        params.put("user_id", ssp.getUserId(getActivity()));
                      //  params.put("sort_type", helper.sort_type);
                       // params.put("sort_by", helper.sort_by);
                        params.put("distance", String.valueOf(helper.filter_distance));
                        params.put("city_to_cover", helper.city_to_id_list);
                        params.put("services", helper.service_id_list);
                        params.put("online_offline_status", helper.online_offline_status);
                        //Log.e("params333333", params.toString());
                    }
                }*/

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
                Manifest.permission.WRITE_CONTACTS)) {

            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, RequestPermissionCode);

        }
    }




}
