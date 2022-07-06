package com.example.progettomobile_07_05.Database;

import static android.webkit.WebViewDatabase.getInstance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {CardItem.class, User.class}, version = 1, exportSchema = true)
public abstract class Database extends RoomDatabase {
    public abstract CardItemDAO cardItemDAO();
    public abstract UserDAO userDAO();



    private static volatile Database ISTANCE;
    static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static Database getDatabase(final Context context){

        if(ISTANCE == null){
            synchronized (Database.class){
                if(ISTANCE == null){
                    ISTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "product_database")
                            .createFromAsset("database/product_database.db")
                            .build();
                }
            }
        }
        return ISTANCE;
    }


}
