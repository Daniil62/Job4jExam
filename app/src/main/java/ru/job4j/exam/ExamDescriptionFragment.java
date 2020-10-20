package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Objects;
import ru.job4j.exam.store.ExamsStore;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public class ExamDescriptionFragment extends Fragment {
    private TextView descText;
    private Button start;
    private int id;
    private int index;
    private String exam_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_description, container, false);
        this.id = Objects.requireNonNull(
                getActivity()).getIntent().getIntExtra("id", -1);
        this.index = Objects.requireNonNull(
                getActivity()).getIntent().getIntExtra("id", 0);
        this.exam_name = Objects.requireNonNull(
                getActivity()).getIntent().getStringExtra("exam_name");
        this.descText = view.findViewById(R.id.exam_description_text);
        this.start = view.findViewById(R.id.exam_description_ok_button);
        Button back = view.findViewById(R.id.exam_description_back_button);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        start.setOnClickListener(v -> confirmClick());
        setButtonStartText();
        setDescription();
        return view;
    }
    private void setDescription() {
        StoreManager sm;
        if (id != -1) {
            sm = new ExamBaseHelper(getContext());
            descText.setText(sm.getExamDescription(id));
        } else {
            sm = new ExamsStore();
            descText.setText(sm.getExamDescription(index));
        }
    }
    private void confirmClick() {
        if (id != -1) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            Date date = new Date();
            intent.putExtra("id", id);
            Toast.makeText(
                    getContext(), getString(R.string.you_select) + exam_name,
                    Toast.LENGTH_SHORT
            ).show();
            StatisticStore.setTime(date.getTime());
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        } else {
            QuestionStore store = ExamsStore.INNER_EXAMS_STORE.get(index);
            ExamBaseHelper helper = new ExamBaseHelper(getContext());
            helper.buildExam(store, 4);
            Toast.makeText(
                    getContext(), getString(R.string.exam)
                            + exam_name + getString(R.string.added_to_te_list),
                    Toast.LENGTH_SHORT
            ).show();
            Intent intent = new Intent(getActivity(), ExamListActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }
    private void setButtonStartText() {
        if (id != -1) {
            start.setText(getString(R.string.start));
        } else {
           start.setText(getString(R.string.add_button_text));
        }
    }
}
