package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
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

import tameed.com.tameed.Adapter.Cities_Adapter;
import tameed.com.tameed.Entity.Cities_list_entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
/**
 * Created by dev on 03-02-2018.
 */

public class Cities extends AppCompatActivity {

    TextView header_txt;
    ImageView header_back;
    ArrayList<Cities_list_entity> Cities_detail=new ArrayList<Cities_list_entity>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Cities_Adapter cities_adapter;

    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    int page = 0,curSize;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount=0;
    private int previousTotal = 0;
    String result_count = "";
    String refresh = "";
    int  flagvalue=0;
    private boolean loading = true;
    private String TAG = "ChatActivity";

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
        setContentView(R.layout.cities_list);

        recyclerView=(RecyclerView)findViewById(R.id.cities_recycle);
        linearLayoutManager=new LinearLayoutManager(Cities.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        header_txt=(TextView)findViewById(R.id.txt_header);
        header_txt.setText("مدن");

        header_back=(ImageView)findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        makeStringRequest();






        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                  ////Log.e("visibleItemCount", "" + visibleItemCount);
                ////Log.e("totalItemCount", "" + totalItemCount);
                    ////Log.e("firstVisibleItem", "" + firstVisibleItem);
                ////Log.e("LogEntity1.size()", "" + Cities_detail.size());
                    ////Log.e("result_count", result_count);
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
                        if (Cities_detail.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);
                            ////Log.e("33333", "33333");
                            page = page + 1;


                            makeStringRequest();


                        } else {
                            ////Log.e("44444", "444444");
                            Toast.makeText(Cities.this,
                                    "End of list", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
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
        progress_dialog_msg = getResources().getString(R.string.loading);;


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

                                ArrayList<Cities_list_entity> list = new ArrayList<Cities_list_entity>();

                                JSONArray jsonArray = obj.getJSONArray("cities");
                                result_count = obj.getString("result_count");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    Cities_list_entity cities_list_entity = new Cities_list_entity();

                                    cities_list_entity.setCity_name(jsonObject.getString("city_name"));
                                    cities_list_entity.setCity_id(jsonObject.getString("city_id"));

                                    list.add(cities_list_entity);




                                if (page == 0) {
                                    Cities_detail.addAll(list);
                                    try {

                                        if (Cities_detail.size() > 0) {

                                            cities_adapter = new Cities_Adapter(Cities.this, Cities_detail);
                                            recyclerView.setAdapter(cities_adapter);

                                        } else {
                                            recyclerView.setVisibility(View.GONE);

                                        }

                                        curSize = Cities_detail.size();
                                        cities_adapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {

                                    curSize = Cities_detail.size();
                                    Cities_detail.addAll(curSize, list);
                                    cities_adapter.notifyItemInserted(curSize);
                                    cities_adapter.notifyItemRangeChanged(curSize, Cities_detail.size());
                                    cities_adapter.notifyDataSetChanged();
                                }
                            }





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

                            Toast.makeText(Cities.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(Cities.this,getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(Cities.this,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(Cities.this,getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

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



                params.put("action", "Cities");
                params.put("page", String.valueOf(page));


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
                progress_dialog = new ProgressDialog(Cities.this, AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(Cities.this);
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


}