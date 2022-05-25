package com.example.progettomobile_07_05.ViewModel;

import static com.example.progettomobile_07_05.Utilities.drawableToBitmap;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.progettomobile_07_05.R;

public class AddViewModel extends AndroidViewModel {
    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();
    public void setImageBitmap(Bitmap bitmap){
        imageBitmap.setValue((bitmap));
    }
    public MutableLiveData<Bitmap> getImageBitmap(){
        return  imageBitmap;
    }

    private final Application application;

    public AddViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        initializeBitmap();

    }

    public void initializeBitmap(){
        Drawable drawable = ResourcesCompat.getDrawable(application.getResources(), R.drawable.ic_launcher_foreground,
                application.getTheme());
        Bitmap bitmap = drawableToBitmap(drawable);

        imageBitmap.setValue(bitmap);
    }



}



















