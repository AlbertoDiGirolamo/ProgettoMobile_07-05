package com.example.progettomobile_07_05.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UserDAO userDAO;
    private LiveData<List<User>> userList;

    public UserRepository(Application application){
        Database db = Database.getDatabase(application);
        userDAO = db.userDAO();
        userList = userDAO.getUsers();

    }

    public LiveData<List<User>> getUsersList(){
        return  userList;
    }

    public User getUserFromEmail(String email){
        return userDAO.getUserEmail(email);
    }
    public LiveData<User> checkLogin(String email, String password){
        return userDAO.getUserForLogin(email, password);
    }

    public void addUser(User user){
        Database.executor.execute(new Runnable() {
            @Override
            public void run() {
                userDAO.addUser(user);
            }
        });
    }
}
