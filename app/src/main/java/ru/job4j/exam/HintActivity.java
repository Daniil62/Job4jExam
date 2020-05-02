package ru.job4j.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import ru.job4j.exam.store.QuestionStore;

public abstract class HintActivity extends AppCompatActivity {
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
            fm.beginTransaction()
                    .add(R.id.content, loadFrg())
                    .commit();
        }
    }
}
