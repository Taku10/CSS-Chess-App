package com.example.csschessclubapp.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.csschessclubapp.data.firebase.FirebaseService;
import com.example.csschessclubapp.data.model.Event;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClubRepository {
    private static final String TAG = "RepoEvents";

    private final FirebaseService svc;
    private final MutableLiveData<List<Event>> eventsLive =
            new MutableLiveData<>(Collections.emptyList());
    private ListenerRegistration eventsReg;

    public ClubRepository(FirebaseService service) { this.svc = service; }

    public LiveData<List<Event>> events() { return eventsLive; }

//    public void ensureSignedIn(RepositoryCallback<Void> cb) {
//        if (svc.currentUser() != null) { cb.onSuccess(null); return; }
//        svc.signInAnonymously().addOnCompleteListener(t -> {
//            if (t.isSuccessful()) {
//                Log.d(TAG, "Signed in uid=" + svc.uidOrNull());
//                cb.onSuccess(null);
//            } else {
//                Log.e(TAG, "Auth failed", t.getException());
//                cb.onError(t.getException());
//            }
//        });
//    }

    public void startHomeStreams() {
        // Start simple (shows ALL, oldest→newest). Once you see items, you can add the future-only filter below.
        Query q = svc.events().orderBy("startAt", Query.Direction.ASCENDING).limit(50);

        // If you only want upcoming events, use this instead (after you confirm data exists):
//         Query q = svc.events()
//                .whereGreaterThanOrEqualTo("startAt", Timestamp.now())
//                .orderBy("startAt", Query.Direction.ASCENDING);

        eventsReg = q.addSnapshotListener((QuerySnapshot snap, FirebaseFirestoreException e) -> {
            if (e != null) { Log.e(TAG, "Listener error", e); return; }
            if (snap == null) { eventsLive.postValue(Collections.emptyList()); return; }

            List<Event> list = new ArrayList<>();
            for (DocumentSnapshot doc : snap.getDocuments()) {
                Event ev = doc.toObject(Event.class);
                if (ev != null) { ev.setId(doc.getId()); list.add(ev); }
            }
            Log.d(TAG, "Emitting " + list.size() + " events");
            eventsLive.postValue(list);
        });
    }

    public void stopHomeStreams() {
        if (eventsReg != null) {
            eventsReg.remove();
            eventsReg = null;
        }
    }

//    public void rsvp(String eventId, boolean attending, RepositoryCallback<Void> cb) {
//        String uid = svc.uidOrNull();
//        if (uid == null) { cb.onError(new IllegalStateException("Not signed in")); return; }
//        svc.upsertRsvp(eventId, uid, attending)
//                .addOnSuccessListener(unused -> cb.onSuccess(null))
//                .addOnFailureListener(cb::onError);
//    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
