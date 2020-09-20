package ru.job4j.exam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DialogExamsDelete extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setMessage(R.string.delete_selected)
                .setPositiveButton(R.string.delete_items, (dialog, which) -> callback.positiveExamsDeleteClick(
                        DialogExamsDelete.this)).setNegativeButton(R.string.cancel, (dialog, which) ->
                        callback.negativeExamsDeleteClick(DialogExamsDelete.this)).create();
    }
    public interface ExamsDeleteDialogListener {
        void positiveExamsDeleteClick(DialogExamsDelete ded);
        void negativeExamsDeleteClick(DialogExamsDelete ded);
    }
    ExamsDeleteDialogListener callback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ExamsDeleteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement ExamsDeleteDialogListener "
                    + context.toString());
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
