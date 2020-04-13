package ru.job4j.exam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Question> questions = Arrays.asList(new Question(
                    1, "How many primitive variables does Java have?",
                    Arrays.asList(new Option(1, "1.1"), new Option(2, "1.2"),
                            new Option(3, "1.3"), new Option(4, "1.4")), 4),
            new Question(2, "What is Java Virtual Machine?",
                    Arrays.asList(new Option(1, "2.1"), new Option(2, "2.2"),
                            new Option(3, "2.3"), new Option(4, "2.4")), 4),
            new Question(3, "What is happen if we try unboxing null?",
                    Arrays.asList(new Option(1, "3.1"), new Option(2, "3.2"),
                            new Option(3, "3.3"), new Option(4, "3.4")), 4),
            new Question(4, "The end",
                    Arrays.asList(new Option(4, ""), new Option(4, ""),
                            new Option(4, ""), new Option(4, "")), 4));

    private int position = 0;
    private static final String TAG = "ExamActivity";
    private static int count = 0;

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }
    private void nextBtn(View view) {
        ++position;
        RadioGroup variants = findViewById(R.id.variants);
        showAnswer();
        variants.clearCheck();
        fillForm();
    }
    private void prevButton(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        variants.clearCheck();
        position--;
        fillForm();
    }
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        Button next = findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);
        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(this::prevButton);
        RadioGroup variants = findViewById(R.id.variants);
        variants.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                next.setEnabled(rb != null && checkedId != -1
                        && position != questions.size() - 1);
            }
        });
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
        if (outState != null) {
            outState.putInt("position", position);
            outState.putBoolean("buttonNextState", findViewById(R.id.next).isEnabled());
            count++;
            outState.putInt("Rotate count", count);
        }
        Log.d(TAG, "onSaveInstanceState");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("position");
        this.fillForm();
        findViewById(R.id.next).setEnabled(savedInstanceState.getBoolean("buttonNextState"));
        Log.d(TAG, "onRestoreInstanceState");
        Log.d(TAG, "count = " + count);
    }
    private void fillForm() {
        RadioGroup variants = findViewById(R.id.variants);
        findViewById(R.id.next).setEnabled(variants.isSelected());
        findViewById(R.id.previous).setEnabled(position != 0);
        final TextView text = findViewById(R.id.question);
        Question question = this.questions.get(this.position);
        text.setText(question.getText());
        for (int i = 0; i != variants.getChildCount(); i++) {
            RadioButton button = (RadioButton) variants.getChildAt(i);
            Option option = question.getOptions().get(i);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }
    private void showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = this.questions.get(this.position);
        Toast.makeText(this, "Your answer is " + id
                        + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }
}
