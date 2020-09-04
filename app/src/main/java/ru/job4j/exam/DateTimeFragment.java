package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateTimeFragment extends Fragment implements DatePickerDialog
        .OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView dateTimeText;
    private String dateTimeString = "";
    public static DateTimeFragment of(int value) {
        DateTimeFragment dtf = new DateTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DateTimeActivity.DATE_TIME_FOR, value);
        dtf.setArguments(bundle);
        return dtf;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_time, container, false);
        getActivity();
        setRetainInstance(true);
        dateTimeText = view.findViewById(R.id.date_and_time);
        Button back = view.findViewById(R.id.date_time_button_back);
        Button set = view.findViewById(R.id.date_time_button_set);
        if (savedInstanceState == null) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            Date date = new Date();
            dateTimeString = sdf.format(date.getTime());
        }
        back.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        set.setOnClickListener(v -> {
            DialogFragment dialog = new DateSetDialogFragment(this);
            assert getFragmentManager() != null;
            dialog.show(getFragmentManager(), "date_set_dialog");
        });
        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("date_time_string", this.dateTimeString);
    }
    @Override
    public void onResume() {
        super.onResume();
        dateTimeText.setText(dateTimeString);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateTimeString = (year + "." + month + "." + dayOfMonth + ", ");
        DialogFragment dialog = new TimeSetDialogFragment(this);
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "time_set_dialog");
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        dateTimeString += (hourOfDay + ":" + minute);
        dateTimeText.setText(dateTimeString);
    }
}
