package com.example.progettomobile_07_05;

import static com.example.progettomobile_07_05.Utilities.REQUEST_IMAGE_CAPTURE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.progettomobile_07_05.ViewModel.AddViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        SearchView sv = activity.findViewById(R.id.search_icon);
        sv.setVisibility(View.INVISIBLE);

        AddFragment addFragment = this;
        View viewAdd = activity.findViewById(R.id.fab_add).getRootView();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.insertFragment((AppCompatActivity) activity, new AddFragment(),
                        AddFragment.class.getSimpleName());
            }

        });

        view.findViewById(R.id.capture_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(activity.getPackageManager()) != null){
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

        view.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bitmap bitmap = addViewModel.getImageBitmap().getValue();
                try{
                    if(bitmap != null){
                        saveImage(bitmap,activity);

                    }
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        });


    }

    private void saveImage(Bitmap bitmap, Activity activity) throws FileNotFoundException {
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


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Activity activity = getActivity();
        SearchView sv = activity.findViewById(R.id.search_icon);
        sv.setVisibility(View.VISIBLE);
    }
}
