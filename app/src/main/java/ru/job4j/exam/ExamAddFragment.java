package ru.job4j.exam;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.Objects;

public class ExamAddFragment extends Fragment {
    private SQLiteDatabase store;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_exam_form, container, false);
        this.store = new ExamBaseHelper(getContext()).getWritableDatabase();
        final EditText et = view.findViewById(R.id.add_exam_editText);
        Button save = view.findViewById(R.id.add_exam_button_save);
        save.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(ExamDbSchema.ExamTable.Cols.TITLE, et.getText().toString());
            store.insert(ExamDbSchema.ExamTable.NAME, null, values);
            FragmentManager manager = Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.exams, new ExamListFragment())
                    .addToBackStack(null).commit();
        });
        return view;
    }
}
