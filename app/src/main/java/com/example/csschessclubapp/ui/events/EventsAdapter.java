package com.example.csschessclubapp.ui.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.csschessclubapp.R;
import com.example.csschessclubapp.data.model.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.VH> {

    /** Callback for the RSVP chip. */
    public interface OnRsvpClick { void onClick(Event event); }

    private final List<Event> data = new ArrayList<>();
    private final OnRsvpClick onRsvpClick;

    public EventsAdapter(OnRsvpClick onRsvpClick) {
        this.onRsvpClick = onRsvpClick;
    }

    /** Replace the list and refresh. (Simple; use DiffUtil later if you want animations.) */
    public void submit(List<Event> newData) {
        data.clear();
        if (newData != null) data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Event e = data.get(position);
        Context ctx = h.itemView.getContext();

        // Title
        h.title.setText(orDefault(e.getTitle(), "Untitled event"));

        // Meta line: Date • start–end • location
        String meta = buildMeta(ctx, e);
        h.meta.setText(meta);

        // Type chip
        if (e.getType() != null && !e.getType().isEmpty()) {
            h.type.setVisibility(View.VISIBLE);
            h.type.setText(cap(e.getType()));
        } else {
            h.type.setVisibility(View.GONE);
        }

        // RSVP chip
        h.rsvp.setText(e.isRsvpOpen() ? "RSVP Open" : "RSVP Closed");
        h.rsvp.setEnabled(e.isRsvpOpen());
        h.rsvp.setOnClickListener(v -> {
            if (onRsvpClick != null && e.isRsvpOpen()) onRsvpClick.onClick(e);
        });
    }

    @Override public int getItemCount() { return data.size(); }

    // ----- ViewHolder -----
    static class VH extends RecyclerView.ViewHolder {
        TextView title, meta;
        Chip type, rsvp;
        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.tvEventTitle);
            meta  = v.findViewById(R.id.tvEventMeta);
            type  = v.findViewById(R.id.chipType);
            rsvp  = v.findViewById(R.id.chipRsvp);
        }
    }

    // ----- helpers -----
    private static String buildMeta(Context ctx, Event e) {
        long startMs = e.getStartAtMillis();
        long endMs   = e.getEndAtMillis();

        String dateStr = "Date TBA";
        String timeStr = "";
        if (startMs > 0) {
            DateFormat df = android.text.format.DateFormat.getMediumDateFormat(ctx);
            DateFormat tf = android.text.format.DateFormat.getTimeFormat(ctx);
            dateStr = df.format(new Date(startMs));
            String startT = tf.format(new Date(startMs));
            if (endMs > 0 && endMs > startMs) {
                String endT = tf.format(new Date(endMs));
                timeStr = startT + "–" + endT;
            } else {
                timeStr = startT;
            }
        }

        String loc = e.getLocation();
        StringBuilder sb = new StringBuilder();
        sb.append(dateStr);
        if (!timeStr.isEmpty()) sb.append(" • ").append(timeStr);
        if (loc != null && !loc.isEmpty()) sb.append(" • ").append(loc);
        return sb.toString();
    }

    private static String orDefault(String s, String d) { return (s == null || s.isEmpty()) ? d : s; }
    private static String cap(String s) { return (s == null || s.isEmpty()) ? s : s.substring(0,1).toUpperCase() + s.substring(1); }
}
