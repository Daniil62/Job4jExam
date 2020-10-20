package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;

public class ExamAddFragment extends Fragment {
    private ExamBaseHelper helper;
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
    private String description = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_exam_form, container, false);
        this.helper = new ExamBaseHelper(getContext());
        this.examName = view.findViewById(R.id.add_exam_editText);
        LinearLayout addDescription = view.findViewById(R.id.add_exam_desc_layout);
        this.questionText = view.findViewById(R.id.add_exam_question_editText);
        this.answerVariant = view.findViewById(R.id.add_exam_answer_editText);
        this.spinner = view.findViewById(R.id.add_exam_spinner);
        this.addAnswer = view.findViewById(R.id.add_exam_add_answer_button);
        this.addQuestionBlock = view.findViewById(R.id.add_exam_add_question_block_button);
        this.variantsComplete = view.findViewById(R.id.add_exam_count_variants_textView);
        this.save = view.findViewById(R.id.add_exam_button_save);
        Button cancel = view.findViewById(R.id.add_exam_cancel_button);
        this.qs = new QuestionStore();
        qs.answersClear();
        qs.questionsClear();
        this.position = 0;
        this.count = 0;
        this.group = 4;
        this.trueAnswerId = 0;
        this.isComplete = false;
        addDescription.setOnClickListener(v -> onAddDescriptionClick());
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
        setHasOptionsMenu(true);
        return view;
    }
    private void onAddDescriptionClick() {
        Objects.requireNonNull(getActivity())
                .getIntent().putExtra("description", description);
        DialogFragment dialog = new AddDescriptionFormFragment();
        assert getFragmentManager() != null;
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialog_tag");
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
        qs.getExam().setName(examName.getText().toString());
        qs.getExam().setDesc(description);
        helper.buildExam(qs, group);
        qs.answersClear();
        qs.questionsClear();
        loadFragment();
    }
    private void loadFragment() {
        Intent intent = new Intent(getActivity(), ExamListActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
    private void cancelClick() {
        qs.answersClear();
        qs.questionsClear();
        Objects.requireNonNull(getActivity()).onBackPressed();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_exam_form, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_exam: {
                Intent intent = new Intent(getActivity(), InnerExamsListActivity.class);
                Toast.makeText(getActivity(),
                        getString(R.string.select_a_completed_exam_from_this_list),
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Activity.RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    this.description = Objects.requireNonNull(getActivity())
                            .getIntent().getStringExtra("description");
                    break;
                }
            }
        }
    }
}
