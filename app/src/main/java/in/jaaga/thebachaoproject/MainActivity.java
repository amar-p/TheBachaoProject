package in.jaaga.thebachaoproject;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{




    private MapView mapView;
    private ProgressBar progressBar;
    private Button getMyLocation;
    double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ILatLng iLatLng=new LatLng(23,77);
        LatLng neLatLng=new LatLng(35,92);
        LatLng swLattLng=new LatLng(8,66);
        BoundingBox boundingBox = new BoundingBox(neLatLng,swLattLng);

        mapView = (MapView) findViewById(R.id.mapview);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        getMyLocation= (Button) findViewById(R.id.btn_location);
        getMyLocation.setOnClickListener(this);

        mapView.loadFromGeoJSONURL("https://a.tiles.mapbox.com/v4/amarp.l46caon4/features.json?access_token=pk.eyJ1IjoiYW1hcnAiLCJhIjoiMzQ2Q2JpZyJ9.qNRj5mHyu5KjGwtjYoOe0w");
        mapView.zoomToBoundingBox(boundingBox);
        mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
        mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
        mapView.setCenter(iLatLng);
        mapView.setAccessToken("sk.eyJ1IjoiYW1hcnAiLCJhIjoiOTZ0N2F4MCJ9.TTvMMwStKFMMN-nONyYJKA");
        mapView.setZoom(0);
        mapView.setSaveEnabled(true);

        if(getConnectivityStatus(getApplicationContext())==0){
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onShowMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onHideMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onTapMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onLongPressMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onTapMap(MapView mapView, ILatLng iLatLng) {

                if(getConnectivityStatus(getApplicationContext())==0){
                    Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }

                lat=iLatLng.getLatitude();
                lng=iLatLng.getLongitude();

                //Toast.makeText(getApplicationContext(),lat+" "+lng,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongPressMap(MapView mapView, ILatLng iLatLng) {

                 lat=iLatLng.getLatitude();
                 lng=iLatLng.getLongitude();
                 showDialog();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment newFragment = Audit.newInstance(lat,lng);
        newFragment.show(ft,"dialog");
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_location:

            System.out.println("Location called");

            mapView.setUserLocationEnabled(true);

            if(checkLocationService()) {
                mapView.goToUserLocation(true);
            }
                else{
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                this.startActivity(myIntent);
            }

            break;
        }
    }

    void setAudit(String name,String email,String feeling,int transport,int street_lights,int rating,double lat,double lng){

        //TODO marker details will be from user data of audit...


       // Toast.makeText(this,name+email+feeling,Toast.LENGTH_SHORT).show();

        ParseObject audit=new ParseObject("audit");
        audit.put("name",name);
        audit.put("email",email);
        audit.put("feeling",feeling);
        audit.put("transport",transport);
        audit.put("streetLights",street_lights);
        audit.put("rating",rating);
        audit.put("location",new ParseGeoPoint(lat,lng));
        boolean response;
        do{
            progressBar.setVisibility(View.VISIBLE);
            response=audit.saveInBackground().isCompleted();
            System.out.println(response);
            if(response){
                progressBar.setVisibility(View.INVISIBLE);

            }

        }
        while(response==false);



        Marker m=new Marker(mapView,feeling,"-"+name,new LatLng(lat,lng));
        m.setIcon(new Icon(getApplicationContext(),Icon.Size.SMALL, "marker-stroked", "ee8a65"));
        m.addTo(mapView);
        mapView.addMarker(m);

    }

    boolean checkLocationService(){

        boolean gps_enabled=false;
        LocationManager locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}

        return gps_enabled;
    }

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }




 }

