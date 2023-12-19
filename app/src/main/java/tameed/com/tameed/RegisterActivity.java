package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.Constant1;
import tameed.com.tameed.Util.GPSTracker;
import tameed.com.tameed.Util.SaveSharedPrefernces;

public class RegisterActivity extends Permission {
    private TextView tetHeader, country_code, loc;
    private ConstraintLayout linearLayout_register;
    ImageView imgBackHeader;
    ImageView img_profile;
    Bitmap bitmap;
    String location_edit;
    List<Address> address;
    private static int LOAD_IMAGE_RESULTS = 1000;
    Uri imageUri;
    GPSTracker gpsTracker;
    Geocoder geocoder;
    double latis, longis;
    // location="Market Gulshan-E-Lahore, Tariq Ismaeel Road, Lahore"
    String lat, lang, location = "", message, mob, userid, code_country;
    int gmtvalue;
    public static final int RequestPermissionCode = 1;
    EditText et_mobile;
    ConstraintLayout register_cons;
    TextView txt_terms_and_condition;
    SaveSharedPrefernces ssp;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    private String android_id;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String TAG = "RegisterActivity";


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

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);


        register_cons = (ConstraintLayout) findViewById(R.id.register_cons);
        register_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        EnableRuntimePermission();

        txt_terms_and_condition = findViewById(R.id.txt_terms_and_condition);
        txt_terms_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, Terms_Condition.class);
                startActivity(i);
            }
        });


        android_id = Settings.Secure.getString(RegisterActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        checkLocationPermission();
        img_profile = (ImageView) findViewById(R.id.img_profile);

        imgBackHeader = (ImageView) findViewById(R.id.header_back);
        imgBackHeader.setVisibility(View.GONE);
        gpsTracker = new GPSTracker(RegisterActivity.this);
        tetHeader = (TextView) findViewById(R.id.txt_header);
        tetHeader.setText("تسجيل/دخول");
        linearLayout_register = (ConstraintLayout) findViewById(R.id.btn_register);
        loc = (TextView) findViewById(R.id.edt_location);
        if (gpsTracker.canGetLocation()) {
            //////Log.e("loc", "dsjisd");
        } else {
            //////Log.e("loc", "notfound");
            gpsTracker.showSettingsAlert();
        }

        et_mobile = (EditText) findViewById(R.id.login_mobile);
        country_code = (TextView) findViewById(R.id.country_code);

        country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Country_list.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        code_country = GetCountryZipCode();
        //////Log.e("Country_Code_Local", " " + code_country);

        if (!TextUtils.isEmpty(code_country)) {
            country_code.setText(code_country);
        } else {
            country_code.setText(helper.calling_code);
        }


        linearLayout_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mob = et_mobile.getText().toString().trim();
                location_edit = loc.getText().toString();
                if (mob.equals("")) {
                    Toast.makeText(RegisterActivity.this, "يرجى إدخال رقم الجوال", Toast.LENGTH_SHORT).show();
                } else if (location_edit.equals("")) {
                    Toast.makeText(RegisterActivity.this, "يرجى إدخال موقعك", Toast.LENGTH_SHORT).show();
                } else {


                    helper.login = true;
                    submit();

                }

            }
        });


        ssp = new SaveSharedPrefernces();
        TimeZone tz = TimeZone.getDefault();
        //////Log.e("shahhs", "" + tz.getDisplayName(false, TimeZone.SHORT));

        TimeZone tzzz = TimeZone.getDefault();
        Date now = new Date();
        gmtvalue = tzzz.getOffset(now.getTime()) / 1000;
        //////Log.e("111111", "" + gmtvalue);
        ////Log.e("gmt", String.valueOf(gmtvalue));

        gpsTracker = new GPSTracker(RegisterActivity.this);
        lat = String.valueOf(gpsTracker.getLatitude());

        ////Log.e("latitude", "" + lat);
        lang = String.valueOf(gpsTracker.getLongitude());
        ////Log.e("longitude", "" + lang);
        latis = Double.parseDouble(lat);
        longis = Double.parseDouble(lang);

        helper.latitude = lat;
        helper.longitude = lang;

        geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());

        try {
            address = geocoder.getFromLocation(latis, longis, 1);

            if (address.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "يرجى الإنتظار..", Toast.LENGTH_SHORT).show();

            } else {
                if (address.size() > 0) {
                    location = address.get(0).getAddressLine(0);
                    ////Log.e("features", location);


                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Intent i = getIntent();
        String map_loc = i.getStringExtra("location");

        if (map_loc != null) {
            loc.setText(map_loc);
        } else {
            loc.setText(location);
        }

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapView_Activity.class));
            }
        });

    }

    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }

        return CountryZipCode;
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


    public void submit() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OTP Response", ": " + response);
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }


                            if (message.equals("An otp has been sent to your mobile number, Please enter it to verify")) {
                                userid = obj.getString("user_id");
                                //ssp.setUserId(RegisterActivity.this, userid); //REMOVE JAY
                                //Log.e(TAG, "userid----->" + userid);
                                Constant1.temp_user_id = userid;


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(RegisterActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(RegisterActivity.this);
                                }
                                alert.setMessage("تم إرسال رمز التفعيل لرقمك ");
                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(RegisterActivity.this, OTP_Activity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            }


//
                        } catch (JSONException e) {
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
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                            //TODO

                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                //Log.e("Registration id:", "......" + ssp.getRegId(RegisterActivity.this));
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Login");
                params.put("calling_code", country_code.getText().toString());
                params.put("mobile_number", mob);
                params.put("device_type", "android");
                params.put("latitude", lat);
                params.put("longitude", lang);
                params.put("location", loc.getText().toString());
                params.put("gmt_value", String.valueOf(gmtvalue));
                params.put("device_id", ssp.getRegId(RegisterActivity.this));
                ////Log.e("params LOGIN", params.toString());
                return params;
            }
//            calling_code , mobile_number , location , latitude , longitude (These all required) , gmt_value , device_id ,

        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }


    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(RegisterActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(RegisterActivity.this);
            }
            progress_dialog.setMessage("يرجى الإنتظار..");
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(helper.calling_code)) {
            country_code.setText("+" + helper.calling_code);
        } else {
            country_code.setText("966+");
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.allow_access), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionCode);
        }
    }

}