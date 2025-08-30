package com.example.capstoneproject.helper;

public class ValidateInputHelper
{
    public boolean isEmailValid(String email)
    {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPHMobile(String phoneNumber)
    {
        return phoneNumber != null && phoneNumber.matches("^(09\\d{9}|9\\d{9})$");
    }

    public boolean isPasswordValid(String password)
    {
        return password != null && password.length() >= 8;
    }

    public boolean isPasswordSame(String password, String confirmPassword)
    {
        return password != null && password.equals(confirmPassword);
    }

    public String normalizePHMobile(String phMobileNumber) {
        if (phMobileNumber == null) {
            return null;
        }

        String trimmedNumber = phMobileNumber.trim();

        if (trimmedNumber.matches("^09\\d{9}$")) {
            return "+63" + trimmedNumber.substring(1);
        }

        else if (trimmedNumber.matches("^9\\d{9}$")) {
            return "+63" + trimmedNumber;
        }

        else if (trimmedNumber.matches("^\\+639\\d{9}$")) {
            return trimmedNumber;
        }

        return null;
    }
}