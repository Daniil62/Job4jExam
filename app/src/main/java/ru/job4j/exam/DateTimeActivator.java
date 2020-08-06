package ru.job4j.exam;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import java.util.Calendar;

public class DateTimeActivator extends DateTimeActivity {
    final Calendar calendar = Calendar.getInstance();
    @Override
    public Fragment loadFrg() {
        return DateTimeFragment.of(getIntent().getIntExtra(DateTimeActivity.DATE_TIME_FOR, 0) );
    }
}
