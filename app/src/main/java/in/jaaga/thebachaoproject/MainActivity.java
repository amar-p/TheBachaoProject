package in.jaaga.thebachaoproject;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
import android.widget.Switch;
import android.widget.Toast;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.tileprovider.tilesource.ITileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.views.MapController;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{




    private MapView mapView;
    private Button getMyLocation;
    double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapview);
        getMyLocation= (Button) findViewById(R.id.btn_location);
        getMyLocation.setOnClickListener(this);
        mapView.loadFromGeoJSONURL("https://a.tiles.mapbox.com/v4/amarp.l46caon4/features.json?access_token=pk.eyJ1IjoiYW1hcnAiLCJhIjoiMzQ2Q2JpZyJ9.qNRj5mHyu5KjGwtjYoOe0w");
        mapView.zoomToBoundingBox(mapView.getTileProvider().getBoundingBox());
        mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
        mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
        mapView.setCenter(mapView.getTileProvider().getCenterCoordinate());
        mapView.setZoom(0);
        mapView.setSaveEnabled(true);

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

                lat=iLatLng.getLatitude();
                lng=iLatLng.getLongitude();

                Toast.makeText(getApplicationContext(),lat+" "+lng,Toast.LENGTH_SHORT).show();

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

    void setAudit(){

        Marker m=new Marker(mapView,"sbc","India",new LatLng(lat,lng));
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

 }

