package ru.job4j.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import ru.job4j.exam.store.QuestionStore;

public abstract class HintActivity extends FragmentActivity {
    public static final String HINT_FOR = "hint_for";
    private final QuestionStore store = QuestionStore.getInstance();
    public abstract Fragment loadFrg();
    public HintActivity() {
        store.getAnswers().put(0, "Hint 1");
        store.getAnswers().put(1, "Hint 2");
        store.getAnswers().put(2, "Hint 3");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_frg);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.content) == null) {
            fm.beginTransaction().add(R.id.content, loadFrg()).commit();
        }
        Button back = findViewById(R.id.back);
        back.setOnClickListener(
                view -> onBackPressed()
        );
        TextView text = findViewById(R.id.hint);
        TextView textQ = findViewById(R.id.questionHint);
        int question = getIntent().getIntExtra(HintActivity.HINT_FOR, 0);
        textQ.setText(this.store.get(question).getText());
        text.setText(store.getAnswers().get(question));
    }
}
