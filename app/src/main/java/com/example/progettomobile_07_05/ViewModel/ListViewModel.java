package com.example.progettomobile_07_05.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.progettomobile_07_05.CardItem;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private  final  MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();
    public  MutableLiveData<List<CardItem>> cardItems;

    public ListViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<CardItem> getItemSelected(){
        return itemSelected;
    }
    public void setItemSelected(CardItem cardItem){
        itemSelected.setValue(cardItem);
    }

    public LiveData<List<CardItem>> getCardItems() {
        if ( cardItems == null){
            cardItems = new MutableLiveData<>();
            loadItems();
        }
        return cardItems;
    }

    private void loadItems() {
        addCardItem(new CardItem("ic_baseline_android_24", "Name 1", "Price","Description", "Position"));
        addCardItem(new CardItem("ic_baseline_android_24", "Name 2", "Price","Description", "Position"));
    }

    public void addCardItem(CardItem item){
        ArrayList<CardItem> list = new ArrayList<>();
        list.add(item);
        if(cardItems.getValue() != null){
            list.addAll(cardItems.getValue());
        }
        cardItems.setValue(list);
    }
}
