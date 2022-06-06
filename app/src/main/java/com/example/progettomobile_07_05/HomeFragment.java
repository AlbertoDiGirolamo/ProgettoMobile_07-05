package com.example.progettomobile_07_05;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.RecyclerView.CardAdapter;
import com.example.progettomobile_07_05.RecyclerView.OnItemListener;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  implements OnItemListener {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ListViewModel listViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        MainActivity activity =(MainActivity) getActivity();
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
                    return false;
                }
            });



        }else{
            Log.e(String.valueOf(LOG), "Activity is null");
        }

    }



    private void setRecyclerView(final Activity activity){
        recyclerView = activity.findViewById(R.id.recycler_view);
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
/*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }*/
}
