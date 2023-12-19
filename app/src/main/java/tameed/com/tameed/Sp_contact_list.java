package tameed.com.tameed;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
/**
 * Created by dev on 17-01-2018.
 */

public class Sp_contact_list extends AppCompatActivity {

  TextView header_txt;
  ImageView header_back;
  RecyclerView recyclerView;
  private String TAG = "Sp_contact_list";


  ArrayList<String> contact_list=new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
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
        //Log.e(TAG,"************ SP CONTACT LIST ****************");
        setContentView(R.layout.sp_contact_list);

        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("مقدمي خدمة من قائمة الاتصال بهاتفك");
        header_txt.setVisibility(View.VISIBLE);
        header_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,25);
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*recyclerView=(RecyclerView)findViewById(R.id.sp_contact_recycle);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        Sp_contact_list_Adapter sp_contact_list_adapter=new Sp_contact_list_Adapter(this,contact_list);

        recyclerView.setAdapter(sp_contact_list_adapter);*/


    }



}