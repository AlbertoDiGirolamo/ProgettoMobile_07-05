package com.example.progettomobile_07_05.RecyclerView;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.MainActivity;
import com.example.progettomobile_07_05.R;
import com.example.progettomobile_07_05.Utilities;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder>{
    ArrayList<CardItem> cardItemList;
    private Activity activity;
    private OnItemListener listener;
    private ArrayList<CardItem> cardItemListNotFiltered;

    public CardAdapter(OnItemListener listener, Activity activity){
        this.listener = listener;
        this.activity = activity;


    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return  new CardViewHolder(layoutView, listener);
    }
    /*public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/




    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentCardItem = cardItemList.get(position);

        String imagePath = currentCardItem.getImageResource();

            Bitmap bitmap = Utilities.getImageBitmap(activity, Uri.parse(imagePath));
            //Log.d("urii", getRealPathFromURI(activity.getBaseContext(), Uri.parse("content://media/external/images/media/76")));


            if (bitmap != null){
                //Log.d("uri", Uri.parse(imagePath).toString());
                //Log.d("uri", getRealPathFromURI(activity.getBaseContext(), Uri.parse(imagePath)));

                holder.productImageView.setImageBitmap(bitmap);
            }


//    content://com.android.providers.media.documents/document/image%3A77

        holder.productNameTextView.setText(currentCardItem.getProductName());
        holder.productPriceTextView.setText(currentCardItem.getProductPrice()+" €");
        holder.productDescriptionTextView.setText(currentCardItem.getProductDescription());
        holder.productPositionTextView.setText(currentCardItem.getProductPosition());
    }

    @Override
    public int getItemCount() {
        if (cardItemList != null)
            return cardItemList.size();
        return 0;


    }

    public void filterSearch(String text){

        ArrayList<CardItem> cardItemsFiltered = new ArrayList<>();
        for (CardItem item : cardItemListNotFiltered){
            if(item.getProductName().toLowerCase().contains(text.toLowerCase())){
                cardItemsFiltered.add(item);
            }
        }
        filterList(cardItemsFiltered);


    }


    public void notFilter(){
        filterList(cardItemListNotFiltered);
    }

    public void filterList(ArrayList<CardItem> filteredList){
        cardItemList = filteredList;
        notifyDataSetChanged();
    }

    public  void setData(List<CardItem> list){
        this.cardItemList = new ArrayList<>(list);
        this.cardItemListNotFiltered = new ArrayList<>(list);

        final CardItemDiffCallBack diffCallBack = new CardItemDiffCallBack(this.cardItemList, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallBack);
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    public CardItem getItemSelected(int position) {
        return  cardItemList.get(position);
    }


}
