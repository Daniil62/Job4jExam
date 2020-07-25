package ru.job4j.exam;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import java.util.Calendar;

public class DateTimeActivator extends DateTimeActivity implements DateSetDialogFragment.DateSetListener {
    final Calendar calendar = Calendar.getInstance();
    @Override
    public Fragment loadFrg() {
        return DateTimeFragment.of(getIntent().getIntExtra(DateTimeActivity.DATE_TIME_FOR, 0) );
    }
    @Override
    public void positiveDateDialogClick(DialogFragment dialog) {
        DateTimeFragment dtf = new DateTimeFragment();
        dtf.setDateTimeString(DateUtils.formatDateTime(this, calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR |
                        DateUtils.FORMAT_SHOW_TIME));
        dialog = new TimeSetDialogFragment();
        assert getFragmentManager() != null;
        dialog.show(getSupportFragmentManager(), "time_set_dialog");
    }
}
