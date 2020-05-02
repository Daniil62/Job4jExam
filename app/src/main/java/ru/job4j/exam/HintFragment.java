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
import ru.job4j.exam.store.QuestionStore;

public class HintFragment extends Fragment {
    public static final String HINT_FOR = "hint_for";
    private final QuestionStore store = QuestionStore.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int question = getActivity().getIntent().getIntExtra(HintActivity.HINT_FOR, 0);
        View view = inflater.inflate(R.layout.hint_activity, container, false);
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(
                v -> getActivity().onBackPressed()
        );
        TextView text = view.findViewById(R.id.hint);
        TextView textQ = view.findViewById(R.id.questionHint);
        textQ.setText(store.get(question).getText());
        text.setText(store.getAnswers().get(question));
        return view;
    }
}
