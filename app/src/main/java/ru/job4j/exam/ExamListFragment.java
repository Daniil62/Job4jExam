package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExamListFragment extends Fragment {
    private RecyclerView recycler;
    private SQLiteDatabase store;
    private ExamBaseHelper helper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exams, container, false);
        this.recycler = view.findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.store = new ExamBaseHelper(getContext()).getWritableDatabase();
        this.helper = new ExamBaseHelper(getContext());
        setHasOptionsMenu(true);
        updateUI();
        return view;
    }
    private void updateUI() {
        this.recycler.setAdapter(new ExamAdapter(helper.getExams()));
    }
    public class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {
        private final List<Exam> exams;
        ExamAdapter(List<Exam> exams) {
            this.exams = exams;
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
            TextView resultTime = holder.view.findViewById(R.id.exam_module_time_result_textView);
            TextView dateText = holder.view.findViewById(R.id.date);
            CheckBox check = holder.view.findViewById(R.id.exam_checkBox);
            holder.view.setId(exam.getId());
            infoText.setText(exam.getName());
            if ((i % 2) == 0) {
                holder.view.setBackgroundColor(Color.parseColor("#F0F0F0"));
            }
            makeItemView(exam, resultText, resultTime, dateText);
            holder.view.setOnClickListener(view -> onExamClick(exam));
            check.setChecked(exam.isMark());
            check.setOnCheckedChangeListener((buttonView, isChecked) -> {
                exam.setMark(isChecked);
                ContentValues values = new ContentValues();
                values.put(ExamDbSchema.ExamTable.Cols.MARK, isChecked);
                store.update(ExamDbSchema.ExamTable.TAB_NAME, values, "_id = "
                        + (exam.getId()), new String[]{});
            });
            holder.view.findViewById(R.id.exam_edit_button).setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ExamUpdateActivity.class);
                intent.putExtra("id", exam.getId());
                intent.putExtra("name", exam.getName());
                startActivity(intent);
            /*    FragmentManager fm = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                Fragment fragment = new ExamUpdateFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", exam.getId());
                bundle.putString("name", exam.getName());
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.list_exams, fragment)
                        .addToBackStack(null).commit();*/
            });
        }
        @Override
        public int getItemCount() {
            return exams.size();
        }
        @SuppressLint("SimpleDateFormat")
        private void makeItemView(Exam exam, TextView resultText, TextView resultTime, TextView dateText) {
            if (exam.getDate() != 0 && exam.getTime() != 0) {
                int f = exam.getResult() == 100.0 ? 0 : 1;
                String result = getString(R.string.result) + ": "
                        + String.format("%." + f + "f", exam.getResult()) + " %";
                SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                resultText.setText(result);
                dateText.setText(sd.format(new Date(exam.getDate())));
                sd = new SimpleDateFormat("mm:ss");
                resultTime.setText(sd.format(new Date(exam.getTime())));
                resultPainter(resultText, exam.getResult());
            }
        }
        private void resultPainter(TextView tv, float score) {
            int r = 255;
            int g = 0;
            double percent = (score * 2.5);
            tv.setTextColor(Color.rgb(r - (int) percent, g + (int) percent, 30));
        }
        private void onExamClick(Exam exam) {
            Intent intent = new Intent(getActivity(), ExamDescriptionActivity.class);
            intent.putExtra("id", exam.getId());
            intent.putExtra("exam_name", exam.getName());
            startActivity(intent);
        }
    }
    class ExamHolder extends RecyclerView.ViewHolder {
        private View view;
        ExamHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exams, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_item: {
                Intent intent = new Intent(getActivity(), ExamAddActivity.class);
                Toast.makeText(getActivity(), getString(R.string.create_new_exam),
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                return true;
            }
            case R.id.menu_delete_items: {
                DialogFragment dialog = new DialogExamsDelete();
                dialog.show(Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager(), "dialog_exams_delete");
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
