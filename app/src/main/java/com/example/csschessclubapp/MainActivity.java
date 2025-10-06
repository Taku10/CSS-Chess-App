package com.example.csschessclubapp;

import android.os.Bundle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fabPrimary);
        fab.setOnClickListener(v ->
                Snackbar.make(findViewById(R.id.root),
                        "RSVP flow coming soon ✨", Snackbar.LENGTH_SHORT).show()
        );
    }
}