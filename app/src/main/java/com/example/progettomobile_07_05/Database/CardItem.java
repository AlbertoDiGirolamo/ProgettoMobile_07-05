package com.example.progettomobile_07_05.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
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




    public CardItem(String imageResource, String productName, String productPrice, String productDescription, String productPosition){
        this.imageResource = imageResource;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productPosition = productPosition;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}