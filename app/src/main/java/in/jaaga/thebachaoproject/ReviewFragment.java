package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;


public class ReviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    /*starts the new instance of this fragment.
    */
    public static ReviewFragment newInstance(LatLng latLng) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putDouble("lat",latLng.getLatitude());
        args.putDouble("long",latLng.getLongitude());
        return fragment;
    }

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){


        }
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_review, container, false);
        TextView avg_rating = (TextView) v.findViewById(R.id.txt_avg_rating);
        Button write_review = (Button) v.findViewById(R.id.btn_write_review_fragment);
        avg_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DetailedReviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.writeReview();

            }
        });

        return v;
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
     */
    public interface OnFragmentInteractionListener {
        void writeReview();
        }

}
