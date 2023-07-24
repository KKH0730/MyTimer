package com.life.myTimer.recyclerView.viewHolders;

import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by isaac on 13/08/17.
 *
 * Extend this abstract class to create a ViewHolder to be used with the ItemAdapter
 */

public abstract class DefaultBindingViewHolder extends RecyclerView.ViewHolder {

    DefaultBindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
    }

    public abstract void bind(Observable observable);
}
