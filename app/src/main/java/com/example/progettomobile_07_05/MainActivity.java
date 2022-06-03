package com.example.progettomobile_07_05;

import static com.example.progettomobile_07_05.Utilities.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.progettomobile_07_05.ViewModel.AddViewModel;
import com.example.progettomobile_07_05.RecyclerView.CardAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private AddViewModel addViewModel;
    private CardAdapter adapter;
    private int actualPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            Utilities.insertFragment(this, new HomeFragment(), HomeFragment.class.getSimpleName());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HomeFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_message);
        }*/

        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);
        actualPage = R.id.nav_home;
        //getCardItemList




    }



    @Override
    public void onBackPressed() {
       if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                if (actualPage != R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    actualPage = R.id.nav_home;
                }
                    break;
            case R.id.nav_message:
                if (actualPage != R.id.nav_message) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();
                    actualPage = R.id.nav_message;
                }
                break;
            case R.id.nav_profile:
                if (actualPage != R.id.nav_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    actualPage = R.id.nav_profile;
                }
                break;
            case R.id.nav_share:

                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            if(extras != null){
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                addViewModel.setImageBitmap(imageBitmap);
            }
        }
    }















}