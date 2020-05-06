package ru.job4j.exam;

import android.support.v4.app.Fragment;

public class MainActivator extends MainActivity {
    @Override
    public Fragment loadFrg() {
        return MainFragment.of(getIntent().getIntExtra(MainActivity.MAIN_FOR, 0));
    }
}
