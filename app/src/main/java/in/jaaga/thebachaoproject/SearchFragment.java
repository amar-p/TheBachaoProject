package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends ListFragment {


    private EditText edit_text_search_box;
    //private ListView suggestions;
    HttpURLConnection connection;
    searchLocationThread thread=null;

    List<String> items;
    TextWatcher textWatcher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        items=new ArrayList<>();
        //items.add("test");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.search_list_item, items);

        setListAdapter(adapter);

        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                //String search_query= (String) s;

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
                try {
                    // adapter.clear();

                    adapter.addAll(searchLocation(s));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==0 || s.length()==-1){
                    items.clear();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container, false);
       // suggestions= (ListView) v.findViewById(R.id.listView);
        edit_text_search_box = (EditText) v.findViewById(R.id.editText_search_box);
        edit_text_search_box.addTextChangedListener(textWatcher);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    public List<String> searchLocation(CharSequence location) throws ExecutionException, InterruptedException {

        String searchLocation=location.toString();


        thread=new searchLocationThread();


        do{System.out.println("running");}while (thread.getStatus().equals(AsyncTask.Status.RUNNING));
        List<String> result=thread.execute(searchLocation).get();

        return result;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        System.out.println(items.get(position).toString());
    }

    protected class searchLocationThread extends AsyncTask<String,Void,List<String>>{

        @Override
        protected List<String> doInBackground(String... params) {



            URL mapboxLocationUrl= null;
            String[] locationarray= params;
            String location=locationarray[0];


            try {
                mapboxLocationUrl = new URL("http://api.tiles.mapbox.com/v4/geocode/mapbox.places-permanent/"+location+".json?access_token=sk.eyJ1IjoiYW1hcnAiLCJhIjoiOTZ0N2F4MCJ9.TTvMMwStKFMMN-nONyYJKA");

                connection = (HttpURLConnection) mapboxLocationUrl.openConnection();
                connection.connect();
                int length=connection.getContentLength();
                if(length==-1){}

                else{
                    char[] data=new char[length];


                    InputStream inputStream=connection.getInputStream();
                    Reader reader=new InputStreamReader(inputStream);
                    reader.read(data);
                    String responseData=new String(data);
                    JSONObject jsonObject=null;
                    try {
                        jsonObject=new JSONObject(responseData);

                        JSONArray jsonArray=jsonObject.getJSONArray("features");
                        //jsonArray.getJSONObject(1).getString("text");
                        System.out.println(jsonArray.getJSONObject(0).getString("place_name"));
                        System.out.println(jsonArray.getJSONObject(0).getString("center"));

                        items.clear();
                        for(int i=0;i<jsonArray.length();i++) {
                            items.add(jsonArray.getJSONObject(i).getString("place_name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        }

                    int responseCode= connection.getResponseCode();
                    System.out.println(responseCode);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return items;
        }
    }

}
