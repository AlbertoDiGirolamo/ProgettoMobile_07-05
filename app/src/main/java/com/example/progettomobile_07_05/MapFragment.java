package com.example.progettomobile_07_05;

import android.graphics.Color;
import android.icu.text.Transliterator;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment {
    private GoogleMap mMap;
    private LatLng actualPosition = new LatLng(44.137221, 12.241975);
    private Circle circle = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                mMap.addMarker(new MarkerOptions().position(actualPosition).title("Cesena"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actualPosition,10));

                /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions= new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude)
                    }
                });*/

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity =(MainActivity) getActivity();
        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setVisibility(View.INVISIBLE);

        Location pos =  ((MainActivity)getActivity()).getActualPosition();
        actualPosition = new LatLng(pos.getLatitude(),pos.getLongitude());

        SeekBar seekBar =getActivity().findViewById(R.id.seekbar);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (circle != null)
                    circle.remove();
                TextView msg = getActivity().findViewById(R.id.distancetext);
                msg.setText(seekBar.getProgress()+" km");
                circle = mMap.addCircle(new CircleOptions()
                        .center(actualPosition)
                        .radius(seekBar.getProgress()*1000)
                        .strokeWidth(0)
                        .strokeColor(Color.parseColor("#E671cce7"))
                        .fillColor(Color.parseColor("#8071cce7")));
                ((MainActivity)getActivity()).setCircle(circle);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
