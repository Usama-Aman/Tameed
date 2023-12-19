package tameed.com.tameed;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Util.GPSTracker;
import tameed.com.tameed.Util.SaveSharedPrefernces;


public class MapView_Activity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {
    TextView toolbar_txt, select_service;
    public static final String TAG = MapView_Activity.class.getSimpleName();
    ImageView FAB;

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView locationSearch, save_txt, header_txt;
    String location, feature = null;
    RelativeLayout search_layout;
    ImageView header_logout, toolbar_back;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Marker marker;
    SaveSharedPrefernces ssp;
    ImageView check;
    Boolean bool = true;
    int flag = 0;
    GoogleMap googleMap;
    public static final int RequestPermissionCode = 1;

    Double latitude = null, longitude = null;
    GPSTracker gpsTracker;
    String shopList, str;

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
        //Log.e(TAG, "****************************");
        setContentView(R.layout.map_view);

        gpsTracker = new GPSTracker(this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        toolbar_back = (ImageView) findViewById(R.id.header_back);
        toolbar_back.setVisibility(View.VISIBLE);

        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("LOCATION");

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EnableRuntimePermission();

        locationSearch = (TextView) findViewById(R.id.locationSearch);

        save_txt = (TextView) findViewById(R.id.txt_save_map);

        Intent intent = getIntent();
        shopList = intent.getStringExtra("shopList");

        save_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (helper.location_set == 1) {

                    Intent i = new Intent(MapView_Activity.this, profile.class);
                    i.putExtra("location", locationSearch.getText().toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);


                } else {
                    Intent i = new Intent(MapView_Activity.this, RegisterActivity.class);
                    i.putExtra("location", locationSearch.getText().toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);

                }


            }
        });
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);
        setUpMapIfNeeded();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        search_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(MapView_Activity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag))
                    .getMapAsync(this);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int RESULT_OK = -1;

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrieve the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                ////Log.e("lat", String.valueOf(place.getLatLng()));
                locationSearch.setText(place.getAddress());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                List<Address> addressList = null;
                location = locationSearch.getText().toString();

                if (location != null && !location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        ////Log.e("addressList", String.valueOf(address));
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        if (mMap != null) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAdminArea()));

                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            MarkerOptions options = new MarkerOptions()
                                    .position(latLng)
                                    .title("I am here!");
                            mMap.addMarker(options);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void onMapSearch(View view) {

        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null && !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                final Address address = addressList.get(0);
                ////Log.e("searchlocation", String.valueOf(address));
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAdminArea()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            String str = marker.getTitle();
                            ////Log.e("addressstr", str);
                            ////Log.e("addressstr", str);
                            locationSearch.setText(str);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(MapView_Activity.this, "Enter Location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                if (mMap != null) {
                    mMap.clear();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
                String cityName = null, locatity = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(latLng.latitude
                            ,
                            latLng.longitude, 1);
                    ////Log.e("list1", addresses.toString());
                    if (addresses.size() > 0)
                        System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getSubAdminArea();
                    locatity = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(cityName)
                        .snippet(locatity);

                if (mMap != null)
                    mMap.addMarker(options);
                locationSearch.setText(String.valueOf(options.getTitle()));

                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            str = marker.getTitle();
                            ////Log.e("addresses", str);
                            locationSearch.setText(str);

                            finish();
                        }
                    });
                }
                ////Log.e("address", cityName);

            }
        });
        setUpMap();
    }


    private void setUpMap() {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng point) {
                    mMap.clear();
                    if (marker != null) {
                        marker.remove();
                    }
                    String s = null;
                    String cityName = "";
                    String address = null;
                    String postalcode = "";
                    String sublocality = "";
                    String addmin = "";
                    final MarkerOptions markerOptions = new MarkerOptions();

                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(point.latitude,
                                point.longitude, 1);
                        ////Log.e("list2", addresses.toString());
                        if (addresses.size() > 0)
                            System.out.println(addresses.get(0).getLocality());
                        address = addresses.get(0).getAddressLine(1) + "," + addresses.get(0).getAddressLine(2) + "," + addresses.get(0).getAddressLine(3);
                        ////Log.e("address", address);
                        cityName = addresses.get(0).getAddressLine(0);
                        addmin = addresses.get(0).getAddressLine(2);
                        postalcode = addresses.get(0).getAddressLine(3);
                        sublocality = addresses.get(0).getLocality();
                        if (addmin == null) {
                            addmin = "";
                        }
                        if (postalcode == null) {
                            postalcode = "";
                        }
                        if (sublocality == null) {
                            sublocality = "";
                        }
                        s = sublocality + " "

                                + addmin + " " + postalcode;
                        ////Log.e("markerplace", s);
                        locationSearch.setText(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    markerOptions.position(point);
                    markerOptions.snippet(sublocality);
                    markerOptions.title(cityName);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                    locationSearch.setText(String.valueOf(markerOptions.getTitle()));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            String str = marker.getTitle();
                            locationSearch.setText(str);
                            ////Log.e("addresses", str);
                        }
                    });

                }
            });
        }
    }

    private void handleNewLocation(Location location) {
        if (mMap != null)
            mMap.clear();
        ////Log.e(TAG, location.toString());
        String s = null;
        String cityName = "";
        String address = null;
        String postalcode = "";
        String sublocality = "";
        String addmin = "";
        final double currentLatitude;
        final double currentLongitude;
        if (latitude == null) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        } else {
            currentLatitude = latitude;
            currentLongitude = longitude;
        }

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if (mMap != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(currentLatitude,
                    currentLongitude, 1);
            ////Log.e("list3", addresses.toString());

            if (addresses != null && addresses.size() != 0) {
                feature = addresses.get(0).getAddressLine(0).toString();
                locationSearch.setText(feature);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());

                cityName = addresses.get(0).getAddressLine(0);
                addmin = addresses.get(0).getAddressLine(2);
                postalcode = addresses.get(0).getAddressLine(3);
                sublocality = addresses.get(0).getAddressLine(1);
                s = cityName;
                ////Log.e("markerplace", s);
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(cityName);
                if (mMap != null)
                    marker = mMap.addMarker(options);
                if (marker != null)
                    marker.showInfoWindow();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mMap != null)
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                }
            });
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {

                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {

                e.printStackTrace();
            }
        } else {

            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MapView_Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(MapView_Activity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MapView_Activity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionCode);
        }
    }
}
