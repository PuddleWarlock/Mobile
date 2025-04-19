package ru.mirea.reznikap.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DateDialogFragment extends DatePickerDialog {
    public DateDialogFragment(@NonNull Context context) {
        super(context);
    }
}
