package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransportFragment.TransportFragmentListener} interface
 * to handle interaction events.
 * Use the {@link TransportFragment} factory method to
 * create an instance of this fragment.
 */
public class TransportFragment extends Fragment {


    private TransportFragmentListener mListener;
    RatingBar publicTransport;
    RatingBar taxi;
    EditText comments;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SafetyFragment.
     */
 /*
    public static TransportFragment newInstance(String param1, String param2) {
        TransportFragment fragment = new TransportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public TransportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transportation, container, false);
        publicTransport = (RatingBar) rootView.findViewById(R.id.rating_bar_public_transport);
        taxi = (RatingBar) rootView.findViewById(R.id.rating_bar_taxi);
        comments = (EditText) rootView.findViewById(R.id.txt_comment_transport);



        rootView.findViewById(R.id.btn_share_transport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postData();

            }
        });

        return rootView;
    }

    private void postData() {
        int transport_rating= (int) publicTransport.getRating();
        int taxi_rating= (int) taxi.getRating();
        String comment = comments.getText().toString();

       mListener.onTransportFragmentInteraction(transport_rating,taxi_rating,comment);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onTransportFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TransportFragmentListener) activity;
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
    public interface TransportFragmentListener {
        // TODO: Update argument type and name
        public void onTransportFragmentInteraction(int transport,int taxi,String comments);
    }

}
