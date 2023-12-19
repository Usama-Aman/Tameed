package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Adapter.Spo_Adapter;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Entity.Category_Entity_Public;
import tameed.com.tameed.Entity.Service_Entity_Public;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.GPSTracker;
import tameed.com.tameed.Util.LoadApi;
import tameed.com.tameed.Util.MarshMallowPermission;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import static tameed.com.tameed.MapView_Activity.RequestPermissionCode;

public class SendPublicOrder extends AppCompatActivity implements View.OnClickListener {

    private TextView tetHeader, txtSubmit;
    private ImageView imgBackHeader, spo_add_img;
    ConstraintLayout imgDatePicker, imgTimePicker;
    TextView txtTime, spo_cat, spo_service;
    static TextView txtDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText spo_des, sop_warranty_days;
    Spo_Adapter spo_adapter;
    Bitmap bitmap;
    private static int LOAD_IMAGE_RESULTS = 1000;

    RecyclerView recyclerView;
    String category_name, category_id, category_id2;
    ArrayList<Bitmap> add_img_detail = new ArrayList<>();
    String path_img = "", pic_status;
    Uri imageUri;
    ProgressDialog dlg = null;
    Bitmap myBitmap, myBitma, rotatedBitmap;
    byte[] byte_arr = null;

    String[] sop_category;
    String[] sop_category_id;
    String[] sop_category_id2;
    String[] sop_service;
    String[] sop_service_id;
    String order_id = "", service_name, service_id;

    SaveSharedPrefernces ssp;
    Activity act;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    int page = 0, curSize;
    String result_count = "";

    String refresh = "", chatCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    ArrayList<byte[]> image_list = new ArrayList<>();
    int flagValue = 0;
    static String selected_time, selected_date;


    List<Address> addresses;
    Double latis, longis;
    String longitude, latitude;
    String no_people, date, time, city, address = "", comment, address1 = "", state = "", device_id, msg = "";
    Geocoder geocoder;
    private String TAG = "SendPublicOrder";

    private MarshMallowPermission marshMallowPermission;

    private int REQUEST_CAMERA = 1;
    private int REQUEST_GALLERY = 0;
    private int PERMISSIONS_MULTIPLE_REQUEST = 5;
    int ACCESS_REQUST;


    //ADD JAY
    private static int AddWarranty_days = 0;

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


        marshMallowPermission = new MarshMallowPermission(SendPublicOrder.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!marshMallowPermission.checkPermissionForAccessCoarseLoc() || !marshMallowPermission.checkPermissionForAccessFineLoc()) {
                marshMallowPermission.requestPermissionForLocation();
            }
        }


        setContentView(R.layout.activity_send_public_order);
        ssp = new SaveSharedPrefernces();
        helper.image_list.clear();
        act = SendPublicOrder.this;
        tetHeader = (TextView) findViewById(R.id.txt_header);
        txtSubmit = (TextView) findViewById(R.id.text_submit_send_public_order);
        imgBackHeader = (ImageView) findViewById(R.id.header_back);
        ////Log.e(TAG, "****************************");
        tetHeader.setText("معاملة عام");

        imgBackHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();


            }
        });
        spo_des = (EditText) findViewById(R.id.spo_des);

        sop_warranty_days = (EditText) findViewById(R.id.sop_warranty_days);
        recyclerView = (RecyclerView) findViewById(R.id.spo_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        spo_adapter = new Spo_Adapter(this, add_img_detail);

        recyclerView.setAdapter(spo_adapter);
        spo_cat = (TextView) findViewById(R.id.sop_txt_categ);
        spo_service = (TextView) findViewById(R.id.sop_txt_service);
        spo_add_img = (ImageView) findViewById(R.id.spo_add_img);
        txtDate = (TextView) findViewById(R.id.spo_txt_date);
        imgDatePicker = (ConstraintLayout) findViewById(R.id.spo_con_date);
        imgDatePicker.setOnClickListener(this);
        //REMOVE JIGS
        //EnableRuntimePermission();
        GPSTracker gpsTracker = new GPSTracker(act);

        longitude = String.valueOf(gpsTracker.getLongitude());
        ////Log.e("longitude", "" + longitude);
        latitude = String.valueOf(gpsTracker.getLatitude());
        ////Log.e("latitude", "" + latitude);

        latis = Double.parseDouble(latitude);
        longis = Double.parseDouble(longitude);

        geocoder = new Geocoder(act, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latis, longis, 1);

           /* if (addresses.isEmpty()) {
                Toast.makeText(Member_login.this,"waiting for the location", Toast.LENGTH_SHORT).show();


            }

            else {*/
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
//	    							zip = addresses.get(0).getPostalCode();
//	    							////Log.e("zip","zip>>>" +zip);
                address1 = address + "," + city + "," + state;
                ////Log.e("Address", "===" + address1);
//	    							createaccount_zip_edtxt.setText(zip);
                //  txt_location.setText(address1);
            }
            //  }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        spo_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagValue = 0;
                makeStringReq();
            }
        });

        spo_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(category_id)) {
                    Toast.makeText(act, "يرجى اختيار الخدمة الأساسية", Toast.LENGTH_SHORT).show();
                } else {
                    flagValue = 1;
                    makeStringReq();
                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(SendPublicOrder.this);
//                builder.setTitle("Service");
//                builder.setItems(sop_service, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int position) {
//                        //here you can use like this... str[position]
//
//                        spo_service.setText(sop_service[position].toString());
//
//                    }
//
//                });
//                Dialog dialog = builder.create();
//                dialog.show();
            }
        });

        spo_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog1 = new Dialog(SendPublicOrder.this, R.style.DialogSlideAnim);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.frnd_custom);

                dialog1.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

                RelativeLayout takepicture = (RelativeLayout) dialog1.findViewById(R.id.gallery_rl);
                RelativeLayout uploadfile = (RelativeLayout) dialog1.findViewById(R.id.camera_rl);
                RelativeLayout cancel_rl = (RelativeLayout) dialog1.findViewById(R.id.cancel_rl);
                RelativeLayout remove_rl = (RelativeLayout) dialog1.findViewById(R.id.remove_rl);
                remove_rl.setVisibility(View.GONE);
                cancel_rl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();

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


               /* takepicture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = null;
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 1);
                        // dialog1.dismiss();
                        dialog1.dismiss();

                    }
                });

                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 0);
                        dialog1.dismiss();

                    }
                });*/


            }
        });


        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(category_id)) {
                    Toast.makeText(act, "يرجى اختيار الخدمة الأساسية", Toast.LENGTH_SHORT).show();
                    ////Log.e(TAG, "category_id*******يرجى اختيار الخدمة الأساسية>");
                } else if (TextUtils.isEmpty(service_id)) {
                    Toast.makeText(act, "يرجى ادخال الوصف", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(spo_des.getText().toString())) {
                    Toast.makeText(act, "يرجى ادخال مدة التعميد", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sop_warranty_days.getText().toString()) || sop_warranty_days.getText().toString().equals("0")) {
                    Toast.makeText(act, "مدة التعميد يجيب ان تكون اكثر من 0", Toast.LENGTH_SHORT).show();
                    ////Log.e(TAG, "sop_warranty_days************مدة التعميد يجيب ان تكون اكثر من 0**************>");
                } else if (TextUtils.isEmpty(selected_date)) {
                    Toast.makeText(act, "يرجى ادخال يوم الخدمة", Toast.LENGTH_SHORT).show();
                    ////Log.e(TAG, "selected_date************يرجى ادخال يوم الخدمة**************>");
                } else if (TextUtils.isEmpty(selected_time)) {
                    Toast.makeText(act, "يرجى اختيار الوقت", Toast.LENGTH_SHORT).show();
                } else {
                    loadData();
                }


            }
        });

    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = null;
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
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

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(SendPublicOrder.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(SendPublicOrder.this, android.Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(SendPublicOrder.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SendPublicOrder.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SendPublicOrder.this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(SendPublicOrder.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null && requestCode == REQUEST_GALLERY) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path_img = cursor.getString(columnIndex);
                    ////Log.e(TAG, "******path_img******" + path_img);
                    cursor.close();
                    new AsyncReceiverTask().execute();
                }
                break;

            case 1:
                if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
                    ContentResolver cr = getContentResolver();
                    Cursor metaCursor = cr.query(imageUri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
                    if (metaCursor != null) {
                        try {
                            if (metaCursor.moveToFirst()) {
                                path_img = metaCursor.getString(0);
                                ////Log.e(TAG, "******cam path_img******" + path_img);
                            }
                            new AsyncReceiverTask().execute();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            imageUri = null;
                            path_img = "";
                        } finally {
                            metaCursor.close();
                        }
                    }
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
        }
    }


    class AsyncReceiverTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= 17) {
                dlg = new ProgressDialog(act, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                dlg = new ProgressDialog(act);
            }
            dlg.setMessage("loading..");
            dlg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (myBitmap != null) {
                myBitmap.recycle();
                myBitmap = null;
            }
            if (myBitma != null) {
                myBitma.recycle();
                myBitma = null;
            }

            try {

                BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                bfOptions.inJustDecodeBounds = false;
                bfOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                bfOptions.inDither = true;

                File file = new File(path_img);
                ////Log.e(TAG, "******file******" + file);
                ////Log.e(TAG, "******path_img******" + path_img);

                FileInputStream fs = null;
                if (file.exists()) {

                    try {
                        fs = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ////Log.e("Camera", "*******Image Picker PATH::::" + path_img);
                    try {
                        if (fs != null) {
                            myBitma = BitmapFactory.decodeFile(file.getAbsolutePath(), bfOptions);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            if (myBitma != null) {
                                //myBitma.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                                myBitma.compress(Bitmap.CompressFormat.JPEG, 70, bao); //ADD JIGS
                                myBitmap = getResizedBitmap(myBitma, 400, 400);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        myBitma.recycle();
                        myBitma = null;

                    } finally {
                        if (fs != null) {
                            try {
                                fs.close();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }

                    ////Log.e("Bitmap", "******Actual bitmap item::Width:" + myBitmap.getWidth() + "   Height:" + myBitmap.getHeight());
                    ////Log.e("setting", "*****img on bitmap from camera");
                    rotatedBitmap = gettingRotatedBitmap(myBitmap, path_img);
                    ////Log.e("Bitmap", "******Rotaated item::Width:" + rotatedBitmap.getWidth() + "   Height:" + rotatedBitmap.getHeight());
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    //rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao); //ADD JIGS
                    ////Log.e("inside", "******camera");

                    //                imgresume = path_img;
                    byte_arr = bao.toByteArray();
                    add_img_detail.add(rotatedBitmap);

                    image_list.add(bao.toByteArray());
                    helper.image_list.add(byte_arr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (dlg.isShowing() && dlg != null) {
                dlg.dismiss();
            } else {
                ////Log.e("SendPublicOrder", "okk");
            }
            try {

                ////Log.e("load", "*****photo upload:" + rotatedBitmap.getHeight() + "   width:" + rotatedBitmap.getWidth());
                //myprofile_img_photo.setImageBitmap(rotatedBitmap);
                //carde_pic.setImageBitmap(rotatedBitmap);
                //setcircularimage(rotatedBitmap);

                spo_adapter.notifyDataSetChanged();
                // helper.company_logo=rotatedBitmap;
                //counter = "photo";

            } catch (Exception e) {
                ////Log.e("SendPublicOrder", e.getMessage() + "okk");
                e.printStackTrace();
            }


        }


        protected Bitmap gettingRotatedBitmap(Bitmap bitmap_leftn, String path) {
            Bitmap rotatedBitmap = null;

            int width = bitmap_leftn.getWidth();
            int height = bitmap_leftn.getHeight();


            ExifInterface exif;
            try {
                exif = new ExifInterface(path);
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);


                ////Log.e("bitmap", "*******11 width:"
                      //  + width + "   height:" + height + "   filepath:" + path);

               // ////Log.e("ROTATION", "ORITATION::::" + rotation
                      //  + "  rotation 90:" + ExifInterface.ORIENTATION_ROTATE_90
                       // + "  rotation 180:" + ExifInterface.ORIENTATION_ROTATE_180 +
                       // "   rotation 270:" + ExifInterface.ORIENTATION_ROTATE_270);
                //if(width > height){
//
                ////Log.e("bitmap", "********22 width:" + width + "   height:" + height);
                Matrix matrix = new Matrix();

                switch (rotation) {
                    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                        matrix.setScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.setRotate(180);
                        break;

                    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                        matrix.setRotate(180);
                        matrix.postScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_TRANSPOSE:
                        matrix.setRotate(90);
                        matrix.postScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        ////Log.e("bitmap", "SWITCH MATRIX:");
                        matrix.setRotate(90);
                        break;

                    case ExifInterface.ORIENTATION_TRANSVERSE:
                        matrix.setRotate(-90);
                        matrix.postScale(-1, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.setRotate(-90);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        break;
                }
                ////Log.e("bitmap", "*******33111 width:" + width + "   height:" + height);

                rotatedBitmap = Bitmap.createBitmap(bitmap_leftn, 0, 0, 400, 400, matrix, true);

                ////Log.e("bitmap", "*******33222 width:" + rotatedBitmap.getWidth() + "   height:" + rotatedBitmap.getHeight());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return rotatedBitmap;
        }


        private void setcircularimage(Bitmap myBitmap) {
            Bitmap newBItmap = getResizedBitmap(myBitmap, 450, 450);
            Bitmap circleBitmap = Bitmap.createBitmap(newBItmap.getWidth(), newBItmap.getHeight(), Bitmap.Config.ARGB_8888);

            BitmapShader shader = new BitmapShader(newBItmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(newBItmap.getWidth() / 2, newBItmap.getHeight() / 2, newBItmap.getWidth() / 2, paint);


            ////Log.e("11111111111111111", "1111111111111");
//                imgresume = path_img;
     /*   photo_image1_add.setImageBitmap(circleBitmap);
        photo_image2_add.setImageBitmap(circleBitmap);
        photo_image3_add.setImageBitmap(circleBitmap);*/


        }

        public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
            ////Log.e("SellItemsNew", "GET_RESIZED_BITMAP:");
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;
        }


    }


    @Override
    public void onClick(View v) {

        if (v == imgDatePicker) {

            //AddWarranty_days = Integer.parseInt(sop_warranty_days.getText().toString());
            //////Log.e(TAG,"PASS ENTER WARRANTY DAY TO CALANDER>>>>>>>"+AddWarranty_days);


//            // Get Current Date
//            final Calendar c = Calendar.getInstance();
//            mYear = c.get(Calendar.YEAR);
//            mMonth = c.get(Calendar.MONTH);
//            mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                    new DatePickerDialog.OnDateSetListener() {
//
//                        @Override
//                        public void onDateSet(DatePicker view, int year,
//                                              int monthOfYear, int dayOfMonth) {
//
//                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                        }
//                    }, mYear, mMonth, mDay);
//            datePickerDialog.show();

            showTruitonTimePickerDialog(v);
            showTruitonDatePickerDialog(v);


        }


    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
//
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Time t = new Time(hourOfDay, minute, 0);
            Format formatter;
            formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String time = formatter.format(t);

            txtDate.setText(txtDate.getText().toString() + "    " + time);
            selected_time = time;


        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            selected_time = "";
        }
    }


    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            Date startDate = null;


            c.add(Calendar.DATE, 0);

            //TODO ADD JAY IF O THEN SELECT CURRENT DATE ELSE AddWarranty_days
            //c.add(Calendar.DATE, AddWarranty_days);

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
            DatePicker dp = d.getDatePicker();
            dp.setMinDate(c.getTimeInMillis());
            // d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);

            return d;


        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
//if (TextUtils.isEmpty(selected_time)) {
            txtDate.setText(year + "-" + (month + 1) + "-" + day);
            selected_date = txtDate.getText().toString();
//}
//else {
//    selected_date=year + "-" + (month + 1) + "-" + day;
//    txtDate.setText(year + "-" + (month + 1) + "-" + day + "    " + txtDate.getText().toString());
//}

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            selected_date = "";
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


    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                progress_dialog = new ProgressDialog(act, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(act);
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

    private void makeStringReq() {
//        if (page==0) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);
        //  }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);


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
                            if (flagValue == 0) {
                                ArrayList<Category_Entity_Public> category_list = new ArrayList<>();
                                JSONArray jsonArray = obj.getJSONArray("categories");

                                ArrayList<String> category_name_list = new ArrayList<>();
                                ArrayList<String> category_id_list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Category_Entity_Public entity_public = new Category_Entity_Public();
                                    entity_public.setCategory_id(object.getString("category_id"));
                                    category_id_list.add(object.getString("category_id"));
                                    entity_public.setCategory_name(object.getString("category_name"));
                                    category_name_list.add(object.getString("category_name"));
                                    entity_public.setType(object.getString("type"));
                                    entity_public.setIs_enable(object.getString("is_enable"));
                                    entity_public.setAdded_date(object.getString("added_date"));
                                    category_list.add(entity_public);

                                }

                                sop_category = new String[category_name_list.size()];
                                sop_category = category_name_list.toArray(sop_category);

                                sop_category_id = new String[category_id_list.size()];
                                sop_category_id = category_id_list.toArray(sop_category_id);


                                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                                builder.setTitle(getResources().getString(R.string.alert_select_category));
                                builder.setItems(sop_category, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {
                                        //here you can use like this... str[position]
                                        category_name = sop_category[position].toString();
                                        category_id = sop_category_id[position].toString();
                                        //  category_id2=sop_category_id[position].toString();
                                        ////Log.e("Category_id2", "=====" + sop_category_id[position].toString());
                                        ////Log.e("Category_Name", "=====" + category_name);
                                        ////Log.e("Category_ID", "=====" + category_id);

                                        spo_cat.setText(sop_category[position].toString());
                                        if (TextUtils.isEmpty(category_id2)) {
                                            spo_service.setText("");
                                            service_id = "";
                                            service_name = "";
                                        } else {
                                            if (!category_id.equals(category_id2)) {
                                                spo_service.setText("");
                                                service_id = "";
                                                service_name = "";
                                            }
                                        }

                                    }

                                });
                                Dialog dialog = builder.create();
                                dialog.show();


                            } else if (flagValue == 1) {
                                ArrayList<Service_Entity_Public> service_list = new ArrayList<>();
                                ArrayList<String> service_name_list = new ArrayList<>();
                                ArrayList<String> service_id_list = new ArrayList<>();
                                ArrayList<String> category_id_list = new ArrayList<>();
                                JSONArray jsonArray = obj.getJSONArray("services");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Service_Entity_Public entity_public = new Service_Entity_Public();
                                    entity_public.setService_id(object.getString("service_id"));
                                    service_id_list.add(object.getString("service_id"));
                                    entity_public.setCategory_id(object.getString("category_id"));
                                    category_id_list.add(object.getString("category_id"));
                                    entity_public.setService_name(object.getString("service_name"));
                                    service_name_list.add(object.getString("service_name"));
                                    entity_public.setType(object.getString("type"));
                                    entity_public.setIs_enable(object.getString("is_enable"));
                                    entity_public.setAdded_date(object.getString("added_date"));
                                    service_list.add(entity_public);
                                }


                                sop_service_id = new String[service_id_list.size()];
                                sop_service_id = service_id_list.toArray(sop_service_id);

                                sop_service = new String[service_name_list.size()];
                                sop_service = service_name_list.toArray(sop_service);

                                sop_category_id2 = new String[category_id_list.size()];
                                sop_category_id2 = category_id_list.toArray(sop_category_id2);


                                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                                builder.setTitle(getResources().getString(R.string.alert_select_services));
                                builder.setItems(sop_service, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {
                                        //here you can use like this... str[position]
                                        service_name = sop_service[position].toString();
                                        service_id = sop_service_id[position].toString();
                                        category_id2 = sop_category_id2[position].toString();
                                        ////Log.e("Category_id2", "=====" + sop_category_id2[position].toString());
                                        ////Log.e("Category_Name", "=====" + category_name);
                                        ////Log.e("Category_ID", "=====" + category_id);

                                        spo_service.setText(sop_service[position].toString());

                                    }

                                });
                                Dialog dialog = builder.create();
                                dialog.show();


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

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

                        //}
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(SendPublicOrder.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(SendPublicOrder.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(SendPublicOrder.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(SendPublicOrder.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (flagValue == 0) {
                    params.put("action", "Categories");


                    // params.put("service_fee",service_fee);

                    ////Log.e("params", params.toString());
                } else if (flagValue == 1) {
                    params.put("action", "Services");
                    params.put("category_id", category_id);
                    //     params.put("page", String.valueOf(page));

                    // params.put("service_fee",service_fee);

                    ////Log.e("params", params.toString());
                }
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


    private AsyncLoadData asyncLoad;

    private void loadData() {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            asyncLoad.execute();
        }
    }

    class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @Override
        protected Void doInBackground(String... params) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = getResources().getString(R.string.loading);
            LoadApi api = new LoadApi();

            // ////Log.e(TAG,"*******helper.image_list********>"+helper.image_list);

            api.Action_PublicOrder_Image("Placepublicorder", ssp.getUserId(act), helper.image_list, "", category_id, service_id, spo_des.getText().toString(),
                    selected_time, selected_date, sop_warranty_days.getText().toString(), latitude, longitude);
            //  ////Log.e("JSON_OBJECT","===="+json);
            //   {"msg":"Your order has been submitted successfully","order_id":4}


            JSONObject object = api.getResult1();

            ////Log.e("JSON_OBJECT1", "====" + object);
            try {
                msg = object.getString("msg");
                order_id = object.getString("order_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);


            try {
                if (msg.equals("Your order has been submitted successfully")) {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 17) {
                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(act);
                    }
                    // alert.setTitle("Sorry");
                    //  alert.setMessage("تم ارسال معاملةك إلى المكتب العام بنجاح");
                    alert.setMessage(getResources().getString(R.string.msg_request_sent_successfully_to_general_office));
                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            helper.service_list.clear();
                            helper.category_list.clear();
                            helper.category_idlist.clear();
                            helper.service_idlist.clear();
                            helper.pblc = 0;
                            Intent intent = new Intent(SendPublicOrder.this, Order_detail.class);
                            intent.putExtra("order_id", order_id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    });

                    try {
                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 17) {
                        alert = new AlertDialog.Builder(act, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(act);
                    }
                    // alert.setTitle("Sorry");
                    alert.setMessage("هناك خطأ ما");
                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    try {
                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SendPublicOrder.this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(SendPublicOrder.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(SendPublicOrder.this, new String[]
                    {
                            //android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE
                            android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, RequestPermissionCode);
        }
    }


}
