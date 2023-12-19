package tameed.com.tameed;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gcm.GCMRegistrar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import tameed.com.tameed.Util.Constant1;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by dell on 9/19/2017.
 */

public class Splash_Screen extends AppCompatActivity {
    int i = 0;
    Timer timer;
    MyReceiver receiver;
    String regId, show_dialog;
    String user_id;
    private SaveSharedPrefernces ssp;

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
        setContentView(R.layout.splash_screen);
//        Fabric.with(this, new Crashlytics());
        ssp = new SaveSharedPrefernces();


        Intent intent = new Intent();
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        sendBroadcast(intent);

        TimerTask task = new MYTimerClass();
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 700, 200);

        receiver = new MyReceiver();
        IntentFilter intentfilter = new IntentFilter(GCMIntentService.REGISTER_ACTION);
        registerReceiver(receiver, intentfilter);

        regId = GCMRegistrar.getRegistrationId(Splash_Screen.this);
        //Log.e("getRegistrationId", "=======>" + regId);

        printKeyHash(this);

        user_id = ssp.getUserId(Splash_Screen.this);


    }


    public static String printKeyHash(Activity context) {


        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

//            //Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                //Log.e("Key Hash=========", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            //Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            //Log.e("Exception", e.toString());
        }

        return key;
    }

    class MYTimerClass extends TimerTask {


        @Override
        public void run() {
            // TODO Auto-generated method stub
            i++;

            ////Log.e("timer", "count " + i);
            if (i == 2) {
                timer.cancel();

                if (!regId.equals("")) {
                    try {
                        if (!user_id.equals("")) {

                            //ssp.setRegId(Splash_Screen.this, regId);
                            //Log.e("ANJALLLLLLIIII", "" + regId);

                            Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                            intent.putExtra("device_token", regId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);


                        } else {
                            //ssp.setRegId(Splash_Screen.this, regId);
                            //Log.e("NEWWWWWWW", "" + regId);

                            Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                            intent.putExtra("device_token", regId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);

                        }


                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } else {

                    regId = GCMRegistrar.getRegistrationId(Splash_Screen.this);
                    GCMRegistrar.register(Splash_Screen.this, Constant1.SENDER_ID);
                    //Log.e("33333333333333", "" + regId);
                    //ssp.setRegId(Splash_Screen.this, regId);
                    //Log.e("55555555555555", ">>" + regId);
                    try {
                        if (!user_id.equals("")) {

                            Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                            intent.putExtra("device_token", regId);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                            intent.putExtra("device_token", regId);
                            startActivity(intent);

                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            regId = intent.getStringExtra(Constant1.REGISTRATION_ID);
            //ssp.setRegId(Splash_Screen.this, regId);

            if (!user_id.equals("")) {
                Intent intentt = new Intent(Splash_Screen.this, MainActivity.class);
                //    intentt.putExtra("device_token", regId);
                startActivity(intentt);
            } else {

                Intent intentt = new Intent(Splash_Screen.this, MainActivity.class);
                //    intentt.putExtra("device_token", regId);
                startActivity(intentt);
            }
//

        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {

            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

    }

    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        sendBroadcast(intent);
    }
}
