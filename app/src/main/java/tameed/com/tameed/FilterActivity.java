package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Abstract_global;
import tameed.com.tameed.Util.Concreate_global;

public class FilterActivity extends AppCompatActivity {
    TextView txtHeader, filter_distance;
    ImageView header_back, radio_on, radio_off;
    SeekBar seekbar_filter;
    EditText filter_serch_city;
    Button reset, apply;
    Activity act;
    Boolean bool1 = false;
    Boolean bool2 = false;
    ConstraintLayout constraint_online, constraint_offline;
    String online_offline_status, services;
    String city_to_cover, city_to_service;
    Abstract_global abstract_global;
    String service_id = "", city_id = "";
    EditText filter_search_city;
    TextView filter_search_service;
    private String TAG = "FilterActivity";

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
        setContentView(R.layout.activity_filter);
        act = FilterActivity.this;
        abstract_global = new Concreate_global();
        filter_serch_city = findViewById(R.id.filter_serch_city);
        txtHeader = (TextView) findViewById(R.id.txt_header);
        txtHeader.setText("الفلتر");
        header_back = (ImageView) findViewById(R.id.header_back);
        constraint_online = findViewById(R.id.constraint_online);
        constraint_offline = findViewById(R.id.constraint_offline);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterActivity.this, MainActivity.class);
                i.putExtra("flag", 18);
                startActivity(i);
            }
        });

        filter_search_city = (EditText) findViewById(R.id.filter_serch_city);
        filter_search_service = (TextView) findViewById(R.id.filter_search_service);

        Intent i = getIntent();
        service_id = i.getStringExtra("service_id");

        Intent j = getIntent();
        city_id = j.getStringExtra("city_id");
        //////Log.e("city_id7777",city_id);
        //////Log.e("service_id",service_id);
        if (!TextUtils.isEmpty(helper.service_name_list)) {
            filter_search_service.setText(helper.service_name_list);
            //filter_search_service.setSelection(filter_search_service.getText().length());
            //helper.service_name_list="";
        }
        if (!TextUtils.isEmpty(helper.city_name_list)) {
            filter_search_city.setText(helper.city_name_list);
            filter_search_city.setSelection(filter_search_city.getText().length());
            //helper.city_name_list="";
        }
        filter_search_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterActivity.this, Search_by_city.class);
                //i.putExtra("city_to_cover",city_to_service);

                helper.city_to_cover = "";
                helper.filter_services = "";
                helper.filter_services = "services";
                startActivity(i);
            }
        });

        filter_search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterActivity.this, Search_by_city.class);
                // i.putExtra("city_to_cover",city_to_cover);
                helper.city_to_cover = "";
                helper.filter_services = "";

                helper.city_to_cover = "cities";
                startActivity(i);
            }
        });

        reset = (Button) findViewById(R.id.button_reset);
        apply = (Button) findViewById(R.id.button_apply);

        radio_on = (ImageView) findViewById(R.id.filter_radio_on);
        radio_off = (ImageView) findViewById(R.id.filter_radio_off);

        filter_distance = (TextView) findViewById(R.id.filter_distance);
        seekbar_filter = (SeekBar) findViewById(R.id.seekbar_filter);


        // seek bar

        filter_distance.setText(+helper.filter_distance + " " + "كيلو");
        seekbar_filter.setProgress(helper.filter_distance);
        seekbar_filter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                helper.filter_distance = progress;

                ////Log.e("filter_filter_distance",String.valueOf(helper.filter_distance));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (TextUtils.isEmpty(String.valueOf(helper.filter_distance))) {
                    filter_distance.setText(+progressChangedValue + " " + "كيلو");
                } else {
                    filter_distance.setText(+helper.filter_distance + " " + "كيلو");
                }


            }
        });

        if (helper.online_offline_status.equals("1")) {
            radio_on.setImageResource(R.mipmap.radio_checked);
        } else if (helper.online_offline_status.equals("0")) {
            radio_on.setImageResource(R.mipmap.radio);
        }


        constraint_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bool2) {
                    radio_off.setImageResource(R.mipmap.radio_checked);
                    online_offline_status = "0";
                    helper.online_offline_status = online_offline_status;
                } else {
                    radio_off.setImageResource(R.mipmap.radio);
                    // online_offline_status="";

                }
                bool2 = !bool2;

                radio_on.setImageResource(R.mipmap.radio);

            }
        });


        constraint_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bool1) {
                    radio_on.setImageResource(R.mipmap.radio_checked);
                    online_offline_status = "1";
                    helper.online_offline_status = online_offline_status;
                } else {
                    radio_on.setImageResource(R.mipmap.radio);
                    //online_offline_status="";

                }
                bool1 = !bool1;

//                helper.online_offline_status=online_offline_status;
//                radio_on.setImageResource(R.mipmap.radio_checked);
                radio_off.setImageResource(R.mipmap.radio);

            }
        });

/*
        radio_on.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        online_offline_status="1";
                        helper.online_offline_status=online_offline_status;
                        radio_on.setImageResource(R.mipmap.radio_checked);
                        radio_off.setImageResource(R.mipmap.radio);
                    }
                }
        );

        radio_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                online_offline_status="0";
                helper.online_offline_status=online_offline_status;
                radio_on.setImageResource(R.mipmap.radio);
                radio_off.setImageResource(R.mipmap.radio_checked);

            }
        });
*/
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_off.setImageResource(R.mipmap.radio);
                radio_on.setImageResource(R.mipmap.radio);
                filter_distance.setText("20 كيلو");
                filter_search_city.getText().clear();
                filter_search_service.setText("");
                seekbar_filter.setProgress(20);
                helper.service_name_list = "";
                helper.online_offline_status = "";
                helper.city_name_list = "";

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.city_to_cover_list = "";
                helper.service_to_cover_list = "";
                //helper.service_id_list="";
                // helper.city_to_id_list="";

                // helper.city_to_id_list=city_id;
               /* city_to_cover = filter_serch_city.getText().toString();
                services = filter_search_service.getText().toString();*/
                //helper.city_to_cover=city_to_cover;
                // helper.filter_services=services;
             /*   if (TextUtils.isEmpty(String.valueOf(helper.filter_distance))) {
                    abstract_global.abstract_toast(act, "Please select distance");

                } *//*else if (TextUtils.isEmpty(helper.city_to_cover_list)) {
                    abstract_global.abstract_toast(act, "Please select city_to_cover");

                }*/
              /*  else if (TextUtils.isEmpty(online_offline_status)) {
                    abstract_global.abstract_toast(act, "Please select online offline status");

                } *//*else if (TextUtils.isEmpty(helper.service_to_cover_list)) {
                    abstract_global.abstract_toast(act, "Please select  services");

                }*/

                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(FilterActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(FilterActivity.this);
                }

                alert.setMessage("هل تريد تطبيق هذه التغييرات؟");
                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.filter_pro = "";
                        helper.search_by_service = "";
                        Intent i = new Intent(FilterActivity.this, MainActivity.class);
                        helper.search_by_service = "";
                        helper.filter_pro = "1";
                        // helper.online_offline_status=online_offline_status;
                        ////Log.e("online_offline_status666",String.valueOf(helper.online_offline_status));

                        helper.city_to_cover_list = filter_search_city.getText().toString();
                        helper.service_to_cover_list = filter_search_service.getText().toString();


                        // helper.service_id_list=service_id;
                        startActivity(i);
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        });

    }

}
