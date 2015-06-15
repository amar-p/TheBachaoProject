package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class ReviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    JSONObject data;

    /*starts the new instance of this fragment.
    */
    public static ReviewFragment newInstance(String data) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString("data", data);

        return fragment;
    }

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){

            try {
                data = new JSONObject(getArguments().getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

        if(data!=null){

            try {
                ((TextView) v.findViewById(R.id.txt_avg_rating)).setText(data.getString("a"));
                ((TextView) v.findViewById(R.id.txt_location_review_frag)).setText(data.getString("a"));
                ((ProgressBar) v.findViewById(R.id.progressBar_five_stars)).setProgress(data.getInt("a"));
                ((ProgressBar) v.findViewById(R.id.progressBar_four_stars)).setProgress(data.getInt("a"));
                ((ProgressBar) v.findViewById(R.id.progressBar_three_stars)).setProgress(data.getInt("a"));
                ((ProgressBar) v.findViewById(R.id.progressBar_two_stars)).setProgress(data.getInt("a"));

            } catch (JSONException e) {
                e.printStackTrace();
         }
      }
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
