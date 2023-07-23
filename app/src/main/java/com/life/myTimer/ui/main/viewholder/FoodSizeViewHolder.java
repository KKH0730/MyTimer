package com.life.myTimer.ui.main.viewholder;

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
                binding.tvSize.setTextColor(binding.getRoot().getContext().getColor(R.color.white));
            } else {
                binding.tvSize.setTextColor(binding.getRoot().getContext().getColor(R.color.color_858585));
            }
        });
    }

    public void bind(String size) {
        binding.setViewModel(viewModel);
        binding.setPosition(getAdapterPosition());

        startObserve();
        binding.tvSize.setText(size);
    }
}
