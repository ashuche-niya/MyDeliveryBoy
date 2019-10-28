package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ListItem> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListItemAdapter listItemAdapter;
    public String deliveryboyno="6377685053";
    public boolean loginornot;
    private Location mLastKnownLocation;
    public Button btnlogout,btntrack;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public DeliveryBoyLocation deliveryBoyLocation=new DeliveryBoyLocation();
    public static final String SHARED_PREFS = "deliveryboysharedpre";
    public static final String SWITCH1 = "loginornot2";
    public static final String TEXT = "mobileno2";
    ArrayList<DeliveryBoy> deliveryBoyList=new ArrayList<>();
    String checkpointlat="29.8689032",checkpointlon="77.8963111";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent=getIntent();
        //deliveryboyno=intent.getStringExtra("deliveryboyno");
        //deliveryboyno=((UserClient)(getApplicationContext())).getPhoneno();
        deliveryBoyList.add(new DeliveryBoy("ishan","6265096224"));
        deliveryBoyList.add(new DeliveryBoy("hemil","6377685053"));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.liveTaskList);
        userpermission();
        Bundle bundle = getIntent().getExtras();
            btnlogout=findViewById(R.id.btnlogout);
            btntrack=findViewById(R.id.btntrack);
            listItemAdapter = new ListItemAdapter(list, MainActivity.this, bundle);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(listItemAdapter);

            prepareSampleList();
//        btnlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                  deliveryboyno="";
//                  loginornot=false;
//                  saveData();
//                  stopLocationService();
//
//
//                  Intent intent1=new Intent(MainActivity.this,Login.class);
//                  startActivity(intent1);
//                  finish();
//            }
//        });
    btntrack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Intent intent1=new Intent(MainActivity.this,OrderTrackingMapActivity.class);
                intent1.putExtra("checkpointlat",checkpointlat);
                intent1.putExtra("checkpointlon",checkpointlon);
                intent1.putExtra("deliveryboylist",deliveryBoyList);
                startActivity(intent1);
        }
    });

    }

    public void stopLocationService()
    {
        if(isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                MainActivity.this.stopService(serviceIntent);
            }else{
                stopService(serviceIntent);
            }
        }
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

                MainActivity.this.startForegroundService(serviceIntent);
            }else{
                startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.deliveryboy.services.LocationService".equals(service.service.getClassName())) {
                //Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        //Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }
    void userpermission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //startActivity(new Intent(HomeActivity.this, MapActivity.class));
            getDeviceLocation();

        }

        Dexter.withActivity(MainActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getDeviceLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(MainActivity.this, "Permission Denied ", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(HomeActivity.this, MapActivity.class));

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }
    void getDeviceLocation(){
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                startLocationService();
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                        startLocationService();
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                            if (mLastKnownLocation!=null){
//                                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void prepareSampleList() {

        ListItem item1 = new ListItem("Delhi", "Roorkee", "Ahmedabad");
        list.add(item1);

        ListItem item2 = new ListItem("Kolkata", "Roorkee", "Ahmedabad");
        list.add(item2);

//        ListItem item3 = new ListItem("Mumbai", "Roorkee", "Ahmedabad");
//        list.add(item3);
//
//        ListItem item4 = new ListItem("Chennai", "Roorkee", "Ahmedabad");
//        list.add(item4);

        listItemAdapter.notifyDataSetChanged();

    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, deliveryboyno);
        editor.putBoolean(SWITCH1, loginornot);
        editor.apply();

        //Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
}
