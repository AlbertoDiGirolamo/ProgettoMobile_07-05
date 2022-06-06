package com.example.progettomobile_07_05.RecyclerView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.progettomobile_07_05.Database.CardItem;

import java.util.List;

public class CardItemDiffCallBack extends DiffUtil.Callback {
    private final List<CardItem> oldCardList;
    private final List<CardItem> newCardList;

    public CardItemDiffCallBack(List<CardItem> oldCardList,
                                List<CardItem> newCardList) {
        this.oldCardList = oldCardList;
        this.newCardList = newCardList;
    }

    @Override
    public int getOldListSize() {
        return oldCardList.size();
    }

    @Override
    public int getNewListSize() {
        return newCardList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final   CardItem oldItem = oldCardList.get(oldItemPosition);
        final   CardItem newItem = newCardList.get(newItemPosition);

        return oldItem.getProductName().equals(newItem.getProductName()) &&
                oldItem.getProductDescription().equals(newItem.getProductDescription()) &&
                oldItem.getProductPrice().equals(newItem.getProductPrice()) &&
                oldItem.getProductPosition().equals(newItem.getProductPosition());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
