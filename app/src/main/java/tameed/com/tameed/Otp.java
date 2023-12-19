package tameed.com.tameed;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Created by dev on 10-01-2018.
 */

public class Otp extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4;
    Button resend;
    TextView header_txt;
    String verify1, verify2, verify3, verify4;

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

        setContentView(R.layout.otp);


        otp1 = (EditText) findViewById(R.id.otp1);
        otp2 = (EditText) findViewById(R.id.otp2);
        otp3 = (EditText) findViewById(R.id.otp3);
        otp4 = (EditText) findViewById(R.id.otp4);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("OTP");
        header_txt.setVisibility(View.VISIBLE);


        otp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp1.getText().toString().length() == 1) {
                    otp2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {


            }

            public void afterTextChanged(Editable s) {
                verify1 = otp1.getText().toString();


            }


        });


        otp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp2.getText().toString().length() == 1) {
                    otp3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {


            }

            public void afterTextChanged(Editable s) {
                verify2 = otp2.getText().toString();


            }


        });


        otp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp3.getText().toString().length() == 1) {
                    otp4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {


            }

            public void afterTextChanged(Editable s) {
                verify3 = otp3.getText().toString();


            }


        });


        verify4 = otp4.getText().toString();


    }
}
