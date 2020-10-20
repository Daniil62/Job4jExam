package ru.job4j.exam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class BackToMenuDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.stop_exam_and_back_to_menu))
                .setPositiveButton(getString(R.string.ok),
                        (dialog, which) -> callback.positiveBackToMenuClick(this))
                .setNegativeButton(getString(R.string.no),
                        (dialog, which) -> callback.negativeBackToMenuClick(this)).create();
    }
    public interface BackToMenuDialogListener {
        void positiveBackToMenuClick(BackToMenuDialogFragment back);
        void negativeBackToMenuClick(BackToMenuDialogFragment back);
    }
    BackToMenuDialogListener callback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (BackToMenuDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "must implement BackToMenuDialogListener" + context.toString());
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
