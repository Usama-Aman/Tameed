package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

import tameed.com.tameed.Adapter.Search_Service_Adapter;
import tameed.com.tameed.Adapter.Search_by_city_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.City_to_cover_Entity;
import tameed.com.tameed.Entity.Search_service_Entity;
import tameed.com.tameed.Util.Apis;
public class Search_by_city extends AppCompatActivity {
    TextView txtHeader;
    ImageView header_back,back_img;
    int flagvalue;
    EditText edt_search;
    ArrayList<City_to_cover_Entity>search_list=new ArrayList<>();
    ArrayList<Search_service_Entity>search_service_list=new ArrayList<>();
    RecyclerView search_by_city_recycle;
    private ProgressDialog progress_dialog;
    Search_Service_Adapter search_service_adapter;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String search_value,city_to_cover;
    LinearLayoutManager layoutManager,layoutManager2;
    Search_by_city_Adapter search_by_city_adapter;
    String s_name;
    private String TAG = "Search_by_city";


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
        setContentView(R.layout.search_by_city);

        header_back=(ImageView)findViewById(R.id.header_back);
        search_by_city_recycle=findViewById(R.id.search_by_city_recycle);
        ////Log.e("city_to_cover",helper.city_to_cover);
        ////Log.e("city_to_service",helper.city_to_service);
        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(Search_by_city.this,FilterActivity.class);
               startActivity(i);
            }
        });


        edt_search=findViewById(R.id.edt_search);
        search_value=edt_search.getText().toString();
        layoutManager=new LinearLayoutManager(Search_by_city.this);
        search_by_city_recycle.setLayoutManager(layoutManager);

        Intent i=getIntent();
        city_to_cover=i.getStringExtra("city_to_cover");

        if((helper.city_to_cover.equals("cities"))) {
             search_by_city();

        }
        else if((helper.filter_services.equals("services")))
        {

                search_by_city();

        }

        edt_search.addTextChangedListener(new TextWatcher() {
                                              @SuppressLint("DefaultLocale")
                                              @Override
                                              public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                                                  //swipeRefreshLayout.setRefreshing(false);
                                                  s_name = edt_search.getText().toString();

                                                  if((helper.city_to_cover.equals("cities"))) {
                                                      if (s_name.length() > 0) {
                                                          search_by_city();

                                                      }
                                                  }
                                                  else if((helper.filter_services.equals("services")))
                                                  {
                                                      if (s_name.length() > 0) {
                                                          search_by_city();
                                                      }
                                                  }


                                              }

                                              @Override
                                              public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                                            int arg3) {
                                                  // TODO Auto-generated method stub

                                              }

                                              @Override
                                              public void afterTextChanged(Editable arg0) {

                                                  // TODO Auto-generated method stub

                                              }
                                          }

        );

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


    private void search_by_city() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;

                        if((helper.city_to_cover.equals("cities")))
                        {
                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////Log.e("Json Data", response.toString().substring(start1, end));
                                }

                                JSONArray jsonArray = obj.getJSONArray("searched_results");
                                ArrayList<City_to_cover_Entity> list = new ArrayList<City_to_cover_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    City_to_cover_Entity city_to_cover_entity = new City_to_cover_Entity();
                                    city_to_cover_entity.setCity_id(jsonObject.getString("city_id"));
                                    city_to_cover_entity.setAdded_date(jsonObject.getString("added_date"));
                                    city_to_cover_entity.setCity_name(jsonObject.getString("city_name"));
                                    list.add(city_to_cover_entity);

                                }
                                search_list.addAll(list);
                                if (search_list.size() > 0) {
                                    search_by_city_recycle.setVisibility(View.VISIBLE);
                                    search_by_city_adapter = new Search_by_city_Adapter(Search_by_city.this, search_list);
                                    search_by_city_recycle.setAdapter(search_by_city_adapter);
                                } else {
                                    search_by_city_recycle.setVisibility(View.GONE);
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
                    else if ((helper.filter_services.equals("services")))
                        {

                            try {
                                obj = new JSONObject(response.toString());
                                int maxLogSize = 1000;
                                for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start1 = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > response.length() ? response.toString().length() : end;
                                    ////Log.e("Json Data", response.toString().substring(start1, end));
                                }

                                JSONArray jsonArray = obj.getJSONArray("searched_results");

                                ArrayList<Search_service_Entity> list1 = new ArrayList<Search_service_Entity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Search_service_Entity search_service_entity=new Search_service_Entity();
                                    search_service_entity.setAdded_date(jsonObject.getString("added_date"));
                                    search_service_entity.setCategory_id(jsonObject.getString("category_id"));
                                    search_service_entity.setIs_enable(jsonObject.getString("is_enable"));
                                    search_service_entity.setService_name(jsonObject.getString("service_name"));
                                    search_service_entity.setType(jsonObject.getString("type"));
                                    search_service_entity.setService_id(jsonObject.getString("service_id"));
                                    list1.add(search_service_entity);

                                }
                                search_service_list.addAll(list1);
                                if (search_service_list.size() > 0) {
                                    search_by_city_recycle.setVisibility(View.VISIBLE);
                                    search_service_adapter = new Search_Service_Adapter(Search_by_city.this, search_service_list);
                                    search_by_city_recycle.setAdapter(search_service_adapter);
                                } else {
                                    search_by_city_recycle.setVisibility(View.GONE);
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

                            Toast.makeText(Search_by_city.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(Search_by_city.this,getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(Search_by_city.this,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(Search_by_city.this,getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

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

                  if (helper.filter_services.equals("services")) {
                    params.put("action", "Searchcityservices");
                    params.put("keywords","");
                    ////Log.e("444444","444444");
                    params.put("searched_for", helper.filter_services);
                }
                else if (helper.city_to_cover.equals("cities")) {
                    params.put("action", "Searchcityservices");
                    params.put("keywords","");
                    ////Log.e("55555","555555");
                    params.put("searched_for", helper.city_to_cover);
                }

                  else if (helper.city_to_cover.equals("cities")) {
                        params.put("action", "Searchcityservices");
                        params.put("keywords", s_name);
                        params.put("searched_for", helper.city_to_cover);
                    }
                    else if (helper.filter_services.equals("services")) {
                        params.put("action", "Searchcityservices");
                        params.put("keywords", s_name);
                        params.put("searched_for", helper.filter_services);
                    }

                    ////Log.e("params", params.toString());

                return params;
            }

        };



        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60*1000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);



    }



    @SuppressLint("InlinedApi") private void showProgDialog() {
        progress_dialog = null;
        try{
            if (Build.VERSION.SDK_INT >= 11 ) {
                progress_dialog = new ProgressDialog(Search_by_city.this, AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(Search_by_city.this);
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
