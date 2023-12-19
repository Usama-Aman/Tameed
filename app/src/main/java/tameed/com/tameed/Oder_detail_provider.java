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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
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
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.AdvertisementModel;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Entity.Order_Pic_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by dev on 23-01-2018.
 */
public class Oder_detail_provider extends AppCompatActivity {
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    TextView header_txt;
    ImageView header_back, provider_img1, provider_img2, provider_img3;
    Button btn_accept, btn_reject;
    Intent intent;
    String order_id, order_reference_number;
    TextView spinner_os;
    Double ratting;
    ConstraintLayout review_layout, constraintLayout_reviews_stars;
    SaveSharedPrefernces ssp;
    String quotation_given, quotation_id, quotation_amount, quotation_type, quotation_status, quotation_rejected_by,
            quotation_date, quotation_modified_date;
    Activity act;
    int spConfirmsCheck = 0;
    int page = 0, curSize;
    String result_count = "";
    String refresh = "", chatCount;
    TextView payment_txt;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    ImageView img_customer, star_img1, star_img2, star_img3, star_img4, star_img5;
    String name, review_count, review_rating, customer_id, order_rating, provider_id, is_review_given, order_description, serving_date, serving_time, order_status, order_cancel_reason,
            order_cost, cancelled_by, order_type, sort_order, latitude, longitude, location, total_fee, last_modified_date, added_date, admin_commission,
            percentage, service_fee, warranty_days, service_id, category_id, category_name, service_name, profile_pic_thumb_url,
            profile_pic_2xthumb_url, profile_pic_3xthumb_url, profile_pic_url, new_format_date, tab_status, payment_status, total_paid, remaining_balance, is_admin_approved;
    TextView txt_customer_name, txt_cat_name, txt_service_name, txt_order_dis, txt_warranty, txt_service_fee, txt_service_percent, txt_service_total, txt_service_date, txt_approve_txt, error_msg;
    ImageView star1, star2, star3, star4, star5;
    RecyclerView photo_view;
    Pic_Adapter adapter;
    String msg = "", reason;
    DecimalFormat df;
    int flagValue = 0;
    ConstraintLayout layout2, layout1, layout3, layout_accept_reject, layout_cancel_compplain, layout_warranty_payment, button_layout;
    TextView make_payment, extent_warranty, cancel, complain, resubmitt, client_completeService;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    private boolean isCancelRequest = false;

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
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private String TAG = "Oder_detail_provider";


    //ADD JAY NEW VIEW FROM TRANSFAR DETAILS
    private View view_from_account_provider;
    private RelativeLayout relativeLayout_transferred_from_provider;
    private TextView textView_from_bank_account_provider;

    private ConstraintLayout layoutCancelRequest;
    private Button btnCancelRequestAccept, btnCancelRequestCancel;

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
        //Log.e(TAG, "**************Loooking here**************");
        setContentView(R.layout.as_provider_order_detail);
        ssp = new SaveSharedPrefernces();
        act = Oder_detail_provider.this;
        df = new DecimalFormat(".##");
        review_layout = findViewById(R.id.review_layout);
        spinner_os = (TextView) findViewById(R.id.spinner_os);
        layout1 = (ConstraintLayout) findViewById(R.id.layout1);
        layout_accept_reject = (ConstraintLayout) findViewById(R.id.layout_accept_reject);
        btn_accept = (Button) findViewById(R.id.provider_accept);
        btn_reject = (Button) findViewById(R.id.provider_reject);
        client_completeService = (Button) findViewById(R.id.client_completeService);
        view_from_account_provider = (View) findViewById(R.id.view_from_account_provider);
        relativeLayout_transferred_from_provider = (RelativeLayout) findViewById(R.id.relativeLayout_transferred_from_provider);
        textView_from_bank_account_provider = (TextView) findViewById(R.id.textView_from_bank_account_provider);


        star1 = (ImageView) findViewById(R.id.imageView11);
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        star2 = (ImageView) findViewById(R.id.imageView13);
        star3 = (ImageView) findViewById(R.id.payment_img1);
        star4 = (ImageView) findViewById(R.id.imageView31);
        star5 = (ImageView) findViewById(R.id.imageView35);

        star_img1 = (ImageView) findViewById(R.id.star1);
        payment_txt = (TextView) findViewById(R.id.textView70);
        star_img2 = (ImageView) findViewById(R.id.star2);
        star_img3 = (ImageView) findViewById(R.id.star3);
        star_img4 = (ImageView) findViewById(R.id.star4);
        star_img5 = (ImageView) findViewById(R.id.star5);
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
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
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
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
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        constraintLayout_reviews_stars = findViewById(R.id.constraintLayout_reviews_stars);
        constraintLayout_reviews_stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oder_detail_provider.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "yes");
                intent.putExtra("from", "order_details");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        layout_cancel_compplain = (ConstraintLayout) findViewById(R.id.layout_cancel_compplain);
        layout_warranty_payment = (ConstraintLayout) findViewById(R.id.layout_warranty_payment);
        photo_view = (RecyclerView) findViewById(R.id.photo_view);
        error_msg = (TextView) findViewById(R.id.error_msg);
        layoutManager = new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false);
        photo_view.setLayoutManager(layoutManager);

        make_payment = (Button) findViewById(R.id.make_payment);
        extent_warranty = (Button) findViewById(R.id.extend_warraty);
        cancel = (Button) findViewById(R.id.provider_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Oder_detail_provider.this);
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
                            flagValue = 4;
                            utilityRequest();
                            dialog.dismiss();
                        }


                    }
                });

            }
        });
        complain = (Button) findViewById(R.id.provider_complete);

        txt_warranty = (TextView) findViewById(R.id.order_detail_warranty);
        txt_service_fee = (TextView) findViewById(R.id.service_fee);
        txt_service_percent = (TextView) findViewById(R.id.service_percent);
        txt_service_total = (TextView) findViewById(R.id.service_total);
        txt_service_date = (TextView) findViewById(R.id.service_date);
        txt_approve_txt = (TextView) findViewById(R.id.textView71);
        txt_order_dis = (TextView) findViewById(R.id.textView55);
        txt_service_name = (TextView) findViewById(R.id.textView53);
        txt_cat_name = (TextView) findViewById(R.id.order_cat_nam);
        img_customer = (ImageView) findViewById(R.id.imageView10);
        txt_customer_name = (TextView) findViewById(R.id.as_provider_customer_name);

        header_txt = (TextView) findViewById(R.id.txt_header);

        intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oder_detail_provider.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("flag", 1);
                startActivity(intent);
            }
        });


        if (AppController.isOnline(Oder_detail_provider.this)) {

            makeStringReq();
        } else {
            AppController.showAlert(Oder_detail_provider.this,
                    getString(R.string.networkError_Message));
        }

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(act);
                }


                alert.setMessage("هل ترغب بإكمال هذا المعاملة");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
//                                    helper.list_name=null;
//                                    helper.list_id=null;
//                                    helper.driver_array_list=null;
//                                    helper.hrw_array_list=null;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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


                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(act);
                }


                alert.setMessage("هل ترغب بإكمال هذه المعاملة؟");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.e("accepting..............", "............");
                        flagValue = 5;
                        utilityRequest();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {
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


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (order_type.equals("private")) {
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
                            try {
//                                    helper.list_name=null;
//                                    helper.list_id=null;
//                                    helper.driver_array_list=null;
//                                    helper.hrw_array_list=null;


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            flagValue = 1;
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
                } else {


                    final Dialog dialog1 = new Dialog(Oder_detail_provider.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.dialog_quotation);

                    dialog1.show();

                    Button extend_Warranty = (Button) dialog1.findViewById(R.id.send_admin);
                    final EditText edt_days = dialog1.findViewById(R.id.edt_quotation);
                    Button chnage_no = (Button) dialog1.findViewById(R.id.send_cancel);
                    extend_Warranty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            quotation_amount = edt_days.getText().toString();
                            if (TextUtils.isEmpty(quotation_amount)) {
                                Toast.makeText(act, "إلغاء", Toast.LENGTH_SHORT).show();
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


            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "reject*******");

                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(act);
                }

                alert.setMessage("هل ترغب برفض المعاملة");
                //Log.e(TAG, "reject***هل ترغب برفض المعاملة****");

                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flagValue = 2;
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


        layoutCancelRequest = findViewById(R.id.layoutCancelRequest);
        btnCancelRequestAccept = findViewById(R.id.btnCancelRequestAccept);
        btnCancelRequestCancel = findViewById(R.id.btnCancelRequestCancel);

        btnCancelRequestAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelRequestDialog(true, "هل تريد الموافقة على طلب إلغاء هذا العميل؟");
            }
        });

        btnCancelRequestCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelRequestDialog(false, "هل تريد رفض طلب إلغاء هذا العميل؟");
            }
        });

    }

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
                        //Log.e(TAG, "Response Data...." + response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                                //Log.e(TAG, "JAY Json Data...." + response.toString().substring(start1, end));
                            }


                            if (obj.has("order_details")) {

                                JSONObject json = obj.getJSONObject("order_details");
                                //Log.e(TAG, "JAY json........." + json);
                                if (json.has("is_approved_sp"))
                                    spConfirmsCheck = json.getInt("is_approved_sp");

                                name = json.getString("name");
                                printLog("name", name);
                                if (name.equals(null) || TextUtils.isEmpty(name)) {
                                    txt_customer_name.setText("لايوجد اسم");
                                } else {
                                    txt_customer_name.setText(name);
                                }

                                if (json.has("is_cancel_request"))
                                    isCancelRequest = json.getBoolean("is_cancel_request");

                                profile_pic_thumb_url = json.getString("profile_pic_thumb_url");
                                printLog("profile_pic_thumb_url", profile_pic_thumb_url);

                                profile_pic_url = json.getString("profile_pic_url");
                                printLog("profile_pic_url", profile_pic_url);

                                profile_pic_2xthumb_url = json.getString("profile_pic_2xthumb_url");
                                printLog("profile_pic_2xthumb_url", profile_pic_2xthumb_url);
                                if (!profile_pic_2xthumb_url.equals(null) && !TextUtils.isEmpty(profile_pic_2xthumb_url)) {
                                    Picasso.with(act).load(profile_pic_2xthumb_url).placeholder(R.mipmap.no_thumb).into(img_customer);
                                } else {
                                    img_customer.setImageResource(R.mipmap.no_thumb);
                                }

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


                                order_reference_number = json.getString("order_reference_number");
                                printLog("order_reference_number", order_reference_number);
                                header_txt.setText("معاملة #" + order_reference_number);

                                order_status = json.getString("order_status");
                                printLog("order_status", order_status);

                                order_cancel_reason = json.getString("order_cancel_reason");
                                printLog("order_cancel_reason", order_cancel_reason);

                                order_cost = json.getString("order_cost");
                                printLog("order_cost", order_cost);

                                cancelled_by = json.getString("cancelled_by");
                                printLog("cancelled_by", cancelled_by);

                                order_type = json.getString("order_type");
                                printLog("order_type", order_type);

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
                                textView_from_bank_account_provider.setText(from_bank_account);
                                if (!from_bank_account.equalsIgnoreCase("")) {
                                    view_from_account_provider.setVisibility(View.VISIBLE);
                                    relativeLayout_transferred_from_provider.setVisibility(View.VISIBLE);
                                } else {
                                    view_from_account_provider.setVisibility(View.GONE);
                                    relativeLayout_transferred_from_provider.setVisibility(View.GONE);
                                }


                                if (json.has("is_review_given")) {
                                    ////Log.e("1111", "review");

                                    is_review_given = json.getString("is_review_given");
                                    printLog("is_review_given", is_review_given);
                                    order_rating = json.getString("order_rating");
                                    printLog("order_rating", order_rating);

                                } else {
                                    is_review_given = "no";
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
                                    //Todo Nardeep
                                    //error_msg.setVisibility(View.VISIBLE);

                                }


                                new_format_date = json.getString("new_format_date");
                                printLog("new_format_date", new_format_date);

                                tab_status = json.getString("tab_status");
                                printLog("tab_status", tab_status);

                                if (json.has("is_admin_approved"))
                                    is_admin_approved = json.getString("is_admin_approved");

                                //  helper.list_type=tab_status;


                                String quotation_date, quotation_modified_date;

                            /*    if (json.has("is_review_given")) {

                                    is_review_given = json.getString("is_review_given");
                                    printLog("is_review_given", is_review_given);
                                    order_rating = json.getString("order_rating");
                                    printLog("order_rating", order_rating);

                                }*/

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


                                if (isCancelRequest) {
                                    layoutCancelRequest.setVisibility(View.VISIBLE);
                                } else {
                                    layoutCancelRequest.setVisibility(View.GONE);

                                    if (tab_status.equals("New")) {
                                        //Log.e(TAG, "tab_status===>>>>>New>>>>>>");


                                        //REMOVE JAY TODO NEW 11_02_2019
                                        // LAST WORKING ALL TIME CHANGES LAST EDIT CLAINT REMOVE REMOVE BUTTON
                                        //layout_accept_reject.setVisibility(View.VISIBLE);


                                        if (order_type.equals("public")) {

                                            //ADD/EDIT JAY TODO NEW 11_02_2019
                                            layout_accept_reject.setVisibility(View.VISIBLE);
                                            btn_accept.setVisibility(View.VISIBLE);
                                            btn_reject.setVisibility(View.GONE); //HERE GONE REJECT BUTTON
                                            btn_reject.setText("تم رفض معاملة");

                                            txt_approve_txt.setText(last_modified_date);
                                            if (quotation_given.equals("no")) {
                                                layout1.setVisibility(View.GONE);
                                            } else {

                                                layout1.setVisibility(View.VISIBLE);
                                                spinner_os.setText(ssp.getName(act));
                                                txt_service_fee.setText("" + quotation_amount + " " + "ريال");
                                                //txt_service_percent.setText(percentage);
                                                txt_service_total.setText("" + quotation_amount + " " + "ريال");

                                            }

                                        } else {

                                            //ADD JAY TODO INSIDE CONDITION NEW 11_02_2019
                                            layout_accept_reject.setVisibility(View.VISIBLE);

                                            txt_approve_txt.setText("عيّن العميل هذا المعاملة مباشرة" + last_modified_date);
                                            layout_cancel_compplain.setVisibility(View.VISIBLE);
                                            complain.setVisibility(View.GONE);
                                            layout1.setVisibility(View.VISIBLE);
                                            spinner_os.setText(ssp.getName(act));
                                            txt_service_fee.setText("" + quotation_amount + " " + "ريال");
                                            txt_service_total.setText("" + quotation_amount + " " + "ريال");
                                        }
                                        layout_cancel_compplain.setVisibility(View.GONE);
                                        complain.setVisibility(View.GONE);

                                    } else if (tab_status.equals("Approved")) {

                                        payment_status = json.getString("payment_status");
                                        //Log.e(TAG, "tab_status===>>>>>Approved>>>>>>" + payment_status);
                                        total_paid = json.getString("total_paid");
                                        if (payment_status.equals("PAID") || payment_status.equals("PARTIALLY PAID")) {
                                            double value = Double.parseDouble(total_fee);
                                            String number = df.format(value);
                                            //TODO JAY REPLACE ARABIC TEXT
                                            payment_txt.setText("حالة المدفوعات " + getString(R.string.str_Paid) + "\n تلقى المبلغ ريال" + value);
                                            layout_cancel_compplain.setVisibility(View.GONE);
                                            if (is_admin_approved != null && is_admin_approved.length() != 0) {
                                                if (is_admin_approved.equalsIgnoreCase("approved")) {
                                                    //TODO ADD JAY
                                                    layout_cancel_compplain.setVisibility(View.VISIBLE);
                                                    complain.setVisibility(View.VISIBLE);
                                                    cancel.setVisibility(View.GONE);
                                                }
                                            }


                                        } else if (payment_status.equals("UNPAID")) {
                                            double value = Double.parseDouble(total_fee);
                                            String number = df.format(value);
                                            //payment_txt.setText("حالة المدفوعات " + payment_status + "\n  المبلغ المعلق ريال سعودي" + value);
                                            //EDIT JAY REMOVE TIS WORD "المعلق"
                                            //payment_txt.setText("حالة المدفوعات " + payment_status + "\n  المبلغ  ريال سعودي" + value);
                                            //TODO JAY REPLACE ARABIC TEXT
                                            payment_txt.setText("حالة المدفوعات " + getString(R.string.str_Unpaid) + "\n  المبلغ  ريال سعودي" + value);
                                            //TODO ADD JAY
                                            layout_cancel_compplain.setVisibility(View.VISIBLE);
                                            complain.setVisibility(View.GONE);
                                            cancel.setVisibility(View.VISIBLE);

                                        } else {
                                            remaining_balance = json.getString("remaining_balance");
                                            double value = Double.parseDouble(remaining_balance);
                                            String number = df.format(value);
                                            //payment_txt.setText("حالة المدفوعات " + payment_status + "\n المبلغ المعلق ريال سعودي " + value);
                                            //EDIT JAY REMOVE TIS WORD "المعلق"
                                            payment_txt.setText("حالة المدفوعات " + payment_status + "\n المبلغ  ريال سعودي " + value);
                                            //TODO ADD JAY
                                            layout_cancel_compplain.setVisibility(View.VISIBLE);
                                            //complain.setVisibility(View.GONE);
                                            cancel.setVisibility(View.GONE);
                                        }
                                        txt_approve_txt.setText("هذا الترتيب من" + quotation_amount + " تمت الموافقة على " + last_modified_date);
                                        layout1.setVisibility(View.VISIBLE);
                                        spinner_os.setText(ssp.getName(act));
                                        txt_service_fee.setText("" + quotation_amount + " " + "ريال");
                                        txt_service_total.setText("" + quotation_amount + " " + "ريال");
                                        layout_accept_reject.setVisibility(View.GONE);
                                        layout_warranty_payment.setVisibility(View.GONE);

                                        //Log.e("SP spConfirmsCheck:", "..." + spConfirmsCheck);


                                    } else if (tab_status.equals("Accepted")) {
                                        //Log.e(TAG, "tab_status===>>>>>Accepted>>>>>>");
                                        txt_approve_txt.setText("هذا الترتيب من" + quotation_amount + "مقبول على " + last_modified_date);
                                        layout_cancel_compplain.setVisibility(View.GONE);

                                        complain.setVisibility(View.GONE);

                                        layout1.setVisibility(View.VISIBLE);
                                        spinner_os.setText(ssp.getName(act));
                                        txt_service_fee.setText("" + quotation_amount + " " + "ريال");
                                        txt_service_total.setText("" + quotation_amount + " " + "ريال");

                                        layout_cancel_compplain.setVisibility(View.VISIBLE);
                                        complain.setVisibility(View.GONE);
//                                        cancel.setVisibility(View.VISIBLE);
                                        cancel.setVisibility(View.GONE);

                                    } else if (tab_status.equals("Completed")) {
                                        //Log.e(TAG, "tab_status===>>>>>Completed>>>>>>");
                                        payment_status = json.getString("payment_status");
                                        total_paid = json.getString("total_paid");
                                        if (payment_status.equals("PAID")) {
                                            double value = Double.parseDouble(total_fee);
                                            String number = df.format(value);
                                            //payment_txt.setText("حالة المدفوعات " + payment_status + "\n تلقى المبلغ ريال" + value);
                                            //TODO JAY REPLACE ARABIC TEXT
                                            payment_txt.setText("حالة المدفوعات " + getString(R.string.str_Paid) + "\n تلقى المبلغ ريال" + value);

                                            ////Log.e("Payment", "=====" + number);
                                            // make_payment.setVisibility(View.GONE);
                                        } else if (payment_status.equals("UNPAID")) {
                                            double value = Double.parseDouble(total_fee);
                                            String number = df.format(value);
                                            //payment_txt.setText("حالة المدفوعات" + payment_status + "\n المبلغ المعلق ريال سعودي" + value);
                                            //EDIT JAY REMOVE TIS WORD "المعلق"
                                            //payment_txt.setText("حالة المدفوعات" + payment_status + "\n المبلغ  ريال سعودي" + value);
                                            //TODO JAY REPLACE ARABIC TEXT
                                            payment_txt.setText("حالة المدفوعات" + getString(R.string.str_Unpaid) + "\n المبلغ  ريال سعودي" + value);

                                            ////Log.e("Payment", "=====" + number);
                                            //make_payment.setVisibility(View.VISIBLE);
                                        } else {
                                            //make_payment.setVisibility(View.GONE);
                                            remaining_balance = json.getString("remaining_balance");
                                            double value = Double.parseDouble(remaining_balance);
                                            String number = df.format(value);
                                            ////Log.e("Payment", "=====" + number);

                                            // payment_txt.setText("حالة المدفوعات" + payment_status + "\n المبلغ المعلق ريال سعودي" + value);
                                            //EDIT JAY REMOVE TIS WORD "المعلق"
                                            payment_txt.setText("حالة المدفوعات" + payment_status + "\n المبلغ  ريال سعودي" + value);
                                        }
                                        txt_approve_txt.setText("هذا الترتيب من" + quotation_amount + " اكتمل على " + last_modified_date);
                                        layout_warranty_payment.setVisibility(View.GONE);
                                        cancel.setVisibility(View.GONE);
                                        extent_warranty.setVisibility(View.GONE);
                                        layout1.setVisibility(View.VISIBLE);
                                        spinner_os.setText(ssp.getName(act));
                                        txt_service_fee.setText("" + quotation_amount + " " + "ريال");
                                        txt_service_total.setText("" + quotation_amount + " " + "ريال");
                                        layout_cancel_compplain.setVisibility(View.GONE);
                                    } else {
                                        if (tab_status.equals("Rejected")) {
                                            //Log.e(TAG, "tab_status===>>>>>Rejected>>>>>>");
                                            if (order_type.equals("public")) {
                                                layout_accept_reject.setVisibility(View.VISIBLE);
                                                btn_reject.setVisibility(View.GONE);
                                                btn_accept.setText("إعادة التسعير");
                                            }

                                        }
                                        txt_approve_txt.setText("هذا المعاملة هو " + tab_status + "على" + last_modified_date);
                                        layout1.setVisibility(View.VISIBLE);
                                        spinner_os.setText(ssp.getName(act));
                                        txt_service_fee.setText("" + quotation_amount + " " + "ريال");
                                        txt_service_total.setText("" + quotation_amount + " " + "ريال");
                                    }

                                    if (is_review_given.equals("yes")) {
                                        // resubmitt.setVisibility(View.GONE);
                                        review_layout.setVisibility(View.VISIBLE);
                                        constraintLayout_reviews_stars.setVisibility(View.VISIBLE);
                                        ratting(order_rating);

                                    } else {
                                        review_layout.setVisibility(View.GONE);
                                        constraintLayout_reviews_stars.setVisibility(View.GONE);
//                                    resubmitt.setText("Give Review");
//                                    resubmitt.setVisibility(View.VISIBLE);
                                    }

                                    double value = Double.parseDouble(review_rating);
                                    String number = df.format(value);
                                    if (number.equals(null) || TextUtils.isEmpty(number)) {
                                        ////Log.e("rattt", "11111111   " + number);
                                        star1.setImageResource(R.mipmap.star_inactive);
                                        star2.setImageResource(R.mipmap.star_inactive);
                                        star3.setImageResource(R.mipmap.star_inactive);
                                        star4.setImageResource(R.mipmap.star_inactive);
                                        star5.setImageResource(R.mipmap.star_inactive);

                                    } else {
                                        ////Log.e("rattt", "22222222    " + number);
                                        ratting = Double.parseDouble(review_rating);
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

                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

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


    private void utilityRequest() {
//        if (page==0) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);
        //  }
        //Log.e("heloooooooooo..........", "...............");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (flagValue == 5 || flagValue == 3) {

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
                                //Log.e(TAG, "msg*******>" + msg);


                                //Log.e("flag 5:", "....");
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

                                        Intent intent = new Intent(Oder_detail_provider.this, Oder_detail_provider.class);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {


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
                                //Log.e(TAG, "msg*******>" + msg);

                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(act);
                                }

                                if (msg.equals("This order has been Accepted by you successfully")) {
                                    alert.setMessage("تم ارسال التسعيرة بنجاح");
                                    //Log.e(TAG, "msg 1*******>" + msg);
                                } else if (msg.equals("This order has been Rejected by you successfully")) //ADD JIGS
                                {
                                    alert.setTitle(getResources().getString(R.string.msg_quotation_rejected_successfully)); //Request rejected successfully
                                    //Log.e(TAG, "msg 2*******>" + msg);
                                } else if (msg.equals("This quotation has been Re-submitted by you successfully")) //ADD JIGS
                                {
                                    alert.setTitle("تم ارسال التسعيرة بنجاح"); //Request rejected successfully
                                    //Log.e(TAG, "msg 2*******>" + msg);
                                } else {
                                    alert.setMessage(msg);
                                    //Log.e(TAG, "msg 3*******>" + msg);
                                }

                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
//                                    helper.list_name=null;
//                                    helper.list_id=null;
//                                    helper.driver_array_list=null;
//                                    helper.hrw_array_list=null;


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Intent intent = new Intent(Oder_detail_provider.this, Oder_detail_provider.class);
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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                //Log.e("There...........", "............");
                Map<String, String> params = new HashMap<String, String>();
                if (flagValue == 1) {
                    params.put("action", "Orderaccept");
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("quotation_amount", quotation_amount);
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } else if (flagValue == 2) {
                    params.put("action", "Rejectorder");
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("quotation_amount", quotation_amount);
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } /*else if (flagValue == 3) {
                    params.put("action", "Completeorder");
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("quotation_amount", quotation_amount);
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                }*/ else if (flagValue == 4) {
                    params.put("action", "Cancelorder");
                    params.put("user_id", ssp.getUserId(act));
                    params.put("order_id", order_id);
                    params.put("quotation_amount", quotation_amount);
                    if (helper.pblc == 0) {
                        params.put("user_type", "Customer");
                    } else {
                        params.put("user_type", "Provider");
                    }
                } else if (flagValue == 5 || flagValue == 3) {
                    params.put("order_id", order_id);
                    params.put("action", "ConfirmOrderSP");
                }

                //Log.e("paramssssssssssssssss", params.toString());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Oder_detail_provider.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("flag", 1);
        startActivity(intent);
    }

    public void ratting(String rate) {
        ////Log.e("RATE", "====" + rate);
        if (rate.equals(null) && TextUtils.isEmpty(rate)) {
            ////Log.e("RATTTING", "========" + rate);
            ////Log.e("rattting", "11111111    " + rate);
            star_img1.setImageResource(R.mipmap.star_inactive);
            star_img2.setImageResource(R.mipmap.star_inactive);
            star_img3.setImageResource(R.mipmap.star_inactive);
            star_img4.setImageResource(R.mipmap.star_inactive);
            star_img5.setImageResource(R.mipmap.star_inactive);

        } else {
            ////Log.e("rattting", "22222222  " + rate);

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

    private void showCancelRequestDialog(boolean isCancelAccepted, String dialogText) {
        {

            AlertDialog.Builder alert;
            if (Build.VERSION.SDK_INT >= 11) {
                alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                alert = new AlertDialog.Builder(act);
            }
            alert.setMessage(dialogText);
            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    requestCancelApi(isCancelAccepted);
                    dialogInterface.dismiss();
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
    }

    private void requestCancelApi(boolean isCancelAccepted) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url, response -> {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            try {
                JSONObject jsonObject = new JSONObject(response.toString());

                if (jsonObject.has("status")) {
                    if (jsonObject.getString("status").equals("Success")) {
                        Intent intent = new Intent(Oder_detail_provider.this, Oder_detail_provider.class);
                        intent.putExtra("order_id", order_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                //TODO
            } else if (error instanceof ServerError) {
                Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                //TODO
            } else if (error instanceof NetworkError) {
                Toast.makeText(Oder_detail_provider.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                //TODO

            } else if (error instanceof ParseError) {
                //TODO
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Cancelorderrequest");
                params.put("user_id", ssp.getUserId(act));
                params.put("order_id", order_id);

                if (isCancelAccepted) {
                    params.put("status", "Approved");
                } else {
                    params.put("status", "Rejected");
                }


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }


}





