package ru.job4j.exam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Objects;

public class DateTimeFragment extends Fragment {
    private Calendar calendar = Calendar.getInstance();
    public Calendar getCalendar() {
        return calendar;
    }
    private TextView dateTimeText;
    private static String dateTimeString = "";
    public void setDateTimeString(String dts) {
        dateTimeString = dts;
    }
    public static DateTimeFragment of(int value) {
        DateTimeFragment dtf = new DateTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DateTimeActivity.DATE_TIME_FOR, 0);
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
        dateTimeText.setText(setInitialDateAndTime(dateTimeString));
        back.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        set.setOnClickListener(v -> {
            DialogFragment dialog = new DateSetDialogFragment();
            assert getFragmentManager() != null;
            dialog.show(getFragmentManager(), "date_set_dialog");
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!dateTimeString.equals("")) {
            dateTimeText.setText(setInitialDateAndTime(dateTimeString));
        }
    }
    public String setInitialDateAndTime(String date) {
        date = (DateUtils.formatDateTime(getContext(), calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
        return date;
    }
}
