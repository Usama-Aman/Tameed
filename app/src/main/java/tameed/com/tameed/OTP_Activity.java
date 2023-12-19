package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.Constant1;
import tameed.com.tameed.Util.SaveSharedPrefernces;

public class OTP_Activity extends AppCompatActivity {

    private String TAG = "OTP_Activity";
    private ImageView imgBackHeader;
    TextView resend_now_txt, counter_time_txt, header_txt;
    String otp1, otp2, otp3, otp4, otp;
    ConstraintLayout counter_relative;
    EditText verify_code1, verify_code2, verify_code3, verify_code4;
    private String progress_dialog_msg = "", tag_string_req = "string_req", message;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    SaveSharedPrefernces ssp;
    String name, email, mobile_number, combine_mobile, latitude, longitude, location, login_status, payment_preference, active_status, user_type, profile_pic_thumb_url, added_date, push_notification, online_ofiline_status, profile_pic_url, description, review_count, review_rating, order_count, country, mobile_visible, city_to_cover, distance, user_bank_name, user_bank_acct_num, user_iban_num;

    String u_id;

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
        setContentView(R.layout.activity_otp);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("رمز التفعيل");

        ssp = new SaveSharedPrefernces();
        //Log.e(TAG, "****************************");
        verify_code1 = (EditText) findViewById(R.id.verify_code1);
        verify_code2 = (EditText) findViewById(R.id.verify_code2);
        verify_code3 = (EditText) findViewById(R.id.verify_code3);
        verify_code4 = (EditText) findViewById(R.id.verify_code4);
        resend_now_txt = (TextView) findViewById(R.id.text_resend);
        counter_relative = (ConstraintLayout) findViewById(R.id.counter_layout);
        counter_time_txt = (TextView) findViewById(R.id.counter_time_txt);

        imgBackHeader = (ImageView) findViewById(R.id.header_back);
        imgBackHeader.setVisibility(View.GONE);

        resend_now_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter_relative.setVisibility(View.VISIBLE);
                resend_now_txt.setVisibility(View.VISIBLE);
                mCountDownTimer.start();

                Resend_otp();
            }
        });


        mCountDownTimer.start();

        verify_code1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code1.getText().toString().length() == 1)     //size as per your requirement
                {
                    verify_code2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                otp1 = verify_code1.getText().toString();
                //////Log.e("otp1", "" + otp1);
                // TODO Auto-generated method stub
            }


        });

        verify_code2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code2.getText().toString().length() == 1)     //size as per your requirement
                {
                    verify_code3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {

                otp2 = verify_code2.getText().toString();
                //////Log.e("otp2", "" + otp2);


                // TODO Auto-generated method stub
            }


        });

        verify_code3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code3.getText().toString().length() == 1)     //size as per your requirement
                {
                    verify_code4.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                otp3 = verify_code3.getText().toString();
                //////Log.e("otp3", "" + otp3);
                // TODO Auto-generated method stub


            }

        });
        //////Log.e("otp1", "" + otp1);
        //////Log.e("otp4", "" + otp4);


        verify_code4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code4.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp4 = verify_code4.getText().toString();
                    otp1 = verify_code1.getText().toString();
                    otp2 = verify_code2.getText().toString();
                    otp3 = verify_code3.getText().toString();
                    otp = otp1 + otp2 + otp3 + otp4;
                    Otp_verify();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                otp4 = verify_code4.getText().toString();
                //////Log.e("otp3", "" + otp4);
                // TODO Auto-generated method stub
            }

        });
        //////Log.e("otp1", "" + otp1);
        //////Log.e("otp4", "" + otp4);

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


    public void Otp_verify() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "يرجى الإنتظار..";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);


                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");


                            if (obj.has("user_details")) {

                                JSONObject jarray = obj.getJSONObject("user_details");
                                //Log.e(TAG, "************RESPONCE ****************" + jarray);

                                u_id = jarray.getString("user_id"); //ADD JAY

                                name = jarray.getString("name");
                                email = jarray.getString("email_address");
                                mobile_number = jarray.getString("mobile_number");
                                combine_mobile = jarray.getString("combine_mobile");
                                latitude = jarray.getString("latitude");
                                longitude = jarray.getString("longitude");
                                location = jarray.getString("location");
                                login_status = jarray.getString("login_status");
                                payment_preference = jarray.getString("payment_preference");
                                active_status = jarray.getString("active_status");
                                user_type = jarray.getString("user_type");
                                profile_pic_thumb_url = jarray.getString("profile_pic_url");
                                added_date = jarray.getString("added_date");
                                push_notification = jarray.getString("push_notification");
                                online_ofiline_status = jarray.getString("online_offline_status");
                                profile_pic_url = jarray.getString("profile_pic_url");
                                description = jarray.getString("description");
                                review_count = jarray.getString("review_count");
                                review_rating = jarray.getString("review_rating");
                                order_count = jarray.getString("order_count");
                                country = jarray.getString("country");
                                mobile_visible = jarray.getString("mobile_visible");
                                city_to_cover = jarray.getString("city_to_cover");
                                distance = jarray.getString("distance");
                                user_bank_name = jarray.getString("user_bank_name");
                                user_bank_acct_num = jarray.getString("user_bank_account_number");
                                user_iban_num = jarray.getString("user_bank_iban_number");
                            }

                            //Log.e("responseeeeeeeeeev TOP", message);
                            if (message.equals("OTP verified successfully")) {


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(OTP_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(OTP_Activity.this);
                                }

                                alert.setMessage("تم التحقق من رقمك بنجاح");
                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //ADD JAY
                                        ssp.setUserId(OTP_Activity.this, u_id);

                                        ssp.setName(OTP_Activity.this, name);
                                        ssp.setEmail(OTP_Activity.this, email);
                                        ssp.setMobile_number(OTP_Activity.this, mobile_number);
                                        ssp.setCombine_Mobile_number(OTP_Activity.this, combine_mobile);
                                        ssp.setLatitude(OTP_Activity.this, latitude);
                                        ssp.setLongitude(OTP_Activity.this, longitude);
                                        ssp.setLocation(OTP_Activity.this, location);
                                        ssp.setLogin_status(OTP_Activity.this, login_status);
                                        ssp.setPayment_preference(OTP_Activity.this, payment_preference);
                                        ssp.setActive_status(OTP_Activity.this, active_status);
                                        ssp.setUser_type(OTP_Activity.this, user_type);
                                        ssp.setKEY_profile_pic_thumb_url(OTP_Activity.this, profile_pic_thumb_url);
                                        ssp.setPush_notification(OTP_Activity.this, push_notification);
                                        ssp.setOnline_offline_status(OTP_Activity.this, online_ofiline_status);
                                        ssp.setDescription(OTP_Activity.this, description);
                                        ssp.setAdded_date(OTP_Activity.this, added_date);
                                        ssp.setReview_count(OTP_Activity.this, review_count);
                                        ssp.setReview_rating(OTP_Activity.this, review_rating);
                                        ssp.setOrder_count(OTP_Activity.this, order_count);
                                        ssp.setCountry(OTP_Activity.this, country);
                                        ssp.setMobile_visible(OTP_Activity.this, mobile_visible);
                                        ssp.setCity_to_cover(OTP_Activity.this, city_to_cover);
                                        ssp.setDistance(OTP_Activity.this, distance);
                                        ssp.setUser_bank_name(OTP_Activity.this, user_bank_name);
                                        ssp.setUser_bank_acct_num(OTP_Activity.this, user_bank_acct_num);
                                        ssp.setUser_bank_iban_num(OTP_Activity.this, user_iban_num);
                                        ssp.setKEY_profile_pic_thumb_url(OTP_Activity.this, profile_pic_thumb_url);

                                        Intent intent = new Intent(OTP_Activity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        helper.home = 1;
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();

                            } else if (message.equals(getResources().getString(R.string.msg_match_wrong_otp))) {
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(OTP_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(OTP_Activity.this);
                                }

                                alert.setMessage(getResources().getString(R.string.msg_entered_wrong_otp));
                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        verify_code1.getText().clear();
                                        verify_code2.getText().clear();
                                        verify_code3.getText().clear();
                                        verify_code4.getText().clear();
                                        verify_code1.requestFocus();

                                        dialog.dismiss();
                                    }
                                });
                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                            //TODO

                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Otpverify");
                params.put("user_id", Constant1.temp_user_id);
                params.put("otp", otp);

                //////Log.e("otcverify", params.toString());

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    public void Resend_otp() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "يرجى الإنتظار..";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");

                            //Log.e("responseeeeeeeeeeeeeeee", response.toString());


                            if (message.equals("Resent OTP sms successfully, Please enter it to verify your account")) {


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(OTP_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(OTP_Activity.this);
                                }

                                alert.setMessage("تم إعادة إرسال رمز التفعيل ");


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(OTP_Activity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Resendotp");
                params.put("user_id", ssp.getUserId(OTP_Activity.this));
                //////Log.e("resend", params.toString());


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(OTP_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(OTP_Activity.this);
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


    CountDownTimer mCountDownTimer = new CountDownTimer(120 * 1000, 1000) {
        @Override
        public void onTick(final long millisUntilFinished) {
            //this will be called every second.

            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

            resend_now_txt.setVisibility(View.GONE);


            if (minutes != 0)
                counter_time_txt.setText(minutes + "min " + seconds + "sec");
            else
                counter_time_txt.setText(seconds + " sec");

            if (millisUntilFinished >= 120 * 1000) {
                resend_now_txt.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onFinish() {
            //you are good to go.
            //30 seconds passed.
            counter_relative.setVisibility(View.GONE);
            resend_now_txt.setVisibility(View.VISIBLE);
        }
    };

}
