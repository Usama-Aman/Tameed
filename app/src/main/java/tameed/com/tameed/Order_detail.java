package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Pic_Adapter;
import tameed.com.tameed.Adapter.SpinnerQuotation;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.All_Quotation_Class;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Entity.Order_Pic_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by dev on 22-01-2018.
 */

public class Order_detail extends AppCompatActivity {
    TextView header_txt;
    DecimalFormat df;
    ImageView header_back, order_detail_img1, order_detail_img2, order_detail_img3;
    Button complain, extent_warranty, cancel, make_payment, complete;

    SaveSharedPrefernces ssp;
    Activity act;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String name, review_count, review_rating, customer_id, provider_id, order_description, serving_date, serving_time, order_status, order_cancel_reason,
            order_cost, cancelled_by, order_type, sort_order, latitude, longitude, location, total_fee, last_modified_date, added_date, admin_commission,
            percentage, service_fee, warranty_days, service_id, category_id, category_name, service_name, profile_pic_thumb_url,
            profile_pic_2xthumb_url, profile_pic_3xthumb_url, profile_pic_url, new_format_date, tab_status, payment_status, total_paid, remaining_balance;
    int page = 0, curSize;
    SpinnerQuotation quotation_adapter;
    String result_count = "";

    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    RecyclerView recyclerView;
    TextView error_msg;
    int spConfirmsCheck = 0;
    String quotation_given, quotation_id, quotation_amount, quotation_type, quotation_status, quotation_rejected_by, quotation_date, quotation_modified_date;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    Pic_Adapter adapter;
    Intent intent;
    String order_id;
    ArrayList<Order_Pic_Entity> pic_list = new ArrayList<>();
    String msg = "";
    int flagValue = 0;
    String is_review_given, order_rating;
    TextView txt_customer_name, txt_cat_name, txt_service_name, txt_order_dis, txt_warranty,
            txt_service_fee, txt_service_percent, txt_service_total, txt_service_date, txt_approve_txt;
    ImageView img_customer, star1, star2, star3, star4, star5;
    RecyclerView photo_view;
    LinearLayoutManager layoutManager;
    Double ratting;
    String days_extend, reason, order_reference_number;
    String amount_quotation, amount_per, amount_fees, amount_per_fee;
    ConstraintLayout layout2, layout1, layout3, layout_accept_reject, layout_cancel_compplain, layout_warranty_payment, button_layout;
    Spinner spin;
    TextView quotatio_service_fee, quotation_service_percent, quotation_service_total, txt_customer_accept, txt_customer_reject, resubmitt, client_completeService;

    ArrayList<All_Quotation_Class> quotation_list = new ArrayList<>();
    TextView ratting_txt;
    ImageView star_img1, star_img2, star_img3, star_img4, star_img5, img_drp;
    ConstraintLayout review_layout, constraintLayout_reviews_stars;
    TextView payment_txt;

    private String TAG = "Order_detail";


    //ADD JAY NEW VIEW FROM TRANSFAR DETAILS
    private View view_from_account;
    private RelativeLayout relativeLayout_transferred_from;
    private TextView textView_from_bank_account;

    private boolean isCancelRequest = false;

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
        //Log.e(TAG, "****************************");
        setContentView(R.layout.as_client_order_detail);
        df = new DecimalFormat(".##");


        helper.image_list.clear();
        helper.pic_list.clear();
        act = Order_detail.this;
        ssp = new SaveSharedPrefernces();
        photo_view = (RecyclerView) findViewById(R.id.photo_view);
        error_msg = (TextView) findViewById(R.id.error_msg);
        resubmitt = (TextView) findViewById(R.id.resubmitt);
        constraintLayout_reviews_stars = findViewById(R.id.constraintLayout_reviews_stars);
        ratting_txt = (TextView) findViewById(R.id.ratting_txt);
        star_img1 = (ImageView) findViewById(R.id.star1);
        star_img2 = (ImageView) findViewById(R.id.star2);
        star_img3 = (ImageView) findViewById(R.id.star3);
        star_img4 = (ImageView) findViewById(R.id.star4);
        star_img5 = (ImageView) findViewById(R.id.star5);
        img_drp = (ImageView) findViewById(R.id.img_drp);


        view_from_account = (View) findViewById(R.id.view_from_account);
        relativeLayout_transferred_from = (RelativeLayout) findViewById(R.id.relativeLayout_transferred_from);
        textView_from_bank_account = (TextView) findViewById(R.id.textView_from_bank_account);

        review_layout = (ConstraintLayout) findViewById(R.id.review_layout);
        review_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "yes");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        payment_txt = (TextView) findViewById(R.id.textView72);
        resubmitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                helper.category_name = category_name;
                helper.category_id = category_id;
                helper.service_name = service_name;
                helper.service_id1 = service_id;
                helper.serving_date = serving_date;
                helper.serving_time = serving_time;
                helper.warranty_days = warranty_days;
                helper.quotation_amout = amount_quotation;
                helper.quotation_perc = amount_per;
                helper.quotation_fee = amount_fees;
                helper.quotation_perc_fee = amount_per_fee;
                helper.order_id = order_id;
                helper.desc = order_description;
                if (order_status.equals("Completed")) {
                    Intent intent = new Intent(Order_detail.this, RatingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("order_id", order_id);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Order_detail.this, Search_ProvideService.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("service_id", service_id);
                    startActivity(intent);
                }
            }
        });
        layout_accept_reject = (ConstraintLayout) findViewById(R.id.layout_accept_reject);
        layout_cancel_compplain = (ConstraintLayout) findViewById(R.id.layout_cancel_compplain);
        layout_warranty_payment = (ConstraintLayout) findViewById(R.id.layout_warranty_payment);
        layoutManager = new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false);
        layout3 = (ConstraintLayout) findViewById(R.id.layout3);
        txt_customer_accept = (Button) findViewById(R.id.provider_accept);
        txt_customer_reject = (Button) findViewById(R.id.provider_reject);
        button_layout = (ConstraintLayout) findViewById(R.id.button_layout);
        client_completeService = (Button) findViewById(R.id.client_completeService);

        quotatio_service_fee = (TextView) findViewById(R.id.quotatio_service_fee);
        quotation_service_percent = (TextView) findViewById(R.id.quotation_service_percent);
        quotation_service_total = (TextView) findViewById(R.id.quotation_service_total);
        photo_view.setLayoutManager(layoutManager);
        star1 = (ImageView) findViewById(R.id.imageView11);
        star2 = (ImageView) findViewById(R.id.imageView13);
        star3 = (ImageView) findViewById(R.id.payment_img1);
        star4 = (ImageView) findViewById(R.id.imageView31);
        star5 = (ImageView) findViewById(R.id.imageView35);


        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("from", "order_details");
                intent.putExtra("click", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("from", "order_details");
                startActivity(intent);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("from", "order_details");
                startActivity(intent);
            }
        });
        layout1 = (ConstraintLayout) findViewById(R.id.layout1);
        layout2 = (ConstraintLayout) findViewById(R.id.layout2);
        spin = (Spinner) findViewById(R.id.spinner_os);
        txt_cat_name = (TextView) findViewById(R.id.order_cat_nam);
        txt_service_name = (TextView) findViewById(R.id.textView53);
        txt_order_dis = (TextView) findViewById(R.id.textView55);
        txt_warranty = (TextView) findViewById(R.id.order_detail_warranty);
        txt_service_fee = (TextView) findViewById(R.id.service_fee);
        txt_service_percent = (TextView) findViewById(R.id.service_percent);
        txt_service_total = (TextView) findViewById(R.id.service_total);
        txt_service_date = (TextView) findViewById(R.id.service_date);
        txt_approve_txt = (TextView) findViewById(R.id.textView71);

        txt_customer_name = (TextView) findViewById(R.id.as_provider_customer_name);
        img_customer = (ImageView) findViewById(R.id.imageView10);


        header_txt = (TextView) findViewById(R.id.txt_header);


        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("flag", 1);
                startActivity(intent);
            }
        });

        intent = getIntent();
        order_id = intent.getStringExtra("order_id");


        make_payment = (Button) findViewById(R.id.make_payment);
        extent_warranty = (Button) findViewById(R.id.extend_warraty);
        cancel = (Button) findViewById(R.id.provider_cancel);
        complain = (Button) findViewById(R.id.provider_complete);
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order_detail.this, Complain_order.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("order_id", order_id);

                intent.putExtra("order_number", order_reference_number);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Order_detail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_cancel_order);

                dialog.show();

                final EditText edt_reason = (EditText) dialog.findViewById(R.id.edt_reason);
                Button send_admin = (Button) dialog.findViewById(R.id.send_admin);
                Button send_cancel = (Button) dialog.findViewById(R.id.send_cancel);
                send_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });
                send_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        reason = edt_reason.getText().toString();
                        if (TextUtils.isEmpty(reason)) {
                            Toast.makeText(act, "سبب الإلغاء", Toast.LENGTH_SHORT).show();
                        } else {
                            flagValue = 2;
                            utilityRequest();
                            dialog.dismiss();
                        }


                    }
                });

            }
        });

        extent_warranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Nardeep Warranty no. days
                final Dialog dialog1 = new Dialog(Order_detail.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_extend_warrranty);

                dialog1.show();

                Button extend_Warranty = (Button) dialog1.findViewById(R.id.chnage_waranty);
                final EditText edt_days = dialog1.findViewById(R.id.edt_days);
                TextView txt_days = (TextView) dialog1.findViewById(R.id.txt_days);
                txt_days.setText("مدة التعميد لإتمام هذا المعاملة يوم(" + warranty_days + " Days)");
                Button chnage_no = (Button) dialog1.findViewById(R.id.chnage_no);
                extend_Warranty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        days_extend = edt_days.getText().toString();

                        if (TextUtils.isEmpty(days_extend)) {
                            Toast.makeText(act, "تمديد مدة التعميد", Toast.LENGTH_SHORT).show();
                        } else {
                            flagValue = 1;
                            utilityRequest();
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

        makeStringReq();

        make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "....");
                Intent intent = new Intent(Order_detail.this, PaymentActivity.class);
                intent.putExtra("order_id", order_id);
                intent.putExtra("provider_id", provider_id);
                intent.putExtra("fromDirectOrder", false);
                if (!TextUtils.isEmpty(payment_status)) {
                    if (payment_status.equals("PAID")) {
                        Double value = Double.parseDouble(total_fee);
                        intent.putExtra("total_fee", String.valueOf(value));
                    } else if (payment_status.equals("UNPAID")) {
                        Double value = Double.parseDouble(total_fee);
                        intent.putExtra("total_fee", String.valueOf(value));
                    } else {
                        Double value = Double.parseDouble(remaining_balance);
                        intent.putExtra("total_fee", String.valueOf(remaining_balance));

                    }
                } else {
                    Double value = Double.parseDouble(total_fee);
                    intent.putExtra("total_fee", String.valueOf(value));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        txt_customer_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(act);
                }


                alert.setMessage("هل ترغب بقبول هذا المعاملة");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flagValue = 3;
                        utilityRequest();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                try {
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        client_completeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flagValue = 5;
                utilityRequest();

            }
        });
        txt_customer_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(act);
                }

                alert.setMessage("هل ترغب برفض هذا المعاملة");
                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        flagValue = 4;
                        utilityRequest();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                try {
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        txt_customer_name.setVisibility(View.VISIBLE);
        img_customer.setVisibility(View.VISIBLE);
        star1.setVisibility(View.VISIBLE);
        star2.setVisibility(View.VISIBLE);
        star3.setVisibility(View.VISIBLE);
        star4.setVisibility(View.VISIBLE);
        star5.setVisibility(View.VISIBLE);


        // getRequestSpStatus();

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


                            if (obj.has("order_details")) {

                                JSONObject json = obj.getJSONObject("order_details");

                                if (json.has("is_approved_sp"))
                                    spConfirmsCheck = json.getInt("is_approved_sp");

                                name = json.getString("name");
                                printLog("name", name);

                                if (json.has("is_cancel_request"))
                                    isCancelRequest = json.getBoolean("is_cancel_request");

                                profile_pic_thumb_url = json.getString("profile_pic_thumb_url");
                                printLog("profile_pic_thumb_url", profile_pic_thumb_url);

                                profile_pic_url = json.getString("profile_pic_url");
                                printLog("profile_pic_url", profile_pic_url);

                                profile_pic_2xthumb_url = json.getString("profile_pic_2xthumb_url");
                                printLog("profile_pic_2xthumb_url", profile_pic_2xthumb_url);


                                profile_pic_3xthumb_url = json.getString("profile_pic_3xthumb_url");
                                printLog("profile_pic_2xthumb_url", profile_pic_2xthumb_url);

                                review_count = json.getString("review_count");
                                printLog("review_count", review_count);

                                review_rating = json.getString("review_rating");
                                ////Log.e("review_rating", review_rating);

                                order_id = json.getString("order_id");
                                printLog("order_id", order_id);

                                customer_id = json.getString("customer_id");
                                printLog("customer_id", customer_id);

                                provider_id = json.getString("provider_id");
                                printLog("provider_id", provider_id);

                                order_description = json.getString("order_description");
                                printLog("order_description", order_description);

                                txt_order_dis.setText(order_description);

                                serving_date = json.getString("serving_date");
                                printLog("serving_date", serving_date);


                                serving_time = json.getString("serving_time");
                                printLog("serving_time", serving_time);

                                txt_service_date.setText(serving_date + " " + serving_time);

                                order_status = json.getString("order_status");
                                printLog("order_status", order_status);

                                order_cancel_reason = json.getString("order_cancel_reason");
                                printLog("order_cancel_reason", order_cancel_reason);

                                order_cost = json.getString("order_cost");
                                printLog("order_cost", order_cost);

                                order_reference_number = json.getString("order_reference_number");
                                printLog("order_reference_number", order_reference_number);
                                header_txt.setText("معاملة #" + order_reference_number);

                                cancelled_by = json.getString("cancelled_by");
                                printLog("cancelled_by", cancelled_by);

                                order_type = json.getString("order_type");
                                printLog("order_type", order_type);
                                if (order_type.equals("public")) {
                                    layout1.setVisibility(View.GONE);
                                } else {
                                    layout1.setVisibility(View.VISIBLE);
                                }

                                sort_order = json.getString("sort_order");
                                printLog("sort_order", sort_order);

                                latitude = json.getString("latitude");
                                printLog("latitude", latitude);

                                longitude = json.getString("longitude");
                                printLog("longitude", longitude);

                                location = json.getString("location");
                                printLog("location", location);

                                total_fee = json.getString("total_fee");
                                printLog("total_fee", total_fee);

                                last_modified_date = json.getString("last_modified_date");
                                printLog("last_modified_date", last_modified_date);

                                added_date = json.getString("added_date");
                                printLog("added_date", added_date);

                                admin_commission = json.getString("admin_commission");
                                printLog("admin_commission", admin_commission);

                                percentage = json.getString("percentage");
                                printLog("percentage", percentage);

                                service_fee = json.getString("service_fee");
                                printLog("service_fee", service_fee);

                                warranty_days = json.getString("warranty_days");
                                printLog("warranty_days", warranty_days);
                                txt_warranty.setText("Days" + " " + warranty_days);


                                //TODO JAY GET NEW TAG
                                String from_bank_account = json.optString("from_bank_account");
                                //Log.e(TAG, "from_bank_account.....>>>" + from_bank_account);
                                textView_from_bank_account.setText(from_bank_account);
                                if (!from_bank_account.equalsIgnoreCase("")) {
                                    view_from_account.setVisibility(View.VISIBLE);
                                    relativeLayout_transferred_from.setVisibility(View.VISIBLE);
                                } else {
                                    view_from_account.setVisibility(View.GONE);
                                    relativeLayout_transferred_from.setVisibility(View.GONE);
                                }


                                if (json.has("is_review_given")) {

                                    is_review_given = json.getString("is_review_given");
                                    printLog("is_review_given", is_review_given);
                                    if (json.has("order_rating")) {
                                        order_rating = json.getString("order_rating");
                                        printLog("order_rating", order_rating);
                                    } else {
                                        order_rating = "0.0";
                                    }

                                }

                                service_id = json.getString("service_id");
                                printLog("service_id", service_id);

                                category_id = json.getString("category_id");
                                printLog("category_id", category_id);

                                category_name = json.getString("category_name");
                                printLog("category_name", category_name);
                                txt_cat_name.setText(category_name);
                                service_name = json.getString("service_name");
                                printLog("service_name", service_name);
                                txt_service_name.setText(service_name);

                                txt_service_fee.setText(service_fee);
                                txt_service_percent.setText(percentage);
                                txt_service_total.setText(total_fee + " SAR");

                                //Log.e(TAG, "******service_fee*****" + service_fee);
                                //Log.e(TAG, "******percentage******" + percentage);
                                //Log.e(TAG, "******total_fee******" + total_fee);

                                JSONArray array = json.getJSONArray("order_pics");
                                ArrayList<Order_Pic_Entity> pic_list = new ArrayList<>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject jsonObject = array.getJSONObject(j);
                                    Order_Pic_Entity entity = new Order_Pic_Entity();
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
                                if (pic_list.size() > 0) {
                                    photo_view.setVisibility(View.VISIBLE);
                                    adapter = new Pic_Adapter(act, pic_list);
                                    photo_view.setAdapter(adapter);
                                    error_msg.setVisibility(View.GONE);
                                } else {
                                    photo_view.setVisibility(View.GONE);
                                    // TODO Nardeep
                                    //error_msg.setVisibility(View.VISIBLE);

                                }


                                new_format_date = json.getString("new_format_date");
                                printLog("new_format_date", new_format_date);

                                tab_status = json.getString("tab_status");
                                printLog("tab_status", tab_status);

                                //helper.list_type=tab_status;


                                JSONArray jsonArray = json.getJSONArray("all_quotations");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject object = jsonArray.getJSONObject(j);
                                    All_Quotation_Class quotation_class = new All_Quotation_Class();
                                    quotation_class.setName(object.getString("name"));
                                    quotation_class.setReview_count(object.getString("review_count"));
                                    quotation_class.setReview_rating(object.getString("review_rating"));
                                    quotation_class.setProfile_pic_url(object.getString("profile_pic_url"));
                                    quotation_class.setProfile_pic_thumb_url(object.getString("profile_pic_thumb_url"));
                                    quotation_class.setProfile_pic_2xthumb_url(object.getString("profile_pic_2xthumb_url"));
                                    quotation_class.setProfile_pic_3xthumb_url(object.getString("profile_pic_3xthumb_url"));
                                    quotation_class.setQuotation_id(object.getString("quotation_id"));
                                    quotation_class.setOrder_id(object.getString("order_id"));
                                    quotation_class.setUser_id(object.getString("user_id"));
                                    quotation_class.setQuotation_amount(object.getString("quotation_amount"));
                                    quotation_class.setQuotation_type(object.getString("quotation_type"));
                                    quotation_class.setQuotation_status(object.getString("quotation_status"));
                                    quotation_class.setQuotation_rejected_by(object.getString("quotation_rejected_by"));
                                    quotation_class.setIs_quotation_modified(object.getString("is_quotation_modified"));
                                    quotation_class.setQuotation_modified_date(object.getString("quotation_modified_date"));
                                    quotation_class.setQuotation_date(object.getString("quotation_date"));
                                    quotation_class.setPercentage_fee(object.getString("percentage_fee"));

                                    quotation_class.setPercentage(object.getString("percentage"));
                                    quotation_class.setTotal_fee(object.getString("total_fee"));
                                    quotation_list.add(quotation_class);

                                }


                                if (json.has("quotation_given")) {
                                    quotation_given = json.getString("quotation_given");
                                    printLog("quotation_given", quotation_given);

                                    quotation_id = json.getString("quotation_id");
                                    printLog("quotation_id", quotation_id);

                                    quotation_amount = json.getString("quotation_amount");
                                    printLog("quotation_amount", quotation_amount);

                                    quotation_type = json.getString("quotation_type");
                                    printLog("quotation_type", quotation_type);

                                    quotation_status = json.getString("quotation_status");
                                    printLog("quotation_status", quotation_status);

                                    quotation_rejected_by = json.getString("quotation_rejected_by");
                                    printLog("quotation_rejected_by", quotation_rejected_by);

                                    quotation_date = json.getString("quotation_date");
                                    printLog("quotation_date", quotation_date);

                                    quotation_modified_date = json.getString("quotation_modified_date");
                                    printLog("quotation_modified_date", quotation_modified_date);


                                }

                                if (tab_status.equals("New")) {
                                    //Log.e(TAG, "tab_status===>>>>>New>>>>>>");
                                    ////Log.e("111111111", "111111111");
                                    txt_approve_txt.setText(last_modified_date);
                                    layout_cancel_compplain.setVisibility(View.VISIBLE);
                                    complain.setVisibility(View.GONE);
                                    layout_warranty_payment.setVisibility(View.GONE);
                                    layout_accept_reject.setVisibility(View.GONE);


                                } else if (tab_status.equals("Approved")) {
                                    //Log.e(TAG, "tab_status===>>>>>Approved>>>>>>" + spConfirmsCheck);
                                    payment_status = json.getString("payment_status");
                                    total_paid = json.getString("total_paid");
                                    //Log.e(TAG, "payment_status===>>>>>>>>>>>" + payment_status);
                                    //Log.e(TAG, "total_paid===>>>>>>>>>>>" + total_paid);
                                    if (payment_status.equals("PAID")) {
                                        double value = Double.parseDouble(total_fee);
                                        String number = df.format(value);
                                        //payment_txt.setText("حالة المدفوعات" + payment_status + "\n مبلغ لدفع ريال سعودي " + value);
                                        //TODO JAY REPLACE ARABIC TEXT
                                        payment_txt.setText("حالة المدفوعات" + getString(R.string.str_Paid) + "\n مبلغ لدفع ريال سعودي " + value);

                                        extent_warranty.setVisibility(View.GONE);

                                        //Log.e("Payment", "=====" + number);
                                        make_payment.setVisibility(View.GONE);

                                        if (spConfirmsCheck == 0) {
                                            client_completeService.setVisibility(View.GONE);
                                            extent_warranty.setVisibility(View.VISIBLE);
                                        } else {
                                            client_completeService.setVisibility(View.VISIBLE);
                                            extent_warranty.setVisibility(View.GONE);
                                        }

                                    } else if (payment_status.equals("UNPAID")) {
                                        double value = Double.parseDouble(total_fee);
                                        String number = df.format(value);
                                        //payment_txt.setText("حالة المدفوعات " + payment_status + "\n مبلغ لدفع ريال سعودي " + value);
                                        //TODO JAY REPLACE ARABIC TEXT
                                        payment_txt.setText("حالة المدفوعات " + getString(R.string.str_Unpaid) + "\n مبلغ لدفع ريال سعودي " + value);


                                        ////Log.e("Payment", "=====" + number);

                                        make_payment.setVisibility(View.VISIBLE);
                                        extent_warranty.setVisibility(View.VISIBLE);

                                    } else {

                                        remaining_balance = json.getString("remaining_balance");
                                        double value = Double.parseDouble(remaining_balance);
                                        String number = df.format(value);
                                        ////Log.e("Payment", "=====" + number);

                                        payment_txt.setText("حالة المدفوعات" + payment_status + "\n مبلغ لدفع ريال سعودي " + value);
                                        //TODO JAY REPLACE ARABIC TEXT
                                        extent_warranty.setVisibility(View.GONE);

                                    }
                                    ////Log.e("22222222", "222222222222");
                                    layout_accept_reject.setVisibility(View.GONE);
                                    layout_cancel_compplain.setVisibility(View.VISIBLE);

                                    /*Show button in case of approved only to cancel complain to service provider*/

                                    if (isCancelRequest)
                                        cancel.setVisibility(View.GONE);
                                    else {

                                        if (spConfirmsCheck == 1)
                                            cancel.setVisibility(View.GONE);
                                        else
                                            cancel.setVisibility(View.VISIBLE);

                                    }

//                                    extent_warranty.setVisibility(View.GONE);
                                    layout_warranty_payment.setVisibility(View.VISIBLE);
                                    txt_approve_txt.setText(last_modified_date);


                                } else if (tab_status.equals("Accepted")) {
                                    //Log.e(TAG, "tab_status===>>>>>Accepted>>>>>>");
                                    ////Log.e("3333333333", "3333333333");
                                    layout_accept_reject.setVisibility(View.VISIBLE);
                                    txt_customer_accept.setText("الموافقة على الاقتباس");
                                    txt_customer_reject.setText("رفض الاقتباس");
                                    complain.setVisibility(View.GONE);
                                    layout_warranty_payment.setVisibility(View.GONE);
                                    layout_cancel_compplain.setVisibility(View.VISIBLE);


                                } else if (tab_status.equals("Completed")) {
                                    //Log.e(TAG, "tab_status===>>>>>Completed>>>>>>");

                                    payment_status = json.getString("payment_status");
                                    total_paid = json.getString("total_paid");


                                    //Log.e(TAG, "payment_status===>>>>>>>>>>>" + payment_status);
                                    //Log.e(TAG, "total_paid===>>>>>>>>>>>" + total_paid);
                                    if (payment_status.equals("PAID")) {
                                        double value = Double.parseDouble(total_fee);
                                        String number = df.format(value);
                                        //payment_txt.setText("حالة المدفوعات " + payment_status + "\n مبلغ لدفع ريال سعودي " + value);
                                        //TODO JAY REPLACE ARABIC TEXT
                                        payment_txt.setText("حالة المدفوعات " + getString(R.string.str_Paid) + "\n مبلغ لدفع ريال سعودي " + value);


                                        ////Log.e("Payment", "=====" + number);
                                        make_payment.setVisibility(View.GONE);
                                    } else if (payment_status.equals("UNPAID")) {
                                        double value = Double.parseDouble(total_fee);
                                        String number = df.format(value);
                                        //payment_txt.setText("حالة المدفوعات " + payment_status + "\n مبلغ لدفع ريال سعودي" + value);
                                        //TODO JAY REPLACE ARABIC TEXT
                                        payment_txt.setText("حالة المدفوعات " + getString(R.string.str_Unpaid) + "\n مبلغ لدفع ريال سعودي" + value);

                                        ////Log.e("Payment", "=====" + number);
                                        make_payment.setVisibility(View.VISIBLE);
                                    } else {
                                        //make_payment.setVisibility(View.GONE);
                                        remaining_balance = json.getString("remaining_balance");
                                        double value = Double.parseDouble(remaining_balance);
                                        String number = df.format(value);
                                        ////Log.e("Payment", "=====" + number);

                                        payment_txt.setText("حالة المدفوعات" + payment_status + "\nمبلغ لدفع ريال سعودي" + value);

                                    }
                                    layout_warranty_payment.setVisibility(View.VISIBLE);

                                    // make_payment.setVisibility(View.GONE); //NEW JAY

                                    cancel.setVisibility(View.GONE);
                                    extent_warranty.setVisibility(View.GONE);
                                    if (is_review_given.equals("yes")) {
                                        resubmitt.setVisibility(View.GONE);
                                        review_layout.setVisibility(View.VISIBLE);
                                        constraintLayout_reviews_stars.setVisibility(View.VISIBLE);
                                        ratting(order_rating);

                                    } else {
                                        //client_completeService.setVisibility(View.VISIBLE);

                                        resubmitt.setText("قيم الخدمة");
                                        resubmitt.setVisibility(View.VISIBLE);
                                        review_layout.setVisibility(View.GONE);
                                        constraintLayout_reviews_stars.setVisibility(View.GONE);
                                    }
                                    layout_cancel_compplain.setVisibility(View.VISIBLE);
                                    complain.setVisibility(View.GONE);
                                    ////Log.e("55555555555", "5555555555555");
                                    txt_approve_txt.setText("تم إكمال هذا المعاملة في " + last_modified_date);


                                } else {
                                    //Log.e(TAG, "tab_status===>>>>>Cancelled>>>>>>");
                                    if (tab_status.equals("Cancelled")) {

                                        txt_approve_txt.setText("يتم إلغاء هذا المعاملة بواسطة " + cancelled_by);
                                    } else if (tab_status.equals("Rejected")) {
                                        txt_approve_txt.setText("هذا المعاملة مرفوض " + last_modified_date);
                                        resubmitt.setText("إعادة إرسال المعاملة");
                                        resubmitt.setVisibility(View.VISIBLE);
                                    }


                                }


                                if (quotation_list.size() > 0) {
                                    layout2.setVisibility(View.VISIBLE);
                                    if (order_type.equals("public")) {
                                        layout3.setVisibility(View.VISIBLE);
                                    } else {
                                        layout3.setVisibility(View.GONE);
                                    }
                                    if (quotation_list.size() <= 1) {

                                        img_drp.setVisibility(View.GONE);
                                        spin.setClickable(false);
                                        spin.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                                    // Toast.makeText(Order_status.this, "NO Quotation", Toast.LENGTH_SHORT).show();
                                                }
                                                return true;
                                            }
                                        });
                                    } else {
                                        img_drp.setVisibility(View.VISIBLE);
                                    }
                                    spin.setPrompt("يرجى اختيار الاقتباس");

                                    quotation_adapter = new SpinnerQuotation(Order_detail.this, quotation_list);
                                    spin.setAdapter(quotation_adapter);
                                    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


//                                            ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                                            ////Log.e("SPin Name", "+++" + quotation_list.get(position).getName());
                                            ////Log.e("SPin Quotation", "+++" + quotation_list.get(position).getQuotation_amount());
                                            ////Log.e("SPin ID", "+++" + quotation_list.get(position).getQuotation_id());
                                            amount_quotation = quotation_list.get(position).getQuotation_amount();
                                            amount_per = quotation_list.get(position).getPercentage();

                                            amount_fees = quotation_list.get(position).getTotal_fee();
                                            amount_per_fee = quotation_list.get(position).getPercentage_fee();


                                            quotation_id = quotation_list.get(position).getQuotation_id();
                                            if (quotation_list.get(position).getQuotation_status().equals("Rejected")) {
                                                layout_accept_reject.setVisibility(View.GONE);
                                                txt_approve_txt.setText("اقتباس من" + quotation_list.get(position).getQuotation_amount() + getResources().getString(R.string.msg_rejected_by) + quotation_list.get(position).getQuotation_rejected_by() + " on " + quotation_list.get(position).getQuotation_modified_date());
                                            } else {
                                                if (tab_status.equals("Accepted")) {
                                                    txt_approve_txt.setText("اقتباس من" + quotation_list.get(position).getQuotation_amount() + getResources().getString(R.string.msg_submitted_by) + quotation_list.get(position).getName() + " on " + quotation_list.get(position).getQuotation_modified_date());
                                                    txt_customer_accept.setText("قبول");
                                                    txt_customer_reject.setText("رفض");
                                                    layout_accept_reject.setVisibility(View.VISIBLE);
                                                } else if (tab_status.equals("Approved")) {
                                                    txt_approve_txt.setText("اقتباس من" + quotation_list.get(position).getQuotation_amount() + getResources().getString(R.string.msg_approved_on) + quotation_list.get(position).getQuotation_modified_date());
                                                }
                                            }

                                            quotatio_service_fee.setText(" (كمية) ريال " + quotation_list.get(position).getQuotation_amount());
                                            quotation_service_percent.setText(quotation_list.get(position).getPercentage());
                                            quotation_service_total.setText(" (كمية) ريال" + quotation_list.get(position).getTotal_fee());

//                                        txt_quotation.setText("$" + quotationlist.get(position).getQuotation_amount());
//                                        quotation_amount = quotationlist.get(position).getQuotation_amount();
//                                        quotation_id = quotationlist.get(position).getQuotation_id();
//                                                if (order_type.equals("private")){
                                            //  provider_layout.setVisibility(View.VISIBLE);
//                                        if (!TextUtils.isEmpty(quotationlist.get(position).getProfile_pic_url())) {
//                                            Picasso.with(Order_status.this).load(quotationlist.get(position).getProfile_pic_url()).placeholder(R.drawable.upic).into(provider_img);
//
//                                        } else {
//                                            provider_img.setImageResource(R.drawable.upic);
//                                        }
//
//                                        if (TextUtils.isEmpty(quotationlist.get(position).getName())) {
//                                            provider_name.setText("N/A");
//                                        } else {
//                                            provider_name.setText(quotationlist.get(position).getName());
//                                        }
                                        }

                                        //}

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parentView) {
                                            // iv_shopback.setVisibility(View.VISIBLE);


                                        }
                                    });
                                } else {

                                    layout2.setVisibility(View.GONE);
                                    if (order_type.equals("public")) {
                                        layout3.setVisibility(View.VISIBLE);
                                    } else {
                                        layout3.setVisibility(View.GONE);
                                    }
                                    spin.setPrompt("لا اقتباس");
                                    spin.setClickable(false);
                                    spin.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                                Toast.makeText(Order_detail.this, "لا اقتباس", Toast.LENGTH_SHORT).show();
                                            }
                                            return true;
                                        }
                                    });
                                }

                                if (provider_id.equals("0")) {
                                    if (customer_id.equals(ssp.getUserId(act))) {
                                        txt_customer_name.setVisibility(View.GONE);
                                        img_customer.setVisibility(View.GONE);
                                        star1.setVisibility(View.GONE);
                                        star2.setVisibility(View.GONE);
                                        star3.setVisibility(View.GONE);
                                        star4.setVisibility(View.GONE);
                                        star5.setVisibility(View.GONE);
                                    } else {
                                        txt_customer_name.setVisibility(View.VISIBLE);
                                        img_customer.setVisibility(View.VISIBLE);
                                        star1.setVisibility(View.VISIBLE);
                                        star2.setVisibility(View.VISIBLE);
                                        star3.setVisibility(View.VISIBLE);
                                        star4.setVisibility(View.VISIBLE);
                                        star5.setVisibility(View.VISIBLE);
                                        String name1 = ssp.getName(act);
                                        String pic = ssp.getKEY_profile_pic_thumb_url(act);
                                        if (name1.equals(null) || TextUtils.isEmpty(name1)) {
                                            txt_customer_name.setText("لا اسم");
                                        } else {
                                            txt_customer_name.setText(name1);
                                        }

                                        if (pic.equals(null) || TextUtils.isEmpty(pic)) {
                                            img_customer.setImageResource(R.mipmap.no_thumb);
                                        } else {
                                            Picasso.with(act).load(pic).error(R.mipmap.no_thumb).placeholder(R.mipmap.no_thumb).into(img_customer);
                                        }

                                        if (review_rating.equals(null) || TextUtils.isEmpty(review_rating)) {
                                            ////Log.e("rattting", "11111111");
                                            star1.setImageResource(R.mipmap.star_inactive);
                                            star2.setImageResource(R.mipmap.star_inactive);
                                            star3.setImageResource(R.mipmap.star_inactive);
                                            star4.setImageResource(R.mipmap.star_inactive);
                                            star5.setImageResource(R.mipmap.star_inactive);

                                        } else {
                                            ////Log.e("rattting", "22222222");

                                            ratting = Double.parseDouble(review_rating);
                                            ////Log.e("rattin", "======" + ratting);
                                            if (ratting >= 0.0 && ratting < 1.0) {
                                                star1.setImageResource(R.mipmap.star_inactive);
                                                star2.setImageResource(R.mipmap.star_inactive);
                                                star3.setImageResource(R.mipmap.star_inactive);
                                                star4.setImageResource(R.mipmap.star_inactive);
                                                star5.setImageResource(R.mipmap.star_inactive);
                                            } else if (ratting >= 1.0 && ratting < 2.0) {
                                                star1.setImageResource(R.mipmap.star_active);
                                                star2.setImageResource(R.mipmap.star_inactive);
                                                star3.setImageResource(R.mipmap.star_inactive);
                                                star4.setImageResource(R.mipmap.star_inactive);
                                                star5.setImageResource(R.mipmap.star_inactive);
                                            } else if (ratting >= 2.0 && ratting < 3.0) {
                                                star1.setImageResource(R.mipmap.star_active);
                                                star2.setImageResource(R.mipmap.star_active);
                                                star3.setImageResource(R.mipmap.star_inactive);
                                                star4.setImageResource(R.mipmap.star_inactive);
                                                star5.setImageResource(R.mipmap.star_inactive);
                                            } else if (ratting >= 3.0 && ratting < 4.0) {
                                                star1.setImageResource(R.mipmap.star_active);
                                                star2.setImageResource(R.mipmap.star_active);
                                                star3.setImageResource(R.mipmap.star_active);
                                                star4.setImageResource(R.mipmap.star_inactive);
                                                star5.setImageResource(R.mipmap.star_inactive);
                                            } else if (ratting >= 4.0 && ratting < 5.0) {
                                                star1.setImageResource(R.mipmap.star_active);
                                                star2.setImageResource(R.mipmap.star_active);
                                                star3.setImageResource(R.mipmap.star_active);
                                                star4.setImageResource(R.mipmap.star_active);
                                                star5.setImageResource(R.mipmap.star_inactive);
                                            } else if (ratting >= 5.0) {
                                                star1.setImageResource(R.mipmap.star_active);
                                                star2.setImageResource(R.mipmap.star_active);
                                                star3.setImageResource(R.mipmap.star_active);
                                                star4.setImageResource(R.mipmap.star_active);
                                                star5.setImageResource(R.mipmap.star_active);
                                            } else {
                                                star1.setImageResource(R.mipmap.star_inactive);
                                                star2.setImageResource(R.mipmap.star_inactive);
                                                star3.setImageResource(R.mipmap.star_inactive);
                                                star4.setImageResource(R.mipmap.star_inactive);
                                                star5.setImageResource(R.mipmap.star_inactive);
                                            }
                                        }
                                    }

                                } else {
                                    txt_customer_name.setVisibility(View.VISIBLE);
                                    img_customer.setVisibility(View.VISIBLE);
                                    star1.setVisibility(View.VISIBLE);
                                    star2.setVisibility(View.VISIBLE);
                                    star3.setVisibility(View.VISIBLE);
                                    star4.setVisibility(View.VISIBLE);
                                    star5.setVisibility(View.VISIBLE);
                                    if (quotation_list.get(0).getName().equals(null) || TextUtils.isEmpty(quotation_list.get(0).getName())) {
                                        txt_customer_name.setText("لا اسم");
                                    } else {
                                        txt_customer_name.setText(quotation_list.get(0).getName());
                                    }

                                    if (quotation_list.get(0).getProfile_pic_thumb_url().equals(null) || TextUtils.isEmpty(quotation_list.get(0).getProfile_pic_thumb_url())) {
                                        img_customer.setImageResource(R.mipmap.no_thumb);
                                    } else {
                                        Picasso.with(act).load(quotation_list.get(0).getProfile_pic_thumb_url()).error(R.mipmap.no_thumb).placeholder(R.mipmap.no_thumb).into(img_customer);
                                    }
                                    if (quotation_list.get(0).getReview_rating().equals(null) || TextUtils.isEmpty(quotation_list.get(0).getReview_rating())) {
                                        ////Log.e("rattting", "11111111");
                                        star1.setImageResource(R.mipmap.star_inactive);
                                        star2.setImageResource(R.mipmap.star_inactive);
                                        star3.setImageResource(R.mipmap.star_inactive);
                                        star4.setImageResource(R.mipmap.star_inactive);
                                        star5.setImageResource(R.mipmap.star_inactive);

                                    } else {
                                        ////Log.e("rattting", "22222222");
                                        ratting = Double.parseDouble(quotation_list.get(0).getReview_rating());
                                        if (ratting >= 0.0 && ratting < 1.0) {
                                            star1.setImageResource(R.mipmap.star_inactive);
                                            star2.setImageResource(R.mipmap.star_inactive);
                                            star3.setImageResource(R.mipmap.star_inactive);
                                            star4.setImageResource(R.mipmap.star_inactive);
                                            star5.setImageResource(R.mipmap.star_inactive);
                                        } else if (ratting >= 1.0 && ratting < 2.0) {
                                            star1.setImageResource(R.mipmap.star_active);
                                            star2.setImageResource(R.mipmap.star_inactive);
                                            star3.setImageResource(R.mipmap.star_inactive);
                                            star4.setImageResource(R.mipmap.star_inactive);
                                            star5.setImageResource(R.mipmap.star_inactive);
                                        } else if (ratting >= 2.0 && ratting < 3.0) {
                                            star1.setImageResource(R.mipmap.star_active);
                                            star2.setImageResource(R.mipmap.star_active);
                                            star3.setImageResource(R.mipmap.star_inactive);
                                            star4.setImageResource(R.mipmap.star_inactive);
                                            star5.setImageResource(R.mipmap.star_inactive);
                                        } else if (ratting >= 3.0 && ratting < 4.0) {
                                            star1.setImageResource(R.mipmap.star_active);
                                            star2.setImageResource(R.mipmap.star_active);
                                            star3.setImageResource(R.mipmap.star_active);
                                            star4.setImageResource(R.mipmap.star_inactive);
                                            star5.setImageResource(R.mipmap.star_inactive);
                                        } else if (ratting >= 4.0 && ratting < 5.0) {
                                            star1.setImageResource(R.mipmap.star_active);
                                            star2.setImageResource(R.mipmap.star_active);
                                            star3.setImageResource(R.mipmap.star_active);
                                            star4.setImageResource(R.mipmap.star_active);
                                            star5.setImageResource(R.mipmap.star_inactive);
                                        } else if (ratting >= 5.0) {
                                            star1.setImageResource(R.mipmap.star_active);
                                            star2.setImageResource(R.mipmap.star_active);
                                            star3.setImageResource(R.mipmap.star_active);
                                            star4.setImageResource(R.mipmap.star_active);
                                            star5.setImageResource(R.mipmap.star_active);
                                        } else {
                                            star1.setImageResource(R.mipmap.star_inactive);
                                            star2.setImageResource(R.mipmap.star_inactive);
                                            star3.setImageResource(R.mipmap.star_inactive);
                                            star4.setImageResource(R.mipmap.star_inactive);
                                            star5.setImageResource(R.mipmap.star_inactive);
                                        }
                                    }


                                }
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

                            Toast.makeText(Order_detail.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Order_detail.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Order_detail.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Order_detail.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Orderdetails");
                params.put("user_id", ssp.getUserId(act));
                params.put("order_id", order_id);
                if (helper.pblc == 0) {
                    params.put("user_type", "Customer");
                } else {
                    params.put("user_type", "Provider");
                }


                ////Log.e("params", params.toString());
                return params;
            }

        };


//
// add(stringRequest);
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public void printLog(String key, String value) {
        ////Log.e(key, "=======" + value);
    }


    private void utilityRequest() {

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
                            msg = obj.getString("msg");
                            if (flagValue == 1) {
                                if (msg.equals("Warranty days extended successfully")) {

                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(act);
                                    }
                                    if (msg.equals("Warranty days extended successfully")) {
                                        alert.setTitle("تم تمديد مدة التعميد بنجاح");
                                    }
                                    if (msg.equals("Warranty days extended successfully")) {
                                        alert.setMessage("تم تمديد مدة التعميد بنجاح");
                                    } else {
                                        alert.setMessage("هناك خطأ ما");
                                    }

                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (msg.equals("Warranty days extended successfully")) {
                                                Intent intent = new Intent(Order_detail.this, Order_detail.class);
                                                intent.putExtra("order_id", order_id);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(intent);
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

                                }
                            } else if (flagValue == 2) {


                                if (msg.equals("This order has been cancelled by you successfully")) {

                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(act);
                                    }

                                    if (msg.equals("This order has been cancelled by you successfully")) {
                                        alert.setMessage("تم ارسال سبب الالغاء الى المشرف بنجاح");
//                                        alert.setMessage("تم إلغاء الطلب بنجاح");
                                    } else {
                                        alert.setMessage("هناك خطأ ما");
                                    }

                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (msg.equals("This order has been cancelled by you successfully")) {
                                                Intent intent = new Intent(Order_detail.this, Order_detail.class);
                                                intent.putExtra("order_id", order_id);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(intent);
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

                                }
                            } else if (flagValue == 3) {

                                if (msg.equals("This quotation has been Approved by you successfully")) {

                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(act);
                                    }
                                    if (msg.equals("This quotation has been Approved by you successfully")) {
                                        alert.setTitle("تم قبول هذه التسعيرة بواسطتك بنجاح");
                                    }
                                    if (msg.equals("This quotation has been Approved by you successfully")) {
                                        alert.setMessage("تم قبول هذه التسعيرة بواسطتك بنجاح");
                                    } else {
                                        alert.setMessage("تم قبول هذه التسعيرة بواسطتك بنجاح");
                                    }

                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (msg.equals("This quotation has been Approved by you successfully")) {
                                                Intent intent = new Intent(Order_detail.this, Order_detail.class);
                                                intent.putExtra("order_id", order_id);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(intent);
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

                                }
                            } else if (flagValue == 5) {

                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(act);
                                }

                                alert.setTitle("رسالة");
                                if (msg.equals("Success")) {
                                    alert.setMessage("تم الانتهاء من الصفقة");
                                } else
                                    alert.setMessage(getResources().getString(R.string.msg_something_went_wrong));
                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(Order_detail.this, Order_detail.class);
                                        intent.putExtra("order_id", order_id);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
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
                            } else {

                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(act);
                                }

                                alert.setMessage("تم رفض هذا الطلب بنجاح");
                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(Order_detail.this, Order_detail.class);
                                        intent.putExtra("order_id", order_id);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);

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

                            Toast.makeText(Order_detail.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Order_detail.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Order_detail.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Order_detail.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (flagValue == 1) {
                    params.put("action", "Extendwarranty");
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("extended_warranty_days", days_extend);

                    //Log.e(TAG, "params.....>" + params.toString());
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } else if (flagValue == 2) {
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("cancel_reason", reason);
                    params.put("action", "Cancelorder");
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } else if (flagValue == 3) {
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("quotation_id", quotation_id);
                    params.put("action", "Approvequotation");
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } else if (flagValue == 4) {
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("quotation_id", quotation_id);
                    params.put("action", "Rejectquotation");
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } else if (flagValue == 5) {
                    params.put("order_id", order_id);
                    params.put("action", "CompleteService");
                } /* else if (flagValue == 5) {
                    params.put("order_id", order_id);
                    params.put("action", "ConfirmOrderSP");
                }*/


                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    private void getRequestSpStatus() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);


                        JSONObject obj = null;
                        //Log.e("Sp status: ", "...." + response);
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
                            //spConfirmsCheck = obj.getString("is_approved_sp");

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

                            Toast.makeText(Order_detail.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Order_detail.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Order_detail.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Order_detail.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "ConfirmOrderSP");
                params.put("order_id", order_id);
                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void ratting(String rate) {
        ////Log.e("RATE", "====" + rate);

        if (rate.equals(null) || TextUtils.isEmpty(rate)) {
            ////Log.e("rattting", "11111111");
            star_img1.setImageResource(R.mipmap.star_inactive);
            star_img2.setImageResource(R.mipmap.star_inactive);
            star_img3.setImageResource(R.mipmap.star_inactive);
            star_img4.setImageResource(R.mipmap.star_inactive);
            star_img5.setImageResource(R.mipmap.star_inactive);

        } else {
            ////Log.e("rattting", "22222222");

            ratting = Double.parseDouble(rate);
            if (ratting >= 0.0 && ratting < 1.0) {
                star_img1.setImageResource(R.mipmap.star_inactive);
                star_img2.setImageResource(R.mipmap.star_inactive);
                star_img3.setImageResource(R.mipmap.star_inactive);
                star_img4.setImageResource(R.mipmap.star_inactive);
                star_img5.setImageResource(R.mipmap.star_inactive);
            } else if (ratting >= 1.0 && ratting < 2.0) {
                star_img1.setImageResource(R.mipmap.star_active);
                star_img2.setImageResource(R.mipmap.star_inactive);
                star_img3.setImageResource(R.mipmap.star_inactive);
                star_img4.setImageResource(R.mipmap.star_inactive);
                star_img5.setImageResource(R.mipmap.star_inactive);
            } else if (ratting >= 2.0 && ratting < 3.0) {
                star_img1.setImageResource(R.mipmap.star_active);
                star_img2.setImageResource(R.mipmap.star_active);
                star_img3.setImageResource(R.mipmap.star_inactive);
                star_img4.setImageResource(R.mipmap.star_inactive);
                star_img5.setImageResource(R.mipmap.star_inactive);
            } else if (ratting >= 3.0 && ratting < 4.0) {
                star_img1.setImageResource(R.mipmap.star_active);
                star_img2.setImageResource(R.mipmap.star_active);
                star_img3.setImageResource(R.mipmap.star_active);
                star_img4.setImageResource(R.mipmap.star_inactive);
                star_img5.setImageResource(R.mipmap.star_inactive);
            } else if (ratting >= 4.0 && ratting < 5.0) {
                star_img1.setImageResource(R.mipmap.star_active);
                star_img2.setImageResource(R.mipmap.star_active);
                star_img3.setImageResource(R.mipmap.star_active);
                star_img4.setImageResource(R.mipmap.star_active);
                star_img5.setImageResource(R.mipmap.star_inactive);
            } else if (ratting >= 5.0) {
                star_img1.setImageResource(R.mipmap.star_active);
                star_img2.setImageResource(R.mipmap.star_active);
                star_img3.setImageResource(R.mipmap.star_active);
                star_img4.setImageResource(R.mipmap.star_active);
                star_img5.setImageResource(R.mipmap.star_inactive);
            } else {
                star_img1.setImageResource(R.mipmap.star_inactive);
                star_img2.setImageResource(R.mipmap.star_inactive);
                star_img3.setImageResource(R.mipmap.star_inactive);
                star_img4.setImageResource(R.mipmap.star_inactive);
                star_img5.setImageResource(R.mipmap.star_inactive);
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Order_detail.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("flag", 1);
        startActivity(intent);
    }
}