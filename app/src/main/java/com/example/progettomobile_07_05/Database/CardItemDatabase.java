package com.example.progettomobile_07_05.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.progettomobile_07_05.CardItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CardItem.class}, version = 1)
public abstract class CardItemDatabase extends RoomDatabase {
    public abstract CardItemDAO cardItemDAO();


    private static volatile CardItemDatabase ISTANCE;
    static final ExecutorService executor = Executors.newFixedThreadPool(4);

    static CardItemDatabase getDatabse(final Context context){

        if(ISTANCE == null){
            synchronized (CardItemDatabase.class){
                if(ISTANCE == null){
                    ISTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CardItemDatabase.class, "product_database")
                            .build();
                }
            }
        }
        return ISTANCE;
    }

}
