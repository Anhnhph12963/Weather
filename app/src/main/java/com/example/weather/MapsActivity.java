package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Model.CommentQualityModel;
import com.example.weather.adapter.QualityWeatherAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    List<CommentQualityModel> lstCommentModel;
    int diameter = 100;
    private static final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private SearchView search_maps;
    Location currentLocation;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ImageView img_location, img_news, img_back;

    TextView tv_number_IAQ, tv_number_date, tv_number_humidity, tv_number_temperature, tv_number_evaluate, tv_number_news, tv_see, tv_title;
    RecyclerView recyclerView;
    LatLng thanhxuan = new LatLng(20.996002041937803, 105.8097114468855);
    LatLng caugiay = new LatLng(21.036958484112965, 105.79056623615422);
    LatLng hadong = new LatLng(20.95724543050617, 105.75632759998462);
    LatLng mydinh = new LatLng(21.02353706410797, 105.77322652116061);
    LatLng thanhxuan1 = new LatLng(21.01415087541889, 105.74054497999343);
    LatLng caugiay1 = new LatLng(21.011666998362294, 105.7452659889851);
    LatLng hadong1 = new LatLng(21.006659048378612, 105.74470811982496);
    LatLng mydinh1 = new LatLng(20.99836560774121, 105.73676918002924);
    LatLng thanhxuan2 = new LatLng(21.00297269601158, 105.73316401537109);
    LatLng caugiay2 = new LatLng(20.999487182804028, 105.7337221918186);
    LatLng hadong2 = new LatLng(20.994799623547337, 105.73316465907484);
    LatLng mydinh2 = new LatLng(20.98805745970365, 105.76109438711876);
    LatLng thanhxuan3 = new LatLng(21.017835495765652, 105.78426644792702);
    LatLng caugiay3 = new LatLng(21.007325463847934, 105.77885067545006);
    LatLng hadong3 = new LatLng(21.041013043984737, 105.79472862600348);
    LatLng mydinh3 = new LatLng(21.031783713413795, 105.80584405810978);
    LatLng thanhxuan4 = new LatLng(21.037307833187484, 105.7953783954595);
    LatLng caugiay4 = new LatLng(21.027808838487825, 105.7911204871416);
    LatLng hadong4 = new LatLng(21.005643375787688, 105.79783263179253);
    LatLng mydinh4 = new LatLng(21.03582494708833, 105.83659092065274);
    LatLng thanhxuan5 = new LatLng(21.028078120798806, 105.83615869624975);
    LatLng caugiay5 = new LatLng(21.02410457222415, 105.85795366275453);
    LatLng hadong5 = new LatLng(20.98148428702097, 105.87182773449652);
    LatLng mydinh5 = new LatLng(20.956608715699733, 105.8883628794604);
    LatLng thanhxuan6 = new LatLng(21.13360891560745, 105.77364318540234);
    LatLng caugiay6 = new LatLng(20.990760029737444, 105.83722413137573);
    LatLng hadong6 = new LatLng(20.961577927780898, 105.87286745032998);
    LatLng mydinh6 = new LatLng(21.01744074505883, 105.81960346072589);
    LatLng thanhxuan7 = new LatLng(21.055973124862845, 105.79780763130194);
    LatLng caugiay7 = new LatLng(21.064662760505943, 105.78835176658349);
    LatLng hadong7 = new LatLng(21.072341445280227, 105.7739856630961);
    LatLng mydinh7 = new LatLng(21.05260424176765, 105.77796431658712);


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
        img_back = findViewById(R.id.img_back);
        tv_title = findViewById(R.id.tv_title);
        search_maps = findViewById(R.id.search_maps);

        search_maps.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = search_maps.getQuery().toString();
                List<Address> addressList = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,30));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_title.setText("Google Maps");
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
        latLngArrayList.add(thanhxuan1);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay1);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong1);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh1);
        locationNameArraylist.add("Mỹ Đình");
        latLngArrayList.add(thanhxuan2);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay2);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong2);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh2);
        locationNameArraylist.add("Mỹ Đình");
        latLngArrayList.add(thanhxuan3);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay3);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong3);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh3);
        locationNameArraylist.add("Mỹ Đình");
        latLngArrayList.add(thanhxuan4);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay4);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong4);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh4);
        locationNameArraylist.add("Mỹ Đình");
        latLngArrayList.add(thanhxuan5);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay5);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong5);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh5);
        locationNameArraylist.add("Mỹ Đình");
        latLngArrayList.add(thanhxuan6);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay6);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong6);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh6);
        locationNameArraylist.add("Mỹ Đình");
        latLngArrayList.add(thanhxuan7);
        locationNameArraylist.add("Thanh xuân");
        latLngArrayList.add(caugiay7);
        locationNameArraylist.add("Cầu Giấy");
        latLngArrayList.add(hadong7);
        locationNameArraylist.add("Hà Đông");
        latLngArrayList.add(mydinh7);
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

        int count = 0;


// paint defines the text color, stroke width and size

        for (int i = 0; i < latLngArrayList.size(); i++) {
            int number = i + 1;
            makeColorPonint(number, bmp);
            Log.d("TAG", "onMapReady: " + number);

            mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(i)).title("Marker in " + locationNameArraylist.get(i)).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
            // below line is use to move camera.

        }

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("vitri1").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        String id = marker.getId();

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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Dialog dialog = new Dialog(MapsActivity.this);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_quality_weather);
                dialog.getWindow().setAttributes(layoutParams);
                addListCommentDialogQW();
//                locationDetailses.add(new LocationDetails(marker.getId(), location_right, location_left, city, Double.parseDouble(deg), Double.parseDouble(speed)));
//                for (int i = 0; i<locationDetailses.size(); i++) {
//                    //matching id so, alert dialog can show specific data
//                    if (marker.getId().equals(locationDetailses.get(i).getMarkerID())){
//                        builder.setTitle("City: "+locationDetailses.get(i).getCity());
//                        builder.setMessage("Wind Speed: "+locationDetailses.get(i).getSpeed()+"\n"+"Degree: "+locationDetailses.get(i).getDeg()+"\n"+"We can plant WindMill here");
//                    }}
                RecyclerView recyclerView1 = dialog.findViewById(R.id.ryc_comment_quality);
                QualityWeatherAdapter adapter = new QualityWeatherAdapter(lstCommentModel);
                recyclerView1.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                img_news = dialog.findViewById(R.id.img_news);
                img_news.setImageResource(R.drawable.abc);
                tv_number_IAQ = dialog.findViewById(R.id.tv_number_IAQ);
                tv_number_IAQ.setText("1000");
                tv_number_date = dialog.findViewById(R.id.tv_number_date);
                tv_number_date.setText("chiều");
                tv_number_humidity = dialog.findViewById(R.id.tv_number_humidity);
                tv_number_humidity.setText("100%");
                tv_number_temperature = dialog.findViewById(R.id.tv_number_temperature);
                tv_number_temperature.setText("29°C");
                tv_number_evaluate = dialog.findViewById(R.id.tv_number_evaluate);
                tv_number_evaluate.setText("Có thể có bão to");
                tv_number_news = dialog.findViewById(R.id.tv_number_news);
                tv_number_news.setText("Sóng thần sắp đến");
                tv_see = dialog.findViewById(R.id.tv_see);
                tv_see.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


//                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

                dialog.show();
                return false;

            }
        });

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


    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void addListCommentDialogQW() {
        lstCommentModel = new ArrayList<>();
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
        lstCommentModel.add(new CommentQualityModel("hello1", "hello2", "10/02/2001"));
    }
}