package com.example.capstoneproject.adminlogic.adminfragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstoneproject.helper.FirebaseHelper;
import com.example.capstoneproject.R;
import com.example.capstoneproject.model.MembershipType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AdminMembershipTypeFragment extends Fragment
{
    private EditText membershipTypeNameEditText;
    private EditText durationEditText;
    private TextView untilDateTextView;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private com.google.android.material.checkbox.MaterialCheckBox doesNotExpireCheckBox;
    private MaterialButton createButton;

    private Long selectedUntilDateInMillis = null;
    private FirebaseHelper firebaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.admin_membershiptype_fragment, container, false);

        membershipTypeNameEditText = view.findViewById(R.id.membershipTypeNameEditText);
        durationEditText = view.findViewById(R.id.durationEditText);
        untilDateTextView = view.findViewById(R.id.untilDateTextView);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        doesNotExpireCheckBox = view.findViewById(R.id.doesNotExpireCheckBox);
        createButton = view.findViewById(R.id.createButton);

        firebaseHelper = new FirebaseHelper(requireContext());

        createButton.setOnClickListener(v ->
        {
            getDataForMembershipType();
        });

        setupUntilDatePicker();
        setupCheckBoxListener();

        return view;
    }

     public void getDataForMembershipType()
     {
         String membershipTypeName = membershipTypeNameEditText.getText().toString().trim();
         if (TextUtils.isEmpty(membershipTypeName))
         {
             membershipTypeNameEditText.setError("Membership Type Name is required");
             membershipTypeNameEditText.requestFocus();
             return;
         }

         if (!validateDurationInput())
         {
             return;
         }
         int duration = Integer.parseInt(durationEditText.getText().toString().trim());

         String description = descriptionEditText.getText().toString().trim();

         if (!validatePriceInput())
         {
             return;
         }
         double price = Double.parseDouble(priceEditText.getText().toString().trim());

         Timestamp selectedUntilDate = null;
         if(!doesNotExpireCheckBox.isChecked() && selectedUntilDateInMillis != null)
         {
             Date date = new Date(selectedUntilDateInMillis);
             selectedUntilDate = new Timestamp(date);
         }

         MembershipType newMembershipType = new MembershipType(
                 membershipTypeName,
                 duration,
                 description,
                 price,
                 selectedUntilDate
         );

         attemptSaveMembershipType(newMembershipType);
     }

     private void attemptSaveMembershipType(MembershipType newMembershipType)
     {
         createButton.setEnabled(false);

         firebaseHelper.createMembershipType(newMembershipType, new FirebaseHelper.GeneralCallback()
         {
             @Override
             public void onSuccess()
             {
                 Toast.makeText(requireContext(), "Membership type created successfully!", Toast.LENGTH_SHORT).show();
                 clearInputFields();
                 if(isAdded())
                 {
                     createButton.setEnabled(true);
                 }
             }
             @Override
             public void onFailure(Exception e)
             {
                 Log.e("AdminMembershipTypeFragment", "Error creating membership type", e);
                 Toast.makeText(requireContext(), "Error creating membership type: " + e.getMessage(), Toast.LENGTH_LONG).show();
                 if(isAdded())
                 {
                     createButton.setEnabled(true);
                 }
             }
         });
     }

    private void setupUntilDatePicker()
    {
        untilDateTextView.setOnClickListener(v -> {
            if (!untilDateTextView.isEnabled()) return;

            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Until Date");

            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            Calendar todayUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            todayUtc.set(Calendar.DAY_OF_MONTH, 1);
            todayUtc.set(Calendar.HOUR_OF_DAY, 0);
            todayUtc.set(Calendar.MINUTE, 0);
            todayUtc.set(Calendar.SECOND, 0);
            todayUtc.set(Calendar.MILLISECOND, 0);

            constraintsBuilder.setStart(todayUtc.getTimeInMillis());
            constraintsBuilder.setValidator(DateValidatorPointForward.now());
            builder.setCalendarConstraints(constraintsBuilder.build());

            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

            final MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener(selection ->
            {
                selectedUntilDateInMillis = selection;

                Calendar calendarUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendarUtc.setTimeInMillis(selection);

                SimpleDateFormat displayFormat  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                untilDateTextView.setText(displayFormat.format(calendarUtc.getTime()));
            });

            datePicker.show(getParentFragmentManager(), "UNTIL_DATE_PICKER_TAG");
        });
    }

    private void setupCheckBoxListener()
    {
        doesNotExpireCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                untilDateTextView.setEnabled(false);
                untilDateTextView.setText(R.string.doesnotexpire);
                untilDateTextView.setError(null);
                selectedUntilDateInMillis = null;
            }
            else
            {
                untilDateTextView.setEnabled(true);
                untilDateTextView.setText("");
                untilDateTextView.setHint(getString(R.string.untildate));
            }
        });
    }

    private boolean validateDurationInput()
    {
        String durationStr = durationEditText.getText().toString().trim();

        if (TextUtils.isEmpty(durationStr))
        {
            showError("Duration is required");
            return false;
        }

        try
        {
            int duration = Integer.parseInt(durationStr);
            if (duration <= 0)
            {
                showError("Duration must be a positive number");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            showError("Invalid number for duration");
            return false;
        }

        durationEditText.setError(null);
        return true;
    }

    private void showError(String errorMessage) {
        durationEditText.setError(errorMessage);
        durationEditText.requestFocus();
    }

    private boolean validatePriceInput()
    {
        String priceStr = priceEditText.getText().toString().trim();

        if (TextUtils.isEmpty(priceStr))
        {
            showPriceError(priceEditText, "Price is required");
            return false;
        }

        try
        {
            double price = Double.parseDouble(priceStr);
            if (price < 0)
            {
                showPriceError(priceEditText, "Price cannot be negative");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            showPriceError(priceEditText, "Invalid number for price");
            return false;
        }

        priceEditText.setError(null);
        return true;
    }

    private void showPriceError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }

    private void clearInputFields()
    {
        membershipTypeNameEditText.setText("");
        durationEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        untilDateTextView.setText("");
        untilDateTextView.setError(null);
        selectedUntilDateInMillis = null;
        doesNotExpireCheckBox.setChecked(false);
        membershipTypeNameEditText.requestFocus();
    }
}