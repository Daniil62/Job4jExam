package ru.job4j.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.Objects;

public class ExamUpdateFragment extends Fragment {
    private InputMethodManager imm;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_update_form, container, false);
        Bundle bundle = getArguments();
        this.imm = (InputMethodManager) Objects.requireNonNull(
                getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        final EditText et = view.findViewById(R.id.exam_update_form_editText);
        Intent intent = getActivity().getIntent();
        ExamBaseHelper helper = new ExamBaseHelper(getContext());
        assert bundle != null;
        et.setText(intent.getStringExtra("name"));
        et.setSelection(et.getText().length());
        et.requestFocus();
        view.findViewById(R.id.exam_update_form_button_back).setOnClickListener(v -> turnBack());
        view.findViewById(R.id.exam_update_form_button_ok).setOnClickListener(v -> {
            helper.renameExam(intent.getIntExtra("id", 0),
                    et.getText().toString());
            turnBack();
        });
        return view;
    }
    private void turnBack() {
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getActivity())
                .getWindow().getDecorView().getWindowToken(), 0);
        getActivity().onBackPressed();
    }
}
