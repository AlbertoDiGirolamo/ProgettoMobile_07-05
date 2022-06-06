package com.example.progettomobile_07_05.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CardItemRepository {
    private CardItemDAO cardItemDAO;
    private LiveData<List<CardItem>> cardItemList;

    public CardItemRepository(Application application){
        CardItemDatabase db = CardItemDatabase.getDatabse(application);
        cardItemDAO = db.cardItemDAO();
        cardItemList = cardItemDAO.getCardItems();
    }

    public LiveData<List<CardItem>> getCardItemList(){
        return  cardItemList;
    }

    public void addCardItem(CardItem cardItem){
        CardItemDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                cardItemDAO.addCardItem(cardItem);
            }
        });
    }
}
