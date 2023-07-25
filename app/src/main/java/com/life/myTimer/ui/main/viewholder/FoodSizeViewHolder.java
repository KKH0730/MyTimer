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
                binding.mcvSize.setCardBackgroundColor(binding.getRoot().getContext().getColor(R.color.color_283964));
            } else {
                binding.mcvSize.setCardBackgroundColor(binding.getRoot().getContext().getColor(R.color.color_CBCBCB));
            }
        });
    }

    public void bind(String size, boolean isLastItem) {
        binding.setViewModel(viewModel);
        binding.setPosition(getAdapterPosition());
        binding.setIsLastItem(isLastItem);

        startObserve();
        binding.tvSize.setText(size);
    }
}
