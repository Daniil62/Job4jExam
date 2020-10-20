package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class InnerExamsListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_frg);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.content);
        if (fragment == null) {
            fragment = new InnerExamsListFragment();
            manager.beginTransaction().add(R.id.content, fragment).commit();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ExamAddActivity.class);
        startActivity(intent);
        finish();
    }
}
