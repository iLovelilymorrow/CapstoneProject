package com.example.capstoneproject.memberlogic.memberfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstoneproject.R;

public class MemberReceiptsFragment extends Fragment
{
    private TextView usernameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.member_receipts_fragment, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);

        return view;
    }
}
