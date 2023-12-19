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
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 19-01-2018.
 */

public class Complain_order extends AppCompatActivity {
    ImageView header_back;
    TextView header_txt;
    Button send_admin;
    SaveSharedPrefernces ssp;
    Activity act;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg = "", order_id, complain_description, order_reference;
    Intent intent;
    TextView order_number;
    EditText spo_des;
    private String TAG = "Complain_order";

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
        setContentView(R.layout.complain_order);
        intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        order_reference = intent.getStringExtra("order_number");
        order_number = (TextView) findViewById(R.id.order_number);
        order_number.setText("# " + order_reference);
        spo_des = (EditText) findViewById(R.id.spo_des);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("اعتراض على المعاملة");

        act = Complain_order.this;
        ssp = new SaveSharedPrefernces();
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send_admin = (Button) findViewById(R.id.compalin_send_admin);

        send_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complain_description = spo_des.getText().toString();
                if (TextUtils.isEmpty(complain_description)) {
                    Toast.makeText(act, "اكتب اعتراضك", Toast.LENGTH_SHORT).show();
                } else {
                    utiltyRequest();
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
                                alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                            } else {
                                alert = new AlertDialog.Builder(act);
                            }

                            alert.setMessage(getResources().getString(R.string.msg_complain_successfully_recorded));


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

                                    Intent intent = new Intent(Complain_order.this, Order_detail.class);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

                        //}
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Complain_order.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Complain_order.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Complain_order.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Complain_order.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Complainorder");
                params.put("user_id", ssp.getUserId(act));
                params.put("order_id", order_id);
                params.put("complain_description", complain_description);

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
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

}
