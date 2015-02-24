package in.jaaga.thebachaoproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


public class MainActivity extends ActionBarActivity implements View.OnClickListener,MenuFragment.OnFragmentInteractionListener,ReviewFragment.OnFragmentInteractionListener{




    private MapView mapView;
    private ProgressBar progressBar;
    private Button menuButton;
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
        menuButton = (Button) findViewById(R.id.btn_menu);
        menuButton.setOnClickListener(this);

        //mapView.loadFromGeoJSONURL("https://a.tiles.mapbox.com/v4/amarp.l46caon4/features.json?access_token=pk.eyJ1IjoiYW1hcnAiLCJhIjoiMzQ2Q2JpZyJ9.qNRj5mHyu5KjGwtjYoOe0w");
        mapView.zoomToBoundingBox(boundingBox);
        mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
        mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
        mapView.setCenter(iLatLng);
        mapView.setAccessToken("sk.eyJ1IjoiYW1hcnAiLCJhIjoiOTZ0N2F4MCJ9.TTvMMwStKFMMN-nONyYJKA");
        mapView.setZoom(0);
        mapView.setSaveEnabled(true);

        goToUserLocation();

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
                 alertDialog();
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

    void showDialog(LatLng latLng) {

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
        DialogFragment newFragment = Audit.newInstance(latLng.getLatitude(),latLng.getLongitude());
        newFragment.show(ft,"dialog");
    }

    void alertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you feeling uncomfortable or unsafe")
                .setPositiveButton("Yes,I am",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("No",null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_menu:

                Fragment prev = getSupportFragmentManager().findFragmentByTag("menu");
                if (prev != null) {
                    // ft.remove(prev);
                }
                else {

                    //MenuFragment menuFragment=new MenuFragment();
                    Fragment menuFragment = MenuFragment.newInstance(getWindowManager().getDefaultDisplay().getWidth());

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.menu_fragment_container, menuFragment,"menu").addToBackStack("menubutton").commit();
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



    public static int getConnectivityStatus(Context context) {


        int TYPE_WIFI = 1;
        int TYPE_MOBILE = 2;
        int TYPE_NOT_CONNECTED = 0;

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


    @Override
    public void goToUserLocation(){

        System.out.println("Location called");

        mapView.setUserLocationEnabled(true);

        if(checkLocationService()) {
            mapView.goToUserLocation(true);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to turn on the Location Services")
                    .setTitle("Turn on Location Service")
                    .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    }).setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void searchLocation() {

    }

    @Override
    public void getReviews() {

        //MenuFragment menuFragment=new MenuFragment();
        Fragment reviewFragment=ReviewFragment.newInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("menu");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right).addToBackStack("menu").replace(R.id.menu_fragment_container,reviewFragment).commit();

    }

    @Override
    public void writeReview() {
        LatLng latLng=mapView.getUserLocation();
        showDialog(latLng);
    }

}

