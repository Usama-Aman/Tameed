package tameed.com.tameed.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Send_public_order_Adapater;
import tameed.com.tameed.AdvertisementActivity;
import tameed.com.tameed.Entity.Public_Room_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.SendPublicOrder;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.Util.URLogs;

/**
 * Created by root on 24/1/18.
 */

public class Public_room_fragment extends Fragment {
    private TextView txtHeader;
    ConstraintLayout con_header;
    private Button proom, services, direct;
    ImageView direct_order, header_back;
    ArrayList<String> public_room = new ArrayList<>();
    RecyclerView public_room_recycle;
    TextView public_error;
    int firstVisibleItem, visibleItemCount, totalItemCount = 0;
    ArrayList<Public_Room_Entity> public_room_list = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    Send_public_order_Adapater send_public_order_adapater;

    SaveSharedPrefernces ssp;
    private String TAG = "Public_room_fragment";

    int page = 0, curSize;
    int flagvalue;
    public static SwipeRefreshLayout my_swipe_refresh_layout2;
    String result_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    View loadmore_progress;
    private Handler adshandler;
    private boolean isPublicRoomActive = false;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String languageToLoad = "ar"; // your language
        mContext = getContext();
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
        View rootView = inflater.inflate(R.layout.activity_public_room, container, false);

        URLogs.m1("Public Roommmmmmmmmmm");

        con_header = (ConstraintLayout) rootView.findViewById(R.id.con_header);
        con_header.setBackgroundResource(R.color.light_green);

        ssp = new SaveSharedPrefernces();
        public_error = rootView.findViewById(R.id.public_error);
        loadmore_progress = (View) rootView.findViewById(R.id.loadmore_progress);

        txtHeader = (TextView) rootView.findViewById(R.id.txt_header);
        txtHeader.setText("إرسال للغرفة العامة");

        my_swipe_refresh_layout2 = rootView.findViewById(R.id.my_swipe_refresh_layout2);

        header_back = (ImageView) rootView.findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        direct_order = (ImageView) rootView.findViewById(R.id.add_direct_order);
        direct_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SendPublicOrder.class));
            }
        });

        ////Log.e(TAG, "****************************");

        public_room_recycle = (RecyclerView) rootView.findViewById(R.id.po_recy);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        public_room_recycle.setLayoutManager(linearLayoutManager);


        my_swipe_refresh_layout2.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);

        my_swipe_refresh_layout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                ////Log.e("refreshh", "333" + refresh);
                public_room_recycle.removeAllViews();
                public_room_recycle.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                public_room_list.clear();
                public_room(true);
            }
        });


        public_room_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = public_room_recycle.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                URLogs.m1(visibleItemCount + "----" + totalItemCount + "----" + firstVisibleItem + visibleThreshold);

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
                        if (public_room_list.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);
                            ////Log.e("33333", "33333");
                            page = page + 1;
                            public_room(false);


                        } else {
                            ////Log.e("44444", "444444");
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });


        public_room(true);
        return rootView;


    }

    Runnable showAds = new Runnable() {
        @Override
        public void run() {
            if (!AppController.isAdShowOnce) {
                if (isPublicRoomActive) {
                    startActivity(new Intent(mContext, AdvertisementActivity.class));
                    AppController.isAdShowOnce = true;
                    adshandler.removeCallbacks(this);
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        isPublicRoomActive = true;
        if (!AppController.isAdShowOnce) {
            if (adshandler != null)
                adshandler.removeCallbacks(showAds);
            adshandler = new Handler();
            adshandler.postDelayed(showAds, 10000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPublicRoomActive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isPublicRoomActive = false;
        adshandler.removeCallbacks(showAds);
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


    private void public_room(Boolean showLoader) {

        if (showLoader) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = getActivity().getResources().getString(R.string.loading);
        } else {
            loadmore_progress.setVisibility(View.VISIBLE);
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Publicroomdataresponse", ": " + response);
                        loadmore_progress.setVisibility(View.GONE);
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
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

                                result_count = obj.getString("result_count");
                                JSONArray jsonArray = obj.getJSONArray("orders");
                                ArrayList<Public_Room_Entity> list = new ArrayList<Public_Room_Entity>();
                                URLogs.m1("jsonArray.length(): " + jsonArray.length() + "---" + page);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    Public_Room_Entity public_room_entity = new Public_Room_Entity();
                                    public_room_entity.setName(jsonObject.getString("name"));
                                    public_room_entity.setReview_rating(jsonObject.getString("review_rating"));
                                    public_room_entity.setReview_count(jsonObject.getString("review_count"));
                                    public_room_entity.setOrder_id(jsonObject.getString("order_id"));
                                    public_room_entity.setCustomer_id(jsonObject.getString("customer_id"));
                                    public_room_entity.setProvider_id(jsonObject.getString("provider_id"));
                                    public_room_entity.setServing_dat(jsonObject.getString("serving_date"));
                                    public_room_entity.setServing_time(jsonObject.getString("serving_time"));
                                    public_room_entity.setOrder_status(jsonObject.getString("order_status"));
                                    public_room_entity.setOrder_cancel_reason(jsonObject.getString("order_cancel_reason"));
                                    public_room_entity.setOrder_cost(jsonObject.getString("order_cost"));
                                    public_room_entity.setCancelled_by(jsonObject.getString("cancelled_by"));
                                    public_room_entity.setOrder_type(jsonObject.getString("order_type"));
                                    public_room_entity.setSort_order(jsonObject.getString("sort_order"));
                                    public_room_entity.setLatitude(jsonObject.getString("latitude"));
                                    public_room_entity.setLongitude(jsonObject.getString("longitude"));
                                    public_room_entity.setLocation(jsonObject.getString("location"));

                                    public_room_entity.setTotal_fee(jsonObject.getString("total_fee"));
                                    public_room_entity.setLast_modified_date(jsonObject.getString("last_modified_date"));
                                    public_room_entity.setAdded_date(jsonObject.getString("added_date"));
                                    public_room_entity.setAdmin_commission(jsonObject.getString("admin_commission"));
                                    public_room_entity.setPercentage(jsonObject.getString("percentage"));
                                    public_room_entity.setService_fee(jsonObject.getString("service_fee"));
                                    public_room_entity.setWarranty_days(jsonObject.getString("warranty_days"));
                                    public_room_entity.setService_id(jsonObject.getString("service_id"));
                                    public_room_entity.setCategory_id(jsonObject.getString("category_id"));
                                    public_room_entity.setCategory_name(jsonObject.getString("category_name"));
                                    public_room_entity.setService_name(jsonObject.getString("service_name"));
                                    public_room_entity.setIs_yours(jsonObject.getString("is_yours"));
                                    public_room_entity.setOrder_pics(jsonObject.getString("order_pics"));
                                    public_room_entity.setOrder_reference_number(jsonObject.getString("order_reference_number"));

                                    public_room_entity.setIs_review_given(jsonObject.getString("is_review_given"));
                                    public_room_entity.setOrder_rating(jsonObject.getString("order_rating"));
                                    list.add(public_room_entity);

                                }

                                if (page == 0) {
                                    public_room_list.addAll(list);
                                    if (public_room_list.size() > 0) {
                                        public_error.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.VISIBLE);
                                        public_room_recycle.setVisibility(View.VISIBLE);
                                        my_swipe_refresh_layout2.setRefreshing(false);
                                        linearLayoutManager = new LinearLayoutManager(getActivity());
                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        public_room_recycle.setLayoutManager(linearLayoutManager);
                                        send_public_order_adapater = new Send_public_order_Adapater(getActivity(), public_room_list);
                                        public_room_recycle.setAdapter(send_public_order_adapater);
                                    } else {
                                        public_room_recycle.setVisibility(View.GONE);
                                        my_swipe_refresh_layout2.setVisibility(View.GONE);
                                        public_error.setVisibility(View.VISIBLE);
                                        curSize = public_room_list.size();
                                        send_public_order_adapater.notifyDataSetChanged();
                                    }
                                } else {
                                    if (list != null && list.size() > 0) {
                                        curSize = public_room_list.size();
                                        public_room_list.addAll(curSize, list);
                                        send_public_order_adapater.notifyItemInserted(curSize);
                                        send_public_order_adapater.notifyItemRangeChanged(curSize, public_room_list.size());
                                        send_public_order_adapater.notifyDataSetChanged();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadmore_progress.setVisibility(View.GONE);
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
                params.put("action", "Publicroom");
                params.put("user_id", ssp.getUserId(getActivity()));
                params.put("page", String.valueOf(page));
                ////Log.e("params", params.toString());

                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

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

}