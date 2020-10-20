package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import ru.job4j.exam.store.StatisticStore;

public abstract class ResultActivity extends FragmentActivity {
    public static final String RESULT_FOR = "result_for";
    public abstract Fragment loadFrg();
    private StatisticStore statisticStore = new StatisticStore();
    @Override
    protected void onCreate(@Nullable Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.host_frg);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.content) == null) {
            fm.beginTransaction()
                    .add(R.id.content, loadFrg())
                    .commit();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ExamListActivity.class);
        statisticStore.clear();
        startActivity(intent);
        finish();
    }
}
