package com.example.csschessclubapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.csschessclubapp.data.repository.ClubRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private final ClubRepository repo;
    public AuthViewModel(ClubRepository repo) { this.repo = repo; }

    public LiveData<FirebaseUser> user() { return repo.user(); }

    public void start() { repo.startAuth(); }
    public void stop()  { repo.stopAuth();  }

    public void signIn(String email, String pass, Callback cb) {
        repo.signInEmail(email, pass, wrap(cb));
    }
    public void signUp(String email, String pass, Callback cb) {
        repo.signUpEmail(email, pass, wrap(cb));
    }
    public void reset(String email, Callback cb) {
        repo.resetPassword(email, wrap(cb));
    }
    public void signOut() { repo.signOut(); }

    private ClubRepository.RepositoryCallback<Void> wrap(Callback cb) {
        return new ClubRepository.RepositoryCallback<Void>() {
            @Override public void onSuccess(Void r) { if (cb!=null) cb.onSuccess(); }
            @Override public void onError(Exception e) { if (cb!=null) cb.onError(e); }
        };
    }
    public interface Callback { void onSuccess(); void onError(Exception e); }
}
