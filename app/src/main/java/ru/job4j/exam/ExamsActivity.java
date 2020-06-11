package ru.job4j.exam;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamsActivity extends AppCompatActivity {
    private RecyclerView recycler;
    @Override
    protected void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.exams);
        this.recycler = findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
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
            final Exam exam = this.exams.get(i);
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
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(
                                    getApplicationContext(), "You select " + exam,
                                    Toast.LENGTH_SHORT
                            ).show();
                            startActivity(new Intent(ExamsActivity.this,
                                    MainActivator.class));
                        }
                    }
            );
        }
        @Override
        public int getItemCount() {
            return this.exams.size();
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
        List<Exam> exams = new ArrayList<Exam>();
        for (int i = 0; i < 10; i++) {
            exams.add(new Exam(i, String.format("Exam %s", i + 1),
                    0, 0));
        }
        ResultFragment resultFragment = new ResultFragment();
        Exam exam = resultFragment.getExam();
        if (exam != null) {
            exams.set(exam.getId(), exam);
        }
        this.recycler.setAdapter(new ExamAdapter(exams));
    }
}
