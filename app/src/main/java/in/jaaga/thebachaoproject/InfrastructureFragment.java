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


public class InfrastructureFragment extends Fragment {

    private FragmentInfrastructureListener mListener;
    RatingBar streetLighting;
    RatingBar peopleAround;
    EditText comments;



    public InfrastructureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_infrastructure, container, false);
        streetLighting = (RatingBar) rootView.findViewById(R.id.rating_bar_street_lighting);
        peopleAround = (RatingBar) rootView.findViewById(R.id.rating_bar_people_around);
        comments = (EditText) rootView.findViewById(R.id.txt_comment_infra);


        rootView.findViewById(R.id.btn_share_infra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postData();

            }
        });
        return rootView;
    }

    private void postData() {


        int street_lighting= (int) streetLighting.getRating();
        int people_around= (int) peopleAround.getRating();
        String comment = comments.getText().toString();
            mListener.onInfrastructureFragmentInteraction(street_lighting,people_around,comment);



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onInfrastructureFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentInfrastructureListener) activity;
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
    public interface FragmentInfrastructureListener {
        // TODO: Update argument type and name
        void onInfrastructureFragmentInteraction(int street_light,int people_around,String comments);
    }

}
