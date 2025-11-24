package com.example.csschessclubapp.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.csschessclubapp.data.firebase.FirebaseService;
import com.example.csschessclubapp.data.repository.ClubRepository;

public class AppViewModelFactory implements ViewModelProvider.Factory {
    private final ClubRepository repo;

    public AppViewModelFactory(Context context) {
        this.repo = new ClubRepository(FirebaseService.getInstance(context.getApplicationContext()));
    }

    @NonNull @Override @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repo);
        }
        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
    }
}
