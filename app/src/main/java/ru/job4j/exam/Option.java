package ru.job4j.exam;

public class Option {
    private int id;
    private String text;
    private int group;
    public Option(int id, String text, int group) {
        this.id = id;
        this.text = text;
        this.group = group;
    }
    public void setId(int id) {
        this.id = id;
    }
    int getId() {
        return this.id;
    }
    public void setText(String text) {
        this.text = text;
    }
    String getText() {
        return this.text;
    }
    int getGroup() {
        return group;
    }
}
