package tameed.com.tameed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Fragment.Direct_T_fragment;
import tameed.com.tameed.Fragment.Public_room_fragment;
import tameed.com.tameed.Fragment.service_fragment;
import tameed.com.tameed.Util.AppController;

/**
 * Created by root on 24/1/18.
 */

public class P_room extends AppCompatActivity {
    private Button proom, services, direct;
    Fragment fragment = null;
    int flag;
    FrameLayout frame;
    Intent i1;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String service, direct_favourite;
    private String TAG = "P_room";

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
        //Log.e(TAG, "****************************");
        setContentView(R.layout.p_room);

        frame = (FrameLayout) findViewById(R.id.frame_p_room);
        proom = (Button) findViewById(R.id.po_public_room);
        services = (Button) findViewById(R.id.po_services);
        direct = (Button) findViewById(R.id.po_direct);

        i1 = getIntent();
        flag = i1.getIntExtra("flag", 0);

        Intent i = getIntent();

        direct_favourite = i.getStringExtra("direct_favourite");


        if (helper.tab == 1) {

            proom.setBackgroundResource(R.color.colorPrimaryDark);
            services.setBackgroundResource(R.color.colorPrimary);
            direct.setBackgroundResource(R.color.colorPrimary);
            fragment = new Public_room_fragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();


            } else {
                //Log.e("proom", "Error in creating fragment");
            }
        } else if (helper.tab == 2) {
            proom.setBackgroundResource(R.color.colorPrimary);
            services.setBackgroundResource(R.color.colorPrimaryDark);
            direct.setBackgroundResource(R.color.colorPrimary);
            //Log.e("P room....1111..", "....11111..");
            fragment = new service_fragment();

            if (fragment != null) {
                helper.service_status = "1";
                helper.home = 13;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();
            }


        } else if (helper.tab == 3) {
            proom.setBackgroundResource(R.color.colorPrimary);
            services.setBackgroundResource(R.color.colorPrimary);
            direct.setBackgroundResource(R.color.colorPrimaryDark);
            fragment = new Direct_T_fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();
            if (fragment != null) {


            }


        } else if (flag == 10) {
            proom.setBackgroundResource(R.color.colorPrimaryDark);
            services.setBackgroundResource(R.color.colorPrimary);
            direct.setBackgroundResource(R.color.colorPrimary);

            fragment = new Public_room_fragment();
            if (fragment != null) {
                helper.p_room = "1";
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();


            } else {
                //Log.e("proom", "Error in creating fragment");
            }
        }

       /* else if(TextUtils.isEmpty(direct_favourite));
        {

            if (fragment != null) {
                helper.p_room="1";
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.
                frame_p_room, fragment).commit();


                } else {
                    //Log.e("proom", "Error in creating fragment");
                }
            }
*/

       /* if(helper.p_room.equals("1"))
            if (fragment != null) {
                helper.p_room="1";
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.
        {frame_p_room, fragment).commit();


            } else {
                //Log.e("proom", "Error in creating fragment");
            }
        }*/


        proom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                proom.setBackgroundResource(R.color.colorPrimaryDark);
                services.setBackgroundResource(R.color.colorPrimary);
                direct.setBackgroundResource(R.color.colorPrimary);

                fragment = new Public_room_fragment();
                if (fragment != null) {
                    helper.p_room = "1";
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();


                } else {
                    //Log.e("proom", "Error in creating fragment");
                }

            }
        });


        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proom.setBackgroundResource(R.color.colorPrimary);
                services.setBackgroundResource(R.color.colorPrimaryDark);
                direct.setBackgroundResource(R.color.colorPrimary);
                //Log.e("P room....2222..", "....2222..");
                fragment = new service_fragment();
                if (fragment != null) {
                    helper.service_status = "1";
                    helper.setting_service = "";
                    helper.home = 13;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();
                }

            }
        });


        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proom.setBackgroundResource(R.color.colorPrimary);
                services.setBackgroundResource(R.color.colorPrimary);
                direct.setBackgroundResource(R.color.colorPrimaryDark);
                fragment = new Direct_T_fragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_p_room, fragment).commit();


                }

            }
        });



    }

}
