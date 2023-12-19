package tameed.com.tameed;

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

public class About extends AppCompatActivity {
    TextView header_txt;
    ImageView header_back;
    WebView about_webview;
//    String url = "http://seemcodersapps.com/tameed/index/about";
    String url = "https://tameed.net/tameed_app/index/about";


    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        setContentView(R.layout.about);

        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("حول");
        header_txt.setVisibility(View.VISIBLE);
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        about_webview = (WebView) findViewById(R.id.about);
        about_webview.getSettings().setLoadsImagesAutomatically(true);

        about_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        about_webview.getSettings().setJavaScriptEnabled(true);
        about_webview.loadUrl(url);
    }

}
