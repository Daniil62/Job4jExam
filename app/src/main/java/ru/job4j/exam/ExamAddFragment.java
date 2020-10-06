package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.TextView;
import java.util.Objects;

import ru.job4j.exam.store.QuestionStore;

public class ExamAddFragment extends Fragment {
    private SQLiteDatabase store;
    private EditText examName;
    private EditText questionText;
    private EditText answerVariant;
    private Spinner spinner;
    private ImageButton addAnswer;
    private Button addQuestionBlock;
    private Button save;
    private TextView variantsComplete;
    private QuestionStore qs;
    private int count;
    private int group;
    private int position;
    private int trueAnswerId;
    private boolean isComplete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_exam_form, container, false);
        this.store = new ExamBaseHelper(getContext()).getWritableDatabase();
        this.examName = view.findViewById(R.id.add_exam_editText);
        this.questionText = view.findViewById(R.id.add_exam_question_editText);
        this.answerVariant = view.findViewById(R.id.add_exam_answer_editText);
        this.spinner = view.findViewById(R.id.add_exam_spinner);
        this.addAnswer = view.findViewById(R.id.add_exam_add_answer_button);
        this.addQuestionBlock = view.findViewById(R.id.add_exam_add_question_block_button);
        this.variantsComplete = view.findViewById(R.id.add_exam_count_variants_textView);
        this.save = view.findViewById(R.id.add_exam_button_save);
        Button cancel = view.findViewById(R.id.add_exam_cancel_button);
        this.qs = new QuestionStore();
        this.position = 0;
        this.count = 0;
        this.group = 4;
        this.trueAnswerId = 0;
        this.isComplete = false;

        questionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                save.setEnabled(false);
            }
        });
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
                trueAnswerId = position;
                spinner.setEnabled(spinner.getSelectedItemPosition() == 0);
                addQuestionBlock.setEnabled(count == 4 && position != 0);
                answerVariant.setEnabled(count != 4);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        addAnswer.setEnabled(!answerVariant.getText().toString().equals(""));
        addAnswer.setOnClickListener(this::addAnswerClick);
        addQuestionBlock.setEnabled(count == 4 && spinner.getSelectedItemPosition() != 0);
        addQuestionBlock.setOnClickListener(v -> addQuestionBlockClick());
        save.setEnabled(isComplete);
        save.setOnClickListener(v -> buttonSaveClick());
        cancel.setOnClickListener(v -> cancelClick());
        return view;
    }
    @SuppressLint("SetTextI18n")
    private void setCount(int c) {
        this.variantsComplete.setText(c + "/4");
    }
    private void addAnswerClick(View view) {
        ++count;
        addQuestionBlock.setEnabled(count == 4 && spinner.getSelectedItemPosition() != 0);
        answerVariant.setEnabled(count != 4);
        if (count <= 4) {
            qs.addAnswer(new Option(count, answerVariant.getText().toString(), position));
        } else {
            view.findViewById(R.id.add_exam_add_answer_button).setEnabled(false);
        }
        setCount(count);
        answerVariant.setText("");
    }
    private void addQuestionBlockClick() {
        Question question = new Question(
                position, questionText.getText().toString(), qs.getAnswers(), 0);
        if (trueAnswerId != 0) {
            question.setAnswer(trueAnswerId);
            qs.addQuestion(question);
        }
        isComplete = true;
        ++position;
        count = 0;
        setCount(count);
        spinner.setSelection(0);
        questionText.setText("");
        questionText.requestFocus();
        save.setEnabled(true);
    }
    private void buttonSaveClick() {
        ContentValues values = new ContentValues();
        values.put(ExamDbSchema.ExamTable.Cols.TITLE, examName.getText().toString());
        values.put(ExamDbSchema.ExamTable.Cols.MARK, 0);
        store.insert(ExamDbSchema.ExamTable.TAB_NAME, null, values);
        values.clear();
        Cursor cursor = store.query(ExamDbSchema.ExamTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToLast();
        int idOfExam = cursor.getInt(cursor.getColumnIndex("_id"));
        for (Question q : qs.getQuestions()) {
            String s = q.getText();
            if (!s.equals("")) {
                values.put(ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY, idOfExam);
                values.put(ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT, q.getText());
                values.put(ExamDbSchema.QuestionTable.Cols.POSITION, q.getId());
                values.put(ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER, q.getAnswer());
                store.insert(ExamDbSchema.QuestionTable.TAB_NAME, null, values);
            }
            cursor.close();
            setAnswersTable(q);
        }
        loadFragment();
    }
    private void setAnswersTable(Question question) {
        ContentValues values = new ContentValues();
        int size = qs.answersSize();
        Cursor cursor = store.query(ExamDbSchema.QuestionTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToLast();
        int idOfQuestions = cursor.getInt(cursor.getColumnIndex("_id"));
        values.clear();
        for ( ; count < group; count++) {
            Option o = question.getOptions().get(count);
            values.put(ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY, idOfQuestions);
            values.put(ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT, o.getText());
            values.put(ExamDbSchema.AnswerTable.Cols.POSITION, o.getGroup());
            values.put(ExamDbSchema.AnswerTable.Cols.ANSWER_ID, o.getId());
            store.insert(ExamDbSchema.AnswerTable.TAB_NAME, null, values);
            values.clear();
        }
        cursor.close();
        if (size > group) {
            group += 4;
        }
    }
    private void loadFragment() {
        FragmentManager manager = Objects.requireNonNull(getActivity())
                .getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                .addToBackStack(null).commit();
    }
    private void cancelClick() {
        qs.answersClear();
        qs.questionsClear();
        FragmentManager manager = Objects.requireNonNull(getActivity())
                .getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                .addToBackStack(null).commit();
    }
}
