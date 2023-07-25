package com.life.myTimer.ui.main.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.life.myTimer.databinding.ViewholderKindOfFoodBinding;
import com.life.myTimer.ui.main.model.KindOfFood;

/**
 * Created by isaac on 13/08/17.
 */

public class KindOfFoodViewHolder extends RecyclerView.ViewHolder {

    private final ViewholderKindOfFoodBinding binding;

    public KindOfFoodViewHolder(ViewholderKindOfFoodBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(KindOfFood kindOfFood) {
        binding.setData(kindOfFood);
        binding.executePendingBindings();
    }
}
