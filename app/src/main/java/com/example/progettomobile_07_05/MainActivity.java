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
import android.widget.TextView;
import android.widget.Toast;

import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.Database.UserRepository;
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
    private int setActualMenuDrawer;

    private User actualUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (actualUser == null){
                Utilities.insertFragment(this, new LoginFragment(), LoginFragment.class.getSimpleName());
                setActualMenuDrawer=2;

            }else{
                Utilities.insertFragment(this, new HomeFragment(), HomeFragment.class.getSimpleName());
                setActualMenuDrawer=0;
            }

        }


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

        setActualMenuDrawer();


    }
    public void setActualMenuDrawer(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(setActualMenuDrawer).setChecked(true);
    }

    public void setUser(User u){
        actualUser = u;
        TextView tEmail = findViewById(R.id.navemail);
        TextView tUsername = findViewById(R.id.navusername);
        tEmail.setText(actualUser.getEmail());
        tUsername.setText(actualUser.getNameUser());
    }
    public User getActualUser(){
        return actualUser;
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
                    setActualMenuDrawer=0;
                }
                    break;
            case R.id.nav_message:
                if (actualPage != R.id.nav_message) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();
                    actualPage = R.id.nav_message;
                    setActualMenuDrawer=1;
                }
                break;
            case R.id.nav_profile:

                if (actualPage != R.id.nav_profile && actualUser != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    actualPage = R.id.nav_profile;
                    setActualMenuDrawer=2;
                }
                break;
            case R.id.nav_myproduct:

                if (actualPage != R.id.nav_myproduct) {
                    Utilities.insertFragment(this, new MyProductFragment(), MyProductFragment.class.getSimpleName());
                    actualPage = R.id.nav_myproduct;
                    setActualMenuDrawer=2;
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