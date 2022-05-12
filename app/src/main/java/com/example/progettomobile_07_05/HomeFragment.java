package com.example.progettomobile_07_05;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.RecyclerView.CardAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        MainActivity activity =(MainActivity) getActivity();
        if(activity != null){
            setRecyclerView(activity);

            /* DrawerLayout drawerLayout = (DrawerLayout) getView();
            Toolbar toolbar =  drawerLayout.findViewById(R.id.toolbar);
            DrawerLayout drawer;
           // MainActivity main = (MainActivity) getActivity();


            drawer = drawerLayout.findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            ImageButton button = (ImageButton) getView().findViewById(R.id.imageButtonMenu);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).openDrawer(drawer);
                }
            });

            View view_fab =  drawerLayout.findViewById(R.id.fab_add).getRootView();

            FloatingActionButton floatingActionButton = view_fab.findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(),
                            AddFragment.class.getSimpleName());
                }

            });



*/
        }else{
            Log.e(String.valueOf(LOG), "Activity is null");
        }

    }



    private void setRecyclerView(final Activity activity){
        recyclerView = activity.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        List<CardItem> list = new ArrayList<>();
        list.add(new CardItem("ic_baseline_android_24", "Name", "Price","description"));
        adapter = new CardAdapter(list, activity);
        recyclerView.setAdapter(adapter);


    }
}
