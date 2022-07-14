package com.example.progettomobile_07_05;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private GoogleMap mMap;
    private LatLng actualPosition = new LatLng(44.137221, 12.241975);
    private Circle circle = null;
    private SeekBar seekBar;
    private TextView msg;
    private ListViewModel listViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MainActivity activity =(MainActivity) getActivity();



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                Location pos =  ((MainActivity)getActivity()).getActualPosition();
                actualPosition = new LatLng(pos.getLatitude(),pos.getLongitude());

                mMap.addMarker(new MarkerOptions().position(actualPosition).title("Io mi trovo qui"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actualPosition,10));



                if (((MainActivity)getActivity()).getCircle()==null){
                    seekBar.setProgress(5);
                }else{
                    seekBar.setProgress((int) ((MainActivity)getActivity()).getCircle().getRadius()/ 1000);
                }


                listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);
                listViewModel.getCardItems().observe(activity, new Observer<List<CardItem>>() {
                    @Override
                    public void onChanged(List<CardItem> cardItem) {
                        for (CardItem c : cardItem){


                            List<Address> address = null;
                            try {
                                Geocoder coder = new Geocoder(getContext());

                                address = coder.getFromLocationName(c.getProductPosition(), 5);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (address == null) {
                                return;
                            }
                            Address location = address.get(0);
                            location.getLatitude();
                            location.getLongitude();
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            mMap.addMarker(new MarkerOptions().position(latLng).title(c.getProductName()));
                        }

                    }
                });

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        FloatingActionButton fl = getActivity().findViewById(R.id.fab_add);
        fl.setVisibility(View.INVISIBLE);
        SearchView searchView = getActivity().findViewById(R.id.search_icon);
        searchView.setVisibility(View.INVISIBLE);

        seekBar =getActivity().findViewById(R.id.seekbar);
        msg = getActivity().findViewById(R.id.distancetext);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (circle != null)
                    circle.remove();

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

        getActivity().findViewById(R.id.btnsalvamap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.insertFragment((AppCompatActivity) getActivity(), new FilterFragment(), FilterFragment.class.getSimpleName());
            }
        });
    }
}
