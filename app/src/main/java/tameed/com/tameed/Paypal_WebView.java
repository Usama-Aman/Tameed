package tameed.com.tameed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.util.EncodingUtils;

import java.util.Locale;
/**
 * Created by This Pc on 7/23/2017.
 */

public class Paypal_WebView extends AppCompatActivity {
    TextView header_logo_txt, service_txt;
    LinearLayout toolbar_home, toolbar_watch, toolbar_service, toolbar_store, toolbar_more;
    ImageView service_img;
    String web_title;
    WebView services_webView;
    String cart_id;
    private String TAG = "Paypal_WebView";

    String shipping_id;
    String total_amount;
    String data;
    ProgressDialog progressDialog;
    public static final String Prefs_remember_value = "UserTypeValues";
    SharedPreferences preferences2;
    SharedPreferences.Editor dev;

    String url = "http://seemcodersapps.com/tameed/card/paymentprocessing";
    String post_data;
    Intent link_intent;
    String title, service_url;
    String order_id, provider_id, user_id, card_id, payment_amount, from, save_card;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG, "****************************");
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        setContentView(R.layout.paypal_webview);
        link_intent = getIntent();
        title = link_intent.getStringExtra("title");
        //////Log.e("Title", "<<>>" + title);
        service_url = link_intent.getStringExtra("url");
        //////Log.e("Service_URL", "<<>>" + service_url);
        header_logo_txt = (TextView) findViewById(R.id.header_txt);

        header_logo_txt.setText("المدفوعات");

        header_logo_txt.setVisibility(View.VISIBLE);


        services_webView = (WebView) findViewById(R.id.paypal_webView);


        progressDialog = new ProgressDialog(Paypal_WebView.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        services_webView.getSettings().setLoadsImagesAutomatically(true);
        services_webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        services_webView.getSettings().setJavaScriptEnabled(true);


        order_id = link_intent.getStringExtra("order_id");
        provider_id = link_intent.getStringExtra("provider_id");
        user_id = link_intent.getStringExtra("user_id");
        card_id = link_intent.getStringExtra("card_id");
        payment_amount = link_intent.getStringExtra("payment_amount");
        from = link_intent.getStringExtra("from");
        save_card = link_intent.getStringExtra("save_card");


        data = link_intent.getStringExtra("url");


        service_url = url;
        //////Log.e("Url", "<<>>" + url);
        url = data;

        post_data = "paypal_card_id=" + card_id + "&user_id=" + user_id + "&payment_amount=" + payment_amount + "&order_id=" + order_id + "&provider_id=" + provider_id + "&from=" + from + "&is_save_card=" + save_card;


        //////Log.e("URL", "<<>>" + url);
        //////Log.e("User_id", "<>" + user_id);
        //////Log.e("Cart_id", "<>" + provider_id);
        //////Log.e("Shipping_id", "<>" + order_id);
        //////Log.e("Total_amount", "<>" + card_id + "     " + payment_amount);
        //////Log.e("from", ",<>" + from);
        //////Log.e("saved_Card", "<>" + save_card);

        // services_webView.loadUrl(url);
//        services_webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        services_webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Paypal_WebView.this.setTitle(view.getTitle());
                web_title = view.getTitle().toString();
                if (web_title.contains("Payment Success")) {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(Paypal_WebView.this, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(Paypal_WebView.this);
                    }
                    alert.setTitle(getResources().getString(R.string.title_payment_success));
                    alert.setMessage(getResources().getString(R.string.msg_payment_success));


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Paypal_WebView.this, Order_detail.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("order_id", order_id);
                            startActivity(intent);
//                            helper.count=0;
//                            preferences2=getSharedPreferences(Prefs_remember_value,MODE_PRIVATE);
//                            dev=preferences2.edit();
//                            dev.putInt("count", helper.count);
//                            dev.commit();


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

                } else if (web_title.contains("Payment Failed")) {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(Paypal_WebView.this, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(Paypal_WebView.this);
                    }
                    alert.setTitle(getResources().getString(R.string.title_payment_failed));
                    alert.setMessage(getResources().getString(R.string.msg_payment_failed));


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
//                            helper.count=0;
//                            preferences2=getSharedPreferences(Prefs_remember_value,MODE_PRIVATE);
//                            dev=preferences2.edit();
//                            dev.putInt("count", helper.count);
//                            dev.commit();


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
                //////Log.e("TITLE", "<>" + view.getTitle());
            }
        });
        services_webView.postUrl(service_url, EncodingUtils.getBytes(post_data, "BASE64"));


        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }


}

