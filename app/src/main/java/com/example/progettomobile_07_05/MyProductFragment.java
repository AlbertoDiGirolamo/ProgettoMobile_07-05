package com.example.progettomobile_07_05;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

public class MyProductFragment extends Fragment  implements OnItemListener{
    private RecyclerView recyclerView;
    private ListViewModel listViewModel;
    private CardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myproduct, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity =(MainActivity) getActivity();
        FloatingActionButton fl = activity.findViewById(R.id.fab_add);
        fl.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_add_24));


        if(activity != null) {
            setRecyclerView(activity);

            listViewModel = new ViewModelProvider(activity).get(ListViewModel.class);
            listViewModel.getCardItems().observe(activity, new Observer<List<CardItem>>() {
                @Override
                public void onChanged(List<CardItem> cardItem) {
                    String userEmail = ((MainActivity)getActivity()).getActualUser().getEmail();
                    List<CardItem> userCardItem = new ArrayList<>();
                    for (CardItem c : cardItem ){
                        if (c.getEmailUser().matches(userEmail)){
                            userCardItem.add(c);
                        }
                    }
                    adapter.setData(userCardItem);
                    recyclerView.setAdapter(adapter);

                }
            });
        }

    }

    private void setRecyclerView(final Activity activity){
        recyclerView = activity.findViewById(R.id.recycler_view_myproduct);
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
