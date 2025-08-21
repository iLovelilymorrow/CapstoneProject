package com.example.capstoneproject.memberlogic.memberfragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstoneproject.loginlogic.LoginActivity;
import com.example.capstoneproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder; // Import MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth;

public class MemberAccountFragment extends Fragment {
    private static final String TAG = "MemberAccountFragment";
    private MaterialButton signOutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_account_fragment, container, false);
        signOutButton = view.findViewById(R.id.signOutButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (signOutButton != null) {
            signOutButton.setOnClickListener(v -> {
                // Show confirmation dialog before signing out
                showSignOutConfirmationDialog();
            });
        } else {
            Log.e(TAG, "signOutButton is null. Check ID in R.layout.member_account_fragment.");
        }
    }

    private void showSignOutConfirmationDialog() {
        // Use MaterialAlertDialogBuilder for Material Design styling
        new MaterialAlertDialogBuilder(requireContext()) // Use requireContext() for non-null Context
                .setTitle("Confirm Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // User clicked "Cancel", so dismiss the dialog
                    dialog.dismiss();
                    Log.d(TAG, "Sign out cancelled by member.");
                })
                .setPositiveButton("Sign Out", (dialog, which) -> {
                    // User clicked "Sign Out", proceed with sign-out
                    Log.d(TAG, "Sign out confirmed by member.");
                    performSignOut();
                })
                .show();
    }

    private void performSignOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }
        Toast.makeText(getContext(), "Signed out successfully.", Toast.LENGTH_SHORT).show();
    }
}
