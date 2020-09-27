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
import android.widget.EditText;
import java.util.Objects;

public class ExamUpdateFragment extends Fragment {
    private SQLiteDatabase store;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_update_form, container, false);
        this.store = new ExamBaseHelper(this.getContext()).getWritableDatabase();
        Bundle bundle = getArguments();
        final EditText et = view.findViewById(R.id.exam_update_form_editText);
        assert bundle != null;
        et.setText(bundle.getString("name"));
        view.findViewById(R.id.exam_update_form_button_back).setOnClickListener(v ->
                Objects.requireNonNull(getActivity()).onBackPressed());
        view.findViewById(R.id.exam_update_form_button_ok).setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(ExamDbSchema.ExamTable.Cols.TITLE, et.getText().toString());
            store.update(ExamDbSchema.ExamTable.TAB_NAME, values, "_id = ?",
                    new String[] {String.valueOf(bundle.getInt("id"))
            });
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                    .addToBackStack(null).commit();
        });
        return view;
    }
}
