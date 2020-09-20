package ru.job4j.exam;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public class MainFragment extends Fragment {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private List<Question> questions;
    private List<Option> answers;
    private StatisticStore statStore = new StatisticStore();
    private final QuestionStore store = QuestionStore.getInstance();
    private int position = 0;
    private int id;
    private int[] buttonsArray = new int[store.size()];
    private TextView text;
    private RadioGroup variants;
    private Button next;
    private Button previous;
    private void nextBtn(View view) {
        fillStatistic();
        showAnswer();
        if (position < store.size() - 1) {
            this.saveButtons();
            position++;
            this.variants.clearCheck();
            fillForm();
        }
        else if (position == store.size() -1) {
            showAnswer();
            Intent intent = new Intent(getActivity(),
                    ResultActivator.class);
            intent.putExtra("id", id);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }
    private void prevButton(View view) {
        this.variants.clearCheck();
        position--;
        this.restoreButtons();
        statStore.remove(position);
        fillForm();
        next.setEnabled(true);
    }
    private void menuButton(View view) {
        statStore.clear();
        Intent intent = new Intent(getActivity(), ExamListActivity.class);
        startActivity(intent);
    }
    public static MainFragment of(int value) {
        MainFragment main = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.MAIN_FOR, value);
        main.setArguments(bundle);
        return main;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        this.helper = new ExamBaseHelper(getContext());
        this.db = helper.getReadableDatabase();
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.text = view.findViewById(R.id.question);
        this.next = view.findViewById(R.id.next);
        this.previous = view.findViewById(R.id.previous);
        this.variants = view.findViewById(R.id.variants);
        this.position = Objects.requireNonNull(getActivity()).getIntent().getIntExtra(
                MainActivity.MAIN_FOR, 0);
  /*      Cursor questionCursor = db.query(ExamDbSchema.ExamTable.TAB_NAME, new String[]{
                "question_id", "question_text", "true_answer"},
                null, null, null, null, null);
        Cursor answerCursor = db.query(ExamDbSchema.ExamTable.TAB_NAME, new String[]{
                "answer_id", "answer_text"},
                null, null, null, null, null);
        List<Option> options = new ArrayList<>();
        questionCursor.moveToFirst();
        answerCursor.moveToFirst();
        while (questionCursor.isAfterLast()) {
            options.add(new Option(
                    answerCursor.getInt(answerCursor.getColumnIndex("answer_id")),
                    answerCursor.getString(answerCursor.getColumnIndex("answer_text"))));
            answerCursor.moveToNext();
            questions.add(new Question(
                    questionCursor.getInt(questionCursor.getColumnIndex("question_id")),
                    questionCursor.getString(questionCursor.getColumnIndex("question_text")),
                    options, questionCursor.getInt(questionCursor.getColumnIndex("question_id"))));
            options.clear();
        }
        questionCursor.close();
        answerCursor.close();                                   */
        next.setOnClickListener(this::nextBtn);
        previous.setOnClickListener(this::prevButton);
        Button menu = view.findViewById(R.id.toMenu);
        menu.setOnClickListener(this::menuButton);
        variants.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            next.setEnabled(rb != null && checkedId != -1);
        });
        Button hint = view.findViewById(R.id.hint);
        hint.setOnClickListener(
                v -> {
                    DialogFragment dialog = new ConfirmHintDialogFragment();
                    assert getFragmentManager() != null;
                    dialog.show(getFragmentManager(), "dialog_tag");
                }
        );
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
            variants.check(savedInstanceState.getInt("checkedVariant"));
            next.setEnabled(savedInstanceState.getBoolean("buttonNextState"));
            previous.setEnabled(savedInstanceState.getBoolean("previousState"));
            buttonsArray = savedInstanceState.getIntArray("radioButtons");
        }
        Intent intent = getActivity().getIntent();
        this.id = intent.getIntExtra("id", 0);
        this.fillForm();
        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.getInt("checkedVariant", variants.getCheckedRadioButtonId());
        outState.putBoolean("buttonNextState", next.isEnabled());
        outState.putBoolean("previousState", previous.isActivated());
        outState.putIntArray("radioButtons", this.buttonsArray);
    }
    private void fillForm() {
        this.next.setEnabled(variants.isSelected());
        this.previous.setEnabled(position != 0);
        Question question = store.get(position);
        this.text.setText(question.getText());
        for (int i = 0; i < variants.getChildCount(); i++) {
            RadioButton button = (RadioButton) variants.getChildAt(i);
            Option option = question.getOptions().get(i);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }
    private void saveButtons() {
        Question question = store.get(position);
        int id = this.variants.getCheckedRadioButtonId();
        Option option = question.getOptions().get(id - 1);
        this.buttonsArray[position] = option.getId();
    }
    private void restoreButtons() {
        this.variants.check(this.buttonsArray[position]);
    }
    private void showAnswer() {
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(position);
        Toast.makeText(getActivity(), "Your answer is " + id
                        + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }
    private void fillStatistic() {
        Question question = store.get(position);
        int id = this.variants.getCheckedRadioButtonId();
        statStore.add(new Statistic(id, question.getAnswer()));
    }
}
