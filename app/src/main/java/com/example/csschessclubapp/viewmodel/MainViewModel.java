// app/src/main/java/com/example/csschessclubapp/viewmodel/MainViewModel.java
package com.example.csschessclubapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.csschessclubapp.data.model.Event;
import com.example.csschessclubapp.data.repository.ClubRepository;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final ClubRepository repo;

    public MainViewModel(ClubRepository repo) { this.repo = repo; }

    public LiveData<List<Event>> events() { return repo.events(); }

    public void start() {
//        repo.ensureSignedIn(new ClubRepository.RepositoryCallback<Void>() {
//            @Override public void onSuccess(Void r) { repo.startHomeStreams(); }
//            @Override public void onError(Exception e) {
                repo.startHomeStreams();
//            }
//        });
    }
    public void stop() { repo.stopHomeStreams(); }

    public void rsvp(String eventId, boolean attending) {
//        repo.rsvp(eventId, attending, new ClubRepository.RepositoryCallback<Void>() {
//            @Override public void onSuccess(Void r) { /* success */ }
//            @Override public void onError(Exception e) { /* show error */ }
//        });
    }
}