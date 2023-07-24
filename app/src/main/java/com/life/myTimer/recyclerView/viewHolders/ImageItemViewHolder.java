package com.life.myTimer.recyclerView.viewHolders;

import androidx.databinding.Observable;

import com.life.myTimer.databinding.ItemImageBinding;
import com.life.myTimer.recyclerView.model.ImageItemModel;

/**
 * Created by isaac on 13/08/17.
 */

public class ImageItemViewHolder extends DefaultBindingViewHolder {

    private final ItemImageBinding mBinding;

    public ImageItemViewHolder(ItemImageBinding binding) {
        super(binding);
        this.mBinding = binding;
    }

    @Override
    public void bind(Observable imageItemModel) {
        mBinding.setItem((ImageItemModel)imageItemModel);
        mBinding.executePendingBindings();
    }
}
