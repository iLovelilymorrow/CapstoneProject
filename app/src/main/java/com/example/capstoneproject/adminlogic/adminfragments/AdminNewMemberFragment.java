package com.example.capstoneproject.adminlogic.adminfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstoneproject.helper.FirebaseHelper;
import com.example.capstoneproject.R;
import com.example.capstoneproject.model.MembershipType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AdminNewMemberFragment extends Fragment
{
    private LinearLayout existingCustomerLayout;
    private EditText memberIdEditText;
    private LinearLayout newCustomerLayout;
    private TextView birthdayTextView;
    private TextView registrationDateTextView;
    private TextView expirationDateTextView;
    private RadioGroup modeOfPaymentRadioGroup;
    private MaterialButton nextButton;
    private Spinner customerCategorySpinner;
    private Spinner membershipTypeSpinner;
    private Spinner genderSpinner;


    private Long selectedBirthDateInMillis = null;


    private FirebaseHelper firebaseHelper;
    private List<MembershipType> availableMembershipTypes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        firebaseHelper = new FirebaseHelper(requireContext());
        availableMembershipTypes = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        loadMembershipTypes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.admin_newmember_fragment, container, false);

        existingCustomerLayout = view.findViewById(R.id.existingCustomerLayout);
        memberIdEditText = view.findViewById(R.id.memberIdEditText);
        newCustomerLayout = view.findViewById(R.id.newCustomerLayout);
        birthdayTextView = view.findViewById(R.id.birthdayTextView);
        registrationDateTextView = view.findViewById(R.id.registrationDateTextView);
        expirationDateTextView = view.findViewById(R.id.expirationDateTextView);
        modeOfPaymentRadioGroup = view.findViewById(R.id.modeOfPaymentRadioGroup);
        customerCategorySpinner = view.findViewById(R.id.customerCategorySpinner);
        membershipTypeSpinner = view.findViewById(R.id.membershipTypeSpinner);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            if(modeOfPaymentRadioGroup.getCheckedRadioButtonId() == R.id.cashRadioButton)
            {

            }
            else
            {

            }
        });

        runAllSetup();
        return view;
    }

    private void runAllSetup()
    {
        setupCategorySpinner();
        setupCustomerCategorySpinnerListener();
        setupBirthdayPicker();
        setupGenderSpinner();
        setupMembershipDatePicker();
    }
    private void setupCustomerCategorySpinnerListener()
    {
        customerCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    newCustomerLayout.setVisibility(View.GONE);
                    nextButton.setEnabled(false);
                }
                else if (position == 1)
                {
                    existingCustomerLayout.setVisibility(View.VISIBLE);
                    nextButton.setEnabled(true);
                }
                else
                {
                    newCustomerLayout.setVisibility(View.VISIBLE);
                    nextButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void setupRegistrationDateClickListener(SimpleDateFormat dateFormat)
    {
        registrationDateTextView.setOnClickListener(v ->
        {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Registration Date");
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

            MaterialDatePicker<Long> datePicker = builder.build();
            datePicker.addOnPositiveButtonClickListener(selection ->
            {
                Calendar selectedCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                selectedCal.setTimeInMillis(selection);
                registrationDateTextView.setText(dateFormat.format(selectedCal.getTime()));

                recalculateExpirationDate();
            });

            datePicker.show(getParentFragmentManager(), "REGISTRATION_DATE_PICKER_TAG");
        });
    }

    private void setupExpirationDateClickListener(SimpleDateFormat dateFormat)
    {
        expirationDateTextView.setOnClickListener(v ->
        {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Expiration Date");
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

            MaterialDatePicker<Long> datePicker = builder.build();
            datePicker.addOnPositiveButtonClickListener(selection ->
            {
                Calendar selectedCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                selectedCal.setTimeInMillis(selection);
                expirationDateTextView.setText(dateFormat.format(selectedCal.getTime()));
            });

            datePicker.show(getParentFragmentManager(), "EXPIRATION_DATE_PICKER_TAG");
        });
    }

    private void setupMembershipDatePicker()
    {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String todayFormatted = dateFormat.format(today.getTime());
        registrationDateTextView.setText(todayFormatted);

        setupRegistrationDateClickListener(dateFormat);

        setupExpirationDateClickListener(dateFormat);

        membershipTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("AdminNewMemberFragment", "Position is : " + position);

                if (position == 0)
                {
                    registrationDateTextView.setVisibility(View.GONE);
                    expirationDateTextView.setVisibility(View.GONE);
                }
                else
                {
                    recalculateExpirationDate();
                    registrationDateTextView.setVisibility(View.VISIBLE);
                    expirationDateTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void recalculateExpirationDate()
    {
        int selectedIndex = membershipTypeSpinner.getSelectedItemPosition();
        if(selectedIndex <= 0) return;

        MembershipType selectedMembershipType = availableMembershipTypes.get(selectedIndex - 1);
        int membershipDuration = selectedMembershipType.getDuration();

        String registrationDateStr = registrationDateTextView.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try
        {
            Calendar registrationDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            registrationDate.setTime(dateFormat.parse(registrationDateStr));

            registrationDate.add(Calendar.DAY_OF_YEAR, membershipDuration);

            expirationDateTextView.setText(dateFormat.format(registrationDate.getTime()));
        }
        catch (Exception e)
        {
            Log.e("AdminNewMemberFragment", "Failed to parse registration date", e);
        }
    }

    private void setupCategorySpinner()
    {
        ArrayAdapter<CharSequence> customerCategoryAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.customer_type, android.R.layout.simple_spinner_item);
        customerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerCategorySpinner.setAdapter(customerCategoryAdapter);
    }

    private void setupGenderSpinner()
    {
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
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

    private void loadMembershipTypes() {
        firebaseHelper.fetchMembershipTypes(new FirebaseHelper.MembershipTypesCallback()
        {
            @Override
            public void onSuccess(List<MembershipType> fetchedMembershipTypes)
            {
                availableMembershipTypes.clear();
                availableMembershipTypes.addAll(fetchedMembershipTypes);

                List<String> membershipTypeNames = new ArrayList<>();
                membershipTypeNames.add(getString(R.string.selectMembershipType));

                for (MembershipType membershipType : availableMembershipTypes)
                {
                    membershipTypeNames.add(membershipType.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, membershipTypeNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                membershipTypeSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e)
            {
                Log.e("AdminNewMemberFragment", "Failed to load membership types", e);
                Toast.makeText(requireContext(), "Error loading membership types: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

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