package tameed.com.tameed;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import tameed.com.tameed.Adapter.helper;

/**
 * Created by dell on 2/26/2018.
 */

public class Customer_Payment extends AppCompatActivity {
    ImageView tick,header_back;
    TextView txt_header;
    String order_id;
    Intent intent;
    private String TAG = "Customer_Payment";

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
        setContentView(R.layout.customer_payment);
        //Log.e(TAG,"****************************");
        intent=getIntent();
        order_id=intent.getStringExtra("order_id");
        tick= (ImageView) findViewById(R.id.imageView2);
        header_back=(ImageView) findViewById(R.id.header_back);
        txt_header=(TextView) findViewById(R.id.txt_header);
        txt_header.setText("مدفوعات العميل");
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Payment.this, Order_detail.class);
                helper.pblc=0;
                intent.putExtra("order_id",order_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Payment.this, Order_detail.class);
                helper.pblc=0;
                intent.putExtra("order_id",order_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Customer_Payment.this, Order_detail.class);
        helper.pblc=0;
        intent.putExtra("order_id",order_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
