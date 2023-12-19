package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.LoadApi;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * progress
 * Created by dev on 12-01-2018.
 */

public class profile extends AppCompatActivity {
    TextView header_txt, txt_km;
    ImageView header_back, header_check, radio_yes, radio_no, on_of, profile_us, btnShareProfile;
    boolean bool = false;
    boolean bool_yes = true;
    boolean on = false;
    String visibile, onli_oflin, distance;
    ProgressBar progress;
    SeekBar seekbar;
    String txt_name, exist_type;
    private String progress_dialog_msg = "", tag_string_req = "string_req", message;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    SaveSharedPrefernces ssp;
    EditText mobile_number, email, city, description, name;
    Bitmap bitmap;
    private static int LOAD_IMAGE_RESULTS = 1000;
    Uri imageUri;
    byte[] picture = null;
    String pic_url, pic_thumb_url, profilepic, pic_thumb, image_url, city_id;
    JSONObject jobj;
    String action = "Uploadprofilepic", user_id, msg = "";
    TextView location, cities;
    private String TAG = "profile";

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        //Log.e(TAG, "****************************");
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        setContentView(R.layout.profile_costraints);
        ssp = new SaveSharedPrefernces();

        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("الملف الشخصي");

        header_check = (ImageView) findViewById(R.id.header_check);
        header_check.setVisibility(View.VISIBLE);
        progress = findViewById(R.id.progress);
//
//        mobile_msg=(TextView)findViewById(R.id.mobile_msg);
//        email_msg=(TextView)findViewById(R.id.email_msg);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        txt_km = (TextView) findViewById(R.id.txt_seek_km);

        btnShareProfile = (ImageView) findViewById(R.id.btnShareProfile);
        btnShareProfile.setVisibility(View.VISIBLE);
        btnShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDynamicLink();
            }
        });

        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.setting = 1;
                Intent i = new Intent(profile.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(i, 0);
            }
        });


        name = (EditText) findViewById(R.id.profile_name);

     /*   int position = name.length();
        Editable etext = name.getText();
        Selection.setSelection(etext, position);*/
//        name.requestFocus();

        name.setSelection(name.getText().toString().length());
        mobile_number = (EditText) findViewById(R.id.profile_number);
        email = (EditText) findViewById(R.id.profile_email);
        location = (TextView) findViewById(R.id.profile_location);
        radio_yes = (ImageView) findViewById(R.id.radio_yes);
        radio_no = (ImageView) findViewById(R.id.radio_no);
        on_of = (ImageView) findViewById(R.id.online_ofline);
        description = (EditText) findViewById(R.id.profile_description);
        profile_us = (ImageView) findViewById(R.id.profile_us);
        cities = (TextView) findViewById(R.id.profile_city);
        cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cities.class));
            }
        });


        ////Log.e("helper city", "" + helper.city_name);
        ////Log.e("city ddddd", "" + ssp.getCity_to_cover(profile.this));

        if (!helper.city_name.equals("")) {
            ////Log.e("helper city", "11111");
            cities.setText(helper.city_name);
            city_id = helper.city_id;


        } else {
            ////Log.e("helper city", "2222222222");
            cities.setText(ssp.getCity_to_cover(profile.this));
            city_id = ssp.getCity_id(profile.this);
        }


//        cities.setText(helper.city_name);


        ////Log.e("help c", helper.city_name);


        header_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile_number.getText().toString().equals("") || mobile_number.getText().toString().length() < 8) {

                    Toast.makeText(profile.this, "يرجى ادخال رقم جوال صحيح", Toast.LENGTH_SHORT).show();
                } else {

                    profile_detail();


                }
            }
        });


        //image upload

        user_id = ssp.getUserId(profile.this);
        profile_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(profile.this, R.style.DialogSlideAnim);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.frnd_custom);

                dialog1.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

                RelativeLayout takepicture = (RelativeLayout) dialog1.findViewById(R.id.gallery_rl);
                RelativeLayout uploadfile = (RelativeLayout) dialog1.findViewById(R.id.camera_rl);
                RelativeLayout cancel_rl = (RelativeLayout) dialog1.findViewById(R.id.cancel_rl);
                RelativeLayout remove_rl = (RelativeLayout) dialog1.findViewById(R.id.remove_rl);

                remove_rl.setVisibility(View.VISIBLE);

                cancel_rl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();

                    }


                });

                remove_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        image_url = ssp.getKEY_profile_pic_thumb_url(profile.this);
                        if (image_url.equals("")) {


                            final AlertDialog.Builder alert;
                            if (Build.VERSION.SDK_INT >= 11) {
                                alert = new AlertDialog.Builder(profile.this, AlertDialog.THEME_HOLO_LIGHT);
                            } else {
                                alert = new AlertDialog.Builder(profile.this);
                            }

                            alert.setMessage(getResources().getString(R.string.msg_no_profile_pic_available_to_remove));


                            alert.setPositiveButton("نعم ", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //prefs= getSharedPreferences(prefname, MODE_PRIVATE);

                                    dialog1.dismiss();
                                }
                            });


                            Dialog dialog = alert.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();


                        } else {


                            final AlertDialog.Builder alert;
                            if (Build.VERSION.SDK_INT >= 11) {
                                alert = new AlertDialog.Builder(profile.this, AlertDialog.THEME_HOLO_LIGHT);
                            } else {
                                alert = new AlertDialog.Builder(profile.this);
                            }

                            alert.setMessage(getResources().getString(R.string.msg_do_you_want_to_remove_this_picture));


                            alert.setPositiveButton(" نعم", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //prefs= getSharedPreferences(prefname, MODE_PRIVATE);
                                    remove_pic();
                                    dialog1.dismiss();


                                }
                            });

                            alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //prefs= getSharedPreferences(prefname, MODE_PRIVATE);

//                                finish();
                                    dialog.dismiss();
                                }
                            });
                            Dialog dialog = alert.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();


                        }
                    }
                });

                takepicture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ACCESS_REQUST = 1;
                        checkAndroidVersion();
                        dialog1.dismiss();

                    }
                });

                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ACCESS_REQUST = 2;
                        checkAndroidVersion();
                        dialog1.dismiss();

                    }
                });


            }


        });


        name.setText(ssp.getName(profile.this));
        name.setSelection(name.getText().length());


        mobile_number.setText((ssp.getMobile_number(profile.this)));
        email.setText(ssp.getEmail(profile.this));
        location.setText(ssp.getLocation(profile.this));


        description.setText(ssp.getDescription(profile.this));

        helper.latitude = ssp.getLatitude(profile.this);
        helper.longitude = ssp.getLongitude(profile.this);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.location_set = 1;
                startActivity(new Intent(getApplicationContext(), MapView_Activity.class));
            }
        });

        Intent i = getIntent();
        String map_loc = i.getStringExtra("location");

        if (map_loc != null) {
            location.setText(map_loc);
        } else {
            location.setText(ssp.getLocation(profile.this));
        }


        visibile = ssp.getMobile_visible(profile.this);
        onli_oflin = ssp.getOnline_offline_status(profile.this);

        //  Radio switch yes/no


        if (ssp.getMobile_visible(profile.this).equals("Yes")) {
            radio_yes.setImageResource(R.mipmap.radio_checked);

            bool = true;

        } else {
            radio_no.setImageResource(R.mipmap.radio_checked);
            bool = false;

        }


        radio_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool) {
                    radio_no.setImageResource(R.mipmap.radio_checked);
                    radio_yes.setImageResource(R.mipmap.radio);
                    visibile = "No";

                }
                bool = !bool;
            }
        });

        radio_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bool_yes) {
                    radio_no.setImageResource(R.mipmap.radio);
                    radio_yes.setImageResource(R.mipmap.radio_checked);
                    visibile = "Yes";
                }
                bool_yes = !bool_yes;
            }


        });


        if (ssp.getOnline_offline_status(profile.this).equals("1")) {

            on_of.setImageResource(R.mipmap.switchon);
            bool = true;

        } else {
            on_of.setImageResource(R.mipmap.switchoff);
            bool = false;
        }


        on_of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (on) {
                    on_of.setImageResource(R.mipmap.switchoff);
                    onli_oflin = "0";
                } else {
                    on_of.setImageResource(R.mipmap.switchon);
                    onli_oflin = "1";
                }
                on = !on;
            }
        });


        // seek bar

        distance = ssp.getDistance(profile.this);
        txt_km.setText(ssp.getDistance(profile.this) + " " + "كيلو");
        seekbar.setProgress(Integer.parseInt(ssp.getDistance(profile.this)));


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                txt_km.setText(+progressChangedValue + " " + "");
                distance = String.valueOf(progressChangedValue);


            }
        });


        image_url = ssp.getKEY_profile_pic_thumb_url(profile.this);
        ////Log.e("hhhhhhhh", image_url);

       /* if (!image_url.equals("")){
            Picasso.with(profile.this)
                    .load(image_url)
                    .noFade()
                    .error(R.mipmap.nouser_2x)
                    .into(profile_us);
        }
        else if(image_url.equals("")){
            profile_us.setImageResource(R.mipmap.nouser_2x);
        }*/

        if (!image_url.equals("")) {
            Glide.with(profile.this)
                    .load(image_url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(profile_us);

        } else if (image_url.equals("")) {
            profile_us.setImageResource(R.mipmap.nouser_2x);
        }

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

                case LOAD_QUESTION_SUCCESS:

                    break;

                default:
                    break;
            }

            return false;
        }

    });


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    public void cameraIntent() {


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = null;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 7);
    }

    public void galleryIntent() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);

    }


    private int PERMISSIONS_MULTIPLE_REQUEST = 5;
    int ACCESS_REQUST;

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(profile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(profile.this, android.Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(profile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(profile.this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 5:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile && writeExternalFile) {

                        if (ACCESS_REQUST == 1) {
                            cameraIntent();
                        } else if (ACCESS_REQUST == 2) {
                            galleryIntent();
                        }

                    } else {
                        Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");

            // user_profile_img.setImageBitmap(bitmap);
            loadData();

        }
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (null != selectedImageUri) {
                String path = getPathFromURI(selectedImageUri);
                if (path != null)
                    bitmap = getBitmapFromURL(path);
            }
        }

    }


    private AsyncLoadData asyncLoad;

    private void loadData() {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            asyncLoad.execute();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor == null) {
            res = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }


        return res;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @Override
        protected Void doInBackground(String... strings) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = getResources().getString(R.string.loading);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

//            if (imagename.equals("profile")) {
            picture = baos.toByteArray();
//            } else {
//
//                coverpicture = baos.toByteArray();
//            }


//            byte[] imageBytes = baos.toByteArray();

            LoadApi api = new LoadApi();

            try {
                if (picture != null) {
                    ////Log.e("action", "" + action);
                    ////Log.e("user_id", "" + user_id);
                    ////Log.e("55555", "" + picture);
//                    ////Log.e("pic_type", "" + pic_type);
//                    if (imagename.equals("profile")) {
//                        pic_type = "profile";

                    jobj = api.Action_profileSetting_Image(action, user_id, picture);

//                    }
                }
//                else if (picture==null||coverpicture==null) {
//                    jobj = api.Action_profileSetting_noImage(action, user_id, picture,pic_type);
//                }
                JSONObject object = api.getResult1();
                ////Log.e("Profile_Setting", ">>" + object);

                msg = object.getString("msg");
                ////Log.e("Meassage", "  " + msg);
                if (object.has("pic_url") && object.has("pic_thumb_url")) {
                    pic_url = object.getString("pic_url");
                    ////Log.e("pic_url", ">>" + pic_url);
                    pic_thumb_url = object.getString("pic_thumb_url");
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (msg.equals("Your picture has been uploaded successfully")) {


               /* Picasso.with(profile.this)
                        .load(pic_url)
                        .noFade()
                        .error(R.mipmap.nouser_2x)
                        .into(profile_us);*/

                if (!pic_url.equals("")) {

                    Glide.with(profile.this)
                            .load(pic_url)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(profile_us);

                } else if (pic_url.equals("")) {
                    profile_us.setImageResource(R.mipmap.nouser_2x);
                }

                ssp.setKEY_profile_pic_thumb_url(profile.this, pic_url);


                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            }

            super.onPostExecute(aVoid);
        }
    }


    public void profile_detail() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "Please wait ...";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");
                            ////Log.e("response", response.toString());
                            //Log.e(TAG, "*************Profilesetting response***************" + response.toString());


                            if (message.equals("Profile updated")) {

                                JSONObject jarray = obj.getJSONObject("user_details");
                                String nam = jarray.getString("name");
                                ssp.setName(profile.this, nam);

                                String em = jarray.getString("email_address");
                                ssp.setEmail(profile.this, em);

                                String m_num = jarray.getString("mobile_number");
                                ssp.setMobile_number(profile.this, m_num);

                                String m_visible = jarray.getString("mobile_visible");
                                ssp.setMobile_visible(profile.this, m_visible);

                                String of = jarray.getString("online_offline_status");
                                ssp.setOnline_offline_status(profile.this, of);

                                String des = jarray.getString("description");
                                ssp.setDescription(profile.this, des);

                                String dist = jarray.getString("distance");
                                ssp.setDistance(profile.this, dist);

                                String locat = jarray.getString("location");
                                ssp.setLocation(profile.this, locat);
                                String cit = jarray.getString("city_to_cover");
                                ssp.setCity_to_cover(profile.this, cit);
                                ////Log.e("city deb svae", ssp.getCity_to_cover(profile.this));


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(profile.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(profile.this);
                                }

                                alert.setMessage("تم تحديث الملف الشخصي");


                                alert.setPositiveButton(" نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.putExtra("flag", 13);
                                        startActivity(i);

                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            } else if (message.equals("Profile updated. Mobile number already existed with another account")) //ADD JAY
                            {

                            } else if (message.equals("Mobile number already existed with another account")) {

                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(profile.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(profile.this);
                                }

                                alert.setMessage(getResources().getString(R.string.msg_mobile_already_exists));


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(profile.this, profile.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivityForResult(i, 0);


                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();


                            } else if (message.equals("Email address already existed with another account")) {

                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(profile.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(profile.this);
                                }

                                alert.setMessage(getResources().getString(R.string.msg_email_already_exists));


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(profile.this, profile.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivityForResult(i, 0);


                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();


                            }


//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(profile.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(profile.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(profile.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(profile.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Profilesetting");
                params.put("user_id", ssp.getUserId(profile.this));
                params.put("name", name.getText().toString());
                params.put("email_address", email.getText().toString());
                params.put("mobile_number", mobile_number.getText().toString());
                params.put("mobile_visible", visibile);
                params.put("online_offline_status", onli_oflin);
                params.put("description", description.getText().toString());
                params.put("distance", distance);
                params.put("location", location.getText().toString());
                params.put("city_to_cover", city_id);


                ////Log.e("params", params.toString());
                //Log.e(TAG, "*************Profilesetting params***************" + params.toString());


                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }


    public void Email_mobile_existance_check() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");
                            ////Log.e("response", response.toString());


                            if (message.equals("Mobile already exist, Please enter another.")) {

//                                mobile_msg.setVisibility(View.VISIBLE);
//                                mobile_msg.setTextColor(R.color.red);
//                                mobile_msg.setText("Mobile already exist, Please enter another.");


                            } else if (message.equals("Mobile available to use.")) {

//                                email_msg.setVisibility(View.VISIBLE);
//                                email_msg.setTextColor(R.color.green);
//                                email_msg.setText("Mobile available to use.");


                            }


//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(profile.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(profile.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(profile.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(profile.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (exist_type.equals("email")) {

                    params.put("action", "Emailmobileexistencecheck");
                    params.put("user_id", ssp.getUserId(profile.this));
                    params.put("email_mobile", email.getText().toString());
                    params.put("content_type", "Email");

                } else if (exist_type.equals("mobile")) {


                    params.put("action", "Emailmobileexistencecheck");
                    params.put("user_id", ssp.getUserId(profile.this));
                    params.put("email_mobile", mobile_number.getText().toString());
                    params.put("content_type", "Mobile");
                    params.put("calling_code", "");

                }

                ////Log.e("Email_mobile_check", params.toString());

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    public void remove_pic() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ////Log.e("Data", "<><>" + response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            ////Log.e("Data", "<><>" + obj.toString());
                            String message = obj.getString("msg");
                            ////Log.e("msg", "==" + message);

                            if (message.equals("Image removed successfully")) {

                                JSONObject jarray = obj.getJSONObject("user_details");

                                if (obj.has("user_details")) {
                                    pic_thumb = jarray.getString("profile_pic_thumb_url");

                                    ssp.setKEY_profile_pic_thumb_url(profile.this, pic_thumb);
                                }
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(profile.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(profile.this);
                                }

                                alert.setMessage(getResources().getString(R.string.msg_image_removed_successfully));


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //prefs= getSharedPreferences(prefname, MODE_PRIVATE);
                                        //helper.setting=1;
                                        Intent i = new Intent(profile.this, profile.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivityForResult(i, 0);

                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Removeimage");

                params.put("user_id", ssp.getUserId(profile.this));


                ////Log.e("params", "===" + params.toString());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void cities() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");
                            ////Log.e("response", response.toString());


//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(profile.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(profile.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(profile.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(profile.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("action", "Cities");


                ////Log.e("Email_mobile_check", params.toString());

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
                progress_dialog = new ProgressDialog(profile.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(profile.this);
            }
            progress_dialog.setMessage(progress_dialog_msg);
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


    private void createDynamicLink() {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.tameed.net")
                .appendQueryParameter("provider_id", ssp.getUserId(profile.this));

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(builder.build())
                .setDomainUriPrefix("https://tameedapp.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.tameed").build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(getResources().getString(R.string.app_name))
                                .setDescription(description.getText().toString())
                                .setImageUrl(Uri.parse(ssp.getKEY_profile_pic_thumb_url(profile.this)))
                                .build())
                .setNavigationInfoParameters(new DynamicLink.NavigationInfoParameters.Builder().setForcedRedirectEnabled(true).build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLinkUri)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            String shareBody = shortLink.toString();
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(
                                    Intent.createChooser(
                                            sharingIntent,
                                            "Select One option"
                                    )
                            );
                            Log.d("Failure", "");


                        } else {
                            Toast.makeText(profile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}