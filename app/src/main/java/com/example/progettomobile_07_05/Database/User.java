package com.example.progettomobile_07_05.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "name")
    private String nameUser;

    @ColumnInfo(name = "surname")
    private String surnameUser;

    @ColumnInfo(name = "telephone_number")
    private String telephoneNumber;

    public User(String email,String password, String nameUser, String surnameUser, String telephoneNumber){
        this.email = email;
        this.password = password;
        this.nameUser = nameUser;
        this.surnameUser = surnameUser;
        this.telephoneNumber = telephoneNumber;



    }

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getSurnameUser() {
        return surnameUser;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }


}
