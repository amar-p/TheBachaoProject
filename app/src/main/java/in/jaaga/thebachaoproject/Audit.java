package in.jaaga.thebachaoproject;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

/***A Dialog Fragment to show to the user when long pressed on mapView
 * Take the inputs in this dialog fragment and pass it to the MainActivity
 * for further processing.
 */

public class Audit extends DialogFragment {
    float mLat;
    float mLng;

    RatingBar ratingBar;
    float rating;
    String mname,memail,mfeeling;
    int check_transport,check_street_light;


    static Audit newInstance(double lat,double lng) {
        Audit f = new Audit();


        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        f.setArguments(args);


        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLat = (float) getArguments().getDouble("lat");
        mLng = (float) getArguments().getDouble("lng");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_audit, container, false);
        final TextView tvRating = (TextView) v.findViewById(R.id.txt_rating);
        TextView tvLat = (TextView) v.findViewById(R.id.txt_lat);
        TextView tvLng = (TextView) v.findViewById(R.id.txt_lng);
        final EditText name= (EditText) v.findViewById(R.id.edit_txt_name);
        final EditText email= (EditText) v.findViewById(R.id.edit_txt_email);
        final EditText feeling= (EditText) v.findViewById(R.id.edit_txt_feeling);
        final CheckBox transport_available= (CheckBox) v.findViewById(R.id.check_transport_available);
        final CheckBox street_lights__available= (CheckBox) v.findViewById(R.id.check_street_lights);
        tvLat.setText("Latitude : " + String.valueOf(mLat));
        tvLng.setText("Longitude : " + String.valueOf(mLng));
        ratingBar= (RatingBar) v.findViewById(R.id.ratingBar);
        rating=ratingBar.getRating();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                System.out.println(rating);

                setRating(rating);

                if(rating==(float)5) {
                    tvRating.setText("Very Safe");
                }
                else if(rating==(float)4){
                        tvRating.setText("Safe");
                    }
                else if(rating==(float)3){
                        tvRating.setText("Ok");
                    }
                else if(rating==(float)2){
                        tvRating.setText("Not Safe");
                    }
                else if(rating==(float)1){
                        tvRating.setText("UnSafe");
                    }

            }
        });

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                mname=name.getText().toString().trim();
                memail=email.getText().toString().trim();
                mfeeling=feeling.getText().toString().trim();
                if(transport_available.isChecked()){
                check_transport=1;
                }
                else{
                    check_transport=0;
                }
                if(street_lights__available.isChecked()){
                    check_street_light=1;
                }
                else{
                    check_street_light=0;
                }

                // When button is clicked, call up to owning activity.
                ((MainActivity)getActivity()).setAudit(mname,memail,mfeeling,check_transport,check_street_light,getRating(),mLat,mLng);
                getDialog().dismiss();
            }
        });
        getDialog().setTitle(getString(R.string.dialog_audit_title));
        return v;
    }

    void setRating(float rating){
        this.rating=rating;
    }

    int getRating(){

        return (int)rating;
    }




}
