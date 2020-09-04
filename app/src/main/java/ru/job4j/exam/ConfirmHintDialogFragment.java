package ru.job4j.exam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class ConfirmHintDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setMessage("Показать подсказку?")
                .setPositiveButton(R.string.ok, (dialog, which) ->
                        callBack.positiveDialogClick(ConfirmHintDialogFragment.this))
                .setNegativeButton(R.string.cancel, (dialog, which) ->
                        callBack.negativeDialogClick(ConfirmHintDialogFragment.this)).create();
    }
    public interface ConfirmHintDialogListener {
        void positiveDialogClick(DialogFragment dialog);
        void negativeDialogClick(DialogFragment dialog);
    }
    private ConfirmHintDialogListener callBack;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (ConfirmHintDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement ConfirmHintDialogListener "
                    + context.toString());
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }
}
