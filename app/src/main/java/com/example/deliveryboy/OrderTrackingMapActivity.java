package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.GeoApiContext;
import com.google.maps.android.SphericalUtil;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderTrackingMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int DEFAULT_ZOOM=2;
    private GoogleMap mMap;
    public View mapView;
    FirebaseFirestore mDb;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    public GeoApiContext geoApiContext;

    private static final int LOCATION_UPDATE_INTERVAL = 3000;
    HashMap<String,List<Marker>> markershashmap=new HashMap<>();
    HashMap<String,List<Polyline>> polylineshashmap=new HashMap<>();
    HashMap<String,Float> marekrscolormap=new HashMap<>();
    String checkpointlat,checkpointlon;
 //    List<String> boynamelist=new ArrayList<>();
//    List<String> boyphonenolist=new ArrayList<>();
    ArrayList<DeliveryBoy> deliveryBoyList=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking_map);
        mDb=FirebaseFirestore.getInstance();
//        geoApiContext=new GeoApiContext
//                .Builder()
//                .apiKey("AIzaSyCpJoD1dUtDytiityihChQV919jqYNEToA")
//                .build();
        Intent intent=getIntent();
        checkpointlat=intent.getStringExtra("checkpointlat");
        checkpointlon=intent.getStringExtra("checkpointlon");
        Bundle bundle = intent.getExtras();
        deliveryBoyList= (ArrayList) bundle.getParcelableArrayList("deliveryboylist");
        for (int i=0;i<deliveryBoyList.size();i++)
        {
            markershashmap.put(deliveryBoyList.get(i).getBoyphoneno(),new ArrayList<Marker>());
            polylineshashmap.put(deliveryBoyList.get(i).getBoyphoneno(),new ArrayList<Polyline>());
            marekrscolormap.put(deliveryBoyList.get(i).getBoyphoneno(), (float) (i*20));

        }
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(OrderTrackingMapActivity.this);
        mapView = supportMapFragment.getView();

    }

    private void startUserLocationsRunnable(){
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                getAllDeliveryBoyLocation();
                //runnablecallnum++;
                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates(){
        mHandler.removeCallbacks(mRunnable);
    }


    void getAllDeliveryBoyLocation(){

//task.getResult().getDocuments().get(0).getId().equals("");
        final CollectionReference locationRef = mDb.collection("DeliveryBoyLocation");
        locationRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (int i=0;i<task.getResult().getDocuments().size();i++)
                {
                    for(int j=0;j<deliveryBoyList.size();j++)
                    {
                        if (task.getResult().getDocuments().get(i).getId().equals(deliveryBoyList.get(j).getBoyphoneno()))
                        {
                            DeliveryBoyLocation deliveryBoyLocation=task.getResult().getDocuments().get(i).toObject(DeliveryBoyLocation.class);
                            showDeliveryBoyLocation(deliveryBoyLocation,deliveryBoyList.get(j));
                        }
                    }
                }
            }
        });
    }
//    void getDeliveryBoyLocationFirstTime(){
//
//        DocumentReference locationRef = mDb.collection("DeliveryBoyLocation")
//                .document(deliveryboyphoneno);
//
//        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    deliveryBoyLocation = task.getResult().toObject(DeliveryBoyLocation.class);
//                    //showDeliveryBoyLocation(deliveryBoyLocation);
////                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(Double.parseDouble(userFullAddress.getLat())
////                            ,Double.parseDouble(userFullAddress.getLon())),new LatLng(deliveryBoyLocation.getGeo_point().getLatitude()
////                            ,deliveryBoyLocation.getGeo_point().getLongitude())),0));
////                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(Double.parseDouble(userFullAddress.getLat())
////                            ,Double.parseDouble(userFullAddress.getLon())),new LatLng(deliveryBoyLocation.getGeo_point().getLatitude()
////                            ,deliveryBoyLocation.getGeo_point().getLongitude())),0));
//                }
//            }
//        });
//
//
//
//    }
    void showDeliveryBoyLocation(DeliveryBoyLocation deliveryboylocation,DeliveryBoy deliveryBoy)
    {
        List<Marker> deliveryroutemarkers=markershashmap.get(deliveryBoy.getBoyphoneno());
        for (int i=0;i<deliveryroutemarkers.size();i++)
        {
            deliveryroutemarkers.get(i).remove();
        }
        calculateDirections(deliveryboylocation,deliveryBoy);
        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(deliveryboylocation.getGeo_point().getLatitude()
                        , deliveryboylocation.getGeo_point().getLongitude()))
                .title(deliveryBoy.getBoyname())
                .snippet("delivery boy")
        .icon(BitmapDescriptorFactory.defaultMarker(marekrscolormap.get(deliveryBoy.getBoyphoneno()))));
        //m1.showInfoWindow();
        deliveryroutemarkers.add(m1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
    void calculateDirections(DeliveryBoyLocation deliveryboylocation,DeliveryBoy deliveryBoy){

//        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
//                Double.parseDouble(userFullAddress.getLat()),
//                Double.parseDouble(userFullAddress.getLon())
//        );
//        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
//
//        directions.alternatives(true);
//        directions.origin(
//                new com.google.maps.model.LatLng(
//                        deliveryBoyLocation.getGeo_point().getLatitude(),
//                        deliveryBoyLocation.getGeo_point().getLongitude()
//                )
//        );
//        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
//            @Override
//            public void onResult(DirectionsResult result) {
//                  tripdistance.setText(result.routes[0].legs[0].distance.toString());
//                  tripduration.setText(result.routes[0].legs[0].duration.toString());
////                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
////                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
////                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
////                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
////                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );
//                Toast.makeText(OrderTrackingMapActivity.this,"Failed to get directions",Toast.LENGTH_SHORT).show();
//
//            }
//        });


        // Declare the variable (the CoreRouter)
        RouteManager routeManager=new RouteManager();
        // Create the RoutePlan and add two waypoints
        RoutePlan routePlan = new RoutePlan();
        routePlan.addWaypoint(new GeoCoordinate(deliveryboylocation.getGeo_point().getLatitude()
                ,deliveryboylocation.getGeo_point().getLongitude()));
        routePlan.addWaypoint(new GeoCoordinate(Double.parseDouble(checkpointlat)
                , Double.parseDouble(checkpointlon)));

        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        routeOptions.setRouteType(RouteOptions.Type.FASTEST);

        routePlan.setRouteOptions(routeOptions);
        routeManager.calculateRoute(routePlan, new RouteListener(deliveryBoy));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        // getDeliveryBoyLocationFirstTime();
        LatLngBounds latLngBounds=toBounds(new LatLng(Double.parseDouble(checkpointlat)
                ,Double.parseDouble(checkpointlon)),10000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(checkpointlat)
                        ,Double.parseDouble(checkpointlon)))
                .title("CheckPoint")
                .icon(BitmapDescriptorFactory.defaultMarker(240))
                .snippet("checkpoint"));
        m2.showInfoWindow();

//        startUserLocationsRunnable();
        MapEngine.getInstance().init(new ApplicationContext(this), new OnEngineInitListener() {
            @Override public void onEngineInitializationCompleted(Error error) {
                if (error != Error.NONE) throw new RuntimeException(error.getDetails(), error.getThrowable());
                Toast.makeText(OrderTrackingMapActivity.this,"ok",Toast.LENGTH_SHORT).show();
                //calculateDirections();
                startUserLocationsRunnable();
            }
        });

    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for(DirectionsRoute route: result.routes){
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(R.color.darkgray);
                    polyline.setClickable(true);

                }
            }
        });
    }

    private class RouteListener implements RouteManager.Listener {
        DeliveryBoy deliveryBoy;

        public RouteListener(DeliveryBoy deliveryBoy) {
            this.deliveryBoy = deliveryBoy;
        }

        public RouteListener() {
        }

        @Override
        public void onProgress(int i) {
            //Toast.makeText(OrderTrackingMapActivity.this,"finding the route",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCalculateRouteFinished(RouteManager.Error error, List<RouteResult> list) {
            Route route=list.get(0).getRoute();
//            LatLngBounds bounds=new LatLngBounds(new LatLng(route.getBoundingBox().getBottomRight().getLatitude()
//                    ,route.getBoundingBox().getBottomRight().getLatitude())
//                    ,new LatLng(route.getBoundingBox().getTopLeft().getLatitude()
//                    ,route.getBoundingBox().getTopLeft().getLongitude()));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
            List<LatLng> newDecodedPath = new ArrayList<>();
            List<GeoCoordinate> geoCoordinates=list.get(0).getRoute().getRouteGeometry();
            for (int i=0;i<geoCoordinates.size();i++)
            {
                newDecodedPath.add(new LatLng(geoCoordinates.get(i).getLatitude()
                        ,geoCoordinates.get(i).getLongitude()));
            }
            List<Polyline> tripspolylines=polylineshashmap.get(deliveryBoy.getBoyphoneno());
            for (int j=0;j<tripspolylines.size();j++)
            {
                tripspolylines.get(j).remove();
            }
            Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
            polyline.setColor(R.color.darkgray);
            tripspolylines.add(polyline);
            //polyline.setClickable(true);
        }
    }
    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }
}
