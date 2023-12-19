package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Typeface;
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

import tameed.com.tameed.Adapter.Country_List_Adapter;
import tameed.com.tameed.Entity.Country_list_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;

public class Country_list extends AppCompatActivity {
    RecyclerView country_recycle;
    TextView header_txt,error;
    ImageView header_back;
    Country_List_Adapter country_list_adapter;
    ArrayList<Country_list_Entity> country_list = new ArrayList<Country_list_Entity>();
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    LinearLayoutManager layoutManager;
    Typeface typeface;
    private String TAG = "Country_list";

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
        setContentView(R.layout.country_list);
        //Log.e(TAG,"****************************");
        header_txt=(TextView)findViewById(R.id.txt_header);
        header_txt.setText("قائمة البلد");

        header_back=(ImageView)findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        country_recycle= (RecyclerView) findViewById(R.id.country_recycle);




        layoutManager=new LinearLayoutManager(Country_list.this);
        country_recycle.setLayoutManager(layoutManager);

        if (AppController.isOnline(Country_list.this)) {

            callingcode();
        } else {
            AppController.showAlert(Country_list.this,
                    getString(R.string.networkError_Message));
        }




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


    private void callingcode() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj=null;
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
                            ArrayList<Country_list_Entity> list = new ArrayList<Country_list_Entity>();

                            JSONArray jsonArray=obj.getJSONArray("result");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                Country_list_Entity country_list_entity=new Country_list_Entity();
                                JSONObject  jsonObject=jsonArray.getJSONObject(i);



                                country_list_entity.setCalling_code(jsonObject.getString("calling_code"));
                                country_list_entity.setCountry_name(jsonObject.getString("country_name"));
                               list.add(country_list_entity);

                            }
                            country_list.addAll(list);
                            ////Log.e("country_list444",list.toString());

                          if(country_list.size()>0)
                          {
                              country_list_adapter = new Country_List_Adapter(Country_list.this,country_list);
                              country_recycle.setAdapter(country_list_adapter);
                          } else {
                              country_recycle.setVisibility(View.GONE);
                              error.setVisibility(View.VISIBLE);
                          }

                        /*    service_list= new String[list_service.size()];
                            service_list = list_service.toArray(service_list);
                            code_list=new String[country_code_list.size()];
                            code_list=country_code_list.toArray(code_list);
                            for(int i = 0; i < service_list.length ; i++){
                                ////Log.e("string is",(String)service_list[i]);
//                                ////Log.e("DATA","<><>"+list_suburb.get(i));
                            }*/
                            /*txt_country_code.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        builder = new AlertDialog.Builder(Country_list.this,
                                                AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        builder = new AlertDialog.Builder(Country_list.this);
                                    }
//                                          builder.setTitle("Select currency");

                                    builder.setSingleChoiceItems(service_list, -1,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int item) {

                                                    String b_name = "";

                                                    calling_code2 = code_list[item];

                                                    txt_country_code.setText(calling_code2);


//                                        sub_name = name;

//
//                                        String contid = code_list.get(item);
                                                    dialog.dismiss();

                                                }
                                            });

                                    AlertDialog alert = builder.create();
                                    alert.show();

                                }
                            });*/





//
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e) {
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

                            Toast.makeText(Country_list.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(Country_list.this,getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(Country_list.this,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(Country_list.this,getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

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
                params.put("action","Countrieslist");
                ////Log.e("params", params.toString());
                return params;
            }

        };



        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60*1000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }



    @SuppressLint("InlinedApi") private void showProgDialog() {
        progress_dialog = null;
        try{
            if (Build.VERSION.SDK_INT >= 11 ) {
                progress_dialog = new ProgressDialog(Country_list.this, AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(Country_list.this);
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

    @Override
    public void onBackPressed() {

    }
}
