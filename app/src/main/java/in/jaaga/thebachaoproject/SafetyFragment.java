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


public class SafetyFragment extends Fragment {

    private SafetyFragmentListener mListener;
    RatingBar safety;
    EditText comments;



    public SafetyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_safety, container, false);
        safety = (RatingBar) rootView.findViewById(R.id.rating_bar_safety);
        comments = (EditText) rootView.findViewById(R.id.txt_comment_safety);

        rootView.findViewById(R.id.btn_share_safety).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postData();

            }
        });

        return rootView;
    }

    private void postData() {

        int safety_rating= (int) safety.getRating();
        String comment = comments.getText().toString();
        mListener.onSafetyFragmentInteraction(safety_rating,comment);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onSafetyFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SafetyFragmentListener) activity;
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

    public interface SafetyFragmentListener {
        // TODO: Update argument type and name
        public void onSafetyFragmentInteraction(int safety,String comments);
    }
}
