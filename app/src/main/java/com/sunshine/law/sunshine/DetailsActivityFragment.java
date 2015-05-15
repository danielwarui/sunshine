package com.sunshine.law.sunshine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView textViewWeather = (TextView) rootView.findViewById(R.id.textViewWeather);

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            String theWeather = bundle.getString("weatherData");
            textViewWeather.setText(theWeather);
        }

        return rootView;
    }
}
