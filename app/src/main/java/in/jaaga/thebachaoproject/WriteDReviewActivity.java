package in.jaaga.thebachaoproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


public class WriteDReviewActivity extends ActionBarActivity implements ActionBar.TabListener,InfrastructureFragment.FragmentInfrastructureListener,SafetyFragment.SafetyFragmentListener,TransportFragment.TransportFragmentListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    ViewPager mViewPager;
    JSONObject jsonObject = new JSONObject();
    PostAsyncTask postAsyncTask;
    double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_dreview);

        if(getIntent().getExtras()!=null){
            lat=getIntent().getExtras().getDouble("lat");
            lon=getIntent().getExtras().getDouble("long");
        }
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(Fragment.instantiate(this, in.jaaga.thebachaoproject.InfrastructureFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, in.jaaga.thebachaoproject.SafetyFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, in.jaaga.thebachaoproject.TransportFragment.class.getName()));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(super.getSupportFragmentManager(),fragments);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // here you add an array of your icon
        final int[] ICONS = new int[] {
                R.drawable.infrastructure,
                R.drawable.safety_1,
                R.drawable.transport_1,

        };

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i)).setIcon(this.getResources().getDrawable(ICONS[i]))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_dreview, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> myFragments;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            myFragments=fragments;
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return myFragments.get(position);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1);
                case 1:
                    return getString(R.string.title_section2);
                case 2:
                    return getString(R.string.title_section3);
            }
            return null;
        }
    }

    @Override
    public void onInfrastructureFragmentInteraction(int street_light,int people_around,String comments) {

        //todo make url from data
        postAsyncTask=new PostAsyncTask();
        postAsyncTask.execute();

    }

    @Override
    public void onSafetyFragmentInteraction(int safety,String comments) {

        //todo make url from data
        postAsyncTask=new PostAsyncTask();
        postAsyncTask.execute();

    }

    @Override
    public void onTransportFragmentInteraction(int transport,int taxi,String comments) {

        //todo make url from data
        postAsyncTask=new PostAsyncTask();
        postAsyncTask.execute();

    }

    private class PostAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";
            try {
            //create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make POST request to the given URL
            HttpPost httpPost = new HttpPost("http://192.168.129.182:8010/maps/submit");
            String json = "";


                JSONObject jsonObject=new JSONObject();
                jsonObject.put("lat","22.700");
                jsonObject.put("long","73.245");
                jsonObject.put("date","2015-04-12");
                jsonObject.put("time","12:52:22");
                jsonObject.put("category","any");
                jsonObject.put("rating","5");
                jsonObject.put("review","sfsf");


            // convert JSONObject to JSON to String
            json = jsonObject.toString();

            // set json to StringEntity
            StringEntity se = new StringEntity(json);
            // set httpPost Entity
            httpPost.setEntity(se);
            //httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }
                else{
                    result = "Data is not posted!";}
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(),result, Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}