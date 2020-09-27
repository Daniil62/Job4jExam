package ru.job4j.exam;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;

public class HintFragment extends Fragment {
    private SQLiteDatabase store;
    private TextView questionText;
    private TextView answerText;
    public static HintFragment of(int index) {
        HintFragment hint = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HintActivity.HINT_FOR, index);
        hint.setArguments(bundle);
        return hint;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        int id = intent.getIntExtra("id_for_hint", 0);
        int position = intent.getIntExtra("position_for_hint", 0);
        View view = inflater.inflate(R.layout.hint_activity, container, false);
        this.store = new ExamBaseHelper(getContext()).getReadableDatabase();
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(
                v -> getActivity().onBackPressed()
        );
        questionText = view.findViewById(R.id.questionHint);
        answerText = view.findViewById(R.id.hint);
        showAnswer(id, position);
        return view;
    }
    private void showAnswer(int id, int position) {
        boolean exit = false;
        Cursor cursor = store.query(ExamDbSchema.QuestionTable.TAB_NAME, null,
                null, null, null, null, null);
        Cursor cursor2 = store.query(ExamDbSchema.AnswerTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        cursor2.moveToFirst();
        while (!cursor.isAfterLast() && !exit) {
            if (cursor.getInt(cursor.getColumnIndex(
                    ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY)) == id
                    && cursor.getInt(cursor.getColumnIndex(
                            ExamDbSchema.QuestionTable.Cols.POSITION)) == position) {
                this.questionText.setText(new StringBuilder(cursor.getString(
                        cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT)) + "\n"));
                while (!cursor2.isAfterLast()) {
                    if (cursor2.getInt(cursor2.getColumnIndex(
                            ExamDbSchema.AnswerTable.Cols.ANSWER_ID)) ==
                            cursor.getInt(cursor.getColumnIndex(
                                    ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER))
                            && cursor2.getInt(cursor2.getColumnIndex(
                            ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY)) == cursor.getInt(
                                    cursor.getColumnIndex("_id"))) {
                        this.answerText.setText(cursor2.getString(
                                cursor2.getColumnIndex(ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT)));
                        exit = true;
                        break;
                    }
                    cursor2.moveToNext();
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        cursor2.close();
    }
}
