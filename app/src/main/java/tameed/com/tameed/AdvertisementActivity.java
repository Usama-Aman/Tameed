package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tameed.com.tameed.Entity.AdvertisementModel;
import tameed.com.tameed.Entity.BankListModel;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;


public class AdvertisementActivity extends AppCompatActivity {

    private ImageView btnCloseAd, adImageView;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    private AdvertisementModel advertisementModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        initViews();
        getAdsFromApi();

    }

    private void initViews() {
        btnCloseAd = findViewById(R.id.btnAddClose);
        adImageView = findViewById(R.id.adImageView);

        btnCloseAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAdsFromApi() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url, response -> {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject results = jsonObject.getJSONObject("result");
                advertisementModel = new Gson().fromJson(results.toString(), AdvertisementModel.class);
                setAd();

            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }

        }, error -> {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            finish();
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(AdvertisementActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(AdvertisementActivity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                //TODO
            } else if (error instanceof ServerError) {
                Toast.makeText(AdvertisementActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                //TODO
            } else if (error instanceof NetworkError) {
                Toast.makeText(AdvertisementActivity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                //TODO

            } else if (error instanceof ParseError) {
                //TODO
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getrandomad");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    private void setAd() {
        Glide.with(AdvertisementActivity.this).load(advertisementModel.getImage()).into(adImageView);
    }

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(AdvertisementActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(AdvertisementActivity.this);
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
                default:
                    break;
            }

            return false;
        }

    });
}
