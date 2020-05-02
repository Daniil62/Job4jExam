package ru.job4j.exam;

import android.support.v4.app.Fragment;

public class HintActivator extends HintActivity {
    @Override
    public Fragment loadFrg() {
        return new HintFragment();
    }
}
