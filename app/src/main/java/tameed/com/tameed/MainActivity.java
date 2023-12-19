package tameed.com.tameed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.ChatHistory_Entity;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.Fragment.CashFragment;
import tameed.com.tameed.Fragment.Favourite;
import tameed.com.tameed.Fragment.HomeFragment;
import tameed.com.tameed.Fragment.NotificationFragment;
import tameed.com.tameed.Fragment.OrdersFragment;
import tameed.com.tameed.Fragment.Setting;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.Util.URLogs;

import static tameed.com.tameed.Fragment.Direct_T_fragment.REQUEST_READ_CONTACTS;
import static tameed.com.tameed.MapView_Activity.RequestPermissionCode;

public class MainActivity extends AppCompatActivity {
    ImageView ivhome, ivorder, ivcash, ivfavorites, ivsetting;
    ConstraintLayout home, order, cash, favorites, setting;
    Fragment fragment = null;
    private boolean backPressedToExitOnce = false;
    private Toast toast = null;
    final Context context = this;
    FrameLayout frameLayout;
    String profile_update;
    int flag;
    /* String user_id,name,email_address,calling_code,mobile_number,combine_mobile,password,gmt_value,latitude,longitude,location,payment_preference,
             device_id,active_status,verify_code,is_verified,login_status,user_type,profile_pic_thumb_url,profile_pic_2xthumb_url,profile_pic_3xthumb_url,
             profile_cover_pic_1xthumb_url,profile_cover_pic_2xthumb_url,profile_cover_pic_3xthumb_url,push_notification,online_offline_status,
             profile_pic_url,description ,review_count,review_rating,order_count,country,user_device_type,mobile_visible ,city_to_cover,distance ,user_bank_name ,user_bank_account_number ,user_bank_iban_number ,is_favourite,
     user_service_id ,sub_service_id ,main_category_id ,service_id ,added_date ,category_name ,service_name ,sub_service_name;
 */
    Intent i1;
    Activity act;
    SaveSharedPrefernces ssp;
    String pending_orders_count_as_customer, online_offline_status, total_order_badge_count;
    public static String unread_chat_count;

    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ContentResolver cr;
    private Cursor cur;

    private String TAG = "MainActivity";

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

        setContentView(R.layout.activity_main);
        ssp = new SaveSharedPrefernces();
        act = MainActivity.this;
        helper.badge_txt = (TextView) findViewById(R.id.badge_txt);
        helper.badge_txt_setting = (TextView) findViewById(R.id.badge_txt_setting);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        ivhome = (ImageView) findViewById(R.id.home_img);
        ivhome.setImageResource(R.drawable.nav_home_active);
        ivorder = (ImageView) findViewById(R.id.order_img);
        ivcash = (ImageView) findViewById(R.id.cash_img);
        ivfavorites = (ImageView) findViewById(R.id.favorites_img);
        ivsetting = (ImageView) findViewById(R.id.setting_img);
        home = (ConstraintLayout) findViewById(R.id.toolbar_home);
        order = (ConstraintLayout) findViewById(R.id.toolbar_orders);
        cash = (ConstraintLayout) findViewById(R.id.toolbar_Cash);
        favorites = (ConstraintLayout) findViewById(R.id.toolbar_favorites);
        setting = (ConstraintLayout) findViewById(R.id.toolbar_setting);
        i1 = getIntent();
        flag = i1.getIntExtra("flag", 0);
        ////Log.e(TAG, "****************************");

//        View decorView = getWindow().getDecorView();
//        final ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);

//

        /*if(ssp.getUserId(MainActivity.this).equals(""))
        {
            final AlertDialog.Builder alert;
            if (Build.VERSION.SDK_INT >= 11) {
                alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                alert = new AlertDialog.Builder(context);
            }

            alert.setMessage("Please Login to continue.");


            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));


                }
            });


            Dialog dialog = alert.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }*/

        if (TextUtils.isEmpty(ssp.getUserId(MainActivity.this))) {
            helper.login = false;
        } else {
            helper.login = true;
            utiltyRequest();
        }

        if (flag == 1) {
            ivhome.setImageResource(R.drawable.nav_home);
            ivorder.setImageResource(R.drawable.nav_list_active);
            ivfavorites.setImageResource(R.drawable.nav_fav);
            ivsetting.setImageResource(R.drawable.nav_setting);
            frameLayout.setVisibility(View.VISIBLE);
            ivcash.setImageResource(R.drawable.nav_income);
            fragment = new OrdersFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }


        } else if (flag == 4) {
            helper.setting = 0;
            ivhome.setImageResource(R.drawable.nav_home);
            ivorder.setImageResource(R.drawable.nav_list);
            ivfavorites.setImageResource(R.drawable.nav_fav);
            ivsetting.setImageResource(R.drawable.nav_setting_active);
            frameLayout.setVisibility(View.VISIBLE);
            ivcash.setImageResource(R.drawable.nav_income);
            fragment = new Setting();

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            }


        } else if (flag == 21) {

            ivhome.setImageResource(R.drawable.nav_home);
            ivorder.setImageResource(R.drawable.nav_list);
            ivfavorites.setImageResource(R.drawable.nav_fav);
            ivsetting.setImageResource(R.drawable.nav_setting_active);
            frameLayout.setVisibility(View.VISIBLE);
            ivcash.setImageResource(R.drawable.nav_income);
            fragment = new CashFragment();

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        } else if (flag == 10) {

            ivhome.setImageResource(R.drawable.nav_home);
            ivorder.setImageResource(R.drawable.nav_list);
            ivfavorites.setImageResource(R.drawable.nav_fav);
            ivsetting.setImageResource(R.drawable.nav_setting_active);
            frameLayout.setVisibility(View.VISIBLE);
            ivcash.setImageResource(R.drawable.nav_income);
            fragment = new Setting();

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        } else if (flag == 11) {
            ivhome.setImageResource(R.drawable.nav_home);
            ivorder.setImageResource(R.drawable.nav_list);
            ivfavorites.setImageResource(R.drawable.nav_fav);
            ivsetting.setImageResource(R.drawable.nav_setting_active);
            frameLayout.setVisibility(View.VISIBLE);
            ivcash.setImageResource(R.drawable.nav_income);
            fragment = new Setting();

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            }
        } else if (flag == 13) {
            ivhome.setImageResource(R.drawable.nav_home);
            ivorder.setImageResource(R.drawable.nav_list);
            ivfavorites.setImageResource(R.drawable.nav_fav);
            ivsetting.setImageResource(R.drawable.nav_setting_active);
            frameLayout.setVisibility(View.VISIBLE);
            ivcash.setImageResource(R.drawable.nav_income);
            fragment = new Setting();

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            }
        } else if (flag == 18) {
            fragment = new HomeFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                ////Log.e("MainActivity", "Error in creating fragment");
            }
        }


      /*  else if(helper.home==9)
            {
                ivhome.setImageResource(R.mipmap.nav_home);
                ivorder.setImageResource(R.mipmap.nav_order);
                ivfavorites.setImageResource(R.mipmap.nav_fav);
                ivsetting.setImageResource(R.mipmap.nav_setting_active);
                frameLayout.setVisibility(View.VISIBLE);
                ivcash.setImageResource(R.mipmap.nav_cash);
                fragment = new Setting();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }

            }
*/

        else if (helper.home == 1) {
            fragment = new HomeFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                ////Log.e("MainActivity", "Error in creating fragment");
            }
        } else if (helper.favourite_list == 7) {
//            fragment = new Favourite();
            fragment = new NotificationFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                ////Log.e("MainActivity", "Error in creating fragment");
            }
        } else {

            fragment = new HomeFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                ////Log.e("MainActivity", "Error in creating fragment");
            }

        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivhome.setImageResource(R.drawable.nav_home_active);
                ivorder.setImageResource(R.drawable.nav_list);
                ivfavorites.setImageResource(R.drawable.nav_fav);
                ivsetting.setImageResource(R.drawable.nav_setting);
                ivcash.setImageResource(R.drawable.nav_income);
                // frameLayout.setVisibility(View.GONE);
                //fragment = null;
                fragment = new HomeFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    helper.city_to_id_list = "";
                    helper.sort_by = "";
                    helper.service_id.clear();


                } else {
                    ////Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivhome.setImageResource(R.drawable.nav_home);
                ivorder.setImageResource(R.drawable.nav_list_active);
                ivfavorites.setImageResource(R.drawable.nav_fav);
                ivsetting.setImageResource(R.drawable.nav_setting);
                frameLayout.setVisibility(View.VISIBLE);
                ivcash.setImageResource(R.drawable.nav_income);

                if (TextUtils.isEmpty(ssp.getUserId(MainActivity.this))) {
                    helper.login = false;
                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));
                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    helper.login = true;
                    fragment = new OrdersFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        utiltyRequest();

                    }
                }
            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivhome.setImageResource(R.drawable.nav_home);
                ivorder.setImageResource(R.drawable.nav_list);
                ivcash.setImageResource(R.drawable.nav_income_active);
                ivfavorites.setImageResource(R.drawable.nav_fav);
                ivsetting.setImageResource(R.drawable.nav_setting);
                frameLayout.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(ssp.getUserId(MainActivity.this))) {
                    helper.login = false;
                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        }
                    });
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    helper.login = true;
                    fragment = new CashFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                        utiltyRequest();
                    } else {
                        ////Log.e("MainActivity", "Error in creating fragment");
                    }
                }
            }
        });


        /*Favourite is changed to notifications*/
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivhome.setImageResource(R.drawable.nav_home);
                ivorder.setImageResource(R.drawable.nav_list);
                ivfavorites.setImageResource(R.drawable.nav_fav_active);
                ivsetting.setImageResource(R.drawable.nav_setting);
                ivcash.setImageResource(R.drawable.nav_income);
                frameLayout.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(ssp.getUserId(MainActivity.this))) {
                    helper.login = false;

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton(" نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        }
                    });
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    helper.login = true;
//                    fragment = new Favourite();
                    fragment = new NotificationFragment();

                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        utiltyRequest();

                    } else {
                        ////Log.e("MainActivity", "Error in creating fragment");
                    }
                }
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivhome.setImageResource(R.drawable.nav_home);
                ivorder.setImageResource(R.drawable.nav_list);
                ivfavorites.setImageResource(R.drawable.nav_fav);
                ivsetting.setImageResource(R.drawable.nav_setting_active);
                frameLayout.setVisibility(View.VISIBLE);
                ivcash.setImageResource(R.drawable.nav_income);
                /*if (TextUtils.isEmpty(ssp.getUserId(MainActivity.this))) {
                    helper.login=false;

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage("Please Login to continue.");


                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        }
                    });
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                }

                else {
                    helper.login = true;
*/

                fragment = new Setting();
                //helper.home = 9;

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }


            }
        });
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContactList();
            if (helper.phone_list.size() > 0) {

            }
            ////Log.e("33333", "3333333");
        } else {
            requestreadcontact();

            ////Log.e("444444", "444444");
        }


        ReadWriteContact();

        checkForDeepLink(getIntent());


    }



    private void utiltyRequest() {
//        if (page==0) {
        // mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        //  progress_dialog_msg = "loading...";
        //  }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        final ArrayList<New_Order_Entity> order_list = new ArrayList<>();

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }
//                            JSONObject jsonObject=obj.getJSONObject("msg");
//                            JSONArray array=jsonObject.getJSONArray("items");
//                            ////Log.e("SIZE",""+array.length());

                            unread_chat_count = obj.getString("unread_chat_count");
                            online_offline_status = obj.getString("online_offline_status");
                            pending_orders_count_as_customer = obj.getString("pending_orders_count_as_customer");
                            total_order_badge_count = obj.getString("total_order_badge_count");

                            ////Log.e(TAG, "unread_chat_count >>>>>>>>>>" + unread_chat_count);
                            ////Log.e(TAG, "total_order_badge_count >>>>" + total_order_badge_count);

                            if (!TextUtils.isEmpty(total_order_badge_count) && !total_order_badge_count.equals("0")) {
                                helper.badge_txt.setVisibility(View.VISIBLE);
                                helper.badge_txt.setText(total_order_badge_count);
                            }

                            //ADD JAY SETTING VIEW SHOW UNREAD MASSAGE
                            if (!TextUtils.isEmpty(unread_chat_count) && !unread_chat_count.equals("0")) {
                                helper.badge_txt_setting.setVisibility(View.VISIBLE);
                                helper.badge_txt_setting.setText(unread_chat_count);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //  mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

                        //}
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(MainActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(MainActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Showbadgecount");
                params.put("user_id", ssp.getUserId(act));


                ////Log.e("params", params.toString());
                return params;
            }

        };


//
// add(stringRequest);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public void onBackPressed() {

    }

    private void getContactList() {

      /*  ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);*/
        LoadContact loadContact = new LoadContact();
        loadContact.execute();

    }

    public void ReadWriteContact() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                android.Manifest.permission.WRITE_CONTACTS)) {
            //Toast.makeText(MainActivity.this, "Read/Write permission allows us to Access Read/Write contact app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS}, RequestPermissionCode);
        }
    }

    protected void requestreadcontact() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getContactList();

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            cr = getContentResolver();
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));
                    URLogs.m1("Contact Name: " + name);
                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        String final_phone = "";
                        while (pCur.moveToNext()) {

                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            URLogs.m1("phone No: " + phoneNo);

                            final_phone = "";
                            if (phoneNo.startsWith("0")) {
                                final_phone = phoneNo.substring(1, phoneNo.length());
                                //  final_phone = final_phone.replace(" ", "").trim();

                                ////Log.e("final_phone5555", final_phone);

                            } else {
                                final_phone = phoneNo;
                                ////Log.e("final_phone", final_phone);


                            }
                            //URLogs.m1("final_phone: " + final_phone);
                            helper.phone_list.add(final_phone);
                            ////Log.e("phoneNo", helper.phone_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                        }

                        pCur.close();
                    }
                }
            }

            // makeStringReq();
            if (cur != null) {
                cur.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            URLogs.m1("PhoneList: " + (new Gson()).toJson(helper.phone_list));

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkForDeepLink(intent);
    }

    private void checkForDeepLink(Intent intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
                .addOnSuccessListener(result -> {
                    if (result != null) {
                        Uri deepLink = null;
                        String id = "";
                        if (result.getLink() != null) deepLink = result.getLink();
                        if (deepLink != null) id = deepLink.getQueryParameter("provider_id");
                        assert id != null;
                        if (!id.equals("")) {

                            ssp = new SaveSharedPrefernces();

//                            if (helper.login)
//                            if (!ssp.getUserId(MainActivity.this).equals(id))
                            callProviderDetailApi(id);
                            Log.d("Deep Link", id.toString());
//                            Intent intent = new Intent(context, DirectOrderDetails.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            context.startActivity(intent);

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Failure", e.toString());
                });
    }

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;
                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;
                default:
                    break;
            }

            return false;
        }

    });

    private void callProviderDetailApi(String providerId) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.toString());
                    JSONObject providersJSONObject = jsonObject.getJSONObject("providers");


                    if (TextUtils.isEmpty(ssp.getUserId(context))) {
                        helper.login = false;
                        final AlertDialog.Builder alert;
                        if (Build.VERSION.SDK_INT >= 11) {
                            alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                        } else {
                            alert = new AlertDialog.Builder(context);
                        }

                        alert.setMessage(context.getResources().getString(R.string.please_first_login));


                        alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, RegisterActivity.class));


                            }
                        });


                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    } else {

                        helper.login = true;
                        helper.service_list.clear();
                        helper.category_list.clear();
                        helper.category_idlist.clear();
                        helper.service_idlist.clear();


                        Intent intent = new Intent(context, DirectOrderDetails.class);
                        intent.putExtra("review_rating", providersJSONObject.getString("review_rating"));
                        intent.putExtra("provider_id", providersJSONObject.getString("user_id"));
                        intent.putExtra("profile_pic", providersJSONObject.getString("profile_pic_2xthumb_url"));
                        intent.putExtra("name", providersJSONObject.getString("name"));

                        ArrayList<String> service_name_list = new ArrayList<>();
                        ArrayList<String> category_name_list = new ArrayList<>();
                        ArrayList<String> service_id_list = new ArrayList<>();
                        ArrayList<String> category_id_list = new ArrayList<>();

                        JSONArray servicesArray = new JSONArray();
                        servicesArray = providersJSONObject.getJSONArray("services");
                        for (int j = 0; j < servicesArray.length(); j++) {
                            JSONObject json = servicesArray.getJSONObject(j);
                            category_id_list.add(json.getString("main_category_id"));
                            service_id_list.add(json.getString("service_id"));
                            category_name_list.add(json.getString("category_name"));
                            service_name_list.add(json.getString("service_name"));
                        }

                        helper.service_list.addAll(service_name_list);
                        helper.service_idlist.addAll(service_id_list);
                        helper.category_idlist.addAll(category_id_list);
                        helper.category_list.addAll(category_name_list);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();
                    //TODO

                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Providersdetail");
                params.put("providers_id", providerId);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(MainActivity.this);
            }
            progress_dialog.setMessage("يرجى الإنتظار..");
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide progress
    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToNotification() {
        favorites.callOnClick();
    }
}

