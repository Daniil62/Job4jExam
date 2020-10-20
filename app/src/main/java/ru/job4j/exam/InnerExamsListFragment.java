package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.job4j.exam.store.ExamsStore;
import ru.job4j.exam.store.QuestionStore;

public class InnerExamsListFragment extends Fragment {
    private RecyclerView recycler;
    private ExamBaseHelper helper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_exams_list, container, false);
        this.recycler = view.findViewById(R.id.inner_exams_recycler);
        this.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.helper = new ExamBaseHelper(getContext());
        examsUpdate();
        return view;
    }
    private void examsUpdate() {
        List<String> list = new ArrayList<>();
        for (QuestionStore store : ExamsStore.INNER_EXAMS_STORE) {
            list.add(store.getExam().getName());
        }
        this.recycler.setAdapter(new InnerExamAdapter(list));
    }
    public class InnerExamAdapter extends RecyclerView.Adapter<InnerExamHolder> {
        private final List<String> exams;
        private InnerExamAdapter(List<String> list) {
            this.exams = list;
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
        public InnerExamHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.inner_exam_module, viewGroup, false);
            return new InnerExamsListFragment.InnerExamHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull InnerExamHolder holder, int i) {
            String string = exams.get(i);
            TextView title = holder.view.findViewById(R.id.inner_exam_module_textView);
            ImageView add = holder.view.findViewById(R.id.inner_exam_module_add_button);
            title.setText(string);
            title.setId(i);
            holder.view.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ExamDescriptionActivity.class);
                intent.putExtra("index", title.getId());
                intent.putExtra("exam_name",
                        ExamsStore.INNER_EXAMS_STORE.get(i).getExam().getName());
                startActivity(intent);
            });
            add.setOnClickListener(v -> {
                QuestionStore store = ExamsStore.INNER_EXAMS_STORE.get(i);
                helper.buildExam(store, 4);
                Toast.makeText(
                        getContext(), getString(R.string.exam)
                                + ExamsStore.INNER_EXAMS_STORE.get(i).getExam().getName()
                                + getString(R.string.added_to_te_list),
                        Toast.LENGTH_SHORT
                ).show();
                Intent intent = new Intent(getActivity(), ExamListActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            });
        }
        @Override
        public int getItemCount() {
            return exams.size();
        }
    }
    class InnerExamHolder extends RecyclerView.ViewHolder {
        private View view;
        InnerExamHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }
}
