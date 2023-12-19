package tameed.com.tameed;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
public class OrderSummary extends AppCompatActivity {

    private TextView txtHeader;
    private ImageView imgBackHeader;
    private String TAG = "OrderSummary";


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
        setContentView(R.layout.activity_order_summary);

        txtHeader=(TextView) findViewById(R.id.txt_header);
        imgBackHeader= (ImageView) findViewById(R.id.header_back);


        txtHeader.setText("ملخص المعاملة");
    }
}
