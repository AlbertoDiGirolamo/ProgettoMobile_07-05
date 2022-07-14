package com.example.progettomobile_07_05;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.ContentObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.Database.CardItemRepository;
import com.example.progettomobile_07_05.RecyclerView.CardAdapter;
import com.example.progettomobile_07_05.RecyclerView.OnItemListener;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Circle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment  implements OnItemListener {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ListViewModel listViewModel;
    private Location location;
    private boolean isView = false;

    private Circle circle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        super.onViewCreated(view, savedInstanceState);

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_myproduct).setVisible(true);


        MainActivity activity =(MainActivity) getActivity();

        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_add_24));
        fl.setVisibility(View.VISIBLE);


        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        circle = activity.getCircle();
        location = activity.getActualPosition();
        int min = activity.getMinPrice();
        int max = activity.getMaxPrice();

        if(activity != null){
            setRecyclerView(activity);


            listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);

            listViewModel.getCardItems().observe(activity, new Observer<List<CardItem>>() {

                @Override
                public void onChanged(List<CardItem> cardItems) {
                    List<CardItem> cardItemFiltered = new ArrayList<>();
                    for (CardItem c : cardItems){
                        List<Address> addresses = new ArrayList<>();
                        try {
                            addresses = geocoder.getFromLocationName(c.getProductPosition(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(addresses.size() > 0) {
                            Double latitude= addresses.get(0).getLatitude();
                            Double longitude= addresses.get(0).getLongitude();
                            Location locationProduct = new Location("locationProduct");
                            locationProduct.setLatitude(latitude);
                            locationProduct.setLongitude(longitude);
                            if(isProductInCircle(locationProduct)){
                                Float price = Float.valueOf(c.getProductPrice());

                                if (min != -1 && max != -1){
                                    if(price > min && price < max){
                                        cardItemFiltered.add(c);
                                    }
                                }else if(min == -1 && max != -1){
                                    if(price < max){
                                        cardItemFiltered.add(c);
                                    }
                                }else if(min != -1 && max == -1){
                                    if(price > min){
                                        cardItemFiltered.add(c);
                                    }
                                }else if(min == -1 && max == -1){
                                    cardItemFiltered.add(c);
                                }


                            }
                        }

                    }

                    adapter.setData(cardItemFiltered);
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
            searchView.setVisibility(View.VISIBLE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.filterSearch(query);
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

       // adapter.notifyDataSetChanged();
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

            //Toast.makeText(getActivity(), "Outside, distance from center: " + distance[0] + " radius: " + radius, Toast.LENGTH_LONG).show();
            return false;
        } else {
            //Toast.makeText(getActivity(), "Inside, distance from center: " + distance[0] + " radius: " + radius , Toast.LENGTH_LONG).show();
            return true;
        }

    }

    private void setRecyclerView(final Activity activity){
        final OnItemListener listener = this;
        adapter = new CardAdapter(listener, getActivity());

        recyclerView = activity.findViewById(R.id.recycler_view_home);
        recyclerView.setHasFixedSize(true);
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

    private List<CardItem> filterPrice(List<CardItem> cardItemToFilter, int min, int max){
        ArrayList<CardItem> cardItemsFiltered = new ArrayList<>();
        for (CardItem item : cardItemToFilter){
            Float price = Float.valueOf(item.getProductPrice());
            if (min != -1 && max != -1){
                if(price > min && price < max){
                    cardItemsFiltered.add(item);
                }
            }else if(min == -1 && max != -1){
                if(price < max){
                    cardItemsFiltered.add(item);
                }
            }else if(min != -1 && max == -1){
                if(price > min){
                    cardItemsFiltered.add(item);
                }
            }else if(min == -1 && max == -1){
                cardItemsFiltered.add(item);
            }

        }
        return cardItemsFiltered;
    }


}
