package com.life.myTimer.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTimer.databinding.ViewholderKindOfFoodBinding;
import com.life.myTimer.ui.main.model.KindOfFood;
import com.life.myTimer.ui.main.viewholder.KindOfFoodViewHolder;

/**
 * Created by isaac on 12/08/17.
 * <p>
 * The Adapter which is used with the SnappyRecyclerView.
 * Can be used with and ViewHolder class which extends DefaultBindingViewHolder
 * and any model which implements the Observable interface
 */

public class KindOfFoodAdapter extends ListAdapter<KindOfFood, KindOfFoodViewHolder> {
    private int mScreenWidth;

    public KindOfFoodAdapter(@NonNull DiffUtil.ItemCallback<KindOfFood> diffCallback, int screenWidth) {
        super(diffCallback);
        this.mScreenWidth = screenWidth;
    }

    @Override
    public KindOfFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        ViewholderKindOfFoodBinding itemBinding =
                ViewholderKindOfFoodBinding.inflate(layoutInflater, parent, false);

        return new KindOfFoodViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(KindOfFoodViewHolder holder, int position) {
        int marginSize = calcItemMarginSize();
        int endItemMarginSize = calcEndItemMarginSize();

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

        int leftMargin = marginSize;
        int rightMargin = marginSize;

        if (position == 0) {
            leftMargin = endItemMarginSize;
            rightMargin /= 2;
        } else if (position == getCurrentList().size() - 1) {
            leftMargin /= 2;
            rightMargin = endItemMarginSize;
        } else {
            leftMargin /= 2;
            rightMargin /= 2;
        }


        lp.setMargins(leftMargin, 0, rightMargin, 0); // left, top, right, bottom

        holder.itemView.setLayoutParams(lp);

        holder.bind(getCurrentList().get(position));
    }

    /**
     * We want to display 5 items on screen, so lets set the width and
     * height to 1/7th of the screen width. Leaving a half width from the margins.
     *
     * @return the margin size in px to be used for all the non-end items
     */
    private int calcItemMarginSize() {
//        return mScreenWidth / 2 - (calcItemSize() * 3 / 2);
        return 0;
    }

    /**
     * We want to display 5 items on screen, so lets set the width and
     * height to 1/7th of the screen width. Leaving a half width from the margins.
     *
     * @return the size to be used for all the items
     */
    private int calcItemSize() {
        int width = (int) (mScreenWidth * 0.58 / 3);
        return width;

    }

    /**
     * We want to center the end items on the screen, so need to know the mid point of the screen
     * minus half the items width
     *
     * @return the margin size in px to be used for the end items
     */
//    private int calcEndItemMarginSize() {
//        return mScreenWidth / 2 - calcItemSize() / 2;
//    }
    private int calcEndItemMarginSize() {
        return (int) ((mScreenWidth * 0.58 / 2) - calcItemSize() / 2);
    }
}
