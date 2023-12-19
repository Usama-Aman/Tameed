package tameed.com.tameed;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import tameed.com.tameed.Adapter.helper;
/**
 * Created by dev on 23-01-2018.
 */

public class Order_detail_provider_accept extends AppCompatActivity {

    TextView header_txt;
    ImageView header_back,accept_img1,accept_img2,accept_img3;
    Button cancel,complete;
    private String TAG = "Order_detail_provider_accept";

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
        ////Log.e(TAG,"****************************");
        setContentView(R.layout.provider_accept_order);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("تفاصي المعاملة");

        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        accept_img1=(ImageView)findViewById(R.id.accept_order_detail_img1);
        accept_img2=(ImageView)findViewById(R.id.accept_order_detail_img2);
        accept_img3=(ImageView)findViewById(R.id.accept_order_detail_img3);

        cancel=(Button)findViewById(R.id.provider_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(Order_detail_provider_accept.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_cancel_order);
                dialog1.show();

                Button send=(Button)dialog1.findViewById(R.id.send_admin);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });


            }
        });


        accept_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.img=6;
                Intent intent=new Intent(getApplicationContext(),Full_Screen.class);
                accept_img1.buildDrawingCache();
                Bitmap image= accept_img1.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("accept_imagebitmap1", image);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        accept_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.img=7;
                Intent intent=new Intent(getApplicationContext(),Full_Screen.class);
                accept_img2.buildDrawingCache();
                Bitmap image= accept_img2.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("accept_imagebitmap2", image);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        accept_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.img=8;
                Intent intent=new Intent(getApplicationContext(),Full_Screen.class);

                accept_img3.buildDrawingCache();
                Bitmap image= accept_img3.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("accept_imagebitmap3", image);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


    }
}