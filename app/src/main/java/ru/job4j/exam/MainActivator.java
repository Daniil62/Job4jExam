package ru.job4j.exam;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class MainActivator extends MainActivity implements ConfirmHintDialogFragment.ConfirmHintDialogListener {
    @Override
    public Fragment loadFrg() {
        return MainFragment.of(getIntent().getIntExtra(MainActivity.MAIN_FOR, 0));
    }
    @Override
    public void positiveDialogClick(DialogFragment dialog) {
        Intent intent = new Intent(MainActivator.this,
                HintActivator.class);
        intent.putExtra(MainActivity.MAIN_FOR, 0);
        startActivity(intent);
    }
    @Override
    public void negativeDialogClick(DialogFragment dialog) {
        Toast.makeText(MainActivator.this, "возьми с полки пирожок.",
                Toast.LENGTH_SHORT).show();
    }
}
