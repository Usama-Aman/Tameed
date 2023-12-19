package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

public class Contact_us extends AppCompatActivity {
    TextView header_txt;
    ImageView header_back;
    ConstraintLayout contact_cons;
    private Button call, submit;
    private String progress_dialog_msg = "", tag_string_req = "string_req", message;
    String calling_num;
    EditText cont_name, cont_number, cont_email, cont_message;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    SaveSharedPrefernces ssp;

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
        setContentView(R.layout.contact_us);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("راسلنا");

        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cont_name = (EditText) findViewById(R.id.contact_name);
        cont_number = (EditText) findViewById(R.id.contact_number);
        cont_email = (EditText) findViewById(R.id.contact_email);
        cont_message = (EditText) findViewById(R.id.contact_message);
        contact_cons = (ConstraintLayout) findViewById(R.id.contact_cons);

        contact_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });


        submit = (Button) findViewById(R.id.contact_us_submit);

        call = (Button) findViewById(R.id.contact_bt_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(Contact_us.this, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(Contact_us.this);
                }

                alert.setMessage(getResources().getString(R.string.msg_do_you_want_to_make_this_call));


                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        admin_call();

                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + calling_num));
                        startActivity(callIntent);


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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cont_name.getText().toString().equals("")) {
                    Toast.makeText(Contact_us.this, "يرجى كتابة الإسم", Toast.LENGTH_SHORT).show();
                } else if (cont_number.getText().toString().equals("")) {
                    Toast.makeText(Contact_us.this, "يرجى كتابة رقم التواصل", Toast.LENGTH_SHORT).show();
                } else if (cont_email.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(cont_email.getText().toString()).matches()) {
                    Toast.makeText(Contact_us.this, "يرجى كتابة البريد الإلكتروني ", Toast.LENGTH_SHORT).show();
                } else if (cont_message.getText().toString().equals("")) {
                    Toast.makeText(Contact_us.this, "اكتب رسالتك", Toast.LENGTH_SHORT).show();
                } else {


                    detail_submit();
                }
            }
        });

        ssp = new SaveSharedPrefernces();


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


    public void detail_submit() {

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
                            ////////Log.e("response", response.toString());

                            if (message.equals("your query has been submitted successfully")) {

                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(Contact_us.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(Contact_us.this);
                                }

                                alert.setMessage("تم إرسال رسالتك بنجاح");


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        helper.setting = 1;
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Contact_us.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Contact_us.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Contact_us.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Contact_us.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Contactus");
                params.put("name", cont_name.getText().toString());
                params.put("email", cont_email.getText().toString());
                params.put("mobile_number", cont_number.getText().toString());
                params.put("message", cont_message.getText().toString());
                params.put("user_id", ssp.getUserId(Contact_us.this));
                ////////Log.e("Contact_us", params.toString());

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    public void admin_call() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "Please wait ...";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
//                            message = obj.getString("msg");
                            ////////Log.e("response", "" + obj);

                            if (obj.has("calling_number")) {
                                calling_num = obj.getString("calling_number");
//
                                ////////Log.e("calling", calling_num);

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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Contact_us.this, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Contact_us.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Contact_us.this, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Contact_us.this, "Network Error", Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Getadmincallnumber");

                ////////Log.e("admin call", params.toString());

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
                progress_dialog = new ProgressDialog(Contact_us.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Contact_us.this);
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
