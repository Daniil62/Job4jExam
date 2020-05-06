package ru.job4j.exam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ru.job4j.exam.store.QuestionStore;

public class HintFragment extends Fragment {
    public static final String HINT_FOR = "hint_for";
    private final QuestionStore store = QuestionStore.getInstance();
    private final Map<Integer, String> answers = new HashMap<Integer, String>();
    public HintFragment() {
        answers.put(0, "Hint 1");
        answers.put(1, "Hint 2");
        answers.put(2, "Hint 3");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int question = getActivity().getIntent().getIntExtra(MainActivity.MAIN_FOR, 0);
        View view = inflater.inflate(R.layout.hint_activity, container, false);
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(
                v -> getActivity().onBackPressed()
        );
        TextView textQ = view.findViewById(R.id.questionHint);
        TextView text = view.findViewById(R.id.hint);
        text.setText(this.answers.get(question));
        textQ.setText(store.get(question).getText());
        return view;
    }
}
