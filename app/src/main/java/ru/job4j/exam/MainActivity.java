package ru.job4j.exam;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import ru.job4j.exam.store.StatisticStore;

public class MainActivity extends FragmentActivity implements
        ConfirmHintDialogFragment.ConfirmHintDialogListener,
        BackToMenuDialogFragment.BackToMenuDialogListener {
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_frg);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.content) == null) {
            Fragment fragment = new MainFragment();
            fm.beginTransaction()
                    .replace(R.id.content, fragment).addToBackStack(null)
                    .commit();
        }
    }
    @Override
    public void positiveDialogClick(DialogFragment dialog) {
        StatisticStore statisticStore = new StatisticStore();
        statisticStore.penaltyIncrease();
    }
    @Override
    public void negativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "возьми с полки пирожок.",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        DialogFragment dialog = new BackToMenuDialogFragment();
        dialog.show(getSupportFragmentManager(), "to_menu_dialog");
    }
    @Override
    public void positiveBackToMenuClick(BackToMenuDialogFragment back) {
        Intent intent = new Intent(this, ExamListActivity.class);
        startActivity(intent);
    }
    @Override
    public void negativeBackToMenuClick(BackToMenuDialogFragment back) {

    }
}
