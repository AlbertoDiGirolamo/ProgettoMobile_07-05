package com.example.progettomobile_07_05;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.example.progettomobile_07_05.Utilities.REQUEST_IMAGE_CAPTURE;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.Database.UserRepository;
import com.example.progettomobile_07_05.ViewModel.AddViewModel;
import com.example.progettomobile_07_05.RecyclerView.CardAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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

        /*if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HomeFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_message);
        }*/

        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);

        actualPage = R.id.nav_home;
        //getCardItemList

        setActualMenuDrawer();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("position",String.valueOf(location.getLongitude() + location.getLatitude()));
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
        actualUser = u;
        TextView tEmail = findViewById(R.id.navemail);
        TextView tUsername = findViewById(R.id.navusername);
        tEmail.setText(actualUser.getEmail());
        tUsername.setText(actualUser.getNameUser());
    }

    public User getActualUser() {
        return actualUser;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

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
            case R.id.nav_message:
                if (actualPage != R.id.nav_message) {
                    /*Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);*/
                    Utilities.insertFragment(this, new FilterFragment(), FilterFragment.class.getSimpleName());
                    actualPage = R.id.nav_message;
                    setActualMenuDrawer = 1;
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
            case R.id.openmap:
                Utilities.insertFragment(this, new MapFragment(), MapFragment.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}