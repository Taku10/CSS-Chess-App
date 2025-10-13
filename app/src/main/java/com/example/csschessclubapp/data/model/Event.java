package com.example.csschessclubapp.data.model;

import com.google.firebase.Timestamp;
import java.util.Date;


public class Event {
    private String id;              // Firestore doc id (set after fetch)
    private String title;           // "Club Night: Rapid Tournament"
    private Timestamp startAt;      // start time (Firestore Timestamp)
    private Timestamp endAt;        // end time   (optional)
    private String location;        // "Science 214"
    private String type;            // "Tournament", "Workshop", etc. -> chipType
    private boolean rsvpOpen;       // -> chipRsvp text

    // Required empty ctor for Firestore
    public Event() {}

    // --- Convenience helpers (safe for adapters) ---
    public long getStartAtMillis() { return startAt == null ? 0L : startAt.toDate().getTime(); }
    public long getEndAtMillis()   { return endAt   == null ? 0L : endAt.toDate().getTime();  }
    public boolean hasEnd()        { return endAt != null; }

    // --- Getters & setters (Firestore needs public accessors) ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Timestamp getStartAt() { return startAt; }
    public void setStartAt(Timestamp startAt) { this.startAt = startAt; }

    public Timestamp getEndAt() { return endAt; }
    public void setEndAt(Timestamp endAt) { this.endAt = endAt; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRsvpOpen() { return rsvpOpen; }
    public void setRsvpOpen(boolean rsvpOpen) { this.rsvpOpen = rsvpOpen; }

    // Seed helper for quick demos
    public static Event demo(String title, String location, long startMillis, long endMillis, String type, boolean rsvpOpen) {
        Event e = new Event();
        e.setTitle(title);
        e.setLocation(location);
        e.setStartAt(new Timestamp(new Date(startMillis)));
        e.setEndAt(endMillis > 0 ? new Timestamp(new Date(endMillis)) : null);
        e.setType(type);
        e.setRsvpOpen(rsvpOpen);
        return e;
    }
}
