package com.example.progettomobile_07_05;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.example.progettomobile_07_05.Utilities.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.ViewModel.AddViewModel;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;
import com.google.android.gms.maps.model.Circle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private AddViewModel addViewModel;
    private int actualPage;
    private int setActualMenuDrawer;
    private User actualUser = null;
    private Circle circle = null;

    private LocationManager locationManager;
    private LocationListener listener;
    private Location actualPosition;

    private int minPrice = -1;
    private int maxPrice = -1;

    private boolean relogin = false;

    private ListViewModel listViewModel;

    public void setRelogin(boolean value){
        this.relogin = value;
    }
    public boolean getRelogin(){
        return this.relogin;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        // c1a4158909d6c23f73df353c7cc0bbe963891a65  alberto
        // 4f40cda291c5f9634e1affd3db44947af61f705c  filippo

        if (savedInstanceState == null) {
            if (actualUser == null) {
                Utilities.insertFragment(this, new LoginFragment(), LoginFragment.class.getSimpleName());
                setActualMenuDrawer = 2;

            } else {
                Utilities.insertFragment(this, new HomeFragment(), HomeFragment.class.getSimpleName());
                setActualMenuDrawer = 0;
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

        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);
        actualPage = R.id.nav_home;
        setActualMenuDrawer();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setActualPosition(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //
            }

            @Override
            public void onProviderEnabled(String s) {
                //
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };


        configureGPS();





    }

    public void setActualMenuDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(setActualMenuDrawer).setChecked(true);
    }

    public void setUser(User u) {
        if(u == null){
            actualUser = null;
            TextView tEmail = findViewById(R.id.navemail);
            TextView tUsername = findViewById(R.id.navusername);
            tEmail.setText("");
            tUsername.setText("Ciao, effettua il login");

        }else{
            actualUser = u;
            TextView tEmail = findViewById(R.id.navemail);
            TextView tUsername = findViewById(R.id.navusername);
            tEmail.setText(actualUser.getEmail());
            tUsername.setText(actualUser.getNameUser());
        }

    }

    public User getActualUser() {
        return actualUser;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

           /* FragmentManager fm = getFragmentManager();
            int count = getFragmentManager().getBackStackEntryCount();
            String name = fm.getBackStackEntryAt(count - 1).getName();*/
            if (getVisibleFragment() instanceof LoginFragment
            || getVisibleFragment() instanceof HomeFragment || getVisibleFragment() instanceof MyProductFragment
            || getVisibleFragment() instanceof ProfileFragment) {

            }else{
                super.onBackPressed();
            }
        }

    }
    private Fragment getVisibleFragment() {
        androidx.fragment.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (actualPage != R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    actualPage = R.id.nav_home;
                    setActualMenuDrawer = 0;
                }
                break;
            case R.id.nav_profile:

                if (actualPage != R.id.nav_profile && actualUser != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    actualPage = R.id.nav_profile;
                    setActualMenuDrawer = 2;
                }
                break;
            case R.id.nav_myproduct:

                if (actualPage != R.id.nav_myproduct) {
                    Utilities.insertFragment(this, new MyProductFragment(), MyProductFragment.class.getSimpleName());
                    actualPage = R.id.nav_myproduct;
                    setActualMenuDrawer = 2;
                }
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
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                addViewModel.setImageBitmap(imageBitmap);
            }
        }
    }

    void configureGPS() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request_permission();
            }
        } else {
            // permission has been granted
            locationManager.requestLocationUpdates("gps", 5000, 0, listener);
        }
    }

    private void request_permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                ACCESS_COARSE_LOCATION)) {

            Snackbar.make(findViewById(R.id.fragment_container_view), "Location permission is needed because ...",
                    Snackbar.LENGTH_LONG)
                    .setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                        }
                    })
                    .show();
        } else {
            // permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                configureGPS();
                break;
            default:
                break;
        }
    }


    public Circle getCircle() {

        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Location getActualPosition() {
        return actualPosition;
    }
    public void setActualPosition(Location actualPosition) {
        this.actualPosition = actualPosition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openfilter:
                Utilities.insertFragment(this, new FilterFragment(), FilterFragment.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setActualPage(int value){
        this.actualPage = value;
    }


}