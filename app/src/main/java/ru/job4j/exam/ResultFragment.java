package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public class ResultFragment extends Fragment {
    private StatisticStore statStore = new StatisticStore();
    private RecyclerView recycler;
    private int size = statStore.getStatistic().size();
    private int id;
    private TextView textScore;
    private TextView textPenalty;
    private SQLiteDatabase db;
    private int index;
    public static ResultFragment of(int value) {
        ResultFragment result = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ResultActivity.RESULT_FOR, value);
        result.setArguments(bundle);
        return result;
    }
    private void menuButton() {
        statStore.clear();
        Intent intent = new Intent(getActivity(), ExamListActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_activity, container, false);
        this.recycler = view.findViewById(R.id.result_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.db = new ExamBaseHelper(getContext()).getWritableDatabase();
        this.id = Objects.requireNonNull(getActivity()).getIntent().getIntExtra("id", 0);
        this.textScore = view.findViewById(R.id.textScore);
        this.textPenalty = view.findViewById(R.id.result_activity_penalty_textView);
        Button menu = view.findViewById(R.id.toMenu);
        menu.setOnClickListener(v -> menuButton());
        Button again = view.findViewById(R.id.try_again);
        again.setOnClickListener(v -> againClick());
        setResultList();
        setScoreText();
        setExamDate(id);
        setExamResult(id);
        setTimeResult(id);
        return view;
    }
    private void againClick() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("id", id);
        Objects.requireNonNull(getActivity()).startActivity(intent);
        statStore.clear();
    }
    private void setResultList() {
        index = 0;
        QuestionStore qs = new QuestionStore();
        for (int i = 0; i < size; i++) {
            if (statStore.getUserAnswer(i) == statStore.getTrueAnswer(i)) {
                index++;
            }
        }
        recycler.setAdapter(new ResultAdapter(qs.getQuestions()));
    }
    public class ResultAdapter extends RecyclerView.Adapter<ResultHolder> {
        private List<Question> questions;
        private ResultAdapter(List<Question> store) {
            this.questions = store;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @NonNull
        @Override
        public ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.result_answer_module, viewGroup, false);
            return new ResultHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ResultHolder holder, int i) {
            Question question = questions.get(i);
            StatisticStore statisticStore = new StatisticStore();
            TextView questionText = holder.view.findViewById(R.id.result_module_question_textView);
            TextView userAnswerTextView = holder.view.findViewById(R.id.result_module_user_answer_textView);
            TextView trueAnswerTextView = holder.view.findViewById(R.id.result_module_true_answer_textView);
            holder.view.setId(i);
            int userAnswerId = statisticStore.getUserAnswer(i);
            int trueAnswerId = statisticStore.getTrueAnswer(i);
            questionText.setText(question.getText());
            String userAnswer = getString(R.string.your_answer_result_string) + + userAnswerId;
            String trueAnswer = " / " + getString(R.string.true_answer_result_string) + trueAnswerId;
            userAnswerTextView.setText(userAnswer);
            trueAnswerTextView.setText(trueAnswer);
            if (userAnswerId != trueAnswerId) {
                userAnswerTextView.setTextColor(Color.parseColor("#B22222"));
            }
            holder.view.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AnswerDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("index", i);
                startActivity(intent);
            });
        }
        @Override
        public int getItemCount() {
            return questions.size();
        }
    }
    class ResultHolder extends RecyclerView.ViewHolder {
        private View view;
        ResultHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }
    private void setScoreText() {
        textScore.setText(MessageFormat.format(
                getString(R.string.score_string) + " {0}/{1}", index, size));
        textPenalty.setText(MessageFormat.format(
                "{0}{1}", getString(R.string.penalty_point_string), statStore.getPenalty()));
        if (index < statStore.getStatistic().size()) {
            textScore.setTextColor(Color.parseColor("#B22222"));
        } else {
            textScore.setTextColor(Color.parseColor("#00FF00"));
        }
    }
    private void setExamDate(int id) {
        ContentValues values = new ContentValues();
        Date date = new Date();
        values.put(ExamDbSchema.ExamTable.Cols.DATE, date.getTime());
        db.update(ExamDbSchema.ExamTable.TAB_NAME, values, "_id = " + id, new String[]{});
    }
    private void setExamResult(int id) {
        ContentValues values = new ContentValues();
        values.put(ExamDbSchema.ExamTable.Cols.RESULT, (
                index - statStore.getPenalty()) / (size * 0.01));
        db.update(ExamDbSchema.ExamTable.TAB_NAME, values, "_id = " + id, new String[]{});
        statStore.undoPenalty();
    }
    private void setTimeResult(int id) {
        ContentValues values = new ContentValues();
        Date date = new Date();
        values.put(ExamDbSchema.ExamTable.Cols.TIME_RESULT, date.getTime() - StatisticStore.getTime());
        db.update(ExamDbSchema.ExamTable.TAB_NAME, values, "_id = " + id, new String[]{});
    }
}
