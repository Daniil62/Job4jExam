package ru.job4j.exam;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Objects;

public class DateSetDialogFragment extends DialogFragment implements DatePickerDialog
        .OnDateSetListener {
    final static Calendar calendar = Calendar.getInstance();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        callback.positiveDateDialogClick(this);
    }
    public interface DateSetListener {
        void positiveDateDialogClick(DialogFragment dialog);
    }
    private DateSetListener callback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DateSetListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException("must implement dateSetListener "
                    + context.toString());
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
