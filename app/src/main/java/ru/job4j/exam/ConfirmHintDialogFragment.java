package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import java.util.Objects;

public class ConfirmHintDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.host_frg, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        return builder.setView(view).setMessage(R.string.confirm_hint_dialog_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
            callBack.positiveDialogClick(ConfirmHintDialogFragment.this);
            Intent intent = new Intent();
            intent.putExtra("value", 1);
            assert getTargetFragment() != null;
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(), Activity.RESULT_OK, intent);
        }).setNegativeButton(R.string.cancel, (dialog, which) ->
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
