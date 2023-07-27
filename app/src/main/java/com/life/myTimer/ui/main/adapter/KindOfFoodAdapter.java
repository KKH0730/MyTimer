package com.life.myTimer.ui.main.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTimer.App;
import com.life.myTimer.databinding.ViewholderKindOfFoodBinding;
import com.life.myTimer.ui.main.model.KindOfFood;
import com.life.myTimer.ui.main.viewholder.KindOfFoodViewHolder;

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
        int endItemMarginSize = calcEndItemMarginSize(position);

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

        int leftMargin = marginSize;
        int rightMargin = marginSize;

        if (position == 0) {
            leftMargin = endItemMarginSize;
        } else if (position == getCurrentList().size() - 1) {
            rightMargin = endItemMarginSize;
        }

        lp.setMargins(leftMargin, 0, rightMargin, 0); // left, top, right, bottom
        holder.itemView.setLayoutParams(lp);
        holder.bind(getCurrentList().get(position));
    }

    private int calcItemMarginSize() {
        return dpToPx(4);
    }

    private int calcItemSize(int position) {
        AppCompatTextView textView = new AppCompatTextView(App.getInstance());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        textView.setText(getCurrentList().get(position).getKindOfFood().getName());
        int textViewWidth = (int) textView.getPaint().measureText(getCurrentList().get(position).getKindOfFood().getName());
        int horizontalPadding = 2 * dpToPx(16);
        return textViewWidth + horizontalPadding;
    }

    private int calcEndItemMarginSize(int position) {
        return (int) ((mScreenWidth * 0.58 / 2) - calcItemSize(position) / 2);
    }

    public int dpToPx(int dp) {
        float density = App.getInstance().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
