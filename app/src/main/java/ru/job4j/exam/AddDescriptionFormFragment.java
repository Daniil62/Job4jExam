package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import java.util.Objects;

public class AddDescriptionFormFragment extends DialogFragment {
    private String description = "";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.add_exam_description_form, null);
        EditText descField = view.findViewById(R.id.add_exam_desc_editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        description = getActivity().getIntent().getStringExtra("description");
        descField.setText(description);
        descField.setSelection(descField.getText().length());
        descField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    description = s.toString();
                }
            }
        });
        return builder.setView(view)
                .setPositiveButton(R.string.complete_button_text,
                        (dialog, which) -> completeClick()).create();
    }
    private void completeClick() {
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        intent.putExtra("value", 1);
        intent.putExtra("description", description);
        assert getTargetFragment() != null;
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
