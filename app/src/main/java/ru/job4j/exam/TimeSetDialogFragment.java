package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class TimeSetDialogFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener listener;
    public TimeSetDialogFragment(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
