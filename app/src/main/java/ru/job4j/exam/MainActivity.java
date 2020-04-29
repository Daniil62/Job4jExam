package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;
import static ru.job4j.exam.HintActivity.HINT_FOR;
import static ru.job4j.exam.ResultActivity.RESULT_FOR;

public abstract class MainActivity extends FragmentActivity {
    private final QuestionStore store = QuestionStore.getInstance();
    private int position = 0;
    private static final String TAG = "ExamActivity";
    private static int count = 0;
    private StatisticStore statStore = new StatisticStore();
    private int[] buttonsArray = new int[store.size()];
    public abstract Fragment loadFrg();

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }
    private void nextBtn(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        fillStatistic();
        if (position < store.size() - 1) {
            this.saveButtons();
            position++;
            variants.clearCheck();
            fillForm();
            showAnswer();
        }
        else if (position == store.size() -1) {
            showAnswer();
            Intent intent = new Intent(MainActivity.this,
                    ResultActivity.class);
            intent.putExtra(RESULT_FOR, position);
            startActivity(intent);
        }
    }
    private void prevButton(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        variants.clearCheck();
        position--;
        this.restoreButtons();
        statStore.remove(position);
        fillForm();
    }
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.host_frg);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.content) == null) {
            fm.beginTransaction().add(R.id.content, loadFrg()).commit();
        }
        Button next = findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);
        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(this::prevButton);
        RadioGroup variants = findViewById(R.id.variants);
        variants.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            next.setEnabled(rb != null && checkedId != -1);
        });
        Button hint = findViewById(R.id.hint);
        hint.setOnClickListener(
                view -> startActivity(new Intent(MainActivity.this,
                        HintActivity.class)));
        hint.setOnClickListener(
                view -> {
                    Intent intent = new Intent(MainActivity.this,
                            HintActivity.class);
                    intent.putExtra(HINT_FOR, position);
                    startActivity(intent);
                }
        );
        this.fillForm();
        Log.d(TAG, "onCreate");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Button next = findViewById(R.id.next);
        if (outState != null) {
            outState.putInt("position", position);
            outState.putBoolean("buttonNextState", next.isEnabled());
            outState.putInt("Rotate count", count);
            outState.putIntArray("radioButtons", this.buttonsArray);
            count++;
        }
        Log.d(TAG, "onSaveInstanceState");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Button next = findViewById(R.id.next);
        position = savedInstanceState.getInt("position");
        fillForm();
        this.buttonsArray = savedInstanceState.getIntArray("radioButtons");
        next.setEnabled(savedInstanceState.getBoolean("buttonNextState"));
        Log.d(TAG, "onRestoreInstanceState");
        Log.d(TAG, "count = " + count + " position " + position
                + " static store size " + statStore.getStatistic().size());
    }
    private void fillForm() {
        RadioGroup variants = findViewById(R.id.variants);
        Button next = findViewById(R.id.next);
        next.setEnabled(variants.isSelected());
        findViewById(R.id.previous).setEnabled(position != 0);
        final TextView text = findViewById(R.id.question);
        Question question = store.get(position);
        text.setText(question.getText());
        for (int i = 0; i != variants.getChildCount(); i++) {
            RadioButton button = (RadioButton) variants.getChildAt(i);
            Option option = question.getOptions().get(i);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }
    private void saveButtons() {
        RadioGroup variants = findViewById(R.id.variants);
        Question question = store.get(position);
        int id = variants.getCheckedRadioButtonId();
        Option option = question.getOptions().get(id - 1);
        this.buttonsArray[position] = option.getId();
    }
    private void restoreButtons() {
        RadioGroup variants = findViewById(R.id.variants);
        variants.check(this.buttonsArray[position]);
    }
    private void showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(position);
        Toast.makeText(this, "Your answer is " + id
                        + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }
    private void fillStatistic() {
        RadioGroup variants = findViewById(R.id.variants);
        Question question = store.get(position);
        int id = variants.getCheckedRadioButtonId();
        statStore.add(new Statistic(id, question.getAnswer()));
    }
}
