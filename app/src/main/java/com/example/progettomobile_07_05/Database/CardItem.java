package com.example.progettomobile_07_05.Database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "item", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "email",
        childColumns = "email",
        onDelete = CASCADE))
public class CardItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    private int id;

    @ColumnInfo(name = "item_image")
    private String imageResource;

    @ColumnInfo(name = "item_name")
    private String productName;

    @ColumnInfo(name = "item_price")
    private String productPrice;

    @ColumnInfo(name = "item_description")
    private String productDescription;

    @ColumnInfo(name = "item_position")
    private String productPosition;

    /*@ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "longitude")
    private String longitude;*/

    @ColumnInfo(name = "email")
    private String emailUser;




    public CardItem(String imageResource, String productName, String productPrice, String productDescription, String productPosition, String emailUser){
        this.imageResource = imageResource;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productPosition = productPosition;
        this.emailUser = emailUser;


    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getProductPosition() {
        return productPosition;
    }
    /*public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }*/

    public String getEmailUser() {
        return emailUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
