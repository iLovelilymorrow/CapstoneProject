package com.example.capstoneproject.adminlogic.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstoneproject.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AdminNewMemberFragment extends Fragment {

    private TextView birthdayTextView;
    private Long selectedBirthDateInMillis = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_newmember_fragment, container, false); // Use your layout file name

        birthdayTextView = view.findViewById(R.id.birthdayTextView);
        setupBirthdayPicker();

        return view;
    }

    private void setupBirthdayPicker() {
        birthdayTextView.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Birthday");

            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

            final MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener(selection ->
            {
                selectedBirthDateInMillis = selection;

                Calendar calendarUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendarUtc.setTimeInMillis(selection);

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                birthdayTextView.setText(outputFormat.format(calendarUtc.getTime()));
            });

            datePicker.show(getParentFragmentManager(), "BIRTHDAY_DATE_PICKER_TAG");
        });
    }

    // When saving the member data:
    // You would use selectedBirthDateInMillis.
    // To store it in the database, you can:
    // 1. Store the Long selectedBirthDateInMillis (UTC milliseconds) directly.
    // 2. Convert it to a String in ISO 8601 format ("YYYY-MM-DD") for better database readability
    //    and interoperability.
    //    Example to convert to "YYYY-MM-DD" string:
    //    if (selectedBirthDateInMillis != null) {
    //        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    //        calendar.setTimeInMillis(selectedBirthDateInMillis);
    //        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // Use US for non-localized digits
    //        dbFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure it's UTC for storage
    //        String birthDateForDb = dbFormat.format(calendar.getTime());
    //        // Now use birthDateForDb
    //    }

    public static int calculateAge(long birthDateInMillis)
    {
        Calendar birthCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        birthCal.setTimeInMillis(birthDateInMillis);

        Calendar todayCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")); // Or TimeZone.getDefault() if comparing with local "today"

        int age = todayCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
        if (todayCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    // In your data saving logic, you'll use 'selectedBirthDateInMillis'
    // For example:
    // public void saveMemberData() {
    //     String firstName = firstNameEditText.getText().toString();
    //     // ... other fields ...
    //
    //     if (selectedBirthDateInMillis != null) {
    //         // Option 1: Store as Long (UTC timestamp)
    //         // myDatabaseHelper.addMember(firstName, ..., selectedBirthDateInMillis, ...);
    //
    //         // Option 2: Store as "YYYY-MM-DD" string
    //         Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    //         calendar.setTimeInMillis(selectedBirthDateInMillis);
    //         SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    //         dbFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    //         String birthDateForDb = dbFormat.format(calendar.getTime());
    //         // myDatabaseHelper.addMember(firstName, ..., birthDateForDb, ...);
    //     } else {
    //         // Handle case where birthday is not selected
    //         Toast.makeText(getContext(), "Please select a birth date", Toast.LENGTH_SHORT).show();
    //         return;
    //     }
    //     // ...
    // }
}

