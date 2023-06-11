package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    int diameter = 100;
    private static final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private SearchView search_maps;
    Location currentLocation;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ImageView img_location;


    LatLng thanhxuan = new LatLng(20.996002041937803, 105.8097114468855);
    LatLng caugiay = new LatLng(21.036958484112965, 105.79056623615422);
    LatLng hadong = new LatLng(20.95724543050617, 105.75632759998462);
    LatLng mydinh = new LatLng(21.02353706410797, 105.77322652116061);

    private ArrayList<LatLng> latLngArrayList;

    private ArrayList<String> locationNameArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastlocation();

        img_location = findViewById(R.id.img_location);

        latLngArrayList = new ArrayList<>();
        locationNameArraylist = new ArrayList<>();
        latLngArrayList.add(thanhxuan);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh);
        locationNameArraylist.add("Mỹ Đình");
//        View locationButton = ((View) ma
        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMyLocation() != null) {
                    onMyLocationButtonClick();
                }
            }
        });
    }

    private void getLastlocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;


                    mapFragment.getMapAsync(MapsActivity.this);

                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(diameter, diameter, conf);


// paint defines the text color, stroke width and size

        for (int i = 0; i < latLngArrayList.size(); i++) {
            int number = i + 20;
            makeColorPonint(number, bmp);
            Log.d("TAG", "onMapReady: " + number);

            mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(i)).title("Marker in " + locationNameArraylist.get(i)).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
            // below line is use to move camera.

        }


// modify canvas
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("vitri1").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//        googleMap.setOnMyLocationButtonClickListener(this);
//        googleMap.setOnMyLocationClickListener(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastlocation();
            } else {
                Toast.makeText(this, "Bạn cần bật quyền location cho ứng dụng này !", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location locationOnClick) {
        currentLocation = locationOnClick;
    }

    @Override
    public boolean onMyLocationButtonClick() {

        currentLocation = mMap.getMyLocation();//Lấy za vị trí hiện tại
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
        return false;
    }



    private void makeColorPonint(int number, Bitmap bmp) {
        Canvas canvas = new Canvas(bmp);
        Paint color = new Paint();
        Paint text = new Paint();
        text.setColor(Color.BLUE);
        text.setTextSize(50);
        text.setStrokeWidth(5);
        text.setTextAlign(Paint.Align.CENTER);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((text.descent() + text.ascent()) / 2));
        color.setColor(getRandomColor());
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, color);
        canvas.drawText("" + number, xPos, yPos, text);
    }


    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}