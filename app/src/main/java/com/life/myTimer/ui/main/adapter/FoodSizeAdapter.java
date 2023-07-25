package com.life.myTimer.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.life.myTimer.databinding.ViewholderFoodSizeBinding;
import com.life.myTimer.ui.main.MainViewModel;
import com.life.myTimer.ui.main.viewholder.FoodSizeViewHolder;

public class FoodSizeAdapter extends ListAdapter<String, FoodSizeViewHolder> {
    private MainViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    public FoodSizeAdapter(@NonNull DiffUtil.ItemCallback<String> diffCallback, MainViewModel viewModel, LifecycleOwner lifecycleOwner) {
        super(diffCallback);

        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public FoodSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodSizeViewHolder(
                ViewholderFoodSizeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                viewModel,
                lifecycleOwner
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSizeViewHolder holder, int position) {

        holder.bind(getCurrentList().get(position), getCurrentList().size() - 1 == position);
    }
}
