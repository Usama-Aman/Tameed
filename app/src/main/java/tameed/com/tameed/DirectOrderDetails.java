package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tameed.com.tameed.Adapter.Direct_order_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.LoadApi;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import static tameed.com.tameed.MapView_Activity.RequestPermissionCode;

public class DirectOrderDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView txtHeader, txtSubmit, txt_sub_categ, sop_txt_service, sop_service_percent, sop_service_total;
    private ImageView imgBackHeader, add_img;
    private String TAG = "DirectOrderDetails";


    ImageView imgDatePicker;
    static TextView txtDate, txtTime;
    private int mYear, mMonth, mDay;
    Bitmap bitmap;
    private static int LOAD_IMAGE_RESULTS = 1000;
    JSONObject json = new JSONObject();


    String msg = "", percentage, percentage_fee, total_fee, profile_pic;
    static String selected_date, selected_time;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Direct_order_Adapter direct_order_adapter;
    TextView txt_name;

    ArrayList<Bitmap> dod_img_upload = new ArrayList<>();
    ArrayList<byte[]> image_list = new ArrayList<>();

    String[] dod_category;
    String[] dod_service;
    String[] dod_service_id;
    String[] dod_category_id;
    String path_img = "", pic_status;
    Uri imageUri;
    ProgressDialog dlg = null;
    Bitmap myBitmap, myBitma, rotatedBitmap;
    byte[] byte_arr = null;
    Intent intent_order;
    String provider_id, rating_count;
    ImageView star5, star4, star3, star2, star1;
    EditText sop_service_fee;
    SaveSharedPrefernces ssp;
    Activity act;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    int page = 0, curSize;
    String result_count = "";

    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    String service_fee, order_id;
    String service_name, service_id, category_name, category_id;
    Double ratting;
    EditText spo_des, sop_warranty_days;
    ImageView provider_pic;
    String name;

    //  Nardeep Edit
    ImageView sp_chat;


    String paymentId = "";

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
        ////Log.e(TAG, "****************************");
        setContentView(R.layout.activity_direct_order_details);
        intent_order = getIntent();
        ssp = new SaveSharedPrefernces();
        act = DirectOrderDetails.this;
        helper.image_list.clear();
        dod_category = new String[helper.category_list.size()];
        dod_category = helper.category_list.toArray(dod_category);
        txt_name = (TextView) findViewById(R.id.text_name_direct_order);
        dod_service = new String[helper.service_list.size()];
        dod_service = helper.service_list.toArray(dod_service);

        dod_category_id = new String[helper.category_idlist.size()];
        dod_category_id = helper.category_idlist.toArray(dod_category_id);

        dod_service_id = new String[helper.service_idlist.size()];
        dod_service_id = helper.service_idlist.toArray(dod_service_id);

        sop_warranty_days = (EditText) findViewById(R.id.sop_warranty_days);
        spo_des = (EditText) findViewById(R.id.spo_des);
        provider_pic = (ImageView) findViewById(R.id.imageView4);
        sop_service_percent = (TextView) findViewById(R.id.sop_service_percent);
        sop_service_total = (TextView) findViewById(R.id.sop_service_total);
        rating_count = intent_order.getStringExtra("review_rating");
        ////Log.e("Ratting", "======" + rating_count);
        provider_id = intent_order.getStringExtra("provider_id");
        profile_pic = intent_order.getStringExtra("profile_pic");
        name = intent_order.getStringExtra("name");

        //Nardeep Edit
        sp_chat = (ImageView) findViewById(R.id.sp_chat);

        sp_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent inen = new Intent(getApplicationContext(), Chat_window.class);

                inen.putExtra("firstname", name);
                inen.putExtra("friend_id", provider_id);
                inen.putExtra("profile_pic", profile_pic);
                inen.putExtra("status", "chat");
                startActivity(inen);
            }
        });

        EnableRuntimePermission();


        if (!TextUtils.isEmpty(profile_pic) && !profile_pic.equals(null)) {
            Picasso.with(act).load(profile_pic).placeholder(R.mipmap.no_thumb).into(provider_pic);
        } else {
            provider_pic.setImageResource(R.mipmap.no_thumb);
        }
        if (!TextUtils.isEmpty(name) && !name.equals(null)) {
            txt_name.setText(name);
        } else {
            txt_name.setText("NO Name");
        }
        star5 = (ImageView) findViewById(R.id.img_star5);
        star4 = (ImageView) findViewById(R.id.img_star4);
        star3 = (ImageView) findViewById(R.id.img_star3);
        star2 = (ImageView) findViewById(R.id.img_star2);
        star1 = (ImageView) findViewById(R.id.img_star1);
        sop_service_fee = (EditText) findViewById(R.id.sop_service_fee);
        sop_service_fee.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */


                    handled = false;
                    service_fee = sop_service_fee.getText().toString();

                }
                return handled;
            }
        });

        addTextWatcherToServiceFee();

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectOrderDetails.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("from", "");
                intent.putExtra("click", "");
                intent.putExtra("id", provider_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectOrderDetails.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "");
                intent.putExtra("id", provider_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectOrderDetails.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.putExtra("from", "");
                intent.putExtra("id", provider_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectOrderDetails.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("from", "");
                intent.putExtra("id", provider_id);
                startActivity(intent);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectOrderDetails.this, Review.class);
                intent.putExtra("provider_id", order_id);
                intent.putExtra("click", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("from", "");
                intent.putExtra("id", provider_id);
                startActivity(intent);
            }
        });

        ratting = Double.parseDouble(rating_count);
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
        txtHeader = (TextView) findViewById(R.id.txt_header);
        txtHeader.setText("تفاصيل التعميد المباشر");
        txtSubmit = (TextView) findViewById(R.id.text_submit_direct_order_details);
        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(category_name)) {
                    Toast.makeText(act, "يرجى اختيار الخدمة الفرعية", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(service_name)) {
                    Toast.makeText(act, "يرجى اختيار الخدمة", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(spo_des.getText().toString())) {
                    Toast.makeText(act, "يرجى ادخال الوصف", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sop_warranty_days.getText().toString()) || sop_warranty_days.getText().toString().equals("0")) {
                    Toast.makeText(act, "يرجى ادخال مدة التعميد", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(service_fee)) {
                    service_fee = sop_service_fee.getText().toString();
                    makeStringReq();
                    Toast.makeText(act, "يرجى ادخال قيمة الخدمة", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(selected_date)) {
                    Toast.makeText(act, "يرجى ادخال تاريخ الخدمة", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(service_fee)) {
                    Toast.makeText(act, "يرجى ادخال قيمة الخدمة", Toast.LENGTH_SHORT).show();
                } else {

                    Intent goToPayment = new Intent(DirectOrderDetails.this, PaymentActivity.class);
                    goToPayment.putExtra("order_id", "0");
                    goToPayment.putExtra("provider_id", provider_id);
                    goToPayment.putExtra("total_fee", sop_service_total.getText().toString());
                    goToPayment.putExtra("fromDirectOrder", true);
                    startActivityForResult(goToPayment, 2500);
//                    loadData();

                }
            }
        });
        imgBackHeader = (ImageView) findViewById(R.id.header_back);
        imgBackHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.service_list.clear();
                ;
                helper.category_list.clear();
                helper.category_idlist.clear();
                helper.service_idlist.clear();
                Intent intent = new Intent(DirectOrderDetails.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.direct_order_detail_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        direct_order_adapter = new Direct_order_Adapter(this, dod_img_upload, image_list);
        recyclerView.setAdapter(direct_order_adapter);


        txt_sub_categ = (TextView) findViewById(R.id.txt_sub_categ);
        sop_txt_service = (TextView) findViewById(R.id.sop_txt_service);
        add_img = (ImageView) findViewById(R.id.spo_add_img);
        imgDatePicker = (ImageView) findViewById(R.id.img_calender);

        txtDate = (TextView) findViewById(R.id.textDate);

        imgDatePicker.setOnClickListener(this);


        txt_sub_categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DirectOrderDetails.this);
                builder.setTitle("فئات فرعية");
                builder.setItems(dod_category, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        //here you can use like this... str[position]
                        category_name = dod_category[position].toString();
                        category_id = dod_category_id[position].toString();
                        ////Log.e("Category_Name", "=====" + category_name);
                        ////Log.e("Category_ID", "=====" + category_id);

                        txt_sub_categ.setText(dod_category[position].toString());

                    }

                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        sop_txt_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(category_id)) {
                    Toast.makeText(act, "", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DirectOrderDetails.this);
                    builder.setTitle("الخدمة");
                    builder.setItems(dod_service, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            //here you can use like this... str[position]
                            service_name = dod_service[position].toString();
                            service_id = dod_service_id[position].toString();
                            ////Log.e("Service_name", "======" + service_name);
                            ////Log.e("Service_id", "======" + service_id);
                            sop_txt_service.setText(dod_service[position].toString());

                        }

                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog1 = new Dialog(DirectOrderDetails.this, R.style.DialogSlideAnim);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.frnd_custom);


                dialog1.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

                RelativeLayout takepicture = (RelativeLayout) dialog1.findViewById(R.id.gallery_rl);
                RelativeLayout uploadfile = (RelativeLayout) dialog1.findViewById(R.id.camera_rl);
                RelativeLayout cancel_rl = (RelativeLayout) dialog1.findViewById(R.id.cancel_rl);
                RelativeLayout remove_rl = (RelativeLayout) dialog1.findViewById(R.id.remove_rl);
                remove_rl.setVisibility(View.GONE);
                cancel_rl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();

                    }


                });


                takepicture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ACCESS_REQUST = 1;
                        checkAndroidVersion();
                        dialog1.dismiss();
                    }
                });

                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ACCESS_REQUST = 2;
                        checkAndroidVersion();
                        dialog1.dismiss();
                    }
                });


            }
        });

    }


    Timer timer;
    String textEdit;

    public void addTextWatcherToServiceFee() {

        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
        sop_service_fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //after the change calling the method and passing the search input

                sop_service_fee.removeTextChangedListener(this);

                textEdit = s.toString();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (textEdit != null && textEdit.length() != 0) {
                                    try {
                                        service_fee = sop_service_fee.getText().toString();
                                        timer.cancel();
                                        makeStringReq();
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }
                            }
                        });


                    }
                }, 1000); // 600ms delay before the timer executes the „run“ method from TimerTask

                addTextWatcherToServiceFee();
            }

        });
    }

    @Override
    public void onClick(final View v) {

        if (v == imgDatePicker) {

            // Get Current Date


            showTruitonTimePickerDialog(v);
            showTruitonDatePickerDialog(v);

//            final Calendar c = Calendar.getInstance();
//            mYear = c.get(Calendar.YEAR);
//            mMonth = c.get(Calendar.MONTH);
//            mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                    new DatePickerDialog.OnDateSetListener() {
//
//                        @Override
//                        public void onDateSet(DatePicker view, int year,
//                                              int monthOfYear, int dayOfMonth) {
//
//                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                        }
//
//                    }, mYear, mMonth, mDay);
//
//            datePickerDialog.show();


        }

    }


    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            Date startDate = null;


            c.add(Calendar.DATE, 0);
            //   }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
            DatePicker dp = d.getDatePicker();
            dp.setMinDate(c.getTimeInMillis());
            // d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);

            return d;


        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
//if (TextUtils.isEmpty(selected_time)) {
            txtDate.setText(year + "-" + (month + 1) + "-" + day);
            selected_date = txtDate.getText().toString();
//}
//else {
//    selected_date=year + "-" + (month + 1) + "-" + day;
//    txtDate.setText(year + "-" + (month + 1) + "-" + day + "    " + txtDate.getText().toString());
//}

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            selected_date = "";
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path_img = cursor.getString(columnIndex);
                    cursor.close();
                    new AsyncReceiverTask().execute();


                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {


                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case 1:
                ContentResolver cr = getContentResolver();
                Cursor metaCursor = cr.query(imageUri,
                        new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
                if (metaCursor != null) {
                    try {
                        if (metaCursor.moveToFirst()) {
                            path_img = metaCursor.getString(0);
                        }
                        new AsyncReceiverTask().execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        imageUri = null;
                        path_img = "";
                    } finally {
                        metaCursor.close();

                    }
                }
                break;
            case 2500:
                Log.d(TAG, "onActivityResult: Test");

                if (resultCode == RESULT_OK) {

                    paymentId = data.getStringExtra("paymentId");
                    loadData();
                }

            default:
                break;
        }
    }


    class AsyncReceiverTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= 11) {
                dlg = new ProgressDialog(DirectOrderDetails.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                dlg = new ProgressDialog(DirectOrderDetails.this);
            }
            dlg.setMessage(getResources().getString(R.string.loading));
            dlg.show();
        }

        @Override
        protected String doInBackground(String... params) {


            if (myBitmap != null) {
                myBitmap.recycle();
                myBitmap = null;
            }
            if (myBitma != null) {
                myBitma.recycle();
                myBitma = null;
            }

            try {

                BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                bfOptions.inJustDecodeBounds = false;
                bfOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                bfOptions.inDither = true;

                File file = new File(path_img);


                FileInputStream fs = null;
                if (file.exists()) {


                    try {
                        fs = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //Log.e("Camera", "Image Picker PATH::::" + path_img);
                    try {
                        if (fs != null) {
                            myBitma = BitmapFactory.decodeFile(file.getAbsolutePath(), bfOptions);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            if (myBitma != null) {
                                //myBitma.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                                myBitma.compress(Bitmap.CompressFormat.JPEG, 70, bao);
                                myBitmap = getResizedBitmap(myBitma, 400, 400);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        myBitma.recycle();
                        myBitma = null;

                    } finally {
                        if (fs != null) {
                            try {
                                fs.close();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }

                    //Log.e("Bitmap", "Actual bitmap item::Width:" + myBitmap.getWidth()
                    //    + "   Height:" + myBitmap.getHeight());


                    //Log.e("setting", "img on bitmap from camera");
                    rotatedBitmap = gettingRotatedBitmap(myBitmap, path_img);

                    //Log.e("Bitmap", "Rotaated item::Width:" + rotatedBitmap.getWidth()
                    //  + "   Height:" + rotatedBitmap.getHeight());

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    //rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
                    //Log.e("inside", "camera");

                    //Log.e("11111111111111111", "1111111111111");
//                imgresume = path_img;
                    byte_arr = bao.toByteArray();
                    dod_img_upload.add(rotatedBitmap);

                    image_list.add(bao.toByteArray());
                    helper.image_list.add(byte_arr);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (dlg.isShowing() && dlg != null) {
                dlg.dismiss();
            }
            try {

                //Log.e("load", "photo upload:" + rotatedBitmap.getHeight() + "   width:" + rotatedBitmap.getWidth());
                //myprofile_img_photo.setImageBitmap(rotatedBitmap);
                //carde_pic.setImageBitmap(rotatedBitmap);
                //setcircularimage(rotatedBitmap);

                direct_order_adapter.notifyDataSetChanged();
                // helper.company_logo=rotatedBitmap;
                //counter = "photo";

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        protected Bitmap gettingRotatedBitmap(Bitmap bitmap_leftn, String path) {
            Bitmap rotatedBitmap = null;

            int width = bitmap_leftn.getWidth();
            int height = bitmap_leftn.getHeight();


            ExifInterface exif;
            try {
                exif = new ExifInterface(path);
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);


                //Log.e("bitmap", "11 width:"
                //+ width + "   height:" + height + "   filepath:" + path);

                //Log.e("ROTATION", "ORITATION::::" + rotation
                // + "  rotation 90:" + ExifInterface.ORIENTATION_ROTATE_90
                //  + "  rotation 180:" + ExifInterface.ORIENTATION_ROTATE_180 +
                // "   rotation 270:" + ExifInterface.ORIENTATION_ROTATE_270);
                //if(width > height){
//
                //Log.e("bitmap", "22 width:" + width + "   height:" + height);
                Matrix matrix = new Matrix();

                switch (rotation) {
                    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                        matrix.setScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.setRotate(180);
                        break;

                    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                        matrix.setRotate(180);
                        matrix.postScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_TRANSPOSE:
                        matrix.setRotate(90);
                        matrix.postScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        //Log.e("bitmap", "SWITCH MATRIX:");
                        matrix.setRotate(90);
                        break;

                    case ExifInterface.ORIENTATION_TRANSVERSE:
                        matrix.setRotate(-90);
                        matrix.postScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.setRotate(-90);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        break;
                }
                //Log.e("bitmap", "33111 width:" + width + "   height:" + height);

                rotatedBitmap = Bitmap.createBitmap(bitmap_leftn, 0, 0, 400, 400, matrix, true);

                //Log.e("bitmap", "33222 width:" + rotatedBitmap.getWidth() + "   height:" + rotatedBitmap.getHeight());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return rotatedBitmap;
        }


        public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
            //Log.e("SellItemsNew", "GET_RESIZED_BITMAP:");
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                    matrix, false);
            return resizedBitmap;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helper.service_list.clear();
        ;
        helper.category_list.clear();
        helper.category_idlist.clear();
        helper.service_idlist.clear();
    }


    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
//
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Time t = new Time(hourOfDay, minute, 0);
            Format formatter;
            formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String time = formatter.format(t);

            txtDate.setText(txtDate.getText().toString() + "    " + time);
            selected_time = time;


        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            selected_time = "";
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

                            msg = obj.getString("msg");
                            if (msg.equals("success")) {
                                percentage = obj.getString("percentage");
                                //Log.e("Total_FEe", "====" + percentage);
                                percentage_fee = obj.getString("percentage_fee");
                                //Log.e("percentage_fee", "=======" + percentage_fee);
                                total_fee = obj.getString("total_fee");
                                //Log.e("total_fee", "=======" + total_fee);
                                sop_service_percent.setText(percentage + " %");
                                sop_service_total.setText(total_fee);
                                txtSubmit.setVisibility(View.VISIBLE);
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

                            Toast.makeText(DirectOrderDetails.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(DirectOrderDetails.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(DirectOrderDetails.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(DirectOrderDetails.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Commissionfeecalculate");
                params.put("user_id", ssp.getUserId(act));

                params.put("service_fee", service_fee);

                //Log.e("params", params.toString());
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

    private AsyncLoadData asyncLoad;

    private void loadData() {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            asyncLoad.execute();
        }
    }

    class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = getResources().getString(R.string.loading);
            LoadApi api = new LoadApi();

            json = api.Action_PrivateOrder_Image("Placeprivateorder", ssp.getUserId(act), helper.image_list, provider_id, category_id, service_id, spo_des.getText().toString(),
                    selected_time, selected_date, sop_warranty_days.getText().toString(), service_fee, percentage, percentage_fee, total_fee, paymentId);
            //Log.e("JSON_OBJECT", "====" + json);
            //   {"msg":"Your order has been submitted successfully","order_id":4}


            JSONObject object = api.getResult1();

            //Log.e("JSON_OBJECT1", "====" + object);
            try {
                msg = object.getString("msg");
                order_id = object.getString("order_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);


            try {
                if (msg.equals("Your order has been submitted successfully")) {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(act);
                    }
                    // alert.setTitle("Sorry");
//                    alert.setMessage("تم إرسال معاملةك بنجاح");
                    alert.setMessage("تم ارسال طلبك إلى مقدم الخدمة بنجاح ");


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            helper.service_list.clear();
                            helper.category_list.clear();
                            helper.category_idlist.clear();
                            helper.service_idlist.clear();
                            helper.pblc = 0;
                            Intent intent = new Intent(DirectOrderDetails.this, Order_detail.class);
                            intent.putExtra("order_id", order_id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

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
                    // alert.setTitle("Sorry");
                    alert.setMessage("هناك خطأ ما\n");


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DirectOrderDetails.this,
                android.Manifest.permission.CAMERA)) {

            Toast.makeText(DirectOrderDetails.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(DirectOrderDetails.this, new String[]{
                    android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    public void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = null;
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    public void galleryIntent() {


        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 0);

    }


    private int PERMISSIONS_MULTIPLE_REQUEST = 5;
    int ACCESS_REQUST;

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(DirectOrderDetails.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(DirectOrderDetails.this, android.Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(DirectOrderDetails.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(DirectOrderDetails.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(DirectOrderDetails.this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(DirectOrderDetails.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 5:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile && writeExternalFile) {

                        if (ACCESS_REQUST == 1) {
                            cameraIntent();
                        } else if (ACCESS_REQUST == 2) {
                            galleryIntent();
                        }

                    } else {
                        Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}

