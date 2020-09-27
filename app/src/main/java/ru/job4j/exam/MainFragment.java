package ru.job4j.exam;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import ru.job4j.exam.store.StatisticStore;

public class MainFragment extends Fragment {
    private SQLiteDatabase db;
    private static List<Question> store = new ArrayList<>();
    public static List<Question> getStore() {
        return store;
    }
    private StatisticStore statStore = new StatisticStore();
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
        else if (position == store.size() - 1) {
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
        this.text = view.findViewById(R.id.question);
        this.next = view.findViewById(R.id.next);
        this.previous = view.findViewById(R.id.previous);
        this.variants = view.findViewById(R.id.variants);
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        this.id = intent.getIntExtra("id", 0);
        this.db = new ExamBaseHelper(getContext()).getReadableDatabase();
        next.setOnClickListener(this::nextBtn);
        previous.setOnClickListener(this::prevButton);
        view.findViewById(R.id.toMenu).setOnClickListener(this::menuButton);
        variants.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            next.setEnabled(rb != null && checkedId != -1);
        });
        Button hint = view.findViewById(R.id.hint);
        hint.setOnClickListener(
                v -> {
                    DialogFragment dialog = new ConfirmHintDialogFragment();

                    intent.putExtra("id_for_hint", id);
                    intent.putExtra("position_for_hint", position);
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
        this.setStore();
        this.fillForm();
        return view;
    }
    private void setStore() {
        store.clear();
        Cursor cursor = db.query(ExamDbSchema.QuestionTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(
                    ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY)) == id) {
                Question question = new Question(cursor.getInt(
                        cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.POSITION)),
                        cursor.getString(cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT)), this.setAnswers(
                                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.POSITION))),
                        cursor.getInt(cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER)));
                store.add(question);
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
    private List<Option> setAnswers(int id, int group) {
        Cursor cursor = db.query(ExamDbSchema.AnswerTable.TAB_NAME, null,
                null, null, null, null, null);
        List<Option> result = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(
                    ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY)) == id
                    && cursor.getInt(cursor.getColumnIndex(
                            ExamDbSchema.AnswerTable.Cols.POSITION)) == group) {
                Option option = new Option(cursor.getInt(cursor.getColumnIndex(
                        ExamDbSchema.AnswerTable.Cols.ANSWER_ID)),
                        cursor.getString(cursor.getColumnIndex(
                                ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT)),
                        cursor.getInt(cursor.getColumnIndex(
                                ExamDbSchema.AnswerTable.Cols.POSITION)));
                result.add(option);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
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
        buttonsArray = new int[store.size()];
        this.buttonsArray[position] = option.getId();
    }
    private void restoreButtons() {
        this.variants.check(this.buttonsArray[position]);
    }
    private void showAnswer() {
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(position);
        Toast.makeText(getActivity(), getString(R.string.your_answer_string) + id
                        + getString(R.string.correct_is) + question.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }
    private void fillStatistic() {
        Question question = store.get(position);
        int id = this.variants.getCheckedRadioButtonId();
        statStore.add(new Statistic(id, question.getAnswer()));
    }
}
