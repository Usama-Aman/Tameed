package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.payment.BrandsValidation;
import com.oppwa.mobile.connect.payment.CheckoutInfo;
import com.oppwa.mobile.connect.payment.ImagesRequest;
import com.oppwa.mobile.connect.payment.PaymentParams;
import com.oppwa.mobile.connect.payment.card.CardPaymentParams;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.ITransactionListener;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Banklist_Vo;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Entity.Saved_Card_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.LoadApi;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.payment.BasePaymentActivity;
import tameed.com.tameed.payment.common.Constants;

public class PaymentActivity extends BasePaymentActivity implements ITransactionListener {
    private TextView txtHeader;
    private ImageView ImgRadio_cash, ImgRadio_Card, header_back;
    private String TAG = "PaymentActivity";

    boolean bool_yes = true;
    EditText amount, card_no, expiry, cvv, holder_name;
    //EditText bank_account;
    ImageView payment_img1, payment_img2, payment_img3;
    Bitmap bitmap;
    private static int LOAD_IMAGE_RESULTS = 1000;
    ConstraintLayout layout4;
    int flag = 0;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg = "", order_id, provider_id, total_fee;
    Boolean check = false, fromDirectOrder = false;
    SaveSharedPrefernces ssp;
    Activity act;
    TextView txt_submitt, txt;

    Intent intent1;

    String result = "", saved_card = "yes", from = "add_card", message = "";
    String payment_type, amount_str, acc_no, card_id, card_name, expire, cvv_str, card_num;
    ArrayList<Saved_Card_Entity> card_list = new ArrayList<>();
    String path_img = "", pic_status;
    Uri imageUri;
    ProgressDialog dlg = null;
    Bitmap myBitmap, myBitma, rotatedBitmap;
    byte[] byte_arr = null;
    int flagValue = 0;
    ArrayList<byte[]> byte_list = new ArrayList<>();
    byte[] image1, image2, image3;
    RecyclerView card_view;
    LinearLayoutManager layoutManager;

    Card_Pic_Adapter adapter;
    ImageView check_image;
    Boolean bool = false;

    TextView txt_month, txt_year;
    String[] month_list = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String month_str, year_str;
    int year;
    String[] year_list = new String[15];
    ImageView profile_pic;


    private ArrayList<String> ArrayListbank_account;
    private Spinner spinner_order_payment_bank_acct;
    private String pass_selected_items = "";

    private EditText order_name;
    private String str_from_bank_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////Log.e(TAG, "****************************");
        this.checkoutId = "";
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        //setContentView(R.layout.test_layout);
        //setContentView(R.layout.bank_layout);
        setContentView(R.layout.bank_layout_new_ar);

        intent1 = getIntent();
        order_id = intent1.getStringExtra("order_id");
        provider_id = intent1.getStringExtra("provider_id");
        total_fee = intent1.getStringExtra("total_fee");
        fromDirectOrder = intent1.getBooleanExtra("fromDirectOrder", false);


        ssp = new SaveSharedPrefernces();
        act = PaymentActivity.this;

        ArrayListbank_account = new ArrayList<>();
        spinner_order_payment_bank_acct = (Spinner) findViewById(R.id.spinner_order_payment_bank_acct);


        order_name = (EditText) findViewById(R.id.order_name);
        txt = (TextView) findViewById(R.id.txt);
        year = Calendar.getInstance().get(Calendar.YEAR);
        profile_pic = (ImageView) findViewById(R.id.imageView11);
        if (TextUtils.isEmpty(ssp.getKEY_profile_pic_thumb_url(act))) {
            profile_pic.setImageResource(R.mipmap.no_thumb);
        } else {
            Picasso.with(act).load(ssp.getKEY_profile_pic_thumb_url(act)).error(R.mipmap.no_thumb).placeholder(R.mipmap.no_thumb).into(profile_pic);
        }
        for (int i = 0; i < 15; i++) {
            year_list[i] = String.valueOf(year + i);
            year = Calendar.getInstance().get(Calendar.YEAR);
            //////Log.e("YEAR   " + i, "=====" + year_list[i]);
        }
        txt_month = (TextView) findViewById(R.id.txt_month);
        txt_year = (TextView) findViewById(R.id.txt_year);
        txt_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_type.equals("Card")) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= 11) {
                        builder = new AlertDialog.Builder(PaymentActivity.this,
                                AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        builder = new AlertDialog.Builder(PaymentActivity.this);
                    }
                    builder.setTitle("اختر الشهر");

                    builder.setSingleChoiceItems(month_list, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    String b_name = "";
                                    month_str = month_list[item];

                                    txt_month.setText(month_str + "/");


//                                        sub_name = name;

//
//                                        String contid = code_list.get(item);
                                    dialog.dismiss();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }
        });
        txt_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        final Dialog d = new Dialog(Test_Layout.this);
//        d.setTitle("Year Picker");
//        d.setContentView(R.layout.yeardialog);
//        Button set = (Button) d.findViewById(R.id.button1);
//        Button cancel = (Button) d.findViewById(R.id.button2);
//        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);
//
//        nopicker.setMinValue(0); //from array first value
//        //Specify the maximum value/number of NumberPicker
//        nopicker.setMaxValue(year_list.length-1); //to array last value
//
//        //Specify the NumberPicker data source as array elements
//        nopicker.setDisplayedValues(year_list);
//
//        //Gets whether the selector wheel wraps when reaching the min/max value.
//        nopicker.setWrapSelectorWheel(false);
//
//       // nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//
////        nopicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
////            @Override
////            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
////                //Display the newly selected value from picker
////                txt_year.setText(year_list[newVal]);
////                d.dismiss();
////            }
////        });
//        set.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                txt_year.setText(String.valueOf(nopicker.getValue()));
//                d.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                txt_year.setText(String.valueOf(year));
//                d.dismiss();
//            }
//        });
//
//        d.show();
                if (payment_type.equals("Card")) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= 11) {
                        builder = new AlertDialog.Builder(PaymentActivity.this,
                                AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        builder = new AlertDialog.Builder(PaymentActivity.this);
                    }
                    builder.setTitle("Select Year");

                    builder.setSingleChoiceItems(year_list, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    String b_name = "";
                                    year_str = year_list[item];

                                    txt_year.setText(year_str);


//                                        sub_name = name;

//
//                                        String contid = code_list.get(item);
                                    dialog.dismiss();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });
        layoutManager = new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false);
        card_view = (RecyclerView) findViewById(R.id.card_view);
        card_view.setLayoutManager(layoutManager);
        txt_submitt = (TextView) findViewById(R.id.textView66);
        txtHeader = (TextView) findViewById(R.id.txt_header);
        txtHeader.setText("المدفوعات");
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        check_image = (ImageView) findViewById(R.id.check_image);

        amount = (EditText) findViewById(R.id.order_payment_amt);
        //bank_account = (EditText) findViewById(R.id.order_payment_bank_acct);
        card_no = (EditText) findViewById(R.id.order_payment_card_number);
        // card_no.setText("4111111111111111");

        cvv = (EditText) findViewById(R.id.order_payment_cvv);
        //cvv.setText("123");
        check_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bool) {
                    from = "add_card";
                    saved_card = "yes";
                    check_image.setImageResource(R.drawable.ic_checktick);
                } else {
                    from = "add_card";
                    saved_card = "";
                    check_image.setImageResource(R.drawable.ic_unchecktick);
                }
                bool = !bool;
            }
        });
        cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String data = charSequence.toString();
                if (data.length() > 2) {
                    if (payment_type.equals("Save")) {
                        utiltyRequest2();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder_name = (EditText) findViewById(R.id.order_payment_holder_name);
        //  holder_name.setText("Jhon");
//utiltyRequest();

        amount.setEnabled(false);
        amount.setBackgroundResource(R.color.grey);
        amount.setText(total_fee);
        //bank_account.setEnabled(false);
        //bank_account.setBackgroundResource(R.color.grey);

        spinner_order_payment_bank_acct.setEnabled(false);

        card_no.setEnabled(false);
        card_no.setBackgroundResource(R.color.grey);


        cvv.setEnabled(false);
        cvv.setBackgroundResource(R.color.grey);

        holder_name.setEnabled(false);
        holder_name.setBackgroundResource(R.color.grey);

        //  Radio switch yes/no

        ImgRadio_cash = (ImageView) findViewById(R.id.img_radio_cash);

        ImgRadio_Card = (ImageView) findViewById(R.id.img_radio_card);
        ImgRadio_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        payment_type = "Card";
        check_image.setVisibility(View.GONE);
        txt.setVisibility(View.GONE);
        ImgRadio_Card.setImageResource(R.mipmap.radio_checked);
        ImgRadio_cash.setImageResource(R.mipmap.radio_w2x);
        amount.setEnabled(false);
        amount.setBackgroundResource(R.color.grey);
        spinner_order_payment_bank_acct.setEnabled(false);
        card_no.setEnabled(true);
        card_no.setBackgroundResource(R.color.white);
        cvv.setEnabled(true);
        cvv.setBackgroundResource(R.color.white);
        holder_name.setEnabled(true);
        holder_name.setBackgroundResource(R.color.white);

        ImgRadio_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check_image.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);

                payment_type = "Cash";
                ImgRadio_Card.setImageResource(R.mipmap.radio_w2x);
                ImgRadio_cash.setImageResource(R.mipmap.radio_checked);
                card_no.setEnabled(false);
                card_no.setBackgroundResource(R.color.grey);
                txt_month.setText("00/");
                txt_year.setText("0000");
                cvv.setEnabled(false);
                cvv.setBackgroundResource(R.color.grey);
                holder_name.setEnabled(false);
                holder_name.setBackgroundResource(R.color.grey);

                //amount.setEnabled(true);
                amount.setEnabled(false);
                amount.setBackgroundResource(R.color.white);

                //bank_account.setEnabled(true);
                //bank_account.setBackgroundResource(R.color.white);

                spinner_order_payment_bank_acct.setEnabled(true);

            }


        });


        payment_img1 = (ImageView) findViewById(R.id.payment_img1);
        payment_img2 = (ImageView) findViewById(R.id.payment_img2);
        payment_img3 = (ImageView) findViewById(R.id.payment_img3);

        payment_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                upload();
            }

        });

        payment_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                upload();
            }
        });
        payment_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 2;
                upload();
            }
        });


        GetBankAccount();

        spinner_order_payment_bank_acct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();
                //////Log.e("IF","**********"+selected);
                if (!selected.equalsIgnoreCase("")) {

                    if (selected.equalsIgnoreCase(PaymentActivity.this.getString(R.string.select_Bank_account_for_spinner))) {
                        acc_no = "";
                    } else {

                        //StringTokenizer tokens = new StringTokenizer(selected, str_static_key);
                        //StringTokenizer tokens = new StringTokenizer(selected, PaymentActivity.this.getString(R.string.Order));
                        //pass_selected_items_ids = ""+tokens.nextToken();
                        //////Log.e("pass_selected_items_ids","==>"+pass_selected_items_ids);
                        acc_no = selected;
                        //////Log.e("acc_no","----"+acc_no);

                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount_str = amount.getText().toString();

                //TODO JAY NEW FILD ACCOUNT DETAILS
                str_from_bank_account = order_name.getText().toString();

                // acc_no = bank_account.getText().toString();

                String ss = getString(R.string.select_Bank_account_for_spinner);
                if (ss.equalsIgnoreCase(acc_no)) {
                    acc_no = "";
                }
                if (!TextUtils.isEmpty(payment_type)) {
                    if (payment_type.equals("Cash")) {
                        if (TextUtils.isEmpty(amount_str)) {
                            Toast.makeText(act, "ادخل القيمة", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(str_from_bank_account)) {
                            Toast.makeText(act, getString(R.string.enter_from_bank_account), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(acc_no)) {
                            Toast.makeText(act, "يرجى ادخال رقم الحساب", Toast.LENGTH_SHORT).show();
                        } else {
                            //  if (payment_type.equals("Cash")) {
                            loadData();

                        }
                    } else if (payment_type.equals("Card")) {
                        if (TextUtils.isEmpty(card_no.getText().toString())) {
                            Toast.makeText(act, "يرجى ادخال رقم البطاقة", Toast.LENGTH_SHORT).show();
                        } else if (txt_month.getText().toString().equals("00/") || txt_year.getText().toString().equals("0000")) {
                            Toast.makeText(act, "يرجى ادخال تاريخ الإنتهاء", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(cvv.getText().toString())) {
                            Toast.makeText(act, "يرجى ادخال رقم الـCVV", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(holder_name.getText().toString())) {
                            Toast.makeText(act, "اسم صاحب البطاقة", Toast.LENGTH_SHORT).show();
                        } else {
                            if (providerBinder != null && checkFields()) {
                                requestCheckoutId(getString(R.string.custom_ui_callback_scheme), total_fee, "SAR");
                            }
                        }
                    } else if (payment_type.equals("Save")) {
                        if (TextUtils.isEmpty(card_no.getText().toString())) {
                            Toast.makeText(act, "يرجى ادخال رقم البطاقة", Toast.LENGTH_SHORT).show();
                        } else if (txt_month.getText().toString().equals("00/") || txt_year.getText().toString().equals("0000")) {
                            Toast.makeText(act, "يرجى ادخال تاريخ الإنتهاء", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(cvv.getText().toString())) {
                            Toast.makeText(act, "يرجى ادخال رقم الـCVV", Toast.LENGTH_SHORT).show();
                        } else if (check == false) {
                            Toast.makeText(act, "رقم الـCVV خاطئ", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(holder_name.getText().toString())) {
                            Toast.makeText(act, "يرجى ادخال الأسم الأول والأخير", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(PaymentActivity.this, Paypal_WebView.class);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("provider_id", provider_id);
                            intent.putExtra("user_id", ssp.getUserId(act));
                            intent.putExtra("card_id", card_id);
                            intent.putExtra("payment_amount", total_fee);
                            intent.putExtra("from", from);
                            intent.putExtra("save_card", saved_card);
                            //////Log.e("Card_id_Payment", "====" + card_id);

                            startActivity(intent);

                        }
                    }
                } else {
                    Toast.makeText(act, "اختر البطاقة", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void upload() {
        final Dialog dialog1 = new Dialog(PaymentActivity.this, R.style.DialogSlideAnim);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.frnd_custom);

        dialog1.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();

        RelativeLayout takepicture = (RelativeLayout) dialog1.findViewById(R.id.gallery_rl);
        RelativeLayout uploadfile = (RelativeLayout) dialog1.findViewById(R.id.camera_rl);
        RelativeLayout cancel_rl = (RelativeLayout) dialog1.findViewById(R.id.cancel_rl);
        RelativeLayout remove_rl = (RelativeLayout) dialog1.findViewById(R.id.remove_rl);
        remove_rl.setVisibility(View.VISIBLE);
        cancel_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }


        });
        if (flag == 0) {
            if (image1 != null) {
                remove_rl.setVisibility(View.VISIBLE);
            } else {
                remove_rl.setVisibility(View.GONE);
            }

        } else if (flag == 1) {
            if (image2 != null) {
                remove_rl.setVisibility(View.VISIBLE);
            } else {
                remove_rl.setVisibility(View.GONE);
            }

        } else {
            if (image3 != null) {
                remove_rl.setVisibility(View.VISIBLE);
            } else {
                remove_rl.setVisibility(View.GONE);
            }
            payment_img3.setImageResource(R.mipmap.original);
        }
        remove_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(PaymentActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(PaymentActivity.this);
                }

                alert.setMessage("هل ترغب بإزالة هذه الصورة");


                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (flag == 0) {
                            payment_img1.setImageResource(R.mipmap.original);
                            byte_list.remove(image1);
                        } else if (flag == 1) {
                            payment_img2.setImageResource(R.mipmap.original);
                            byte_list.remove(image2);
                        } else {
                            payment_img3.setImageResource(R.mipmap.original);
                            byte_list.remove(image3);
                        }


                        dialog1.dismiss();
                    }
                });

                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

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


    private int PERMISSIONS_MULTIPLE_REQUEST = 5;
    int ACCESS_REQUST;

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    private void galleryIntent() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 0);
    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = null;
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
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

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(PaymentActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(PaymentActivity.this, android.Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(PaymentActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(PaymentActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(PaymentActivity.this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(PaymentActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

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


    public void printLog(String key, String value) {
        //////Log.e(key, "=======" + value);
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
        }
    }


    class AsyncReceiverTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= 11) {
                dlg = new ProgressDialog(PaymentActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                dlg = new ProgressDialog(PaymentActivity.this);
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
                    ////Log.e("Camera", "Image Picker PATH::::" + path_img);
                    try {
                        if (fs != null) {
                            myBitma = BitmapFactory.decodeFile(file.getAbsolutePath(), bfOptions);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            if (myBitma != null) {
                                myBitma.compress(Bitmap.CompressFormat.JPEG, 80, bao);
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

                    //////Log.e("Bitmap", "Actual bitmap item::Width:" + myBitmap.getWidth()
                    //    + "   Height:" + myBitmap.getHeight());


                    //////Log.e("setting", "img on bitmap from camera");
                    rotatedBitmap = gettingRotatedBitmap(myBitmap, path_img);

                    //////Log.e("Bitmap", "Rotaated item::Width:" + rotatedBitmap.getWidth()
                    //   + "   Height:" + rotatedBitmap.getHeight());

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                    //////Log.e("inside", "camera");

                    //////Log.e("11111111111111111", "1111111111111");
//                imgresume = path_img;
                    byte_arr = bao.toByteArray();
                    if (flag == 0) {
                        image1 = byte_arr;
                        byte_list.add(image1);
                    } else if (flag == 1) {
                        image2 = byte_arr;
                        byte_list.add(image2);
                    } else {
                        image3 = byte_arr;
                        byte_list.add(image3);
                    }
                    // image_list.add(byte_arr);


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

                //////Log.e("load", "photo upload:" + rotatedBitmap.getHeight() + "   width:" + rotatedBitmap.getWidth());

                if (flag == 0) {
                    payment_img1.setImageBitmap(rotatedBitmap);
                } else if (flag == 1) {
                    payment_img2.setImageBitmap(rotatedBitmap);
                } else {
                    payment_img3.setImageBitmap(rotatedBitmap);
                }

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


                ////Log.e("bitmap", "11 width:"
                // + width + "   height:" + height + "   filepath:" + path);

                //////Log.e("ROTATION", "ORITATION::::" + rotation
                // + "  rotation 90:" + ExifInterface.ORIENTATION_ROTATE_90
                // + "  rotation 180:" + ExifInterface.ORIENTATION_ROTATE_180 +
                // "   rotation 270:" + ExifInterface.ORIENTATION_ROTATE_270);


                ////Log.e("bitmap", "22 width:" + width + "   height:" + height);
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
                        ////Log.e("bitmap", "SWITCH MATRIX:");
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
                ////Log.e("bitmap", "33111 width:" + width + "   height:" + height);

                rotatedBitmap = Bitmap.createBitmap(bitmap_leftn, 0, 0, 400, 400, matrix, true);

                ////Log.e("bitmap", "33222 width:" + rotatedBitmap.getWidth() + "   height:" + rotatedBitmap.getHeight());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return rotatedBitmap;
        }


        private void setcircularimage(Bitmap myBitmap) {
            Bitmap newBItmap = getResizedBitmap(myBitmap, 450, 450);
            Bitmap circleBitmap = Bitmap.createBitmap(newBItmap.getWidth(), newBItmap.getHeight(), Bitmap.Config.ARGB_8888);

            BitmapShader shader = new BitmapShader(newBItmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(newBItmap.getWidth() / 2, newBItmap.getHeight() / 2, newBItmap.getWidth() / 2, paint);

            //////Log.e("11111111111111111", "1111111111111");

        }

        public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
            //////Log.e("SellItemsNew", "GET_RESIZED_BITMAP:");
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


        card_list.clear();
        card_view.removeAllViews();
        card_view.removeAllViewsInLayout();

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
                                //////Log.e("Json Data", response.toString().substring(start1, end));
                            }

                            JSONArray array = obj.getJSONArray("msg");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                Saved_Card_Entity entity = new Saved_Card_Entity();
                                entity.setCard_id(object.getString("card_id"));
                                entity.setUser_id(object.getString("user_id"));
                                entity.setPaypal_card_id(object.getString("paypal_card_id"));
                                entity.setCard_number(object.getString("card_number"));
                                entity.setLast_four_digit(object.getString("last_four_digit"));

                                entity.setName_on_card(object.getString("name_on_card"));
                                entity.setExpiry_date(object.getString("expiry_date"));

                                entity.setCard_pic("http://seemcodersapps.com/free_work_zone/assets/images/creditcard.png");
                                card_list.add(entity);

                            }

                            adapter = new Card_Pic_Adapter(act, card_list);
                            card_view.setAdapter(adapter);


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

                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Showsavedcards");
                params.put("user_id", ssp.getUserId(act));
                //  params.put("user_id", "13");
                params.put("order_id", order_id);
                params.put("provider_id", provider_id);


                //////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);
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

        @Override
        protected Void doInBackground(String... params) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = getResources().getString(R.string.loading);
            LoadApi api = new LoadApi();
            JSONObject json = new JSONObject();

            //TODO ADD NEW PARAM THIS API.
            if (payment_type.equals("Card")) {
                json = api.Action_CashPaymentImage("Makepayment", ssp.getUserId(act), byte_list, provider_id, order_id, payment_type, acc_no, amount_str, str_from_bank_account, resourcePath);

            } else {
                json = api.Action_CashPaymentImage("Makepayment", ssp.getUserId(act), byte_list, provider_id, order_id, payment_type, acc_no, amount_str, str_from_bank_account, "");
            }
            ////Log.e("JSON_OBJECT", "====" + json);


            JSONObject object = api.getResult1();

            //////Log.e("JSON_OBJECT1", "====" + object);
            try {
                msg = object.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

            try {
                if (msg.equals("Your transaction details has been submitted successfully")) {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(act);
                    }

//                    if (fromDirectOrder)
//                        alert.setMessage("c");
//                    else
                    alert.setMessage("تم الدفع بنجاح");


                    alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }

                            helper.service_list.clear();
                            helper.category_list.clear();
                            helper.category_idlist.clear();
                            helper.service_idlist.clear();

                            Intent intent = new Intent(PaymentActivity.this, Order_detail.class);
                            helper.pblc = 0;
                            intent.putExtra("order_id", order_id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

//                                Intent intent = new Intent(PaymentActivity.this, Customer_Payment.class);
//                                helper.pblc = 0;
//                                intent.putExtra("order_id", order_id);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
                            finish();

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

                    alert.setMessage(getResources().getString(R.string.msg_something_went_wrong));
                    alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

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


    class Card_Pic_Adapter extends RecyclerView.Adapter<Card_Pic_Adapter.MyViewHolder> {

        Context context;
        ArrayList<Saved_Card_Entity> cash_detail;

        public Card_Pic_Adapter(Context context, ArrayList<Saved_Card_Entity> cash_detail) {
            this.context = context;
            this.cash_detail = cash_detail;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_item, parent, false);
            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            Picasso.with(context).load(cash_detail.get(position).getCard_pic()).placeholder(R.mipmap.credit_card).error(R.mipmap.credit_card).into(holder.card_pic);

            holder.card_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog1 = new Dialog(context, R.style.DialogSlideAnim);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.frnd_custom);


                    dialog1.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog1.show();

                    RelativeLayout takepicture = (RelativeLayout) dialog1.findViewById(R.id.gallery_rl);
                    RelativeLayout uploadfile = (RelativeLayout) dialog1.findViewById(R.id.camera_rl);
                    RelativeLayout cancel_rl = (RelativeLayout) dialog1.findViewById(R.id.cancel_rl);
                    RelativeLayout remove_rl = (RelativeLayout) dialog1.findViewById(R.id.remove_rl);
                    TextView pay_txt = (TextView) dialog1.findViewById(R.id.gallery_txt);
                    TextView delete_txt = (TextView) dialog1.findViewById(R.id.camera_txt);
                    TextView remove_txt = (TextView) dialog1.findViewById(R.id.remove_txt);
                    pay_txt.setText("ادفع ببطاقة جديدة(" + cash_detail.get(position).getCard_number() + ")");
                    delete_txt.setText("حذف البطاقة");

                    remove_txt.setText("ادفع ببطاقة جديدة");

                    remove_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payment_type = "Card";
                            check_image.setVisibility(View.GONE);
                            txt.setVisibility(View.GONE);
                            ImgRadio_Card.setImageResource(R.mipmap.radio_checked);
                            ImgRadio_cash.setImageResource(R.mipmap.radio_w2x);

                            amount.setEnabled(false);
                            amount.setBackgroundResource(R.color.grey);


                            spinner_order_payment_bank_acct.setEnabled(false);

                            txt_month.setText("00/");
                            txt_year.setText("0000");
                            card_no.setEnabled(true);
                            from = "إضافة بطاقة";
                            saved_card = "نعم";
                            check_image.setImageResource(R.drawable.ic_checktick);
                            bool = false;

                            card_no.setBackgroundResource(R.color.white);


                            cvv.setEnabled(true);

                            cvv.setBackgroundResource(R.color.white);
                            holder_name.setEnabled(true);

                            holder_name.setBackgroundResource(R.color.white);

                            txt.setVisibility(View.GONE);
                            check_image.setVisibility(View.GONE);
                            dialog1.dismiss();

                        }
                    });
                    cancel_rl.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();

                        }


                    });


                    takepicture.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            payment_type = "Save";
                            txt.setVisibility(View.GONE);
                            check_image.setVisibility(View.GONE);
                            holder_name.setText(cash_detail.get(position).getName_on_card());
                            ImgRadio_Card.setImageResource(R.mipmap.radio_w2x);
                            ImgRadio_cash.setImageResource(R.mipmap.radio_w2x);

                            amount.setEnabled(false);
                            amount.setBackgroundResource(R.color.grey);

                            spinner_order_payment_bank_acct.setEnabled(false);

                            card_no.setText(cash_detail.get(position).getCard_number());

                            card_no.setEnabled(false);
                            card_no.setBackgroundResource(R.color.white);
                            String[] token = cash_detail.get(position).getExpiry_date().split("/");
                            String str = token[0];
                            String str2 = token[1];
                            //////Log.e("STRING!", "=====" + str);
                            //////Log.e("STRING2", "=====" + str2);

                            txt_year.setText(str2);
                            txt_month.setText(str + "/");

                            cvv.setEnabled(true);
                            cvv.setBackgroundResource(R.color.white);
                            holder_name.setEnabled(false);
                            holder_name.setText(cash_detail.get(position).getName_on_card());
                            holder_name.setBackgroundResource(R.color.white);
                            expire = cash_detail.get(position).getExpiry_date();
                            card_num = cash_detail.get(position).getCard_number();
                            card_name = cash_detail.get(position).getName_on_card();
                            card_id = cash_detail.get(position).getPaypal_card_id();
                            //////Log.e("Card_id_ave", "====" + card_id);

                            dialog1.dismiss();

                        }
                    });

                    uploadfile.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            card_id = cash_detail.get(position).getPaypal_card_id();
                            //////Log.e("Card_id_Delete", "====" + card_id);
                            Delete();
                            cash_detail.remove(cash_detail.get(position));
                            notifyDataSetChanged();
                            dialog1.dismiss();

                        }
                    });


                }


            });


        }

        @Override
        public int getItemCount() {
            return cash_detail.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView card_pic;
            TextView paid_unpaid, payment_type_date, dollor_price;

            public MyViewHolder(View itemView) {
                super(itemView);
                card_pic = (ImageView) itemView.findViewById(R.id.card_pic);

            }
        }
    }


    private void utiltyRequest2() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "يرجى الإنتظار....";


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
                            if (msg.equals("Failed")) {
                                check = false;
                                cvv.setError("رقم الـCVV خاطئ");
                            } else if (msg.equals("Success")) {
                                check = true;
                                cvv.setError(null);
                                Toast.makeText(act, "", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(act, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(act, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act, "Network Error", Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Checkforcvv");
                params.put("user_id", ssp.getUserId(act));
                params.put("order_id", order_id);
                params.put("provider_id", provider_id);


                params.put("cardId", card_id);

                params.put("cvv", cvv.getText().toString().trim());


                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    private void utiltyRequest3() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "يرجى الإنتظار....";

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
                                //////Log.e("Json Data", response.toString().substring(start1, end));
                            }


                            result = obj.getString("result");

                            ////Log.e("result***************>", result.toString());

                            if (result.equals("Failed")) {
                                message = obj.getString("message");
                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(act);
                                }
                                // alert.setTitle("Successful");
                                alert.setMessage(message);


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
                            if (obj.has("paypal_card_id")) {
                                ////Log.e(">>>>>>>", "Match paypal_card_id*******Paypal_WebView********>");
                                card_id = obj.getString("paypal_card_id");
                                //////Log.e("Card_id", "====" + card_id);
                                Intent intent = new Intent(PaymentActivity.this, Paypal_WebView.class);
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("provider_id", provider_id);
                                intent.putExtra("user_id", ssp.getUserId(act));
                                intent.putExtra("card_id", card_id);
                                intent.putExtra("payment_amount", total_fee);
                                intent.putExtra("from", from);
                                intent.putExtra("save_card", saved_card);
                                startActivity(intent);
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

                            Toast.makeText(act, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(act, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act, "Network Error", Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Makepayment");
                params.put("user_id", ssp.getUserId(act));
                params.put("payment_amount", total_fee);
                params.put("payment_type", "Card");
                params.put("order_id", order_id);
                params.put("provider_id", provider_id);
                params.put("card_number", card_no.getText().toString().trim());
                params.put("cvv", cvv.getText().toString().trim());
                params.put("name_on_card", holder_name.getText().toString());
                params.put("expiry_date", txt_month.getText().toString() + txt_year.getText().toString());

                ////Log.e("params***************>", params.toString());
                ////Log.e("payment_type*********>", "Card");
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void Delete() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "يرجى الإنتظار....";
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
                            ////Log.e("MSG", "=====" + msg);
                            if (msg.equals("Your card has been deleted successfully")) {
                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(act);
                                }
                                alert.setTitle("تم حذف البطاقة بنجاح");
                                alert.setMessage(getResources().getString(R.string.msg_card_has_been_deleted_successfully));

                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        utiltyRequest();
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
                                // alert.setTitle("Successful");
                                alert.setMessage(getResources().getString(R.string.msg_something_went_wrong));

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
                            Toast.makeText(act, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(act, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act, "Network Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Deletesavedcard");
                params.put("user_id", ssp.getUserId(act));
                params.put("paypal_card_id", card_id);

                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();

        utiltyRequest();
    }


    //ADD JIG's
    private void GetBankAccount() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                //////Log.e("Json Data", response.toString().substring(start1, end));
                                //Log.e(TAG, "****************Json Data*****" + response.toString().substring(start1, end));
                            }
                            if (flagValue == 0) {

                                ArrayList<Banklist_Vo> banklist_voArrayList = new ArrayList<>();
                                JSONArray jsonArray = obj.getJSONArray("result");

                                ArrayListbank_account.add(getString(R.string.select_Bank_account_for_spinner));

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    ////Log.e(TAG,"****************object*****"+object.toString());
                                    String bank_name = object.getString("bank_name");
                                    String bank_account_iban = object.getString("bank_account_iban");

                                    //ArrayListbank_account.add(bank_account_iban);
                                    ArrayListbank_account.add(bank_name + " " + bank_account_iban);

                                    if (ArrayListbank_account.size() > 0 && ArrayListbank_account != null) {
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PaymentActivity.this, R.layout.spinner_item_bank, ArrayListbank_account);
                                        dataAdapter.setDropDownViewResource(R.layout.custom_dropdown_view);
                                        spinner_order_payment_bank_acct.setAdapter(dataAdapter);
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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ParseError) {

                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (flagValue == 0) {
                    params.put("action", "Banklist");
                    //Log.e(TAG, "****************params*****" + params.toString());
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    /**************************************************************************/

    private boolean checkFields() {
        if (holder_name.getText().length() == 0 ||
                card_no.getText().length() == 0 ||
                txt_month.getText().length() == 0 ||
                txt_year.getText().length() == 0 ||
                cvv.getText().length() == 0) {
            showAlertDialog(R.string.error_empty_fields);

            return false;
        }

        return true;
    }


    private IProviderBinder providerBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /* we have a connection to the service */
            providerBinder = (IProviderBinder) service;
            providerBinder.addTransactionListener(PaymentActivity.this);

            try {
                providerBinder.initializeProvider(Connect.ProviderMode.TEST);
            } catch (PaymentException ee) {
                showErrorDialog(ee.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            providerBinder = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, ConnectService.class);

        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unbindService(serviceConnection);
        stopService(new Intent(this, ConnectService.class));
    }


    private void showErrorDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertDialog(message);
            }
        });
    }

    private void showErrorDialog(PaymentError paymentError) {
        showErrorDialog(paymentError.getErrorMessage());
    }

    protected void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("نعم", null)
                .setCancelable(false)
                .show();
    }


    String idPayment = "";

    @Override
    public void onCheckoutIdReceived(String checkoutId) {
        super.onCheckoutIdReceived(checkoutId);

        try {
            JSONObject temp = new JSONObject(checkoutId);
            // JSONObject redirect = new JSONObject(temp.getString("redirect"));
            ////Log.e("Here.......", "redirect..." + redirect);
            idPayment = temp.getString("id");

            if (idPayment != null) {
                this.checkoutId = idPayment;

                requestCheckoutInfo(this.checkoutId);
            }
        } catch (JSONException e) {
            e.getMessage();
        }


    }

    private void requestCheckoutInfo(String checkoutId) {
        if (providerBinder != null) {
            try {
                providerBinder.requestCheckoutInfo(checkoutId);
                showProgressDialog(R.string.progress_message_checkout_info);
            } catch (PaymentException e) {
                showAlertDialog(e.getMessage());
            }
        }
    }

    private void pay(String checkoutId) {
        try {
            PaymentParams paymentParams = createPaymentParams(checkoutId);
            paymentParams.setShopperResultUrl(getString(R.string.custom_ui_callback_scheme) + "://test");
            Transaction transaction = new Transaction(paymentParams);

            providerBinder.submitTransaction(transaction);
            showProgressDialog(R.string.progress_message_processing_payment);
        } catch (PaymentException e) {
            showErrorDialog(e.getError());
        }
    }

    private PaymentParams createPaymentParams(String checkoutId) throws PaymentException {
        String cardHolder = holder_name.getText().toString();
        String cardNumber = card_no.getText().toString();
        String cardExpiryMonth = txt_month.getText().toString().substring(0, 2);
        String cardExpiryYear = txt_year.getText().toString().substring(2, txt_year.getText().toString().length());
        String cardCVV = cvv.getText().toString();
        return new CardPaymentParams(
                checkoutId,
                Constants.Config.CARD_BRAND,
                cardNumber,
                cardHolder,
                cardExpiryMonth,
                "20" + cardExpiryYear,
                cardCVV
        );
    }

    @Override
    public void brandsValidationRequestSucceeded(BrandsValidation brandsValidation) {

    }

    @Override
    public void brandsValidationRequestFailed(PaymentError paymentError) {

    }

    @Override
    public void imagesRequestSucceeded(ImagesRequest imagesRequest) {

    }

    @Override
    public void imagesRequestFailed() {

    }

    @Override
    public void paymentConfigRequestSucceeded(final CheckoutInfo checkoutInfo) {
        hideProgressDialog();

        if (checkoutInfo == null) {
            showErrorDialog(getString(R.string.error_message));

            return;
        }

        /* Get the resource path from checkout info to request the payment status later. */
        resourcePath = checkoutInfo.getResourcePath();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showConfirmationDialog(
                        String.valueOf(checkoutInfo.getAmount()),
                        checkoutInfo.getCurrencyCode()
                );
            }
        });
    }

    private void showConfirmationDialog(String amount, String currency) {
        new AlertDialog.Builder(this)
                .setMessage(String.format(getString(R.string.message_payment_confirmation), amount, currency))
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pay(checkoutId);
                    }
                })
                .setNegativeButton(R.string.button_cancel, null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void paymentConfigRequestFailed(PaymentError paymentError) {
        hideProgressDialog();
        showErrorDialog(paymentError);
    }

    @Override
    public void transactionCompleted(Transaction transaction) {
        hideProgressDialog();

        if (transaction == null) {
            showErrorDialog(getString(R.string.error_message));

            return;
        }


        if (transaction.getTransactionType() == TransactionType.SYNC) {
            /* check the status of synchronous transaction */
            requestPaymentStatus(resourcePath);
            // requestPaymentStatusManual(this.checkoutId);
        } else {
            /* wait for the callback in the onNewIntent() */
            showProgressDialog(R.string.progress_message_please_wait);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(transaction.getRedirectUrl())));
        }
    }

    @Override
    public void transactionFailed(Transaction transaction, PaymentError paymentError) {
        hideProgressDialog();
        showErrorDialog(paymentError);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);


        /* Check if the intent contains the callback scheme. */
        if (resourcePath != null && hasCallbackScheme(intent)) {
            requestPaymentStatus(resourcePath);
        }
    }

    @Override
    public void onPaymentStatusReceived(String paymentStatus) {
        hideProgressDialog();

        if (paymentStatus != null && paymentStatus.length() != 0) {

            try {
                JSONObject temp = new JSONObject(paymentStatus);
                // JSONObject redirect = new JSONObject(temp.getString("redirect"));

                String paymentResult = temp.getString("paymentResult");

                if (paymentResult != null) {

                    if ("OK".equalsIgnoreCase(paymentResult)) {


                        if (fromDirectOrder) {


                            Intent goBackToDirectOrder = new Intent();
                            goBackToDirectOrder.putExtra("paymentId", resourcePath);
                            setResult(RESULT_OK, goBackToDirectOrder);
                            finish();
                        } else {
                            loadData();
                        }
                    } else showAlertDialog(R.string.message_unsuccessful_payment);
                }
            } catch (JSONException e) {
                e.getMessage();
            }

        } else {

            showAlertDialog(R.string.message_unsuccessful_payment);
        }

    }
}







