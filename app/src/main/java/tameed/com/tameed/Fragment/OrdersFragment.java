package tameed.com.tameed.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import tameed.com.tameed.Adapter.OrderAdapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class OrdersFragment extends Fragment {

    ConstraintLayout client, provider;
    Boolean bool = true;

    Button completed, cancelled, rejected;
    TextView bt_new, pending;
    ArrayList<String> order_detail = new ArrayList<>();
    ArrayList<New_Order_Entity> order_detail_list = new ArrayList<>();
    SaveSharedPrefernces ssp;
    Activity act;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    int page = 0, curSize;
    String result_count = "";


    public static SwipeRefreshLayout my_swipe_refresh_layout2;

    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    TextView error_msg;
    OrderAdapter orderAdapter;
    String all_pending_order_as_customer, all_new_order_as_provider, total_order_badge_count;
    TextView badge_new, badge_pending;
    private String TAG = "OrdersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());

        View rootView = inflater.inflate(R.layout.order_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.order_recy);
        act = getActivity();
        ssp = new SaveSharedPrefernces();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        recyclerView.setLayoutManager(layoutManager);


        badge_new = (TextView) rootView.findViewById(R.id.badge_new);
        badge_pending = (TextView) rootView.findViewById(R.id.badge_pending);
        completed = (Button) rootView.findViewById(R.id.bt_completed);
        pending = (TextView) rootView.findViewById(R.id.bt_pending);
        bt_new = (TextView) rootView.findViewById(R.id.bt_new);
        cancelled = (Button) rootView.findViewById(R.id.bt_cancelled);
        rejected = (Button) rootView.findViewById(R.id.bt_rejected);

        my_swipe_refresh_layout2 = rootView.findViewById(R.id.my_swipe_refresh_layout2);

        my_swipe_refresh_layout2.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);

        my_swipe_refresh_layout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                ////Log.e("refreshh", "333" + refresh);
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                order_detail_list.clear();
                //customerorderlist_orderdetail_second.clear();
                //list.clear();

                makeStringReq();
            }
        });


        error_msg = (TextView) rootView.findViewById(R.id.error_msg);

        provider = (ConstraintLayout) rootView.findViewById(R.id.con_ser_provider);
        client = (ConstraintLayout) rootView.findViewById(R.id.con_as_client);

        if (helper.pblc == 1) {
            provider.setBackgroundResource(R.drawable.shape_primary_color2);
            client.setBackgroundResource(R.drawable.shape_plg);

//            helper.list_type="New";


            if (helper.list_type.equals("Rejected")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;

                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.White));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.black));
                cancelled.setTextColor(getResources().getColor(R.color.White));
                makeStringReq();
            } else if (helper.list_type.equals("Pending")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.black));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                makeStringReq();
            } else if (helper.list_type.equals("Completed")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.White));
                completed.setTextColor(getResources().getColor(R.color.black));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                makeStringReq();
            } else if (helper.list_type.equals("Cancelled")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.White));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.black));
                makeStringReq();
            } else {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.black));
                pending.setTextColor(getResources().getColor(R.color.white));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                if (AppController.isOnline(getActivity())) {

                    makeStringReq();
                } else {
                    AppController.showAlert(getActivity(),
                            getString(R.string.networkError_Message));
                }

            }


            //REMOVE JAY
            /*badge_pending.setVisibility(View.GONE);
            if (!badge_new.getText().toString().equals("0"))
            {
                badge_new.setVisibility(View.VISIBLE);
            } else {
                badge_new.setVisibility(View.GONE);
            }*/


            badge_pending.setVisibility(View.GONE);
            String bd_new = badge_new.getText().toString();
            //Log.e(TAG, "bd_new....1..." + bd_new);
            if (!bd_new.equalsIgnoreCase("")) {
                if (bd_new.equalsIgnoreCase("0")) {
                    badge_new.setVisibility(View.GONE);
                } else {
                    badge_new.setVisibility(View.VISIBLE);
                }
            }


        } else {


            if (helper.list_type.equals("Rejected")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.White));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.black));
                cancelled.setTextColor(getResources().getColor(R.color.White));
                makeStringReq();
            } else if (helper.list_type.equals("Pending")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.black));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                makeStringReq();
            } else if (helper.list_type.equals("Completed")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.White));
                completed.setTextColor(getResources().getColor(R.color.black));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                makeStringReq();
            } else if (helper.list_type.equals("Cancelled")) {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.White));
                pending.setTextColor(getResources().getColor(R.color.White));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.black));
                makeStringReq();
            } else {
                recyclerView.removeAllViews();
                recyclerView.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.black));
                pending.setTextColor(getResources().getColor(R.color.white));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                makeStringReq();
            }

            client.setBackgroundResource(R.drawable.shape_primary_color);
            provider.setBackgroundResource(R.drawable.shape_plg2);
//            helper.list_type="New";
//            helper.pblc=0;



            /*//Log.e(TAG,"PANNDIN BADGET 1==>"+badge_pending.getText().toString());
            //REMOVE JAY
            if (!badge_pending.getText().toString().equals("0"))
            {
                badge_pending.setVisibility(View.VISIBLE);
            } else {
                badge_pending.setVisibility(View.GONE);
            }
            badge_new.setVisibility(View.GONE);*/


            badge_new.setVisibility(View.GONE);
            String pd_new = badge_pending.getText().toString();
            //Log.e(TAG, "pd_new....1..." + pd_new);
            if (!pd_new.equalsIgnoreCase("")) {
                if (pd_new.equalsIgnoreCase("0")) {
                    badge_pending.setVisibility(View.GONE);
                } else {
                    badge_pending.setVisibility(View.VISIBLE);
                }
            }

        }


        provider.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                helper.pblc = 1;
                layoutManager.removeAllViews();
                recyclerView.removeAllViews();
                order_detail_list.clear();
                makeStringReq();
                provider.setBackgroundResource(R.drawable.shape_primary_color2);
                client.setBackgroundResource(R.drawable.shape_plg);

                badge_pending.setVisibility(View.GONE);
                String bd_new = badge_new.getText().toString();

                if (!bd_new.equalsIgnoreCase("")) {
                    if (bd_new.equalsIgnoreCase("0")) {
                        badge_new.setVisibility(View.GONE);
                    } else {
                        badge_new.setVisibility(View.VISIBLE);
                    }
                }


            }
        });

        client.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                helper.pblc = 0;
                layoutManager.removeAllViews();
                recyclerView.removeAllViews();
                order_detail_list.clear();
                makeStringReq();
                client.setBackgroundResource(R.drawable.shape_primary_color);
                provider.setBackgroundResource(R.drawable.shape_plg2);

                /*//Log.e(TAG,"PANNDIN BADGET 2==>"+badge_pending.getText().toString());
                //REMOVE JAY
                if (!badge_pending.getText().toString().equals("0")) {
                    badge_pending.setVisibility(View.VISIBLE);
                } else {
                    badge_pending.setVisibility(View.GONE);
                }
                badge_new.setVisibility(View.GONE);*/


                badge_new.setVisibility(View.GONE);
                String pd_new = badge_pending.getText().toString();
                Log.e(TAG, "pd_new....2..." + pd_new);
                if (!pd_new.equalsIgnoreCase("")) {
                    if (pd_new.equalsIgnoreCase("0")) {
                        badge_pending.setVisibility(View.GONE);
                    } else {
                        badge_pending.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        bt_new.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.black));
                pending.setTextColor(getResources().getColor(R.color.white));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                helper.list_type = "New";
                order_detail_list.clear();
                layoutManager.removeAllViews();
                recyclerView.removeAllViews();
                makeStringReq();

            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.white));
                pending.setTextColor(getResources().getColor(R.color.black));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                helper.list_type = "Pending";
                order_detail_list.clear();
                layoutManager.removeAllViews();
                recyclerView.removeAllViews();
                makeStringReq();

            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.white));
                pending.setTextColor(getResources().getColor(R.color.white));
                completed.setTextColor(getResources().getColor(R.color.black));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.white));
                helper.list_type = "Completed";
                layoutManager.removeAllViews();
                order_detail_list.clear();
                recyclerView.removeAllViews();
                makeStringReq();

            }
        });
        // TODO badge count logic
        rejected.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.white));
                pending.setTextColor(getResources().getColor(R.color.white));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.black));
                cancelled.setTextColor(getResources().getColor(R.color.white));

                helper.list_type = "Rejected";
                layoutManager.removeAllViews();
                recyclerView.removeAllViews();
                order_detail_list.clear();
                makeStringReq();

            }
        });
        cancelled.setOnClickListener(new View.OnClickListener() {
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
                order_detail_list.clear();
                page = 0;
                bt_new.setTextColor(getResources().getColor(R.color.white));
                pending.setTextColor(getResources().getColor(R.color.white));
                completed.setTextColor(getResources().getColor(R.color.white));
                rejected.setTextColor(getResources().getColor(R.color.white));
                cancelled.setTextColor(getResources().getColor(R.color.black));
                helper.list_type = "Cancelled";
                layoutManager.removeAllViews();
                order_detail_list.clear();
                recyclerView.removeAllViews();
                makeStringReq();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                //Log.e("visibleItemCount", "" + visibleItemCount);
                //Log.e("totalItemCount", "" + totalItemCount);
                //Log.e("firstVisibleItem", "" + firstVisibleItem);
                //Log.e("LogEntity1.size()", "" + order_detail_list.size());
                //Log.e("total_count", result_count);
                //Log.e("page", "" + page);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    //Log.e("page1", "" + page);
                    try {
                        if (order_detail_list.size() < Integer.parseInt(result_count)) {
                            //Log.e("page2", "" + page);

                            page = page + 1;
                            makeStringReq();


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

    private void makeStringReq() {
//        if (page==0) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getActivity().getResources().getString(R.string.loading);
        //  }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        ArrayList<New_Order_Entity> order_list = new ArrayList<>();

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());

                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                //Log.e("Json Data", response.toString().substring(start1, end));
                            }

                            //   String all_pending_order_as_customer,all_new_order_as_provider,total_order_badge_count;

                            all_pending_order_as_customer = obj.getString("all_pending_order_as_customer");
                            all_new_order_as_provider = obj.getString("all_new_order_as_provider");
                            total_order_badge_count = obj.getString("total_order_badge_count");

                            Log.e(TAG, "all_pending_order_as_customer...." + all_pending_order_as_customer);
                            Log.e(TAG, "all_new_order_as_provider........" + all_new_order_as_provider);
                            Log.e(TAG, "total_order_badge_count.........." + total_order_badge_count);


                            if (!total_order_badge_count.equals("0")) {
                                helper.badge_txt.setVisibility(View.VISIBLE);
                                helper.badge_txt.setText(total_order_badge_count);
                            } else {
                                helper.badge_txt.setVisibility(View.GONE);
                            }

                            //REMOVE JAY
                            badge_pending.setText(all_pending_order_as_customer);
                            badge_new.setText(all_new_order_as_provider);


                            if (all_new_order_as_provider.equalsIgnoreCase("0")) {
                                badge_new.setVisibility(View.GONE);
                            }

                            if (all_pending_order_as_customer.equalsIgnoreCase("0")) {
                                badge_pending.setVisibility(View.GONE);
                            }

                            //ADD JAY
                            /*if (helper.pblc == 1)
                            {
                                if(all_pending_order_as_customer.equalsIgnoreCase("0"))
                                {
                                    badge_pending.setText("");
                                    badge_pending.setVisibility(View.GONE);
                                }
                                else
                                {
                                    badge_pending.setText(all_pending_order_as_customer);
                                    badge_pending.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                //ADD JAY
                                if(all_new_order_as_provider.equalsIgnoreCase("0"))
                                {
                                    badge_new.setText("");
                                    badge_new.setVisibility(View.GONE);
                                }
                                else
                                {
                                    badge_new.setText(all_new_order_as_provider);
                                    badge_new.setVisibility(View.VISIBLE);
                                }

                            }*/


//                            if(!all_pending_order_as_customer.equals("0"))
//                            {
//                                badge_pending.setVisibility(View.VISIBLE);
                            // badge_pending.setText(all_pending_order_as_customer); //REMOVE JAY

//                            }
//                            else
//                            {
//                                badge_pending.setVisibility(View.GONE);
//                            }

//                            if(!all_new_order_as_provider.equals("0")) {
//                                badge_new.setVisibility(View.VISIBLE);
                            // badge_new.setText(all_new_order_as_provider); //REMOVE JAY
//                            }
//                            else
//                            {
//                                badge_new.setVisibility(View.GONE);
//                            }
                            if (obj.has("result_count")) {
                                result_count = obj.getString("result_count");
                                if (TextUtils.isEmpty(result_count)) {
                                    result_count = "0";
                                }

                            }
                            Log.e(TAG, "OBJECT" + obj);
                            JSONArray jsonArray = obj.getJSONArray("orders");
                            Log.e(TAG, "JSONARRAY" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                New_Order_Entity order_entity = new New_Order_Entity();
                                JSONObject object = jsonArray.getJSONObject(i);

                                order_entity.setName(object.getString("name"));
                                order_entity.setReview_count(object.getString("review_count"));
                                order_entity.setReview_rating(object.getString("review_rating"));
                                order_entity.setOrder_id(object.getString("order_id"));
                                order_entity.setCustomer_id(object.getString("customer_id"));
                                order_entity.setProvider_id(object.getString("provider_id"));
                                order_entity.setOrder_description(object.getString("order_description"));
                                order_entity.setServing_date(object.getString("serving_date"));
                                order_entity.setServing_time(object.getString("serving_time"));
                                order_entity.setOrder_status(object.getString("order_status"));
                                order_entity.setOrder_cancel_reason(object.getString("order_cancel_reason"));
                                order_entity.setOrder_cost(object.getString("order_cost"));
                                order_entity.setCancelled_by(object.getString("cancelled_by"));
                                order_entity.setOrder_type(object.getString("order_type"));
                                order_entity.setSort_order(object.getString("sort_order"));
                                order_entity.setLatitude(object.getString("latitude"));
                                order_entity.setLongitude(object.getString("longitude"));
                                order_entity.setLocation(object.getString("location"));
                                order_entity.setTotal_fee(object.getString("total_fee"));
                                order_entity.setLast_modified_date(object.getString("last_modified_date"));
                                order_entity.setAdded_date(object.getString("added_date"));
                                order_entity.setAdmin_commission(object.getString("admin_commission"));
                                order_entity.setPercentage(object.getString("percentage"));
                                order_entity.setService_fee(object.getString("service_fee"));
                                order_entity.setWarranty_days(object.getString("warranty_days"));
                                order_entity.setService_id(object.getString("service_id"));
                                order_entity.setCategory_id(object.getString("category_id"));
                                order_entity.setCategory_name(object.getString("category_name"));
                                order_entity.setService_name(object.getString("service_name"));
                                order_entity.setOrder_reference_number(object.getString("order_reference_number"));
                                JSONArray array = object.getJSONArray("order_pics");

                                ArrayList<New_Order_Entity.Order_Pic_Entity> pic_list = new ArrayList<>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject jsonObject = array.getJSONObject(j);
                                    New_Order_Entity.Order_Pic_Entity entity = new New_Order_Entity.Order_Pic_Entity();
                                    entity.setPic_id(jsonObject.getString("pic_id"));
                                    entity.setOrder_id(jsonObject.getString("order_id"));
                                    entity.setPic_url(jsonObject.getString("pic_url"));
                                    entity.setPic_1xthumb_url(jsonObject.getString("pic_1xthumb_url"));
                                    entity.setPic_2xthumb_url(jsonObject.getString("pic_2xthumb_url"));
                                    entity.setPic_3xthumb_url(jsonObject.getString("pic_3xthumb_url"));
                                    entity.setPic_type(jsonObject.getString("pic_type"));
                                    entity.setAdded_date(jsonObject.getString("added_date"));
                                    pic_list.add(entity);

                                }
                                if (object.has("order_rating")) {
                                    order_entity.setIs_review_given(object.getString("is_review_given"));
                                    order_entity.setOrder_rating(object.getString("order_rating"));
                                }

                                order_entity.setPic_list(pic_list);
                                order_list.add(order_entity);


                            }


                            if (page == 0) {
                                curSize = 0;
                                //Log.e("6666", "66666");
                                order_detail_list.addAll(order_list);

                                //Log.e("List_Size", "===" + order_detail_list.size());

                                try {

                                    if (order_detail_list.size() > 0) {
                                        //Log.e("7777", "7777");
                                        //Log.e("List_Size>0", "===" + order_detail_list.size());
                                        error_msg.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setRefreshing(false);
                                        orderAdapter = new OrderAdapter(getActivity(), order_detail_list);
                                        recyclerView.setAdapter(orderAdapter);
                                    } else {
                                        //Log.e("88888", "8888");
                                        //Log.e("List_Size", "===" + order_detail_list.size());
                                        my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        error_msg.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);

                                    }

                                    curSize = order_detail_list.size();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                //Log.e("9999", "9999");
                                curSize = order_detail_list.size();
                                order_detail_list.addAll(curSize, order_list);
                                orderAdapter.notifyItemInserted(curSize);
                                orderAdapter.notifyItemRangeChanged(curSize, order_detail_list.size());
                                orderAdapter.notifyDataSetChanged();
                            }

//                            order_detail_list.addAll(order_list);
//                            if (order_detail_list.size()>0) {
//                                error_msg.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                                OrderAdapter orderAdapter = new OrderAdapter(getActivity(), order_detail_list);
//                                recyclerView.setAdapter(orderAdapter);
//                            }
//                            else {
//                                error_msg.setVisibility(View.VISIBLE);
//                                recyclerView.setVisibility(View.GONE);
//                            }


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
                        helper.badge_txt.setVisibility(View.GONE);
                        badge_new.setVisibility(View.GONE);
                        badge_pending.setVisibility(View.GONE);

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
                params.put("action", "Orderslist");
                params.put("user_id", ssp.getUserId(act));
                params.put("order_type", helper.list_type);
                if (helper.pblc == 0) {
                    params.put("user_type", "Customer");
                } else {
                    params.put("user_type", "Provider");
                }

                params.put("page", String.valueOf(page));
                //Log.e("params", params.toString());
                return params;
            }

        };


//
// add(stringRequest);
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


}
