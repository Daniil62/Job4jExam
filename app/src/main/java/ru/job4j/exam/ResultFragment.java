package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public class ResultFragment extends Fragment {
    private StringBuilder sb = new StringBuilder();
    private StatisticStore statStore = new StatisticStore();
    private final QuestionStore qStore = QuestionStore.getInstance();
    private int size = statStore.getStatistic().size();
    private static Exam exam;
    public Exam getExam() {
        return exam;
    }
    public static ResultFragment of(int value) {
        ResultFragment result = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ResultActivity.RESULT_FOR, value);
        result.setArguments(bundle);
        return result;
    }
    private void menuButton(View view) {
        statStore.clear();
        Intent intent = new Intent(getActivity(), ExamListActivator.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("examId", exam.getId());
        outState.putString("examName", exam.getName());
        outState.putLong("examTime", exam.getTime());
        outState.putFloat("examResult", exam.getResult());
    }
    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_activity, container, false);
        TextView textAnswers = view.findViewById(R.id.textAnswers);
        TextView textScore = view.findViewById(R.id.textScore);
        Button menu = view.findViewById(R.id.toMenu);
        menu.setOnClickListener(this::menuButton);
        Button again = view.findViewById(R.id.try_again);
        again.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivator.class);
            Objects.requireNonNull(getActivity()).startActivity(intent);
            statStore.clear();
        });
        int index = 0;
        for (int i = 0; i < size; i++) {
            sb.append(qStore.get(i).getText() + "\n" + "Your answer: "
                    + statStore.getUserAnswer(i) + "\t" + "True answer: "
                    + statStore.getTrueAnswer(i) + "\n\n");
            textAnswers.setText(sb);
            if (statStore.getUserAnswer(i) == statStore.getTrueAnswer(i)) {
                index++;
            }
        }
        textScore.setText(MessageFormat.format("Score: {0}/{1}", index, size));
        if (index < statStore.getStatistic().size()) {
            textScore.setTextColor(Color.parseColor("#B22222"));
        } else {
            textScore.setTextColor(Color.parseColor("#00FF00"));
        }
        if (savedInstanceState != null) {
            exam = new Exam(savedInstanceState.getInt("examId"),
                    savedInstanceState.getString("examName"),
                    savedInstanceState.getLong("examTime"),
                    savedInstanceState.getFloat("examResult"));
        } else {
            Date date = new Date();
            double result = index / (size * 0.01);
            exam = new Exam(0, "Exam 1", date.getTime(), (int) result);
        }
        return view;
    }
}
