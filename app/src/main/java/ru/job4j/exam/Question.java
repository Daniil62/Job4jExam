package ru.job4j.exam;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Option> options;
    private int answer;
    public Question(int id, String text, List<Option> options, int answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    public void setText(String text) {
        this.text = text;
    }
    String getText() {
        return this.text;
    }
    List<Option> getOptions() {
        return  this.options;
    }
    void setAnswer(int answer) {
        this.answer = answer;
    }
    int getAnswer() {
        return  this.answer;
    }
}
