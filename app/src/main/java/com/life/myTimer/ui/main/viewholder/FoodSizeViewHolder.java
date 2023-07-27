package com.life.myTimer.ui.main.viewholder;

import static androidx.appcompat.widget.LinearLayoutCompat.*;

import android.util.Log;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTimer.R;
import com.life.myTimer.databinding.ViewholderFoodSizeBinding;
import com.life.myTimer.ui.main.MainViewModel;

public class FoodSizeViewHolder extends RecyclerView.ViewHolder {
    private ViewholderFoodSizeBinding binding;
    private MainViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    public FoodSizeViewHolder(
            ViewholderFoodSizeBinding binding,
            MainViewModel viewModel,
            LifecycleOwner lifecycleOwner

    ) {
        super(binding.getRoot());

        this.binding = binding;
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    private void startObserve() {
        viewModel.selectedFoodSizeIndex.observe(lifecycleOwner, index -> {
            if (getAdapterPosition() == index) {
                binding.tvSize.setBackgroundResource(R.drawable.circle_solid_283964);
            } else {
                binding.tvSize.setBackgroundResource(R.drawable.circle_solid_cbcbcb);
            }
        });
    }

    public void bind(String size, boolean isLastItem) {
        binding.setViewModel(viewModel);
        binding.setPosition(getAdapterPosition());
        binding.setIsLastItem(isLastItem);

        startObserve();
        binding.tvSize.setText(size);
        binding.tvSize.post(() -> {
            int tvWidth = binding.tvSize.getMeasuredWidth();
            int tvHeight = binding.tvSize.getMeasuredHeight();

            if (tvWidth < tvHeight) {
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) binding.tvSize.getLayoutParams();
                layoutParams.width  = tvHeight;
                binding.tvSize.setLayoutParams(layoutParams);
            }
        });

    }
}
