package com.life.myTimer.recyclerView.model;

import androidx.annotation.DrawableRes;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

/**
 * Created by isaac on 13/08/17.
 *
 * A model which implements the Observable interface to allow for Data Binding
 *
 * Has an image which is used as the back ground
 */

public class ImageItemModel implements Observable {

    //DataBinding
    private transient PropertyChangeRegistry mCallbacks = new PropertyChangeRegistry();

    private @DrawableRes int mImageRes;

    public ImageItemModel(@DrawableRes int imageRes) {
        mImageRes = imageRes;
    }

    @Bindable
    public @DrawableRes int getImageRes() {
        return mImageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.mImageRes = imageRes;
        notifyPropertyChanged(imageRes);
    }

    //Observable Interface
    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mCallbacks.add(callback);
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mCallbacks.remove(callback);
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public synchronized void notifyChange() {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, 0, null);
        }
    }

    public void notifyPropertyChanged(int fieldId) {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }
}
