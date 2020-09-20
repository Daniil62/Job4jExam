package ru.job4j.exam;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import java.util.Objects;

public class ExamAddFragment extends Fragment {
    private SQLiteDatabase store;
    private int count;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_exam_form, container, false);
        this.store = new ExamBaseHelper(getContext()).getWritableDatabase();
        EditText examName = view.findViewById(R.id.add_exam_editText);
        EditText questionText = view.findViewById(R.id.add_exam_question_editText);
        EditText answerVariant = view.findViewById(R.id.add_exam_answer_editText);
        Spinner spinner = view.findViewById(R.id.add_exam_spinner);
        ImageButton addAnswer = view.findViewById(R.id.add_exam_add_answer_button);
        Button save = view.findViewById(R.id.add_exam_button_save);
        this.count = 0;
        answerVariant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                addAnswer.setEnabled(!s.toString().equals(""));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                save.setEnabled(spinner.getSelectedItemPosition() != 0 && count == 4);
                spinner.setEnabled(spinner.getSelectedItemPosition() == 0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        addAnswer.setEnabled(!answerVariant.getText().toString().equals(""));
        addAnswer.setOnClickListener(v -> {
            ++count;
            save.setEnabled(count == 4 && spinner.getSelectedItemPosition() != 0);
            answerVariant.setEnabled(count != 4);
            if (count <= 4) {
                ContentValues values = new ContentValues();
                values.put(ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT, answerVariant.getText().toString());
                values.put(ExamDbSchema.AnswerTable.Cols.ANSWER_ID, count);
                store.insert(ExamDbSchema.AnswerTable.TAB_NAME, null, values);
                values.clear();
                values.put(ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER,
                        spinner.getSelectedItemPosition());
            } else {
                addAnswer.setEnabled(false);
            }
            answerVariant.setText("");
        });
        save.setEnabled(count == 4 && spinner.getSelectedItemPosition() != 0);
        save.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(ExamDbSchema.ExamTable.Cols.TITLE, examName.getText().toString());
            values.put(ExamDbSchema.ExamTable.Cols.MARK, 0);
            store.insert(ExamDbSchema.ExamTable.TAB_NAME, null, values);
            values.clear();
            values.put(ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT,
                    questionText.getText().toString());
            store.insert(ExamDbSchema.QuestionTable.TAB_NAME, null, values);
            FragmentManager manager = Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                    .addToBackStack(null).commit();
        });
        return view;
    }
}
