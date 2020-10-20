package ru.job4j.exam;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public class MainFragment extends Fragment {
    private QuestionStore qs;
    private StatisticStore statStore = new StatisticStore();
    private int position = 0;
    private int id;
    private ExamBaseHelper helper;
    private int[] buttonsArray = new int[]{};
    private boolean[] radioButtonsStateArray = new boolean[]{};
    private TextView text;
    private RadioGroup variants;
    private Button next;
    private Button previous;
    private Button hintButton;
    private boolean isBack = false;
    private int previousSelectedAnswerId;
    private void nextBtn() {
        fillStatistic();
        showAnswer();
        hintButton.setEnabled(true);
        variants.setEnabled(true);
        if (position < qs.questionsSize() - 1) {
            this.saveButtons();
            position++;
            if (isBack) {
                setPenaltyIfItSecondAttempt(variants.getCheckedRadioButtonId());
                isBack = false;
            }
            this.variants.clearCheck();
            fillForm();
        }
        else if (position == qs.questionsSize() - 1) {
            showAnswer();
            Intent intent = new Intent(getActivity(),
                    ResultActivator.class);
            intent.putExtra("id", id);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
        updateRadioButtons();
    }
    private void setPenaltyIfItSecondAttempt(int selectedVariantId) {
        if (isBack) {
            if (previousSelectedAnswerId != selectedVariantId) {
                statStore.penaltyIncrease(1);
            }
        }
    }
    private void hintClick() {
        DialogFragment dialog = new ConfirmHintDialogFragment();
        assert getFragmentManager() != null;
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialog_tag");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Activity.RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    setHint();
                    break;
                }
            }
        }
    }
    public void setHint() {
        deleteTwoIncorrectVariants(getTwoIncorrectAnswers(helper.getTrueAnswer(id, position) - 1));
        hintButton.setEnabled(false);
    }
    private List<Integer> getTwoIncorrectAnswers(int correct) {
        List<Integer> list = Arrays.asList(0, 1, 2, 3);
        List<Integer> result = new ArrayList<>();
        int random = (int) (Math.random() * 4);
        for (int i = 0; i < list.size(); i++) {
            if (i != correct && i != random) {
                result.add(i);
            }
        }
        return result;
    }
    private void deleteTwoIncorrectVariants(List<Integer> list) {
        RadioButton button = (RadioButton) variants.getChildAt(list.get(0));
        button.setChecked(false);
        button.setEnabled(false);
        button = (RadioButton) variants.getChildAt(list.get(1));
        button.setChecked(false);
        button.setEnabled(false);
    }
    private void prevButton() {
        this.variants.clearCheck();
        position--;
        this.restoreButtons();
        statStore.remove(position);
        fillForm();
        next.setEnabled(true);
        previous.setEnabled(false);
        isBack = true;
        previousSelectedAnswerId = variants.getCheckedRadioButtonId();
    }
    private void menuButton() {
        statStore.clear();
        Intent intent = new Intent(getActivity(), ExamListActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
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
        this.qs = new QuestionStore();
        this.helper = new ExamBaseHelper(getContext());
        this.radioButtonsStateArray = new boolean[variants.getChildCount()];
        this.hintButton = view.findViewById(R.id.hint);
        next.setOnClickListener(v -> nextBtn());
        previous.setOnClickListener(v -> prevButton());
        view.findViewById(R.id.toMenu).setOnClickListener(v -> menuButton());
        variants.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            next.setEnabled(rb != null && checkedId != -1);
        });
        hintButton.setOnClickListener(v -> hintClick());
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
            variants.check(savedInstanceState.getInt("checkedVariant"));
            next.setEnabled(savedInstanceState.getBoolean("buttonNextState"));
            previous.setEnabled(savedInstanceState.getBoolean("previousState"));
            buttonsArray = savedInstanceState.getIntArray("radioButtons");
            hintButton.setEnabled(savedInstanceState.getBoolean("hintButtonState"));
            radioButtonsStateArray = savedInstanceState.getBooleanArray("radioButtonsState");
            restoreRadioButtonsState();
        }
        this.setStore();
        this.fillForm();
        return view;
    }
    private void setStore() {
        qs.questionsClear();
        qs = helper.setQuestionStore(id);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.getInt("checkedVariant", variants.getCheckedRadioButtonId());
        outState.putBoolean("buttonNextState", next.isEnabled());
        outState.putBoolean("previousState", previous.isActivated());
        outState.putIntArray("radioButtons", this.buttonsArray);
        saveRadioButtonsState();
        outState.putBooleanArray("radioButtonsState", this.radioButtonsStateArray);
        outState.putBoolean("hintButtonState", hintButton.isEnabled());
    }
    private void saveRadioButtonsState() {
        for (int i = 0; i < radioButtonsStateArray.length; i++) {
            radioButtonsStateArray[i] = variants.getChildAt(i).isEnabled();
        }
    }
    private void restoreRadioButtonsState() {
        for (int i = 0; i < radioButtonsStateArray.length; i++) {
            variants.getChildAt(i).setEnabled(radioButtonsStateArray[i]);
        }
    }
    private void updateRadioButtons() {
        for (int i = 0; i < variants.getChildCount(); i++) {
            RadioButton rb = (RadioButton) variants.getChildAt(i);
            if (!rb.isEnabled()) {
                rb.setEnabled(true);
            }
        }
    }
    private void fillForm() {
        this.next.setEnabled(variants.isSelected());
        this.previous.setEnabled(position != 0);
        Question question = qs.getQuestion(position);
        this.text.setText(question.getText());
        for (int i = 0; i < variants.getChildCount(); i++) {
            RadioButton button = (RadioButton) variants.getChildAt(i);
            Option option = question.getOptions().get(i);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }
    private void saveButtons() {
        Question question = qs.getQuestion(position);
        int id = this.variants.getCheckedRadioButtonId();
        Option option = question.getOptions().get(id - 1);
        buttonsArray = new int[qs.questionsSize()];
        this.buttonsArray[position] = option.getId();
    }
    private void restoreButtons() {
        this.variants.check(this.buttonsArray[position]);
    }
    private void showAnswer() {
        int id = variants.getCheckedRadioButtonId();
        Question question = qs.getQuestion(position);
        Toast.makeText(getActivity(), getString(R.string.your_answer_string) + id
                        + getString(R.string.correct_is) + question.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }
    private void fillStatistic() {
        Question question = qs.getQuestion(position);
        int id = this.variants.getCheckedRadioButtonId();
        statStore.add(new Statistic(id, question.getAnswer()));
    }
}
