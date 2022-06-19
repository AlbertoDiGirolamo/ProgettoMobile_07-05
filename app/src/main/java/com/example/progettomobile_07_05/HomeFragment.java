package com.example.progettomobile_07_05;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.RecyclerView.CardAdapter;
import com.example.progettomobile_07_05.RecyclerView.OnItemListener;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment  implements OnItemListener {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ListViewModel listViewModel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Location location;

    private Circle circle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_message).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_myproduct).setVisible(true);


        MainActivity activity =(MainActivity) getActivity();

        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setVisibility(View.VISIBLE);

        if(activity != null){
            setRecyclerView(activity);

            listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);
            listViewModel.getCardItems().observe(activity, new Observer<List<CardItem>>() {
                @Override
                public void onChanged(List<CardItem> cardItem) {

                    adapter.setData(cardItem);
                    recyclerView.setAdapter(adapter);

                }
            });

            FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(),
                            AddFragment.class.getSimpleName());
                }

            });

            SearchView searchView = getActivity().findViewById(R.id.search_icon);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText ==""){
                        adapter.notFilter();
                    }
                    return false;
                }

            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    adapter.notFilter();
                    return false;
                }
            });



        }else{
            Log.e(String.valueOf(LOG), "Activity is null");
        }

        //43.129479, 12.970972
        Location roma = new Location("");
        roma.setLatitude(41.859816);
        roma.setLongitude(12.548127);
        Location cesena = new Location("");
        cesena.setLatitude(44.138317);
        cesena.setLongitude(12.241815);
        circle = ((MainActivity)getActivity()).getCircle();
        location = ((MainActivity)getActivity()).getActualPosition();
        //a@gisProductInCircle(roma);
        isProductInCircle(roma);
    }

    private boolean isProductInCircle(Location productLocation){
        float[] distance = new float[1];
        Location.distanceBetween( productLocation.getLatitude(), productLocation.getLongitude(),
                location.getLatitude(), location.getLongitude(), distance);
        double radius;
        if (circle == null){
            radius = 5000;

        }else{
            radius = circle.getRadius();
        }


        if( distance[0] > radius ){

            Toast.makeText(getActivity(), "Outside, distance from center: " + distance[0] + " radius: " + radius, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(getActivity(), "Inside, distance from center: " + distance[0] + " radius: " + radius , Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private void setRecyclerView(final Activity activity){
        recyclerView = activity.findViewById(R.id.recycler_view_home);
        recyclerView.setHasFixedSize(true);


        final OnItemListener listener = this;
        adapter = new CardAdapter(listener, activity);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(int position) {
        Activity activity = getActivity();
        if (activity != null){
            Utilities.insertFragment((AppCompatActivity) activity, new DetailsFragment(), DetailsFragment.class.getSimpleName());
            listViewModel.setItemSelected(adapter.getItemSelected(position));
        }
    }

}
