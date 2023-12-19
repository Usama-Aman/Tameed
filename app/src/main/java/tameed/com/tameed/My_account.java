package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tameed.com.tameed.Entity.BankListModel;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 16-01-2018.
 */

public class My_account extends AppCompatActivity {
    TextView header_txt, bank_name, textView18;
    ImageView header_back;
    CircleImageView imageView;
    private String TAG = "My_account";
    private Button btnSaveBankDetail;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    private EditText editText13, editText14;
    SaveSharedPrefernces ssp;

    ArrayList<BankListModel> bankLists = new ArrayList<>();
    ArrayList<String> bankListsString = new ArrayList<>();

    private String selectedBankID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG,"****************************");
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        setContentView(R.layout.my_account);
        header_txt = (TextView) findViewById(R.id.txt_header);
        btnSaveBankDetail = findViewById(R.id.btnSaveBankDetail);
        editText13 = findViewById(R.id.editText13);
        editText14 = findViewById(R.id.editText14);
        bank_name = (TextView) findViewById(R.id.bank_name_txt);
        header_back = (ImageView) findViewById(R.id.header_back);
        textView18 = findViewById(R.id.textView18);
        imageView = findViewById(R.id.imageView);

        header_txt.setText("حسابي");

        ssp = new SaveSharedPrefernces();
        String bn = ssp.getUser_bank_name(this);
        String bno = ssp.getUser_bank_acct_num(this);
        String iban = ssp.getUser_bank_aban_num(this);
        String userName = ssp.getName(this);
        String userImage = ssp.getKEY_profile_pic_thumb_url(this);
        selectedBankID = ssp.getUser_bank_id(this);

        if (!bn.equals("") && bn != null) {
            bank_name.setText(bn);
        }
        if (!bno.equals("") && bno != null) {
            editText13.setText(bno);
        }
        if (!iban.equals("") && iban != null) {
            editText14.setText(iban);
        }
        if (!userName.equals("") && userName != null) {
            textView18.setText(userName);
        }
        if (!userImage.equals("") && userImage != null) {
            Glide.with(My_account.this).load(userImage).placeholder(R.mipmap.nouser_2x).into(imageView);
        }

        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bank_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(My_account.this);
                builder.setTitle("اسم البنك");
                builder.setItems(bankListsString.toArray(new String[bankListsString.size()]), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        //here you can use like this... str[position]
                        bank_name.setText(bankListsString.get(position).toString());
                        selectedBankID = bankLists.get(position).getBankslist_id();
                    }

                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        btnSaveBankDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bank_name.getText().toString().equals("")) {
                    Toast.makeText(My_account.this, "Please select bank name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editText13.getText().toString().equals("")) {
                    Toast.makeText(My_account.this, "Please type Bank account number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editText13.getText().toString().equals("")) {
                    Toast.makeText(My_account.this, "Please type bank IBAN number", Toast.LENGTH_SHORT).show();
                    return;
                }
                callSaveDetailApi();
            }
        });


        bankLists.clear();
        bankListsString.clear();
        getBankListApi();

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

    private void callSaveDetailApi() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                ssp.setUser_bank_name(My_account.this, bank_name.getText().toString());
                ssp.setUser_bank_id(My_account.this, selectedBankID);
                ssp.setUser_bank_acct_num(My_account.this, editText13.getText().toString());
                ssp.setUser_bank_iban_num(My_account.this, editText14.getText().toString());

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(My_account.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(My_account.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(My_account.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(My_account.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                    //TODO

                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Mybankaccountsetting");
                params.put("user_id", ssp.getUserId(My_account.this));
                params.put("user_bank_name", bank_name.getText().toString());
                params.put("user_bank_account_number", editText13.getText().toString());
                params.put("user_bank_iban_number", editText14.getText().toString());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }


    private void getBankListApi() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url, response -> {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            try {
                JSONObject jsonObject = new JSONObject(response.toString());

                JSONArray results = jsonObject.getJSONArray("result");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject o = results.getJSONObject(i);
                    bankLists.add(new BankListModel(o.getString("bank_name"), o.getString("bankslist_id")));
                    bankListsString.add(o.getString("bank_name"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(My_account.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(My_account.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                //TODO
            } else if (error instanceof ServerError) {
                Toast.makeText(My_account.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                //TODO
            } else if (error instanceof NetworkError) {
                Toast.makeText(My_account.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                //TODO

            } else if (error instanceof ParseError) {
                //TODO
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Bankslistdropdown");
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
                progress_dialog = new ProgressDialog(My_account.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(My_account.this);
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
}
