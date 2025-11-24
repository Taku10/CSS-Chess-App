// app/src/main/java/com/example/csschessclubapp/ui/MainActivity.java
package com.example.csschessclubapp.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.csschessclubapp.R;
import com.example.csschessclubapp.ui.events.EventsAdapter;
import com.example.csschessclubapp.viewmodel.AppViewModelFactory;
import com.example.csschessclubapp.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private MainViewModel vm;
    private EventsAdapter eventsAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        com.example.csschessclubapp.data.firebase.FirebaseService svc =
                com.example.csschessclubapp.data.firebase.FirebaseService.getInstance(getApplicationContext());
        android.util.Log.d("FirebaseCheck", svc.debugDescribe());
        // ViewModel
        vm = new ViewModelProvider(this, new AppViewModelFactory(getApplicationContext()))
                .get(MainViewModel.class);

        // RecyclerView + Adapter
        RecyclerView rv = findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);

        eventsAdapter = new EventsAdapter(event -> {
            vm.rsvp(event.getId(), true);
            Snackbar.make(findViewById(R.id.root),
                    "RSVP sent for " + event.getTitle(),
                    Snackbar.LENGTH_SHORT).show();
        });
        rv.setAdapter(eventsAdapter);

        // Observe events and render
        vm.events().observe(this, eventsAdapter::submit);
    }

    @Override protected void onStart() { super.onStart(); vm.start(); }
    @Override protected void onStop()  { super.onStop();  vm.stop();  }
}
