package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
/*Fragment to show suggestions for a place in List when user types in the search parameters*/
public class SearchSuggestionsFragment extends ListFragment{

    List<String> items;
    ArrayList<HashMap<String,Double>> place;
    private OnFragmentInteractionListener mListener;
    ArrayAdapter<String> adapter;
    searchLocationThread thread;
    ProgressBar progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchSuggestionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items=new ArrayList<>();
        place=new ArrayList<>();

        final ArrayAdapter<String> mAdapter= new ArrayAdapter<>(getActivity(),
                R.layout.search_list_item,items);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        progressBar= (ProgressBar) view.findViewById(R.id.progress_search);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(queryListener);
    }


    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {

            } else {

            }
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
           // Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
            try {

                searchLocation(query);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onFragmentInteraction(place.get(position));
        getListView().setVisibility(View.INVISIBLE);

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
     /*   View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }*/
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(HashMap<String,Double> data);
    }

    /*set the listener for this fragment */
    public void setListener(OnFragmentInteractionListener l){

        mListener=l;

    }

    /* Get the location data from AsyncTask and refresh the list adapter to show that data */
    public void searchLocation(String location) throws ExecutionException, InterruptedException {

        progressBar.setVisibility(View.VISIBLE);
        thread=new searchLocationThread();

        adapter = (ArrayAdapter<String>) getListAdapter();
        //adapter.clear();
        //adapter.notifyDataSetChanged();

        thread.execute(location);
      /*  do{System.out.println("running");}while (thread.getStatus().equals(AsyncTask.Status.RUNNING));
        */
       // adapter.notifyDataSetChanged();
    }

    /*Fetch location data from "http://nominatim.openstreetmap.org" with search String that user has entered*/
    protected class searchLocationThread extends AsyncTask<String,Void,List<String>> {

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
                HashMap<String,Double> map=new HashMap<>();
                map.clear();
                place.clear();

               // center.clear();
                for(int i=0;i<jsonArray.length();i++) {
                    items.add(jsonArray.getJSONObject(i).getString("display_name"));
                   // map.put("display_name", jsonArray.getJSONObject(i).getString("display_name"));
                    map.put("lat", jsonArray.getJSONObject(i).getDouble("lat"));
                    map.put("lon",jsonArray.getJSONObject(i).getDouble("lon"));
                    place.add(i,map);
                }
                //suggestionsFragment.updateData(items);

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
          //  adapter = (ArrayAdapter<String>) getListAdapter();
            //adapter.clear();
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

}
