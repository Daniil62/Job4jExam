package ru.job4j.exam;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        ExamBaseHelper helper = new ExamBaseHelper(getContext());
        assert bundle != null;
        et.setText(bundle.getString("name"));
        et.setSelection(et.getText().length());
        view.findViewById(R.id.exam_update_form_button_back).setOnClickListener(v -> turnBack());
        view.findViewById(R.id.exam_update_form_button_ok).setOnClickListener(v -> {
            helper.renameExam(bundle.getInt("id"), et.getText().toString());
            turnBack();
        });
        return view;
    }
    private void turnBack() {
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        imm.hideSoftInputFromWindow(getActivity()
                .getWindow().getDecorView().getWindowToken(), 0);
        fm.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                .addToBackStack(null).commit();
    }
}
