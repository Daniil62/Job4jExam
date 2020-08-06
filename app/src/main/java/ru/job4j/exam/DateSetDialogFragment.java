package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class DateSetDialogFragment extends DialogFragment {
    private final DatePickerDialog.OnDateSetListener listener;
    public DateSetDialogFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(Objects.requireNonNull(getContext()),
                listener, year, month, day);
    }
}
