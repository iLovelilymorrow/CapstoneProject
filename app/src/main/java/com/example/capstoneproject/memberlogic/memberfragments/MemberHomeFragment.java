package com.example.capstoneproject.memberlogic.memberfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstoneproject.R;

public class MemberHomeFragment extends Fragment
{
    private static final String TAG = "MemberHomeFragment";
    private TextView usernameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.member_home_fragment, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String username = "Member"; // Default value
        if (getArguments() != null) {
            // Using Safe Args (recommended):
            // try {
            //     navArgs = AdminHomeFragmentArgs.fromBundle(getArguments());
            //     username = navArgs.getUsername();
            // } catch (IllegalArgumentException e) {
            //     Log.e(TAG, "Error getting username from NavArgs", e);
            // }

            // Manual retrieval:
            if (getArguments().containsKey("username")) {
                username = getArguments().getString("username", "Member");
            } else {
                Log.w(TAG, "Username argument not found in bundle.");
            }
        } else {
            Log.w(TAG, "Arguments bundle is null.");
        }

        if (usernameTextView != null) {
            usernameTextView.setText("Welcome, " + username + "!");
            Log.d(TAG, "Displaying username: " + username);
        } else {
            Log.e(TAG, "usernameTextView is null. Check layout ID.");
        }
    }
}
