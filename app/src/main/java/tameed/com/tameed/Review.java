package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Entity.Reviews_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.LoadApi;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by dev on 17-01-2018.
 */

public class Review extends AppCompatActivity {
    TextView header_txt;
    ImageView header_back;
    RecyclerView recyclerView;
    private String TAG = "Review";

    ArrayList<String> review_detail = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

    SaveSharedPrefernces ssp;
    Activity act;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    int page = 0, curSize;
    Review_Adapter review_adapter;
    String result_count = "";
    String order_rating;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    LinearLayoutManager layoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    String provider_id;
    Intent intent;
    TextView error_msg;
    ArrayList<Reviews_Entity> order_detail_list = new ArrayList<>();
    String form, id;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        //Log.e(TAG, "****************************");
        setContentView(R.layout.reviews);
        intent = getIntent();
        provider_id = intent.getStringExtra("provider_id");
        order_rating = intent.getStringExtra("click");
        form = intent.getStringExtra("from");
        id = intent.getStringExtra("id");
        act = Review.this;
        makeStringReq();
        ssp = new SaveSharedPrefernces();
        header_txt = (TextView) findViewById(R.id.txt_header);

        header_txt.setText("التقيم");
        header_txt.setVisibility(View.VISIBLE);
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.review_recycle);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        error_msg = (TextView) findViewById(R.id.error_msg);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                ////Log.e("visibleItemCount", "" + visibleItemCount);
                ////Log.e("totalItemCount", "" + totalItemCount);
                ////Log.e("firstVisibleItem", "" + firstVisibleItem);
                ////Log.e("LogEntity1.size()", "" + order_detail_list.size());
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
                        if (order_detail_list.size() < Integer.parseInt(result_count)) {
                            ////Log.e("page2", "" + page);

                            page = page + 1;
                            makeStringReq();


                        } else {
                            Toast.makeText(act,
                                    "نهاية معاملاتي", Toast.LENGTH_SHORT).show();
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
                        ArrayList<New_Order_Entity> order_list = new ArrayList<>();

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

                            //   String all_pending_order_as_customer,all_new_order_as_provider,total_order_badge_count;

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

                            JSONArray jsonArray = obj.getJSONArray("reviews");
                            ArrayList<Reviews_Entity> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Reviews_Entity entity = new Reviews_Entity();

                                entity.setName(object.getString("name"));
                                entity.setProfile_pic_url(object.getString("profile_pic_url"));
                                entity.setProfile_pic_thumb_url(object.getString("profile_pic_thumb_url"));
                                entity.setOrder_description(object.getString("order_description"));
                                entity.setReview_id(object.getString("review_id"));
                                entity.setReview_given_by_id(object.getString("review_given_by_id"));
                                entity.setReview_given_to_id(object.getString("review_given_to_id"));
                                entity.setOrder_id(object.getString("order_id"));
                                entity.setReview_description(object.getString("review_description"));
                                entity.setReview_rating(object.getString("review_rating"));
                                entity.setReview_given_date(object.getString("review_given_date"));
                                entity.setCategory_name(object.getString("category_name"));
                                entity.setService_name(object.getString("service_name"));
                                if (!ssp.getUserId(act).equals(object.getString("review_given_to_id"))) {
                                    ////Log.e("Review_Id", "=======" + object.getString("review_given_to_id"));
                                    entity.setMine("no");
                                } else {
                                    ////Log.e("Review_Id", "=======" + object.getString("review_given_to_id"));
                                    entity.setMine("yes");
                                }

                                entity.setIs_reported(object.getString("is_reported"));

                                list.add(entity);


                            }


//

                            if (page == 0) {
                                curSize = 0;
                                ////Log.e("6666", "66666");
                                order_detail_list.addAll(list);

                                ////Log.e("List_Size", "===" + order_detail_list.size());

                                try {

                                    if (order_detail_list.size() > 0) {
                                        ////Log.e("7777", "7777");
                                        ////Log.e("List_Size>0", "===" + order_detail_list.size());
                                        error_msg.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);

                                        review_adapter = new Review_Adapter(act, order_detail_list);

                                        recyclerView.setAdapter(review_adapter);
                                    } else {
                                        ////Log.e("88888", "8888");
                                        ////Log.e("List_Size", "===" + order_detail_list.size());

                                        error_msg.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);

                                    }

                                    curSize = order_detail_list.size();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                ////Log.e("9999", "9999");
                                curSize = order_detail_list.size();
                                order_detail_list.addAll(curSize, list);
                                review_adapter.notifyItemInserted(curSize);
                                review_adapter.notifyItemRangeChanged(curSize, order_detail_list.size());
                                review_adapter.notifyDataSetChanged();
                            }

//                            order_detail_list.addAll(order_list);
//                            if (order_detail_list.size()>0) {
//                                error_msg.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                                OrderAdapter orderAdapter = new OrderAdapter(act, order_detail_list);
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


                        //}
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Review.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Review.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Review.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Review.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Showreviews");
                //   params.put("user_id", provider_id);
                if (TextUtils.isEmpty(form)) {
                    params.put("user_id", id);


                    params.put("order_id", "");


                    params.put("user_type", "");


                    params.put("is_click_on_order_rating", "");
                } else {
                    params.put("order_id", provider_id);

                    params.put("user_id", ssp.getUserId(act));

                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }

                    params.put("is_click_on_order_rating", order_rating);
                }
                params.put("page", String.valueOf(page));
                ////Log.e("params", params.toString());
                return params;
            }

        };




//
// add(stringRequest);
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(act);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.MyViewHolder> {
        Boolean bool = true;

        Context context;
        ArrayList<Reviews_Entity> review_detail;
        String msg = "";
        String reason;
        private ProgressDialog progress_dialog;
        private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
        private String progress_dialog_msg = "", tag_string_req = "string_req";

        float rate;
        SaveSharedPrefernces ssp;
        String ordder_id, review_id;

        public Review_Adapter(Context context, ArrayList<Reviews_Entity> review_detail) {

            this.context = context;
            this.review_detail = review_detail;
            ssp = new SaveSharedPrefernces();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycle, parent, false);

            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            // TextView report_txt,review_customer_name,review_main_service,review_date;
            holder.review_customer_name.setText(review_detail.get(position).getName());
            holder.review_main_service.setText(review_detail.get(position).getService_name());
            holder.review_date.setText(LoadApi.parseDateToddMMyyyy(review_detail.get(position).getReview_given_date()));
            if (!TextUtils.isEmpty(review_detail.get(position).getProfile_pic_thumb_url())) {
                Picasso.with(context).load(review_detail.get(position).getProfile_pic_thumb_url()).placeholder(R.mipmap.no_thumb).error(R.mipmap.no_thumb).into(holder.review_image);
            } else {
                holder.review_image.setImageResource(R.mipmap.no_thumb);

            }
            try {
                rate = Float.parseFloat(review_detail.get(position).getReview_rating());
                ////Log.e("RAtteing", "=====" + rate);

            } catch (NumberFormatException ex) {
//                                    rate = 0.0; // default ??
            }
            if (review_detail.get(position).getMine().equals("no")) {

                holder.report_txt.setVisibility(View.INVISIBLE);
            } else {
                holder.report_txt.setVisibility(View.VISIBLE);
            }

            if (rate >= 1.0 && rate < 2.0) {
                ////Log.e("RAtteing", "=====" + rate);
                holder.star1.setImageResource(R.drawable.star_active);
                holder.star2.setImageResource(R.drawable.star_inactive);
                holder.star3.setImageResource(R.drawable.star_inactive);
                holder.star4.setImageResource(R.drawable.star_inactive);
                holder.star5.setImageResource(R.drawable.star_inactive);
            } else if (rate >= 2.0 && rate < 3.0) {
                ////Log.e("RAtteing", "=====" + rate);
                holder.star1.setImageResource(R.drawable.star_active);
                holder.star2.setImageResource(R.drawable.star_active);
                holder.star3.setImageResource(R.drawable.star_inactive);
                holder.star4.setImageResource(R.drawable.star_inactive);
                holder.star5.setImageResource(R.drawable.star_inactive);
            } else if (rate >= 3.0 && rate < 4.0) {
                ////Log.e("RAtteing", "=====" + rate);
                holder.star1.setImageResource(R.drawable.star_active);
                holder.star2.setImageResource(R.drawable.star_active);
                holder.star3.setImageResource(R.drawable.star_active);
                holder.star4.setImageResource(R.drawable.star_inactive);
                holder.star5.setImageResource(R.drawable.star_inactive);
            } else if (rate >= 4.0 && rate < 5.0) {
                ////Log.e("RAtteing", "=====" + rate);
                holder.star1.setImageResource(R.drawable.star_active);
                holder.star2.setImageResource(R.drawable.star_active);
                holder.star3.setImageResource(R.drawable.star_active);
                holder.star4.setImageResource(R.drawable.star_active);
                holder.star5.setImageResource(R.drawable.star_inactive);
            } else if (rate >= 5.0) {
                ////Log.e("RAtteing", "=====" + rate);
                holder.star1.setImageResource(R.drawable.star_active);
                holder.star2.setImageResource(R.drawable.star_active);
                holder.star3.setImageResource(R.drawable.star_active);
                holder.star4.setImageResource(R.drawable.star_active);
                holder.star5.setImageResource(R.drawable.star_inactive);
            } else {
                ////Log.e("RAtteing", "=====" + rate);
                holder.star1.setImageResource(R.drawable.star_inactive);
                holder.star2.setImageResource(R.drawable.star_inactive);
                holder.star3.setImageResource(R.drawable.star_inactive);
                holder.star4.setImageResource(R.drawable.star_inactive);
                holder.star5.setImageResource(R.drawable.star_inactive);
            }


            if (review_detail.get(position).getIs_reported().equals("yes")) {
                holder.report_txt.setText("تم ارسال التقرير");

            } else {
                holder.report_txt.setText("ابلاغ");
                holder.report_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ordder_id = review_detail.get(position).getOrder_id();
                        review_id = review_detail.get(position).getReview_id();
                        final Dialog dialog1 = new Dialog(context);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialog_quotation);

                        dialog1.show();

                        Button extend_Warranty = (Button) dialog1.findViewById(R.id.send_admin);
                        final EditText edt_days = dialog1.findViewById(R.id.edt_quotation);
                        edt_days.setHint("ادخل تعليقك");
                        edt_days.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        TextView txt = (TextView) dialog1.findViewById(R.id.txt);
                        TextView txt2 = (TextView) dialog1.findViewById(R.id.txt2);
                        txt2.setVisibility(View.GONE);
                        txt.setText("ابلغ عن هذا التقيم");
                        extend_Warranty.setText("ارسال");
                        Button chnage_no = (Button) dialog1.findViewById(R.id.send_cancel);
                        extend_Warranty.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reason = edt_days.getText().toString();
                                if (TextUtils.isEmpty(reason)) {
                                    Toast.makeText(context, "ادخل تعليقك", Toast.LENGTH_SHORT).show();
                                } else {

                                    utiltyRequest();
                                    if (msg.equals("Your complain has been submitted successfully")) {
                                        review_detail.get(position).setIs_reported("yes");
                                        holder.report_txt.setText("تم ارسال التقرير");
                                    } else {
                                        review_detail.get(position).setIs_reported("no");
                                        holder.report_txt.setText("ابلاغ");
                                    }
                                    dialog1.dismiss();

                                }
                            }
                        });

                        chnage_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog1.dismiss();
                            }
                        });
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return review_detail.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView star1, star2, star3, star4, star5, review_image;
            TextView report_txt, review_customer_name, review_main_service, review_date;

            public MyViewHolder(View itemView) {
                super(itemView);
                review_image = (ImageView) itemView.findViewById(R.id.review_image);
                star1 = (ImageView) itemView.findViewById(R.id.filled_star1);
                star2 = (ImageView) itemView.findViewById(R.id.filled_star2);
                star3 = (ImageView) itemView.findViewById(R.id.filled_star3);
                star4 = (ImageView) itemView.findViewById(R.id.review_star4);
                star5 = (ImageView) itemView.findViewById(R.id.review_star5);
                report_txt = (TextView) itemView.findViewById(R.id.textView3);
                review_customer_name = (TextView) itemView.findViewById(R.id.review_customer_name);
                review_main_service = (TextView) itemView.findViewById(R.id.review_main_service);
                review_date = (TextView) itemView.findViewById(R.id.review_date);


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


        private void showProgDialog() {
            progress_dialog = null;
            try {
                if (Build.VERSION.SDK_INT >= 11) {
                    progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    progress_dialog = new ProgressDialog(context);
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
            progress_dialog_msg = getResources().getString(R.string.loading);
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
                                msg = obj.getString("msg");


                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(context);
                                }
                                if (msg.equals("This order has been Accepted by you successfully")) {
                                    alert.setTitle("تم قبول هذا المعاملة بنجاح");
                                }
                                if (msg.equals("This order has been Accepted by you successfully")) {
                                    alert.setMessage("تم قبول هذا المعاملة بنجاح");
                                } else {
                                    alert.setMessage(msg);
                                }

                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
//                                    helper.list_name=null;
//                                    helper.list_id=null;
//                                    helper.driver_array_list=null;
//                                    helper.hrw_array_list=null;
                                            order_detail_list.clear();
                                            //  ////Log.e("refreshh", "333" + refresh);
                                            recyclerView.removeAllViews();
                                            recyclerView.removeAllViewsInLayout();
                                            loading = true;
                                            previousTotal = 0;
                                            visibleThreshold = 5;
                                            firstVisibleItem = 0;
                                            visibleItemCount = 0;
                                            totalItemCount = 0;
                                            page = 0;
                                            makeStringReq();


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        dialog.dismiss();
                                    }
                                });

                                try {
                                    Dialog dialog = alert.create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
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

                                Toast.makeText(Review.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(Review.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                                //TODO
                            } else if (error instanceof ServerError) {

                                Toast.makeText(Review.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                //TODO
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(Review.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                                //TODO

                            } else if (error instanceof ParseError) {


                                //TODO
                            }

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("action", "Reportreview");
                    params.put("user_id", ssp.getUserId(context));
                    params.put("order_id", ordder_id);
                    params.put("review_id", review_id);
                    params.put("report_message", reason);



                    ////Log.e("params", params.toString());
                    return params;
                }

            };




//
// add(stringRequest);
            // Adding request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);


            // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
        }

    }


}