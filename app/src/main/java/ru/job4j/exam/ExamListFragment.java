package ru.job4j.exam;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import ru.job4j.exam.store.ExamStore;

public class ExamListFragment extends Fragment implements DialogExamsDelete
        .ExamsDeleteDialogListener {
    private RecyclerView recycler;
    private ExamStore examStore;
/*    @Override
    public void onBackPressed() {
        super.getActivity().onBackPressed();
        getActivity().finish();
    }  */
    private SQLiteDatabase store;
    public static ExamListFragment of (int value) {
        ExamListFragment elf = new ExamListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamListActivity.EXAM_LIST_FOR, value);
        elf.setArguments(bundle);
        return elf;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exams, container, false);
        this.recycler = view.findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.store = new ExamBaseHelper(getContext()).getWritableDatabase();
        this.examStore = new ExamStore();
        updateUI();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {
        private final List<Exam> exams;
        ExamAdapter(List<Exam> exams) {
            this.exams = exams;
        }
        @NonNull
        @Override
        public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.info_exam, parent, false);
            return new ExamHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ExamHolder holder, int i) {
            final Exam exam = exams.get(i);
            TextView infoText = holder.view.findViewById(R.id.info);
            TextView resultText = holder.view.findViewById(R.id.result);
            TextView dateText = holder.view.findViewById(R.id.date);
            infoText.setText(exam.getName());
            if (exam.getTime() != 0) {
                SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                resultText.setText(getString(R.string.result) + ": " + exam.getResult() + " %");
                dateText.setText(getString(R.string.date) + ": " + sd.format(new Date(exam.getTime())));
                if (exam.getResult() < 95) {
                    resultText.setTextColor(Color.parseColor("#B22222"));
                } else {
                    resultText.setTextColor(Color.parseColor("#00FF00"));
                }
            }
            infoText.setOnClickListener(
                    view -> {
                        Toast.makeText(
                                getContext(), "You select " + exam,
                                Toast.LENGTH_SHORT
                        ).show();
                        startActivity(new Intent(getActivity(),
                                MainActivator.class));
                        Objects.requireNonNull(getActivity()).finish();
                    }
            );
        }
        @Override
        public int getItemCount() {
            return examStore.getExams().size();
        }
    }
    class ExamHolder extends RecyclerView.ViewHolder {
        private View view;
        ExamHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }
    private void updateUI() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = this.store.query(ExamDbSchema.ExamTable.NAME, null, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            exams.add(new Exam(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    0, (float) 0.0));
            cursor.moveToNext();
        }
        cursor.close();
        this.recycler.setAdapter(new ExamAdapter(exams));
     /*   for (int i = 0; i < 1; i++) {
            examStore.add(new Exam(i, String.format("Exam %s", i + 1),
                    0, 0));
        }
        ResultFragment resultFragment = new ResultFragment();
        Exam exam = resultFragment.getExam();
        if (exam != null) {
            examStore.set(exam.getId(), exam);
        }
        this.recycler.setAdapter(new ExamAdapter(examStore.getExams()));   */
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exams, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_date_time_set: {
                Intent intent = new Intent(getActivity(), DateTimeActivator.class);
                startActivity(intent);
                return true;
            }
            case R.id.menu_add_item: {
                FragmentManager manager = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.exams, new ExamAddFragment())
                        .addToBackStack(null).commit();
                return true;
           /*     ExamStore es = new ExamStore();
                es.add(new Exam(es.getExams().size(), "new exam", 0, 0));
                updateUI();
                Toast.makeText(getActivity(), R.string.new_item_added,
                        Toast.LENGTH_SHORT).show();
                return true;    */
            }
            case R.id.menu_delete_items: {
                DialogFragment dialog = new DialogExamsDelete();
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "delete_items_dialog");
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
    @Override
    public void positiveExamsDeleteClick(DialogExamsDelete ded) {
        examStore.clear();
        updateUI();
        Toast.makeText(getContext(), R.string.all_items_deleted,
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void negativeExamsDeleteClick(DialogExamsDelete ded) {
    }
}
