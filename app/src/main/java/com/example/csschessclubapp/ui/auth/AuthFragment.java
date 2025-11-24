package com.example.csschessclubapp.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.csschessclubapp.R;
import com.example.csschessclubapp.viewmodel.AppViewModelFactory;
import com.example.csschessclubapp.viewmodel.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

public class AuthFragment extends Fragment {
    private AuthViewModel vm;
    private TextInputEditText etEmail, etPass;
    private LinearProgressIndicator progress;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
        return inf.inflate(R.layout.fragment_auth, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        super.onViewCreated(v, b);
        vm = new ViewModelProvider(requireActivity(), new AppViewModelFactory(requireContext().getApplicationContext()))
                .get(AuthViewModel.class);

        etEmail = v.findViewById(R.id.etEmail);
        etPass  = v.findViewById(R.id.etPassword);
        progress = v.findViewById(R.id.progress);

        MaterialButton btnSignIn = v.findViewById(R.id.btnSignIn);
        MaterialButton btnCreate = v.findViewById(R.id.btnCreate);
        MaterialButton btnReset  = v.findViewById(R.id.btnReset);

        btnSignIn.setOnClickListener(_v -> submit(false));
        btnCreate.setOnClickListener(_v -> submit(true));
        btnReset.setOnClickListener(_v -> {
            String email = s(etEmail);
            if (email.isEmpty()) { toast("Enter email"); return; }
            progress(true);
            vm.reset(email, new AuthViewModel.Callback() {
                @Override public void onSuccess() { progress(false); toast("Reset email sent"); }
                @Override public void onError(Exception e) { progress(false); toast(e.getMessage()); }
            });
        });

        // If already signed in, Activity will swap to Home; still start to listen
        vm.user().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Activity should switch fragments when user != null
            }
        });
    }

    @Override public void onStart() { super.onStart(); vm.start(); }
    @Override public void onStop()  { super.onStop();  vm.stop();  }

    private void submit(boolean create) {
        String email = s(etEmail), pass = s(etPass);
        if (email.isEmpty() || pass.isEmpty()) { toast("Enter email & password"); return; }
        progress(true);
        AuthViewModel.Callback cb = new AuthViewModel.Callback() {
            @Override public void onSuccess() { progress(false); toast(create ? "Account created" : "Signed in"); }
            @Override public void onError(Exception e) { progress(false); toast(e.getMessage()); }
        };
        if (create) vm.signUp(email, pass, cb); else vm.signIn(email, pass, cb);
    }

    private void progress(boolean show) { progress.setVisibility(show ? View.VISIBLE : View.GONE); }
    private void toast(String m) { Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show(); }
    private static String s(TextInputEditText e){ return e.getText()==null? "": e.getText().toString().trim(); }
}
