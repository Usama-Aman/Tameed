package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
/**
 * Created by dev on 15-01-2018.
 */

public class Terms_Condition extends AppCompatActivity {
    TextView header_txt;
    ImageView header_back;
    WebView terms_ofuse_webview;
    //String url = "http://seemcodersapps.com/tameed/index/terms";
    String url = "https://tameed.net/Terms_Services.html";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        setContentView(R.layout.terms_condition);

        header_txt= (TextView) findViewById(R.id.txt_header);
        header_txt.setText("الشروط والأحكام");
        header_txt.setVisibility(View.VISIBLE);
        header_back= (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        terms_ofuse_webview= (WebView) findViewById(R.id.term_condition);
        terms_ofuse_webview.getSettings().setLoadsImagesAutomatically(true);

        terms_ofuse_webview.setBackgroundResource(R.drawable.shape_white);
        terms_ofuse_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        terms_ofuse_webview.getSettings().setJavaScriptEnabled(true);
        terms_ofuse_webview.loadUrl(url);



    }
}