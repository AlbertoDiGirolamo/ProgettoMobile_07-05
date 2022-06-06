package com.example.progettomobile_07_05;

import static com.example.progettomobile_07_05.Utilities.REQUEST_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.ViewModel.AddViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddFragment extends Fragment {
    TextInputEditText productTIET;
    TextInputEditText descriptionTIET;
    TextInputEditText priceTIET;
    TextInputEditText positionTIET;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private boolean requestingLocationUpdates = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            initializeLocation(activity);
            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result){
                                startLocationUpdates(activity);
                                Log.d("LAB", "PERMISSION GRANTED");
                            }else{
                                Log.d("LAB", "PERMISSION NOT GRANTED");
                                showDialog(activity);
                            }
                        }
                    });

        }
        SearchView sv = activity.findViewById(R.id.search_icon);
        sv.setVisibility(View.INVISIBLE);

        AddFragment addFragment = this;
        View viewAdd = getActivity().findViewById(R.id.fab_add);
        /*FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(),
                        AddFragment.class.getSimpleName());
            }

        });*/

        view.findViewById(R.id.capture_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.picture_displayed_imageview);
        AddViewModel addViewModel = new ViewModelProvider((ViewModelStoreOwner) activity)
                .get(AddViewModel.class);

        addViewModel.getImageBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });

        productTIET = this.getView().findViewById(R.id.product_name_edittext);
        descriptionTIET = this.getView().findViewById(R.id.description_edittext);
        priceTIET = this.getView().findViewById(R.id.price_edittext);
        positionTIET = this.getView().findViewById(R.id.product_position_edittext);

        priceTIET.setRawInputType(Configuration.KEYBOARD_12KEY);

        priceTIET.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    priceTIET.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[€,.]", "");
                    cleanString = cleanString.replaceAll("\\s+", "");
                    //cleanString = cleanString.replaceAll(".", "");

                    //Log.d("cleanString",String.valueOf(cleanString.length()));
                    Log.d("cleanString",cleanString);
                    double parsed = Double.parseDouble(cleanString);

                    Log.d("parsed",String.valueOf(parsed));
                    String formatted = NumberFormat.getCurrencyInstance(Locale.ITALY).format((parsed/100));
                    //formatted = formatted.replaceAll("[.]", ",");
                    current = formatted;
                    priceTIET.setText(formatted);
                    priceTIET.setSelection(formatted.length());

                    priceTIET.addTextChangedListener(this);
                }
            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        priceTIET.setMovementMethod(null);


        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = addViewModel.getImageBitmap().getValue();
                String imageUriString;
                try {
                    if (bitmap != null) {
                        imageUriString = String.valueOf(saveImage(bitmap, activity));

                    } else {
                        imageUriString = "ic_baseline_android_24";
                    }
                    if (productTIET.getText() != null && descriptionTIET.getText() != null
                            && priceTIET.getText() != null && positionTIET.getText() != null) {
                        addViewModel.addCardItem(new CardItem(imageUriString, productTIET.getText().toString(),
                                priceTIET.getText().toString(), descriptionTIET.getText().toString(),
                                positionTIET.getText().toString()));
                        addViewModel.setImageBitmap(null);

                        ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.gps_button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                requestingLocationUpdates = true;

                startLocationUpdates(activity);
            }
        });


    }


    public void onResume(){
        super.onResume();
        if(requestingLocationUpdates && getActivity() != null){
            startLocationUpdates(getActivity());
        }
    }
    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void initializeLocation(Activity activity) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        //Set the desired interval for active location updates
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //Update UI with the location data
                Location location = locationResult.getLastLocation();
                String text = location.getLatitude() + ", " + location.getLongitude();

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    /*String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();*/
                    positionTIET.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }





                requestingLocationUpdates = false;
                stopLocationUpdates();


            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates(Activity activity) {
        final String PERMISSION_REQUESTED = Manifest.permission.ACCESS_FINE_LOCATION;
        //permission granted
        if (ActivityCompat.checkSelfPermission(activity, PERMISSION_REQUESTED) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else if (ActivityCompat
                .shouldShowRequestPermissionRationale(activity, PERMISSION_REQUESTED)) {
            //permission denied before
            showDialog(activity);
        } else {
            //ask for the permission
            requestPermissionLauncher.launch(PERMISSION_REQUESTED);
        }

    }
    private void showDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("Permission denied, but needed for gps functionality.")
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) ->
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))))
                .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.cancel()))
                .create()
                .show();
    }

    private Uri saveImage(Bitmap bitmap, Activity activity) throws FileNotFoundException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ITALY)
                .format(new Date());
        String name = "JPEG_" + timestamp + ".jpg";

        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");

        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues);

        Log.d("AddFragment", String.valueOf(imageUri));

        OutputStream outputStream = contentResolver.openOutputStream(imageUri);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Activity activity = getActivity();
        SearchView sv = activity.findViewById(R.id.search_icon);
        sv.setVisibility(View.VISIBLE);
    }
}
