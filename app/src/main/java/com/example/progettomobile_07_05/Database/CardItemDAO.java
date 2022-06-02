package com.example.progettomobile_07_05.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.progettomobile_07_05.CardItem;

import java.util.List;

@Dao
public interface CardItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addCardItem(CardItem cardItem);

    @Transaction
    @Query("SELECT * FROM item ORDER BY item_id DESC")
    LiveData<List<CardItem>> getCardItems();
}
