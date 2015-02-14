package in.jaaga.thebachaoproject;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/***A Dialog Fragment to show to the user when long pressed on mapView
 * Take the inputs in this dialog fragment and pass it to the MainActivity
 * for further processing.
 */

public class Audit extends DialogFragment {
    float mLat;
    float mLng;


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
        TextView tvLat = (TextView) v.findViewById(R.id.txt_lat);
        tvLat.setText("Latitude : " + String.valueOf(mLat));
        TextView tvLng = (TextView) v.findViewById(R.id.txt_lng);
        tvLng.setText("Longitude : " + String.valueOf(mLng));

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((MainActivity)getActivity()).setAudit();
                getDialog().dismiss();
            }
        });
        getDialog().setTitle(getString(R.string.dialog_audit_title));
        return v;
    }

}
