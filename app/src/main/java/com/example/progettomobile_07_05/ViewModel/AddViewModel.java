package com.example.progettomobile_07_05.ViewModel;

import static com.example.progettomobile_07_05.Utilities.drawableToBitmap;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.progettomobile_07_05.CardItem;
import com.example.progettomobile_07_05.Database.CardItemRepository;
import com.example.progettomobile_07_05.R;

public class AddViewModel extends AndroidViewModel {
    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();
    public MutableLiveData<Bitmap> getImageBitmap(){
        return  imageBitmap;
    }

    private CardItemRepository repository;

    public AddViewModel(@NonNull Application application) {
        super(application);
        repository = new CardItemRepository(application);

    }
    public void setImageBitmap(Bitmap bitmap){
        imageBitmap.setValue(bitmap);
    }

    public LiveData<Bitmap> getBitmap(){
        return imageBitmap;
    }

    public void addCardItem(CardItem item){
        repository.addCardItem(item);
    }





}



















