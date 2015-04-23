package in.jaaga.thebachaoproject;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,MenuFragment.OnFragmentInteractionListener,ReviewFragment.OnFragmentInteractionListener,SearchSuggestionsFragment.OnFragmentInteractionListener{


    private MapView mapView;
    private ProgressBar progressBar;
    private ImageButton menuButton;
    double lat,lng;
    getLocationInfoThread locationInfo;
    SearchSuggestionsFragment searchSuggestionsFragment;
    String location_name="";


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
        menuButton = (ImageButton) findViewById(R.id.btn_menu);

        menuButton.setOnClickListener(this);
        //mapView.loadFromGeoJSONURL("https://a.tiles.mapbox.com/v4/amarp.l46caon4/features.json?access_token=pk.eyJ1IjoiYW1hcnAiLCJhIjoiMzQ2Q2JpZyJ9.qNRj5mHyu5KjGwtjYoOe0w");
        mapView.zoomToBoundingBox(boundingBox);
        mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
        mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
        mapView.setCenter(iLatLng);
        //mapView.setAccessToken("sk.eyJ1IjoiYW1hcnAiLCJhIjoiOTZ0N2F4MCJ9.TTvMMwStKFMMN-nONyYJKA");
        //mapView.setZoom(0);
        mapView.setSaveEnabled(true);

        searchSuggestionsFragment=new SearchSuggestionsFragment();
        searchSuggestionsFragment.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_suggestions_container,searchSuggestionsFragment).commit();


        if(getConnectivityStatus(getApplicationContext())==0){
            Toast.makeText(getApplicationContext(),getString(R.string.msg_no_internet),Toast.LENGTH_SHORT).show();
        }
        goToUserLocation();

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
                    Toast.makeText(getApplicationContext(),getString(R.string.msg_no_internet),Toast.LENGTH_SHORT).show();
                }
/*
                lat=iLatLng.getLatitude();
                lng=iLatLng.getLongitude();
                System.out.println(mapView.getBoundingBox());*/
                getSupportFragmentManager().popBackStackImmediate();
                try {
                    searchSuggestionsFragment.adapter.clear();
                    searchSuggestionsFragment.adapter.notifyDataSetChanged();
                }
                catch (NullPointerException e){

                }
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
    public void onStart(){
        super.onStart();
       // getMarkers();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedState){
        super.onRestoreInstanceState(savedState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    void showDialog(LatLng latLng,String place_name) {

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
        DialogFragment newFragment = Audit.newInstance(latLng.getLatitude(), latLng.getLongitude(), place_name);
        newFragment.show(ft,"dialog");
    }

    void alertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you feeling uncomfortable or unsafe")
                .setPositiveButton("Yes,I am",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("No", null);
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


                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.menu_fragment_container, menuFragment, "menu").addToBackStack("menubutton").commit();
                }
            break;

        }
    }

    void setAudit(String locationname,String email,String feeling,int transport,int street_lights,int rating,double lat,double lon){

        Calendar cal=Calendar.getInstance();
        Date date=cal.getTime();
        System.out.println(date.getTime());
        ParseObject audit=new ParseObject("audit");
        audit.put("location_name",locationname);
        audit.put("email",email);
        audit.put("feeling",feeling);
        audit.put("transport", transport);
        audit.put("streetLights",street_lights);
        audit.put("rating",rating);
        audit.put("location",new ParseGeoPoint(lat,lon));
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
        getMarkers(lat, lon);



    }

    public void getMarkers(double lat,double lon){

        //            progressBar.setVisibility(View.VISIBLE);
        //code="current";

        ParseQuery<ParseObject> query = ParseQuery.getQuery("audit");
        query.whereWithinKilometers("location", new ParseGeoPoint(lat, lon), 2);
        //query.whereWithinKilometers("location",new ParseGeoPoint(mapView.getCenter().getLatitude(),mapView.getCenter().getLongitude()),2);
        //  query.fromLocalDatastore();
        //query.whereExists("objectId");
        //query.orderByAscending("createdAt");
        //query.setLimit(1000);


        try {

            System.out.println("count is" +query.count());
            //count=query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        query.findInBackground(new FindCallback<ParseObject>() {

                                   @Override
                                   public void done(List<ParseObject> parseObjects, ParseException e) {
                                       if (e == null) {

                                           int size = parseObjects.size();
                                           System.out.println("Size is" + size);
                                           for (int i = 0; i < parseObjects.size(); i++) {

                                               ParseObject data = parseObjects.get(i);

                                               Marker m = new Marker(mapView, data.getString("feeling"), "-" + data.getString("name"), new LatLng(data.getParseGeoPoint("location").getLatitude(), data.getParseGeoPoint("location").getLongitude()));
                                               m.setIcon(new Icon(getApplicationContext(), Icon.Size.SMALL, "marker-stroked", "ee8a65"));
                                               mapView.addMarker(m);

                                               data.pinInBackground();
                                               // txt.setText(i);
                                               //System.out.println(i);
                                               //txt.append(data.getString("question") + "\n");
                                           }
                                           // object will be your game score

                                       } else {

                                           System.out.println("something went wrong");

                                           // something went wrong
                                       }
                                   }
                               }


        );

    }

    //check if location service is available or not.
    boolean checkLocationService(){

        boolean gps_enabled=false;
        LocationManager locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}

        return gps_enabled;
    }



    public int getConnectivityStatus(Context context) {


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
            mapView.setZoom(16).goToUserLocation(true);
            LatLng latLng = mapView.getUserLocation();
            try{
                getMarkers(latLng.getLatitude(),latLng.getLongitude());
            }catch (NullPointerException e){}
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.msg_location_service_dialog))
                    .setTitle(getString(R.string.title_location_service_dialog))
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

      //  SearchActivity fragment=new SearchActivity();
       // getSupportFragmentManager().beginTransaction().add(fragment,"search").commit();

        getSupportFragmentManager().popBackStack();

    }


    @Override
    public void getReviews() {

        getSupportFragmentManager().popBackStack();
        //MenuFragment menuFragment=new MenuFragment();
        LatLng latLng=mapView.getCenter();

        Fragment reviewFragment=ReviewFragment.newInstance(latLng);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("menu");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right).addToBackStack("menu").replace(R.id.menu_fragment_container, reviewFragment).commit();

    }

    @Override
    public void writeReview() {

        progressBar.setVisibility(View.VISIBLE);
        getSupportFragmentManager().popBackStackImmediate();

            LatLng latLng=mapView.getCenter();

            locationInfo=new getLocationInfoThread();
            locationInfo.execute(latLng);

         //   WriteDReviewActivity.newInstance(latLng,location_name);
     //   progressBar.setVisibility(View.INVISIBLE);
    }

    public void startReviewActivity(LatLng latLng){


        Intent in = new Intent(this,WriteDReviewActivity.class);
        in.putExtra("lat",latLng.getLatitude());
        in.putExtra("long",latLng.getLongitude());
        in.putExtra("location",location_name);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);

    }

    protected class getLocationInfoThread extends AsyncTask<LatLng,Void,String> {

        @Override
        protected String doInBackground(LatLng... params) {

            HttpClient httpClient = new DefaultHttpClient();
            LatLng latLng=params.clone()[0];
            String url="http://nominatim.openstreetmap.org/reverse?format=json&lat=&lon=&zoom=18&addressdetails=1";
            HttpResponse response = null;
            JSONObject object=new JSONObject();
            String place_name="";


            try {
                HttpGet getMethod = new HttpGet(url);
                response = httpClient.execute(getMethod);
                String result = EntityUtils.toString(response.getEntity());
                object=new JSONObject(result);
                place_name=object.getString("display_name");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            //showDialog(latLng,place_name);
            location_name=place_name;
            startReviewActivity(latLng);

            return place_name;
        }
        @Override
        protected void onPostExecute(String location) {
            super.onPostExecute(location);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFragmentInteraction(HashMap<String,Double> data) {

        double lat= Double.valueOf(data.get("lat"));
        double lon= Double.valueOf(data.get("lon"));
        ILatLng iLatLng=new LatLng(lat,lon);
        mapView.zoomToBoundingBox(mapView.setCenter(iLatLng).getBoundingBox()).setZoom(17);
        getMarkers(lat,lon);
        getReviews();


    }


}

