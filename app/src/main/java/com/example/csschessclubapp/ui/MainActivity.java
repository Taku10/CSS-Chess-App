package com.example.csschessclubapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csschessclubapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


//this is for the ui
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.rvEvents);
        rv.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false); // since it sits inside NestedScrollView
        rv.setHasFixedSize(false);           // optional
        rv.setAdapter(adapter);              // make sure this is set
        FloatingActionButton fab = findViewById(R.id.fabPrimary);
        fab.setOnClickListener(v ->
                Snackbar.make(findViewById(R.id.root),
                        "RSVP flow coming soon ✨", Snackbar.LENGTH_SHORT).show()
        );
    }
}