package com.example.progettomobile_07_05.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addUser(User user);

    @Transaction
    @Query("SELECT * FROM user ORDER BY user_id DESC")
    LiveData<List<User>> getUsers();

    @Query("SELECT * FROM user WHERE email LIKE :email AND " +
            "password LIKE :password LIMIT 1")
    LiveData<User> getUserForLogin(String email, String password);

    @Query("SELECT * FROM user WHERE email LIKE :email")
    LiveData<User> getUserEmail(String email);


}
