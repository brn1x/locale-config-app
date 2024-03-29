package com.example.testemapproject;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testemapproject.Model.LocaleConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {
    //private static final String TAG = "MapsActivity";

    /* Variaveis */
    private GoogleMap mMap;
    private static Location currentLocation;
    private LatLng markerLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    /* Constants */
    private static final int REQUEST_CODE = 101;
    private static final int PERMISSION_ID = 44;
    private static final int DEFAULT_ZOOM = 16;

    /* Edit Variables */
    private int actvID = 0, editId, editZoom, editWifi, editMedia, editRing, editAlarm;
    private String editName;
    private double editLat = 0;
    private double editLongi = 0;

    /* Widgets */
    private EditText mSearchText;
    private ImageView mLocationIcon;
    private Button mSelectBtn;
    private ImageView mMarkerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSearchText = (EditText) findViewById(R.id.input_search);
        mLocationIcon = (ImageView) findViewById(R.id.ic_location);
        mSelectBtn = (Button) findViewById(R.id.select_btn);
        mMarkerIcon = (ImageView) findViewById(R.id.ic_marker);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getLastLocation();
                }
            }
        }).start();

        /* Edit content */
        Intent receivedIntent = getIntent();
        /*actvID = receivedIntent.getIntExtra("actvID", 0);
        if(actvID == 2){

        }*/
        editId = receivedIntent.getIntExtra("id", 0);
        editName = receivedIntent.getStringExtra("cName");
        editLat = receivedIntent.getDoubleExtra("lat", 0);
        editLongi = receivedIntent.getDoubleExtra("longi", 0);
        editZoom = receivedIntent.getIntExtra("zoom", 0);
        editWifi = receivedIntent.getIntExtra("wifi",0);
        editMedia = receivedIntent.getIntExtra("media",0);
        editRing = receivedIntent.getIntExtra("ring",0);
        editAlarm = receivedIntent.getIntExtra("alarm",0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /* Inicialização dos componentes */
    private void init() {
        mSearchText.setInputType(InputType.TYPE_CLASS_TEXT);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    // search method
                    geoLocate();
                }

                return false;
            }
        });

        mLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocation != null){
//                    getLastLocation();
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                }
            }
        });

        mMarkerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markerLocation != null) {
                    moveCamera(markerLocation, DEFAULT_ZOOM);
                } else {
                    Toast.makeText(MapsActivity.this, "Selecione uma localização", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideKeyboard();
    }

    /* Pega a localização  */
    private void geoLocate() {
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e){
            System.out.println("Geolocate: "+e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            LatLng position = new LatLng(address.getLatitude(), address.getLongitude());

            CircleOptions mCircle =
                    new CircleOptions()
                            .center(position)
                            .radius(50)
                            .strokeWidth(3f)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(70, 50, 50, 150));

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(position).draggable(true)).setTitle(address.getAddressLine(0));
            mMap.addCircle(mCircle);
            moveCamera(position, DEFAULT_ZOOM);
            checkCirclesRadius(mCircle);
            markerLocation = position;
        }
    }

    /* Move o mapa pro local passado e seta um marcador */
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        hideKeyboard();
    }

    /* Pega a ultima localização se as permissões de GPS/Localização estiver corretas */
    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        System.out.println("GetLastLocation");
        if(checkPermissions()) {
            if(isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if(location == null) {
                                    requestNewLocationData();
                                } else {
                                    currentLocation = location;
//                                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//                                    supportMapFragment.getMapAsync(MapsActivity.this);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    /* Verificar se a permissão de localização do app está ativada */
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /* Verificar se o GPS está ativado no smartphone */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    /* Faz o novo request caso de erro ao pegar a localização */
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    /* Pega a nova localização apos o novo request */
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            currentLocation = mLastLocation;
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(MapsActivity.this);
        }
    };

    /* Request de permissão de localização */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    /* Refaz o request de permissão de localização */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }
                break;
        }
    }

    /* Criação do Mapa na tela */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(currentLocation != null){
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            moveCamera(latLng, DEFAULT_ZOOM);

            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            init();

            if (editLat != 0 && editLongi != 0) {
                LatLng editLatLng = new LatLng(editLat, editLongi);
                CircleOptions mCircle = new CircleOptions()
                        .center(editLatLng)
                        .radius(50)
                        .strokeWidth(3f)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.argb(70, 50, 50, 150));
                mMap.addMarker(new MarkerOptions().position(editLatLng).draggable(true)).setTitle("Localização Escolhida");
                mMap.addCircle(mCircle);
                markerLocation = editLatLng;
                moveCamera(editLatLng, DEFAULT_ZOOM);
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng position) {
                    mMap.clear();
                    CircleOptions mCircle = new CircleOptions()
                            .center(position)
                            .radius(50)
                            .strokeWidth(3f)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(70, 50, 50, 150));
                    mMap.addMarker(new MarkerOptions().position(position).draggable(true)).setTitle("Localização Escolhida");
                    mMap.addCircle(mCircle);
                    markerLocation = position;
                    moveCamera(position, DEFAULT_ZOOM);

                    checkCirclesRadius(mCircle);
                }
            });

            mSelectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editLat == 0) {
                        if(markerLocation != null) {
                            Intent newIntent = new Intent(MapsActivity.this, ConfigActivity.class);
                            newIntent.putExtra("longitude", markerLocation.longitude);
                            newIntent.putExtra("latitude", markerLocation.latitude);
                            /*MapsActivity.this.*/startActivity(newIntent);
                        } else {
                            Toast.makeText(MapsActivity.this, "Selecione uma localização", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent editIntent = new Intent(MapsActivity.this, EditConfigActivity.class);
                        editIntent.putExtra("id", editId);
                        editIntent.putExtra("cName", editName);
                        editIntent.putExtra("lat", markerLocation.latitude);
                        editIntent.putExtra("longi", markerLocation.longitude);
                        editIntent.putExtra("zoom", editZoom);
                        editIntent.putExtra("wifi", editWifi);
                        editIntent.putExtra("media", editMedia);
                        editIntent.putExtra("ring", editRing);
                        editIntent.putExtra("alarm", editAlarm);
                        /*MapsActivity.this.*/startActivity(editIntent);
                        //Toast.makeText(MapsActivity.this, "Teste", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    /* Check Radius */
    private void checkCirclesRadius(final CircleOptions circleOptions){
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                float[] distance = new float[2];

                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        circleOptions.getCenter().latitude, circleOptions.getCenter().longitude, distance);

                if(distance[0] < circleOptions.getRadius()){
                    // efetuar as configurações de posicionamento(Verificar se está dentro do circulo e disparar configs)
                    Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + circleOptions.getRadius(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    /* Esconde o teclado */
    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static Location getCurrentLocation(){
        return currentLocation;
    }
}
