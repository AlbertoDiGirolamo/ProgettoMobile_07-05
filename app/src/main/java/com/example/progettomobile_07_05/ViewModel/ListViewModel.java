package com.example.progettomobile_07_05.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.Database.CardItemRepository;
import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.Database.UserRepository;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();
    private LiveData<List<CardItem>> cardItems;

    private LiveData<List<User>> users;

    public ListViewModel(@NonNull Application application) {

        super(application);
        CardItemRepository cardItemRepository = new CardItemRepository(application);
        cardItems = cardItemRepository.getCardItemList();

        UserRepository userRepository = new UserRepository(application);
        users = userRepository.getUsersList();


    }
    public LiveData<List<User>> getUsers() {

        return users;
    }
    public LiveData<List<CardItem>> getCardItems() {

        return cardItems;
    }

    public MutableLiveData<CardItem> getItemSelected(){
        return itemSelected;
    }

    public void setItemSelected(CardItem cardItem){
        itemSelected.setValue(cardItem);
    }



}













