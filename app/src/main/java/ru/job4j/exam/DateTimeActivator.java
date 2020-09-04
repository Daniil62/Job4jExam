package ru.job4j.exam;

import android.support.v4.app.Fragment;

public class DateTimeActivator extends DateTimeActivity {
    @Override
    public Fragment loadFrg() {
        return DateTimeFragment.of(getIntent().getIntExtra(DateTimeActivity.DATE_TIME_FOR,
                0) );
    }
}
