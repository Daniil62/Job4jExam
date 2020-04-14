package ru.job4j.exam;

public class Option {
    private int id;
    private String text;
    public Option(int id, String text) {
        this.id = id;
        this.text = text;
    }
    int getId() {
        return this.id;
    }
    String getText() {
        return this.text;
    }
}
