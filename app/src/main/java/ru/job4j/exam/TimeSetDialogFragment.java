package ru.job4j.exam;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import java.util.Calendar;

public class TimeSetDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    final static Calendar calendar = Calendar.getInstance();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
