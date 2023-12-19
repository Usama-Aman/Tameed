package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import tameed.com.tameed.Adapter.Cash_Adapter;
import tameed.com.tameed.Entity.All_Payment_Entity;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by dev on 16-01-2018.
 */

public class Payment_type extends AppCompatActivity {

    ImageView header_back, radio_all, radio_paid, radio_unpaid;
    TextView header_txt;
    ConstraintLayout constraint_all, constraint_paid, constraint_unpaid;
    private String TAG = "Payment_type";

    Button cash_report;

    RecyclerView recyclerView;

    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    SaveSharedPrefernces ssp;
    Activity act;
    ArrayList<String> payment_detail = new ArrayList<>();
    ArrayList<Integer> payment_detail1 = new ArrayList<>();
    ArrayList<All_Payment_Entity> paymrnt_list = new ArrayList<>();

    Cash_Adapter cash_adapter;
    String tab_type;


    int page = 0, curSize;
    String result_count = "";

    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    TextView error_msg;

    String customer;
    TextView user_type_all;

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
        setContentView(R.layout.payment_type);
        ssp = new SaveSharedPrefernces();
        act = Payment_type.this;
        cash_report = (Button) findViewById(R.id.cash_report);
        error_msg = (TextView) findViewById(R.id.error_msg);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("نوع الدفع");

        ////Log.e(TAG, "****************************");
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.payment_recycle);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        constraint_all = (ConstraintLayout) findViewById(R.id.constarint_all);
        constraint_paid = (ConstraintLayout) findViewById(R.id.constraint_paid);
        constraint_unpaid = (ConstraintLayout) findViewById(R.id.constraint_unpaid);
        radio_all = (ImageView) findViewById(R.id.radio_all);
        radio_paid = (ImageView) findViewById(R.id.radio_paid);
        radio_unpaid = (ImageView) findViewById(R.id.radio_unpaid);
        tab_type = "All";

        constraint_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                paymrnt_list.clear();
                page = 0;
                radio_all.setImageResource(R.mipmap.radio_checked);
                radio_paid.setImageResource(R.mipmap.radio_w2x);
                radio_unpaid.setImageResource(R.mipmap.radio_w2x);
                tab_type = "All";
                utiltyRequest();


            }
        });
        constraint_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                paymrnt_list.clear();
                page = 0;
                radio_all.setImageResource(R.mipmap.radio_w2x);
                radio_paid.setImageResource(R.mipmap.radio_checked);
                radio_unpaid.setImageResource(R.mipmap.radio_w2x);
                tab_type = "Paid";
                utiltyRequest();
            }
        });

        constraint_unpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                page = 0;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                paymrnt_list.clear();
                radio_all.setImageResource(R.mipmap.radio_w2x);
                radio_paid.setImageResource(R.mipmap.radio_w2x);
                radio_unpaid.setImageResource(R.mipmap.radio_checked);
                tab_type = "Unpaid";
                utiltyRequest();
            }
        });


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
                ////Log.e("LogEntity1.size()", "" + paymrnt_list.size());
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
                        if (paymrnt_list.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);

                            page = page + 1;
                            utiltyRequest();


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


        cash_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cash_Report.class));
            }
        });


        user_type_all = findViewById(R.id.user_type_all);
        user_type_all.setText(ssp.getUser_type(act));
        user_type_all.setVisibility(View.VISIBLE);


        customer = ssp.getUser_type(act);
        utiltyRequest();


        user_type_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(act, R.style.DialogSlideAnim);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.custom_cash_report);

                dialog1.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
                final RelativeLayout customer_type = (RelativeLayout) dialog1.findViewById(R.id.customer);
                final RelativeLayout provider_type = (RelativeLayout) dialog1.findViewById(R.id.provider);
                final TextView customer_txt = dialog1.findViewById(R.id.customer_txt);
                final TextView provider_txt = dialog1.findViewById(R.id.provider_txt);


                customer_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////Log.e("Customer", "44444444");
                        recyclerView.removeAllViews();
                        recyclerView.removeAllViewsInLayout();
                        loading = true;
                        previousTotal = 0;
                        visibleThreshold = 5;
                        page = 0;
                        firstVisibleItem = 0;
                        visibleItemCount = 0;
                        totalItemCount = 0;
                        paymrnt_list.clear();
                        customer = "Customer";
                        user_type_all.setText("عميل");
                        utiltyRequest();
                        dialog1.dismiss();
                    }
                });


                provider_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////Log.e("Provider", "555555");
                        customer = "Provider";
                        user_type_all.setText("مقدم الخدمة");
                        recyclerView.removeAllViews();
                        recyclerView.removeAllViewsInLayout();
                        loading = true;
                        previousTotal = 0;
                        visibleThreshold = 5;
                        page = 0;
                        firstVisibleItem = 0;
                        visibleItemCount = 0;
                        totalItemCount = 0;
                        paymrnt_list.clear();
                        utiltyRequest();

                        dialog1.dismiss();
                    }
                });
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


    private void utiltyRequest() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        final ArrayList<New_Order_Entity> order_list = new ArrayList<>();

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

                            JSONArray jsonArray = obj.getJSONArray("all_payments");
                            ArrayList<All_Payment_Entity> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                All_Payment_Entity payment_entity = new All_Payment_Entity();
                                payment_entity.setOrder_id(object.getString("order_id"));
                                payment_entity.setLast_modified_date(object.getString("last_modified_date"));
                                payment_entity.setOrder_cost(object.getString("order_cost"));
                                payment_entity.setPayment_status(object.getString("payment_status"));
                                list.add(payment_entity);

                            }

                            result_count = obj.getString("result_count");


                            if (page == 0) {
                                curSize = 0;
                                ////Log.e("6666", "66666");
                                paymrnt_list.addAll(list);

                                ////Log.e("List_Size", "===" + paymrnt_list.size());

                                try {

                                    if (paymrnt_list.size() > 0) {
                                        ////Log.e("7777", "7777");
                                        ////Log.e("List_Size>0", "===" + paymrnt_list.size());
                                        error_msg.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        cash_adapter = new Cash_Adapter(act, paymrnt_list);
                                        recyclerView.setAdapter(cash_adapter);
                                    } else {
                                        ////Log.e("88888", "8888");
                                        ////Log.e("List_Size", "===" + paymrnt_list.size());

                                        error_msg.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);

                                    }

                                    curSize = paymrnt_list.size();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ////Log.e("9999", "9999");
                                curSize = paymrnt_list.size();
                                paymrnt_list.addAll(curSize, list);
                                cash_adapter.notifyItemInserted(curSize);
                                cash_adapter.notifyItemRangeChanged(curSize, paymrnt_list.size());
                                cash_adapter.notifyDataSetChanged();
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

                            Toast.makeText(Payment_type.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Payment_type.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Payment_type.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Payment_type.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Paymenthistory");
                params.put("user_id", ssp.getUserId(act));


                params.put("user_type", customer);

                params.put("tab_type", tab_type);

                ////Log.e("params", params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);

    }


}

