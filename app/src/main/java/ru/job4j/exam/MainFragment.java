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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;
import static ru.job4j.exam.ResultActivity.RESULT_FOR;

public class MainFragment extends Fragment {
    private StatisticStore statStore = new StatisticStore();
    private final QuestionStore store = QuestionStore.getInstance();
    private int position = 0;
    private int[] buttonsArray = new int[store.size()];
    private void nextBtn(View view) {
        RadioGroup variants = view.findViewById(R.id.variants);
        fillStatistic(view);
        if (position < store.size() - 1) {
            this.saveButtons(view);
            position++;
            variants.clearCheck();
            fillForm(view);
            showAnswer(view);
        }
        else if (position == store.size() -1) {
            showAnswer(view);
            Intent intent = new Intent(getActivity(),
                    ResultActivity.class);
            intent.putExtra(RESULT_FOR, position);
            startActivity(intent);
        }
    }
    private void prevButton(View view) {
        RadioGroup variants = view.findViewById(R.id.variants);
        variants.clearCheck();
        position--;
        this.restoreButtons(view);
        statStore.remove(position);
        fillForm(view);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        getActivity();
        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);
        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this::prevButton);
        RadioGroup variants = view.findViewById(R.id.variants);
        variants.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            next.setEnabled(rb != null && checkedId != -1);
        });
        this.position = Objects.requireNonNull(getActivity()).getIntent().getIntExtra(
                MainActivity.MAIN_FOR, 0);
        Button hint = view.findViewById(R.id.hint);
        hint.setOnClickListener(
                v -> startActivity(new Intent(getActivity(),
                        HintActivity.class)));
        hint.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getActivity(),
                            HintActivator.class);
                    getActivity().startActivity(intent);
                }
        );
        this.fillForm(view);
        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Button next = Objects.requireNonNull(getView()).findViewById(R.id.next);
        outState.putInt("position", position);
        outState.putBoolean("buttonNextState", next.isEnabled());
        outState.putIntArray("radioButtons", this.buttonsArray);
    }
    private void fillForm(View view) {
        RadioGroup variants = (RadioGroup) view.findViewById(R.id.variants);
        view.findViewById(R.id.next).setEnabled(variants.isSelected());
        Button previous = view.findViewById(R.id.previous);
        previous.setEnabled(position != 0);
        final TextView text = view.findViewById(R.id.question);
        Question question = store.get(position);
        text.setText(question.getText());
        for (int i = 0; i != 4; i++) {
            RadioButton button = (RadioButton) variants.getChildAt(i);
            Option option = question.getOptions().get(i);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }
    private void saveButtons(View view) {
        RadioGroup variants = (RadioGroup) view.findViewById(R.id.variants);
        Question question = store.get(position);
        int id = variants.getCheckedRadioButtonId();
        Option option = question.getOptions().get(id - 1);
        this.buttonsArray[position] = option.getId();
    }
    private void restoreButtons(View view) {
        RadioGroup variants = view.findViewById(R.id.variants);
        variants.check(this.buttonsArray[position]);
    }
    private void showAnswer(View view) {
        RadioGroup variants = view.findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(position);
        Toast.makeText(getActivity(), "Your answer is " + id
                        + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }
    private void fillStatistic(View view) {
        RadioGroup variants = (RadioGroup) view.findViewById(R.id.variants);
        Question question = store.get(position);
        int id = variants.getCheckedRadioButtonId();
        statStore.add(new Statistic(id, question.getAnswer()));
    }
}
