package ru.job4j.exam;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.MessageFormat;
import ru.job4j.exam.store.QuestionStore;
import ru.job4j.exam.store.StatisticStore;

public abstract class ResultActivity extends FragmentActivity {
    public static final String RESULT_FOR = "result_for";
    private StringBuilder sb = new StringBuilder();
    private StatisticStore statStore = new StatisticStore();
    private final QuestionStore qStore = QuestionStore.getInstance();
    private int size = statStore.getStatistic().size();
    public abstract Fragment loadFrg();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_frg);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.content) == null) {
            fm.beginTransaction().add(R.id.content, loadFrg()).commit();
        }
        Button again = findViewById(R.id.try_again);
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this,
                        MainActivity.class));
                statStore.clear();
            }
        });
        TextView textAnswers = findViewById(R.id.textAnswers);
        TextView textScore = findViewById(R.id.textScore);
        int index = 0;
        for (int i = 0; i < size; i++) {
            sb.append(qStore.get(i).getText() + "\n" + "Your answer: "
                    +statStore.getUserAnswer(i) + "\t" + "True answer: "
                    + statStore.getTrueAnswer(i) + "\n\n");
            textAnswers.setText(sb);
            if (statStore.getUserAnswer(i) == statStore.getTrueAnswer(i)) {
                index++;
            }
        }
        textScore.setText(MessageFormat.format("Score: {0}/{1}", index, size));
        if (index < statStore.getStatistic().size()) {
            textScore.setTextColor(Color.parseColor("#B22222"));
        }
        else {
            textScore.setTextColor(Color.parseColor("#00FF00"));
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
