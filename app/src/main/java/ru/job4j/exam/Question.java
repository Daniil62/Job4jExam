package ru.job4j.exam;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Option> options;
    private int answer;
    Question(int id, String text, List<Option> options, int answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }
    public int getId() {
        return this.id;
    }
    String getText() {
        return this.text;
    }
    List<Option> getOptions() {
        return  this.options;
    }
    public int getAnswer() {
        return  this.answer;
    }
}
