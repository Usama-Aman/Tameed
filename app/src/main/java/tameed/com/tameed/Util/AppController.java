package tameed.com.tameed.Util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.multidex.MultiDexApplication;

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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.All_Payment_Entity;
import tameed.com.tameed.R;


public class AppController extends MultiDexApplication implements MultiDexApplication.ActivityLifecycleCallbacks {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    Activity activity;
    Context context;
    public static final String Prefs_remember_value = "UserTypeValues";
    SharedPreferences preferences2;
    SharedPreferences.Editor dev;
    String admin_id = "", status = "";
    private static AppController mInstance;
    private static boolean isInterestingActivityVisible;
    ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req", tag_json_arry = "jarray_req";
    public static All_Payment_Entity advertiseModel;
    public static boolean isAdShowOnce = false;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        registerActivityLifecycleCallbacks(this);

        isAdShowOnce = false;

    }

    public void setAdvertiseModel(All_Payment_Entity advertiseModel) {
        this.advertiseModel = advertiseModel;
    }

    public All_Payment_Entity getAdvertiseModel() {
        return advertiseModel;
    }

    public boolean isInterestingActivityVisible() {
        return isInterestingActivityVisible;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
//		////Log.e("visible","create");
        ++helper.create;
//		////Log.e("Create",""+helper.create);
//		////Log.e("Stop",""+helper.stop);
//		////Log.e("Resume",""+helper.resume);
//		////Log.e("Pause",""+helper.pause);
    }

    @Override
    public void onActivityStarted(Activity activity) {
//		////Log.e("visible","start");
    }

    @Override
    public void onActivityResumed(Activity activity) {
//		if (activity instanceof Dashboard) {
        isInterestingActivityVisible = true;
//		////Log.e("visible","resume");
//		////Log.e("Create",""+helper.create);
//		////Log.e("Stop",""+helper.stop);
//		////Log.e("Resume",""+helper.resume);
//		////Log.e("Pause",""+helper.pause);
        helper.resume++;
//			////Log.e("visible1",String.valueOf(isInterestingActivityVisible));
        if (helper.stop == helper.create) {
            ++helper.create;
            if (isOnline(activity)) {
                status = "1";
                //		makeStringReq();

            } else {
                showAlert(activity,
                        getString(R.string.networkError_Message));
            }
        } else if (helper.stop > helper.create) {
            ++helper.create;
            if (isOnline(activity)) {
                status = "1";
                //makeStringReq();

            } else {
                showAlert(activity,
                        getString(R.string.networkError_Message));
            }
        }
//		}
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ////Log.e("visible","pause");
        ++helper.pause;
    }

    @Override
    public void onActivityStopped(Activity activity) {
//		if (activity instanceof Dashboard) {
        isInterestingActivityVisible = false;
//			////Log.e("visible1",String.valueOf(isInterestingActivityVisible));
        ++helper.stop;


//		////Log.e("Create",""+helper.create);
//		////Log.e("Stop",""+helper.stop);
//		////Log.e("Resume",""+helper.resume);
//		////Log.e("Pause",""+helper.pause);
//		////Log.e("visible","stop");
        if (helper.stop > helper.create) {
            ++helper.create;
        }
        if (helper.stop == helper.create) {
            if (helper.resume == helper.pause) {
                ////Log.e("BackGround", "BackGround");
                if (isOnline(activity)) {
                    status = "0";
                    //	makeStringReq();

                } else {
                    showAlert(activity,
                            getString(R.string.networkError_Message));
                }
            }
        }
//		}
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ////Log.e("visible","destroy");
        ////Log.e("Create",""+helper.create);
        ////Log.e("Stop",""+helper.stop);
        ////Log.e("Resume",""+helper.resume);
        ////Log.e("Pause",""+helper.pause);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public static void showAlert(Activity mActivity, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();

        // Setting Dialog Title
		/*alertDialog.setTitle(mActivity.getResources().getString(
				R.string.sliderheading));
		*/// alertDialog.setIcon(mActivity.getResources().getDrawable(R.drawable.ic_launcher));
        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public <T> void addToRequestQueue(Request<T> req) {

        req.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void Login() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject obj = null;
                        try {
                            ////Log.e("Json Data", response.toString());
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
//                            Toast.makeText(Night_life.this,"1",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            // TODO Auto-generated catch block
//                            Toast.makeText(Night_life.this,"2",Toast.LENGTH_LONG).show();
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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

//							Toast.makeText(context,"Time Out Error",Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
//							Toast.makeText(context,"Authentication Error",Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

//							Toast.makeText(context,"Server Error",Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
//							Toast.makeText(context,"Network Error",Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("action", "Doctoronlineofflinestatusupdate");
                params.put("doctor_id", helper.doctor_id);
                params.put("status", status);
                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}
