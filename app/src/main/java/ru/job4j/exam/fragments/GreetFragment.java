package ru.job4j.exam.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.job4j.exam.R;

public class GreetFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.greet, container, false);
        EditText text = view.findViewById(R.id.message);
        Button save = view.findViewById(R.id.save);
        save.setOnClickListener(
                event -> Toast.makeText(
                        getContext(),
                        text.getText().toString(),
                        Toast.LENGTH_SHORT
                ).show()
        );
        return view;
    }
}
