package in.jaaga.thebachaoproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {
        private int mwidth;

    private OnFragmentInteractionListener mListener;


    public static MenuFragment newInstance(int width) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
      //  args.putString(ARG_PARAM1, param1);
        args.putInt("mwidth", width);
        fragment.setArguments(args);
        return fragment;
    }

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
            mwidth = getArguments().getInt("mwidth");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        TableLayout table_menu= (TableLayout) v.findViewById(R.id.table_menu_container);
        Button btn_location_search= (Button) v.findViewById(R.id.btn_search_location);
        Button btn_get_reviews= (Button) v.findViewById(R.id.btn_get_reviews);
        Button btn_write_reviews= (Button) v.findViewById(R.id.btn_write_review);
        Button btn_user_location= (Button) v.findViewById(R.id.btn_location);

        btn_location_search.setOnClickListener(this);
        btn_get_reviews.setOnClickListener(this);
        btn_write_reviews.setOnClickListener(this);
        btn_user_location.setOnClickListener(this);


        //table_menu.setLayoutParams(new TableLayout.LayoutParams()).(mwidth/2);

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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_location:
                mListener.goToUserLocation();

                break;
            case R.id.btn_search_location:
                mListener.searchLocation();
                break;
            case R.id.btn_write_review:
                mListener.writeReview();
                break;
            case R.id.btn_get_reviews:
                mListener.getReviews();
                break;

        }
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

         public void goToUserLocation();
         public void searchLocation();
         public void getReviews();
         public void writeReview();

    }
}
