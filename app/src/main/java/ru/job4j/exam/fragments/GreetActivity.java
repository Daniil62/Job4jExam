package ru.job4j.exam.fragments;

import android.support.v4.app.Fragment;

public class GreetActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return new GreetFragment();
    }
}
