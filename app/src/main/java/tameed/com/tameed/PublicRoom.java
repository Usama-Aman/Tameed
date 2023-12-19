package tameed.com.tameed;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
public class PublicRoom extends AppCompatActivity {
    private TextView txtHeader;
   private Button proom,services,direct;
   ImageView direct_order,header_back;
   ArrayList<String> public_room=new ArrayList<>();
    private String TAG = "PublicRoom";

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
        setContentView(R.layout.activity_public_room);
        txtHeader=(TextView) findViewById(R.id.txt_header);
        txtHeader.setText("إرسال للغرفة العامة");
        header_back=(ImageView)findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Log.e(TAG,"****************************");
 /*       RecyclerView recyclerView=(RecyclerView)findViewById(R.id.po_recy);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        Send_public_order_Adapater send_public_order_adapater=new Send_public_order_Adapater(this,public_room);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(send_public_order_adapater);*/

            proom=(Button)findViewById(R.id.po_public_room);
            services=(Button)findViewById(R.id.po_services);
            direct=(Button)findViewById(R.id.po_direct);
             direct_order=(ImageView) findViewById(R.id.add_direct_order);
             direct_order.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     startActivity(new Intent(getApplicationContext(),SendPublicOrder.class));
                 }
             });

            proom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    proom.setBackgroundResource(R.color.colorPrimaryDark);
                    services.setBackgroundResource(R.color.colorPrimary);
                    direct.setBackgroundResource(R.color.colorPrimary);
                }
            });
services.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        proom.setBackgroundResource(R.color.colorPrimary);
        services.setBackgroundResource(R.color.colorPrimaryDark);
        direct.setBackgroundResource(R.color.colorPrimary);
        startActivity(new Intent(getApplicationContext(),My_services.class));

    }
});

direct.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        proom.setBackgroundResource(R.color.colorPrimary);
        services.setBackgroundResource(R.color.colorPrimary);
        direct.setBackgroundResource(R.color.colorPrimaryDark);
        startActivity(new Intent(getApplicationContext(),Sp_contact_list.class));

    }
});




    }
}
