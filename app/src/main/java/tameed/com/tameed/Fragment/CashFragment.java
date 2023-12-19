package tameed.com.tameed.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import tameed.com.tameed.Adapter.Cash_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Cash_Report;
import tameed.com.tameed.Entity.All_Payment_Entity;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

public class CashFragment extends Fragment {
    ImageView header_back, radio_all, radio_paid, radio_unpaid;
    TextView header_txt;
    ConstraintLayout constraint_all, constraint_paid, constraint_unpaid;
    RecyclerView recyclerView;
    Button cash_report;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    SaveSharedPrefernces ssp;
    Activity act;

    ArrayList<String> cash_detail = new ArrayList<>();
    String tab_type;

    ArrayList<All_Payment_Entity> paymrnt_list = new ArrayList<>();

    Cash_Adapter cash_adapter;
    LinearLayoutManager linearLayoutManager;

    int page = 0, curSize;
    String result_count = "";

    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    TextView error_msg;
    String customer;
    TextView user_type_all;

    private String TAG = "CashFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());

        View rootView = inflater.inflate(R.layout.fragment_cash, container, false);
        ////Log.e(TAG, "****************************");
        ssp = new SaveSharedPrefernces();
        act = getActivity();
        cash_report = (Button) rootView.findViewById(R.id.fr_cash_cash_report);

        header_txt = (TextView) rootView.findViewById(R.id.txt_header);
        header_txt.setText("التقارير");

        error_msg = (TextView) rootView.findViewById(R.id.error_msg);
        header_back = (ImageView) rootView.findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.frcash_payment_recycle);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (AppController.isOnline(getActivity())) {

            tab_type = "All";
            utiltyRequest();
        } else {
            AppController.showAlert(getActivity(),
                    getString(R.string.networkError_Message));
        }

        constraint_all = (ConstraintLayout) rootView.findViewById(R.id.frcash_constarint_all);
        constraint_paid = (ConstraintLayout) rootView.findViewById(R.id.fr_constraint_paid);
        constraint_unpaid = (ConstraintLayout) rootView.findViewById(R.id.fr_cash_constraint_unpaid);
        radio_all = (ImageView) rootView.findViewById(R.id.radio_all);
        radio_paid = (ImageView) rootView.findViewById(R.id.radio_paid);
        radio_unpaid = (ImageView) rootView.findViewById(R.id.radio_unpaid);


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


        user_type_all = rootView.findViewById(R.id.user_type_all);

        if (ssp.getUser_type(act).equals("Provider")) {
            user_type_all.setText("مقدم الخدمة");
            user_type_all.setVisibility(View.VISIBLE);
        } else {
            user_type_all.setText("العميل");
            user_type_all.setVisibility(View.VISIBLE);
        }


        customer = ssp.getUser_type(act);


        user_type_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(getActivity(), R.style.DialogSlideAnim);
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
                        page = 0;
                        visibleThreshold = 5;
                        firstVisibleItem = 0;
                        visibleItemCount = 0;
                        totalItemCount = 0;
                        paymrnt_list.clear();
                        customer = "Customer";
                        user_type_all.setText("العميل");
                        utiltyRequest();
                        dialog1.dismiss();
                    }
                });


                provider_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////Log.e("Provider", "555555");
                        customer = "Provider";
                        recyclerView.removeAllViews();
                        recyclerView.removeAllViewsInLayout();
                        loading = true;
                        previousTotal = 0;
                        page = 0;
                        visibleThreshold = 5;
                        firstVisibleItem = 0;
                        visibleItemCount = 0;
                        totalItemCount = 0;
                        paymrnt_list.clear();
                        user_type_all.setText("مقدم الخدمة");
                        utiltyRequest();

                        dialog1.dismiss();
                    }
                });
//                if(customer.equals("Customer"))
//                {
//                    ////Log.e("Customer","222222");
//                    user_type_all.setText(customer);
//                }
//                else
//                {
//                    user_type_all.setText(customer);
//                }


               /* if (bool) {

                    user_type="Customer";
                    user_type_all.setText("Customer");
                    user_type_all.setVisibility(View.VISIBLE);


                } else {
//
                    user_type="Provider";
                    user_type_all.setText("Provider");
                    user_type_all.setVisibility(View.VISIBLE);

                            *//*else
                            {
                                notification_clear_all.setText("Customer");
                                notification_clear_all.setVisibility(View.VISIBLE);
                            }//


                }
                bool = !bool;*/

                // dialog.show();
            }
        });


        constraint_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.pay = 1;
                radio_all.setImageResource(R.mipmap.radio_checked);
                radio_paid.setImageResource(R.mipmap.radio_w2x);
                radio_unpaid.setImageResource(R.mipmap.radio_w2x);


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
                page = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                paymrnt_list.clear();
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
                tab_type = "Unpaid";
                utiltyRequest();
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
                radio_paid.setImageResource(R.mipmap.radio_w2x);
                radio_unpaid.setImageResource(R.mipmap.radio_checked);


            }
        });


        cash_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Cash_Report.class));
            }
        });
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
//        if (page==0) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getActivity().getResources().getString(R.string.loading);
        //  }


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
                            ////Log.e("all_payments",jsonArray+"");
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
                                        cash_adapter = new Cash_Adapter(getActivity(), paymrnt_list);
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

                        //}
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

                params.put("action", "Paymenthistory");
                params.put("user_id", ssp.getUserId(act));


                params.put("user_type", customer);

                params.put("tab_type", tab_type);
                params.put("page", String.valueOf(page));


                ////Log.e("params", params.toString());
                return params;
            }

        };


//
// add(stringRequest);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


}
