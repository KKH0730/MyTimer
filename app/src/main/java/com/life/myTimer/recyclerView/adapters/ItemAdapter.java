package com.life.myTimer.recyclerView.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.Observable;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTimer.databinding.ItemImageBinding;
import com.life.myTimer.recyclerView.viewHolders.DefaultBindingViewHolder;
import com.life.myTimer.recyclerView.viewHolders.ImageItemViewHolder;

import java.util.List;

/**
 * Created by isaac on 12/08/17.
 * <p>
 * The Adapter which is used with the SnappyRecyclerView.
 * Can be used with and ViewHolder class which extends DefaultBindingViewHolder
 * and any model which implements the Observable interface
 */

public class ItemAdapter extends RecyclerView.Adapter<DefaultBindingViewHolder> {

    public enum ItemType {
        Default,
        Image
    }

    private ItemType mItemType;
    private List<Observable> mItemList;
    private int mScreenWidth;

    public ItemAdapter(ItemType itemType, List<Observable> itemList, int screenWidth) {
        this.mItemType = itemType;
        this.mItemList = itemList;
        this.mScreenWidth = screenWidth;
    }

    @Override
    public DefaultBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        ItemImageBinding itemBinding =
                ItemImageBinding.inflate(layoutInflater, parent, false);

        return new ImageItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(DefaultBindingViewHolder holder, int position) {
        Observable listItem = mItemList.get(position);

        int itemSize = calcItemSize();
        int marginSize = calcItemMarginSize();
        int endItemMarginSize = calcEndItemMarginSize();

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        lp.height = itemSize;
        lp.width = itemSize;

        int leftMargin = marginSize;
        int rightMargin = marginSize;

        if (position == 0) {
            leftMargin = endItemMarginSize;
        } else if (position == mItemList.size() - 1) {
            rightMargin = endItemMarginSize;
        }

        lp.setMargins(leftMargin, marginSize, rightMargin, marginSize); // left, top, right, bottom

        holder.itemView.setLayoutParams(lp);

        holder.bind(listItem);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * We want to display 5 items on screen, so lets set the width and
     * height to 1/7th of the screen width. Leaving a half width from the margins.
     *
     * @return the margin size in px to be used for all the non-end items
     */
    private int calcItemMarginSize() {
        return calcItemSize() / 4;
    }

    /**
     * We want to display 5 items on screen, so lets set the width and
     * height to 1/7th of the screen width. Leaving a half width from the margins.
     *
     * @return the size to be used for all the items
     */
//    private int calcItemSize() {
//        return mScreenWidth / 7;
//    }
    private int calcItemSize() {
        return mScreenWidth / 5;
    }

    /**
     * We want to center the end items on the screen, so need to know the mid point of the screen
     * minus half the items width
     *
     * @return the margin size in px to be used for the end items
     */
    private int calcEndItemMarginSize() {
        return mScreenWidth / 2 - calcItemSize() / 2;
    }
}
