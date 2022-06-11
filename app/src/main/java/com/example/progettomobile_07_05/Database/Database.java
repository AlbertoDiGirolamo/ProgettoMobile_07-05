package com.example.progettomobile_07_05.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {CardItem.class, User.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract CardItemDAO cardItemDAO();
    public abstract UserDAO userDAO();


    private static volatile Database ISTANCE;
    static final ExecutorService executor = Executors.newFixedThreadPool(4);

    static Database getDatabse(final Context context){

        if(ISTANCE == null){
            synchronized (Database.class){
                if(ISTANCE == null){
                    ISTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "product_database")
                            .build();
                }
            }
        }
        return ISTANCE;
    }

}
