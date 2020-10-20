package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public class AnswerDetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answer_details_dialog, container, false);
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        int index = intent.getIntExtra("index", -1);
        TextView questionTextView = view.findViewById(R.id.answer_details_question_textView);
        TextView userAnswerTextView = view.findViewById(R.id.answer_details_user_textView);
        TextView trueAnswerTextView = view.findViewById(R.id.answer_details_true_textView);
        ImageView closeButton = view.findViewById(R.id.answer_details_close_button);
        QuestionStore qs = new QuestionStore();
        StatisticStore statisticStore = new StatisticStore();
        if (index != -1) {
            Question question = qs.getQuestion(index);
            int userAnswerIndex = statisticStore.getUserAnswer(index) - 1;
            int trueAnswerIndex = statisticStore.getTrueAnswer(index) - 1;
            questionTextView.setText(question.getText());
            String yourAnswerText = getString(R.string.your_answer_result_string)
                    + "\n" + question.getOptions().get(userAnswerIndex).getText();
            userAnswerTextView.setText(yourAnswerText);
            String trueAnswerText = getString(R.string.true_answer_result_string)
                    + "\n" + question.getOptions().get(trueAnswerIndex).getText();
            trueAnswerTextView.setText(trueAnswerText);
        }
        closeButton.setOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }
}
