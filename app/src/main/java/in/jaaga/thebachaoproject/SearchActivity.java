package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends Activity {

    private EditText edit_text_search_box;
    //private ListView suggestions;
    HttpURLConnection connection;
    searchLocationThread thread=null;

    List<String> items;
    List<String> center;
    TextWatcher textWatcher;
    ProgressBar progressBar;
    ArrayAdapter<String> adapter;
    SearchSuggestionsFragment suggestionsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items=new ArrayList<>();
        center=new ArrayList<>();
        //items.add("test");
       // SearchSuggestionsFragment.newInstance("a","b");

        suggestionsFragment=new SearchSuggestionsFragment();

       // setContentView(R.layout.activity_search);
       /*    adapter = new ArrayAdapter<String>(this,
                R.layout.search_list_item, items);*/
       // setListAdapter(adapter);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {
                searchLocation(query);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



     /*   textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                //String search_query= (String) s;
                try {
                    searchLocation(s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==0 || s.length()==-1){
                    items.clear();
                    center.clear();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }

            }
        };*/
    }



    public void searchLocation(String location) throws ExecutionException, InterruptedException {

        //String searchLocation=location;
       // progressBar.setVisibility(View.VISIBLE);

     //   ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();

        thread=new searchLocationThread();


        do{System.out.println("running");}while (thread.getStatus().equals(AsyncTask.Status.RUNNING));
        //List<String> result=
                thread.execute(location);

            // adapter.clear();
  /*      suggestionsFragment.updateData(result);*/
        /*adapter.addAll(result);
        adapter.notifyDataSetChanged();*/
    }
/*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //mListener.onFragmentInteraction(center.get(position));

    }*/

    private class searchLocationThread extends AsyncTask<String,Void,List<String>>{

        @Override
        protected List<String> doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            String[] locationarray= params;
            String location=locationarray[0];
            String url="http://nominatim.openstreetmap.org/search?q="+ URLEncoder.encode(location)+"&format=json&addressdetails=1";
            HttpResponse response = null;


            try {
                HttpGet getMethod = new HttpGet(url);
                response = httpClient.execute(getMethod);
                String result = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray=new JSONArray(result);
                System.out.println("Length of JsonArray:"+jsonArray.length());
                items.clear();
                for(int i=0;i<jsonArray.length();i++) {
                    items.add(jsonArray.getJSONObject(i).getString("display_name"));
                    center.add(jsonArray.getJSONObject(i).getString("boundingbox"));
                }
//                suggestionsFragment.updateData(items);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            return items;
        }
        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);

           // progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
