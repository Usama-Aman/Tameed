package tameed.com.tameed;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

public class RatingActivity extends AppCompatActivity {

    Typeface typeface;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    TextView Write_submit_txt, project_comleted_txt;
    ImageView congrat_star1, congrat_star2, congrat_star3, congrat_star4, congrat_star5;
    Boolean bool = true;
    Boolean bool2 = true;
    Boolean bool3 = true;
    Boolean bool4 = true;
    Boolean bool5 = true;
    String msg, review_rating, review_description, count, project_title, projectId, projectStatus;
    private SaveSharedPrefernces ssp;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    Intent intent;
    TextView congratulation_txt, limit_txt;
    private String TAG = "RatingActivity";


    Boolean s = true;

    RatingBar ratingbar;
    String orderId, shopId, shopName;

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
        //Log.e(TAG,"****************************");
        setContentView(R.layout.activity_rating);

        //  ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        ssp = new SaveSharedPrefernces();
        tv1 = (TextView) findViewById(R.id.txt_tv1);
      /*  tv2 = (TextView) findViewById(R.id.txt_tv2);
        tv3 = (TextView) findViewById(R.id.txt_tv3);*/
        tv4 = (TextView) findViewById(R.id.txt_tv4);
        tv5 = (TextView) findViewById(R.id.txt_tv5);
        tv6 = (TextView) findViewById(R.id.txt_tv6);
        tv7 = (TextView) findViewById(R.id.txt_tv7);
tv7.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent =new Intent(RatingActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("flag",1);
        startActivity(intent);
    }
});
//        write_review_edit = (EditText) findViewById(R.id.write_review_edit);
        Write_submit_txt = (TextView) findViewById(R.id.Write_submit_txt);
        congrat_star1 = (ImageView) findViewById(R.id.congrat_star1);
        congrat_star2 = (ImageView) findViewById(R.id.congrat_star2);
        congrat_star3 = (ImageView) findViewById(R.id.congrat_star3);
        congrat_star4 = (ImageView) findViewById(R.id.congrat_star4);
        congrat_star5 = (ImageView) findViewById(R.id.congrat_star5);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("order_id");
        shopName = intent.getStringExtra("shopName");
        shopId = intent.getStringExtra("shop_id");

        tv6.setText(shopName);


        Write_submit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               review_description = write_review_edit.getText().toString();
//                if (TextUtils.isEmpty(review_description)) {
//                    Toast.makeText(RatingActivity.this, "Please Type Comment", Toast.LENGTH_SHORT).show();
                //  } else
                if (TextUtils.isEmpty(count)) {
                    Toast.makeText(RatingActivity.this, "Please Give Ratting", Toast.LENGTH_SHORT).show();
                } else {
                    if (AppController.isOnline(RatingActivity.this)) {

                        makeStringReq();
                    } else {
                        AppController.showAlert(RatingActivity.this,
                                getString(R.string.networkError_Message));
                    }
                }
            }
        });

        congrat_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNumber(v);
                Animation animation1 =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reviewanimation);
                congrat_star1.startAnimation(animation1);


                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {


                        congrat_star1.setImageResource(R.drawable.rate_yellow);

                     //   bool = !bool;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_white);
                        congrat_star3.setImageResource(R.drawable.rate_white);
                        congrat_star4.setImageResource(R.drawable.rate_white);
                        congrat_star5.setImageResource(R.drawable.rate_white);


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (bool) {

                            congrat_star1.setImageResource(R.drawable.rate_yellow);
                            count = "1";
                        } else {
                            congrat_star1.setImageResource(R.drawable.rate_white);
                            count = "0";
                        }
                        bool = !bool;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_white);
                        congrat_star3.setImageResource(R.drawable.rate_white);
                        congrat_star4.setImageResource(R.drawable.rate_white);
                        congrat_star5.setImageResource(R.drawable.rate_white);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        congrat_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNumber(v);
                Animation animation1 =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reviewanimation);
                congrat_star2.startAnimation(animation1);


                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {


                       // bool2 = !bool2;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_white);
                        congrat_star4.setImageResource(R.drawable.rate_white);
                        congrat_star5.setImageResource(R.drawable.rate_white);


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (bool2) {

                            congrat_star2.setImageResource(R.drawable.rate_yellow);
                            count = "2";
                        } else {
                            congrat_star2.setImageResource(R.drawable.rate_white);
                            count = "1";
                        }
                        bool2 = !bool2;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_white);
                        congrat_star4.setImageResource(R.drawable.rate_white);
                        congrat_star5.setImageResource(R.drawable.rate_white);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        congrat_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNumber(v);
                Animation animation1 =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reviewanimation);
                congrat_star3.startAnimation(animation1);


                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        //bool3 = !bool3;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_yellow);
                        congrat_star4.setImageResource(R.drawable.rate_white);
                        congrat_star5.setImageResource(R.drawable.rate_white);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (bool3) {

                            congrat_star3.setImageResource(R.drawable.rate_yellow);
                            count = "3";
                        } else {
                            congrat_star3.setImageResource(R.drawable.rate_white);
                            count = "2";

                        }
                        //bool3 = !bool3;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_yellow);
                        congrat_star4.setImageResource(R.drawable.rate_white);
                        congrat_star5.setImageResource(R.drawable.rate_white);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        congrat_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNumber(v);
                Animation animation1 =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reviewanimation);
                congrat_star4.startAnimation(animation1);


                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        //bool4 = !bool4;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_yellow);
                        congrat_star4.setImageResource(R.drawable.rate_yellow);
                        congrat_star5.setImageResource(R.drawable.rate_white);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (bool4) {

                            congrat_star4.setImageResource(R.drawable.rate_yellow);
                            count = "4";
                        } else {
                            congrat_star4.setImageResource(R.drawable.rate_white);
                            count = "3";
                        }
                        bool4 = !bool4;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_yellow);
                        congrat_star4.setImageResource(R.drawable.rate_yellow);
                        congrat_star5.setImageResource(R.drawable.rate_white);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        congrat_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNumber(v);
                Animation animation1 =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reviewanimation);
                congrat_star5.startAnimation(animation1);


                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        //bool5 = !bool5;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_yellow);
                        congrat_star4.setImageResource(R.drawable.rate_yellow);
                        congrat_star5.setImageResource(R.drawable.rate_yellow);
                    }


                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (bool5) {

                            congrat_star5.setImageResource(R.drawable.rate_yellow);
                            count = "5";
                        } else {
                            congrat_star5.setImageResource(R.drawable.rate_white);
                            count = "4";
                        }
                        bool5 = !bool5;
                        congrat_star1.setImageResource(R.drawable.rate_yellow);
                        congrat_star2.setImageResource(R.drawable.rate_yellow);
                        congrat_star3.setImageResource(R.drawable.rate_yellow);
                        congrat_star4.setImageResource(R.drawable.rate_yellow);
                        congrat_star5.setImageResource(R.drawable.rate_yellow);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
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

    private void makeStringReq() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
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
                            ////Log.e("Message", "<<>>" + msg);
                           // Your review has been submitted successfully
                            if (msg.equals("Your review has been submitted successfully")) {
                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(RatingActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(RatingActivity.this);
                                }
                                alert.setTitle("Successful");
                                alert.setMessage("تم ارسال التقيم بنجاح");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent =new Intent(RatingActivity.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("flag",1);
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
                                    alert = new AlertDialog.Builder(RatingActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(RatingActivity.this);
                                }
                              //  alert.setTitle("Successful");
                                alert.setMessage(msg);


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Intent intent =new Intent(RatingActivity.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("flag",1);
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


//
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
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(RatingActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(RatingActivity.this,getResources().getString(R.string.time_out_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {

                            Toast.makeText(RatingActivity.this,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(RatingActivity.this,getResources().getString(R.string.networkError_Message),Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Givereview");
                params.put("user_id", ssp.getUserId(RatingActivity.this));
                params.put("order_id", orderId);

                params.put("review_rating", count);
                ////Log.e("params", params.toString());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);

    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(RatingActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(RatingActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(RatingActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("flag",1);
        startActivity(intent);
    }
}

