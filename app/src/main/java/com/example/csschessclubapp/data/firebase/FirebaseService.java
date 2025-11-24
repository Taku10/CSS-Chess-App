package com.example.csschessclubapp.data.firebase;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public final class FirebaseService {
    private static volatile FirebaseService INSTANCE;

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    private FirebaseService(Context appContext) {
        // Use default app from google-services.json (safe to call multiple times)
        FirebaseApp.initializeApp(appContext);

        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        // Offline cache
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    /** Singleton */
    public static FirebaseService getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FirebaseService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseService(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    /** Debug helper to confirm which project is wired */
    public String debugDescribe() {
        return "FirebaseApp connected to projectId="
                + FirebaseApp.getInstance().getOptions().getProjectId()
                + ", appId=" + FirebaseApp.getInstance().getOptions().getApplicationId();
    }

    // -------------------- Auth --------------------

    @Nullable public FirebaseUser currentUser() { return auth.getCurrentUser(); }

    public String uidOrNull() {
        FirebaseUser u = auth.getCurrentUser();
        return (u == null) ? null : u.getUid();
    }

    /** Enable 'Email/Password' in Firebase Console → Auth → Sign-in method */
    public Task<AuthResult> signInWithEmailPassword(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createUserWithEmailPassword(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<Void> sendPasswordResetEmail(String email) {
        return auth.sendPasswordResetEmail(email);
    }

    /** Optional (dev): enable 'Anonymous' in Console if you use this */
    public Task<AuthResult> signInAnonymously() {
        return auth.signInAnonymously();
    }

    public void signOut() { auth.signOut(); }

    public void addAuthStateListener(FirebaseAuth.AuthStateListener l) { auth.addAuthStateListener(l); }
    public void removeAuthStateListener(FirebaseAuth.AuthStateListener l) { auth.removeAuthStateListener(l); }

    // -------------------- Firestore --------------------

    public FirebaseFirestore firestore() { return db; }

    public CollectionReference events()        { return db.collection("events"); }
    public CollectionReference announcements() { return db.collection("announcements"); }

    public CollectionReference rsvps(String eventId) {
        return events().document(eventId).collection("rsvps");
    }

    /** Create/update RSVP: events/{eventId}/rsvps/{userId} */
    public Task<Void> upsertRsvp(String eventId, String userId, boolean attending) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("attending", attending);
        data.put("updatedAt", com.google.firebase.Timestamp.now());
        return rsvps(eventId).document(userId).set(data);
    }
}
