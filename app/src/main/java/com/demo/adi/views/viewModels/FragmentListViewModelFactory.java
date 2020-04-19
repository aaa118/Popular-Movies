package com.demo.adi.views.viewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FragmentListViewModelFactory extends ViewModelProvider.NewInstanceFactory  {
    private static Context context;
    private static FragmentListViewModelFactory instance;

    private FragmentListViewModelFactory(Context context) {
        this.context = context;
    }

    public static FragmentListViewModelFactory getInstance(Context context) {
        if (instance == null) {
            return instance = new FragmentListViewModelFactory(context);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FragmentListViewModel(context);
    }
}
