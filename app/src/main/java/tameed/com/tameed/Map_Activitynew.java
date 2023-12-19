//package tameed.com.tameed;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.pm.PackageManager;
//import android.graphics.Typeface;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkError;
//import com.android.volley.NoConnectionError;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.ServerError;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import tameed.com.tameed.Util.Apis;
//import tameed.com.tameed.Util.SaveSharedPrefernces;
//import tameed.com.tameed.Util.helper;
//
////import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//
//public class Map_Activity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener, OnMapReadyCallback {
//    String currentlongitude2, currentlatitude2;
//    Marker startPerc;
//    LatLng coordinate;
//    private GoogleMap mMap;
//    TextView locationSearch,header_done_txt;
//    static double currentLatitude, currentLongitude;
//    Double latitude = null, longitude = null;
//    RelativeLayout header_map_back;
//    String location,st,locat;
//    String reg_loc_value,msg;
//    Address address;
//    String home_location,home_lattitude,home_longitude,update_latitude,update_longitude;
//
//    String   service_name_arabic,service_name_english,description_arabic,description_english,
//            period_one_from,period_one_to,period_two_to_am_pm,period_two_to,period_two_from_am_pm,
//            period_two_from,  period_one_to_am_pm, is_verified_by_admin,user_type,latitude2,name,
//            longitude2,location2,period_one_from_am_pm,language_setting,payment_preference,category_id,combine_mobile,profile_pic_thumb_url
//            ,category_name;
//    String  user_id,member_login,facebook_id,username,full_name,profile_name,profile_description,email_address,calling_code,mobile_number,password,profile_pic_url,background_pic_url,
//            login_status,active_status,device_id,is_verified ,verify_code,gmt_value,push_notification,email_notification ,subscribed_plan ,bid_limit ,bid_used ,subscribed_date ,project_fee_percent ,added_date;
//
//    SaveSharedPrefernces ssp;
//    String   str,current_location;
//    private ProgressDialog progress_dialog;
//    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
//    private String progress_dialog_msg = "", tag_string_req = "string_req";
//
//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
//    public static final String TAG = Map_Activity.class.getSimpleName();
//    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
//    EditText editsearch;
//    TextView txt_back,header_txt,search_cancel_txt;
//    Typeface typeface;
//    Double latt = null,longgi = null;
//    Marker currentLocationMarker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map);
//        ssp=new SaveSharedPrefernces();
//        typeface= Typeface.createFromAsset(getAssets(), "montserrat.regular.ttf");
//
//        header_map_back= (RelativeLayout) findViewById(R.id.back_relative);
//        header_done_txt= (TextView) findViewById(R.id.header_done_txt);
//        txt_back= (TextView) findViewById(R.id.txt_back);
//        header_txt=(TextView)findViewById(R.id.header_txt);
//        search_cancel_txt=(TextView)findViewById(R.id.search_cancel_txt);
//
//        txt_back.setTypeface(typeface);
//        header_done_txt.setTypeface(typeface);
//        header_txt.setTypeface(typeface);
//        search_cancel_txt.setTypeface(typeface);
//
//        header_map_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        Intent i=getIntent();
//        reg_loc_value=i.getStringExtra("reg_loc_value");
////        update_latitude=ssp.getKEY_latitude(Map_Activity.this);
////        update_longitude=ssp.getKEY_longitude(Map_Activity.this);
//
//        latt=Double.parseDouble(update_latitude);
//        longgi=Double.parseDouble(update_longitude);
//
//
//
//      /*  home_location=i.getStringExtra("location");
//        home_lattitude=i.getStringExtra("latitude");
//        home_longitude=i.getStringExtra("longitude");*/
//
//       // //////Log.e("home_location",home_location);
//
////        header_done_txt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(helper.item_count==1)
////                {
////                    Intent i=new Intent(Map_Activity.this,Register.class);
////                    //i.putExtra("location", st);
////                    helper.category_location=editsearch.getText().toString();
////                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////                    finish();
////                }
////                else
////                {
////                    if (AppController.isOnline(Map_Activity.this)) {
////
////                    maplocationupdate();
////                }
////
////                else {
////                    AppController.showAlert(Map_Activity.this,
////                            getString(R.string.networkError_Message));
////                }
////
////                }
////            }
////        });
//
//
//        editsearch= (EditText) findViewById(R.id.editsearch);
//        editsearch.setTypeface(typeface);
//      //  editsearch.setText(current_location);
//        editsearch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (editsearch.getRight() - editsearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        //do your stuff here
//                        editsearch.setText(" ");
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//
//        editsearch.setMaxLines(1);
//        setUpMapIfNeeded();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        // Create the LocationRequest object
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
//                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
//
//
//
//        editsearch.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete
//                                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .build(Map_Activity.this);
//                    startActivityForResult(intent, 1);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO: Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                }
//
//
//            }
//        });
//
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setUpMapIfNeeded();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//
//    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMapAsync(this);
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        int RESULT_OK = -1;
//
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                // retrive the data by using getPlace() method.
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
//                //////Log.e("lat", String.valueOf(place.getLatLng()));
//
//                editsearch.setText(place.getAddress());
//                latitude = place.getLatLng().latitude;
//                longitude = place.getLatLng().longitude;
////                helper.lat=String.valueOf(place.getLatLng().latitude);
////                helper.lang=String.valueOf(place.getLatLng().longitude);
//                List<Address> addressList = null;
//                location = editsearch.getText().toString();
//                st=place.getAddress().toString();
//
//                if (location != null && !location.equals("")) {
//                    Geocoder geocoder = new Geocoder(this);
//                    try {
//                        addressList = geocoder.getFromLocationName(location, 1);
//                         address = addressList.get(0);
//                        //current_location=addressList.get(0).getAddressLine(0);
//                        //editsearch.setText(current_location);
//                        //////Log.e("address", String.valueOf(address));
//                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAdminArea()));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//                        MarkerOptions options = new MarkerOptions()
//                                .position(latLng)
//                                .title("I am here!");
//                        mMap.addMarker(options);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                // TODO: Handle the error.
//                //Log.e("Tag", status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }
//
//    public void onMapSearch(View view) {
//
//        String location = editsearch.getText().toString();
//        List<Address> addressList = null;
//
//        if (location != null && !location.equals("")) {
//            Geocoder geocoder = new Geocoder(this);
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//                Address address = addressList.get(0);
//                //////Log.e("searchlocation", String.valueOf(address));
//                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAdminArea()));
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            Toast.makeText(Map_Activity.this, "Enter Location", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                mMap.clear();
//
//
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//                String cityName = null, locatity = null;
//                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//                List<Address> addresses;
//                try {
//                    addresses = gcd.getFromLocation(latLng.latitude
//                            ,
//                            latLng.longitude, 1);
//                    //////Log.e("list", addresses.toString());
//                    if (addresses.size() > 0)
//                        System.out.println(addresses.get(0).getLocality());
//                    cityName = addresses.get(0).getSubAdminArea();
//                    locatity = addresses.get(0).getLocality();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                MarkerOptions options = new MarkerOptions()
//                        .position(latLng)
//                        .title(cityName)
//                        .snippet(locatity);
//                mMap.addMarker(options);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//
////                //////Log.e("address", cityName);
//
//            }
//        });
////        setUpMap();
//        setupMapOnClick();
//
//
//
//
//    }
//
//
//    private void setUpMap() {
//        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//    }
//
//    private void handleNewLocation(Location location) {
//        mMap.clear();
//        //////Log.e(TAG, location.toString());
//        double currentLatitude;
//        double currentLongitude;
////        if(TextUtils.isEmpty(update_latitude)||update_latitude.equals("0")) {
////             if (latitude == null) {
//                currentLatitude = location.getLatitude();
//                currentLongitude = location.getLongitude();
////            } else {
////                currentLatitude = latitude;
////                currentLongitude = longitude;
////            }
////        }
////        else
////        {
////            if (latitude == null) {
////                currentLatitude = latt;
////                currentLongitude = longgi;
////            } else {
////                currentLatitude = latitude;
////                currentLongitude = longitude;
////            }
////        }
//
//
//
//        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//        String cityName = null, locatity = null,features=null;
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(currentLatitude,
//                    currentLongitude, 1);
//            //////Log.e("list", addresses.toString());
//            if (addresses.size() > 0)
//                System.out.println(addresses.get(0).getLocality());
////            cityName = addresses.get(0).getSubAdminArea();
////            locatity = addresses.get(0).getLocality();
//            features=addresses.get(0).getAddressLine(0);
//            //////Log.e("features444444",features);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        if(helper.category_location.equals("")){
//            editsearch.setText(features);
////
////        }
////        else{
////            editsearch.setText(helper.category_location);
////        }
//
//        String s = currentLatitude + "\n" + currentLongitude + "\n\nMy Currrent City is: "
//                + cityName + "\n\nMy Postalcode is: "
//
//                + locatity;
//
//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .title(features)
//                .snippet(locatity);
//
//
//        mMap.addMarker(options);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//
////        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
////            @Override
////            public void onInfoWindowClick(Marker marker) {
////                str = marker.getTitle();
////
////                st=str;
////
////                //editsearch.setText(str);
////
////
////            }
////        });
//
//
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        //////Log.e("location55555",String.valueOf(location));
//        if (location == null) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        } else {
//            handleNewLocation(location);
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        /*
//         * Google Play services can resolve some errors it detects.
//         * If the error has a resolution, try sending an Intent to
//         * start a Google Play services activity that can resolve
//         * error.
//         */
//        if (connectionResult.hasResolution()) {
//            try {
//                // Start an Activity that tries to resolve the error
//                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//                /*
//                 * Thrown if Google Play services canceled the original
//                 * PendingIntent
//                 */
//            } catch (IntentSender.SendIntentException e) {
//                // Log the error
//                e.printStackTrace();
//            }
//        } else {
//            /*
//             * If no resolution is available, display a dialog to the
//             * user with the error.
//             */
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        handleNewLocation(location);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        if(helper.item_count==1)
////        {
////            Intent i=new Intent(Map_Activity.this,Register.class);
////            //i.putExtra("location", st);
////            helper.category_location=st;
////            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////            finish();
////        }
////        else
////        {
////            if (AppController.isOnline(Map_Activity.this)) {
////
////                maplocationupdate();
////            }
////
////            else {
////                AppController.showAlert(Map_Activity.this,
////                        getString(R.string.networkError_Message));
////            }
////
////        }
//    }
//
//    Handler mHandler = new Handler(new Handler.Callback() {
//
//        @Override
//        public boolean handleMessage(android.os.Message msg) {
//
//            switch (msg.what) {
//                case SHOW_PROG_DIALOG:
//                    showProgDialog();
//                    break;
//
//                case HIDE_PROG_DIALOG:
//                    hideProgDialog();
//                    break;
//
//                case LOAD_QUESTION_SUCCESS:
//
//                    break;
//
//                default:
//                    break;
//            }
//
//            return false;
//        }
//
//    });
//
//
//
//    private void maplocationupdate() {
//        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
//        progress_dialog_msg = "loading...";
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
//                        JSONObject obj=null;
//                        try {
//                            obj = new JSONObject(response.toString());
//                            int maxLogSize = 1000;
//                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
//                                int start1 = i * maxLogSize;
//                                int end = (i + 1) * maxLogSize;
//                                end = end > response.length() ? response.toString().length() : end;
//                                //////Log.e("Json Data", response.toString().substring(start1, end));
//                            }
//                            msg=obj.getString("msg");
//                            //////Log.e("Message","<<>>"+msg);
//
//
//
//                            JSONObject jarray = obj.getJSONObject("user_details");
//                            user_id = jarray.getString("user_id");
//
//
//
//
//                            name = jarray.getString("name");
//                            email_address = jarray.getString("email_address");
//
//                            calling_code = jarray.getString("calling_code");
//
//                            mobile_number = jarray.getString("mobile_number");
//                            combine_mobile=jarray.getString("combine_mobile");
//                            password = jarray.getString("password");
//                            category_id=jarray.getString("category_id");
//                            gmt_value=jarray.getString("gmt_value");
//                            latitude2=jarray.getString("latitude");
//                            longitude2=jarray.getString("longitude");
//                            location2=jarray.getString("location");
//                            payment_preference=jarray.getString("payment_preference");
//                            device_id = jarray.getString("device_id");
//
//                            active_status = jarray.getString("active_status");
//
//
//                            is_verified = jarray.getString("is_verified");
//
//                            verify_code = jarray.getString("verify_code");
//
//
//
//                            is_verified_by_admin=jarray.getString("is_verified_by_admin");
//                            user_type=jarray.getString("user_type");
//                            profile_pic_thumb_url=jarray.getString("profile_pic_thumb_url");
//                            profile_pic_url=jarray.getString("profile_pic_url");
//                            login_status = jarray.getString("login_status");
//
//                            push_notification = jarray.getString("push_notification");
//                               /* if (jarray.has("plan_name")){
//                                    plan_name=jarray.getString("plan_name");
//
//
//                                    //////Log.e("PLAN_NAME","===="+plan_name);
//                                }*/
//                            service_name_arabic=jarray.getString("service_name_arabic");
//                            service_name_english=jarray.getString("service_name_english");
//                            description_arabic=jarray.getString("description_arabic");
//                            description_english=jarray.getString("description_english");
//                            period_one_from=jarray.getString("period_one_from");
//                            period_one_to=jarray.getString("period_one_to");
//                            period_two_to_am_pm=jarray.getString("period_two_to_am_pm");
//                            period_two_to=jarray.getString("period_two_to");
//                            period_two_from_am_pm=jarray.getString("period_two_from_am_pm");
//                            period_two_from=jarray.getString("period_two_from");
//                            period_one_to_am_pm=jarray.getString("period_one_to_am_pm");
//                            period_one_from_am_pm=jarray.getString("period_one_from_am_pm");
//                            language_setting=jarray.getString("language_setting");
//                            category_name=jarray.getString("category_name");
//                            added_date=jarray.getString("added_date");
//
//                            if(msg.equals("Your location has been updated successfully"))
//
//                            {
//                                AlertDialog.Builder alert;
//                                if (Build.VERSION.SDK_INT >= 11) {
//                                    alert = new AlertDialog.Builder(Map_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
//                                } else {
//                                    alert = new AlertDialog.Builder(Map_Activity.this);
//                                }
//                                //alert.setTitle("Registration!");
//                                alert.setTitle("Successful");
//                                alert.setMessage("Your location has been updated successfully");
//
//
//                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//
////                                                ssp.setKEY_latitude(Map_Activity.this,latitude2);
////
////                                        ssp.setKEY_longitude(Map_Activity.this,longitude2);
////                                        ssp.setKEY_location(Map_Activity.this,location2);
////
////
////                                      Intent intent=new Intent(Map_Activity.this,Home.class);
////                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////                                        startActivity(intent);
//
//
//                                            }
//                                });
//
//
//                                try {
//                                    Dialog dialog = alert.create();
//                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                    dialog.show();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }
//
//
//
////
//                        catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//
//                            Toast.makeText(Map_Activity.this,"Time Out Error",Toast.LENGTH_LONG).show();
//                        }
//                        else if (error instanceof AuthFailureError) {
//                            Toast.makeText(Map_Activity.this,"Authentication Error",Toast.LENGTH_LONG).show();
//                            //TODO
//                        }
//                        else if (error instanceof ServerError) {
//
//                            Toast.makeText(Map_Activity.this,"Server Error",Toast.LENGTH_LONG).show();
//                            //TODO
//                        }
//                        else if (error instanceof NetworkError) {
//                            Toast.makeText(Map_Activity.this,"Network Error",Toast.LENGTH_LONG).show();
//
//                            //TODO
//
//                        }
//                        else if (error instanceof ParseError) {
//
//
//                            //TODO
//                        }
//
//                    }
//                }){
//
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("action","Locationupdate");
////                params.put("user_id",ssp.get_id(Map_Activity.this));
//                params.put("location",editsearch.getText().toString());
//                params.put("latitude",String.valueOf(latitude));
//                params.put("longitude",String.valueOf(longitude));
//                //////Log.e("params", params.toString());
//                return params;
//            }
//
//        };
//
//
//        // Adding request to request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60*1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//
//
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
//    }
//
//
//
//    @SuppressLint("InlinedApi") private void showProgDialog() {
//        progress_dialog = null;
//        try{
//            if (Build.VERSION.SDK_INT >= 11 ) {
//                progress_dialog = new ProgressDialog(Map_Activity.this, AlertDialog.THEME_HOLO_LIGHT );
//            } else {
//                progress_dialog = new ProgressDialog(Map_Activity.this);
//            }
//            progress_dialog.setMessage(progress_dialog_msg);
//            progress_dialog.setCancelable(false);
//            progress_dialog.show();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//    // hide progress
//    private void hideProgDialog() {
//        try{
//            if (progress_dialog != null && progress_dialog.isShowing())
//                progress_dialog.dismiss();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    private void setupMapOnClick() {
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(final LatLng point) {
//                //  mGoogleMap.clear();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));
//                if (currentLocationMarker != null) {
//                    currentLocationMarker.remove();
//                }
//                String s = null;
//                String cityName = "";
//                String address = null;
//                String postalcode = "";
//                String sublocality = "";
//                String addmin = "";
//                final MarkerOptions markerOptions = new MarkerOptions();
//                //////Log.e("helper.lat", "" + latitude);
//                //////Log.e("helper.long", "" + longitude);
//                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//                List<Address> addresses;
//                try {
//                    addresses = gcd.getFromLocation(point.latitude,
//                            point.longitude, 1);
//                    //////Log.e("list", addresses.toString());
//                    if (addresses.size() > 0)
//
//
//                    System.out.println(addresses.get(0).getLocality());
//                    address = addresses.get(0).getAddressLine(1) + "," + addresses.get(0).getAddressLine(2) + "," + addresses.get(0).getAddressLine(3);
//                    //////Log.e("address", address);
//                    cityName = addresses.get(0).getAddressLine(0);
//                    addmin = addresses.get(0).getAddressLine(2);
//                    postalcode = addresses.get(0).getAddressLine(3);
//                    sublocality = addresses.get(0).getAddressLine(1);
//                    locat=addresses.get(0).getAddressLine(0);
//                    //////Log.e("location map", ""+locat);
////                    helper.map_location=addresses.get(0).getLocality();
//
//
//                    if (addmin == null) {
//                        addmin = "";
//                    }
//                    if (postalcode == null) {
//                        postalcode = "";
//                    }
//                    if (sublocality == null) {
//                        sublocality = "";
//                    }
//                    s = sublocality + " "
//
//                            + addmin + " " + postalcode;
//                    ////Log.e("markerplace", s);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //  mMap.addMarker(new MarkerOptions().position(point).title(s).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                markerOptions.position(point);
//                // markerOptions.position(adapter.getItem(0).getLatLng());
//                markerOptions.snippet(s);
//                markerOptions.title(cityName);
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                currentLocationMarker = mMap.addMarker(markerOptions);
//                helper.category_location=(String.valueOf(markerOptions.getTitle()) + ", " + String.valueOf(markerOptions.getSnippet()));
//                editsearch.setText((String.valueOf(markerOptions.getTitle()) + ", " + String.valueOf(markerOptions.getSnippet())));
//
////                mMap.setInfoWindowAdapter(new PopupAdapterMarker(getLayoutInflater()));
////                currentLocationMarker.showInfoWindow();
//                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick(Marker marker) {
//                        // helper.location1 = helper.location = location;
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));
////                        helper.search_value = 1;
//                        helper.latitude = String.valueOf(point.latitude);
//                        helper.longitude = String.valueOf(point.longitude);
//
//
////                        ////Log.e("ANJALIII map", ""+helper.map_location);
//// PrefMangr.getInstance().setlatitude(String.valueOf(point.latitude));
////                        PrefMangr.getInstance().setlogitude(String.valueOf(point.longitude));
////                        PrefMangr.getInstance().setDeliveryAddress(String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));
//                        //  onRestart();
//                        if (ActivityCompat.checkSelfPermission(Map_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        if (ActivityCompat.checkSelfPermission(Map_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mMap.setMyLocationEnabled(false);
//                        // currentLocationMarker.hideInfoWindow();
//                        // ShowrestaurantlistOnResume();
//
////                        Intent intent = new Intent(Map_Activity.this, Register.class);
////                        startActivity(intent);
////                        finish();
//
//                    }
//                });
//               /* mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//
//                        currentLocationMarker.showInfoWindow();
//                        return true;
//                    }
//                });*/
//
//            }
//        });
//
//    }
//
//
//
//
//
//
//
//}
