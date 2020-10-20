package ru.job4j.exam;

import android.support.annotation.NonNull;

public class Exam {
    private int id;
    private String name;
    private long date;
    private float result;
    private boolean mark;
    private String desc;
    private long time;
    public Exam(int id, String name, long date, float result, boolean mark, String desc, long time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.result = result;
        this.mark = mark;
        this.desc = desc;
        this.time = time;
    }
    public Exam(){}
    public void setName(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }
    long getDate() {
        return date;
    }
    float getResult() {
        return result;
    }
    public int getId() {
        return id;
    }
    boolean isMark() {
        return mark;
    }
    void setMark(boolean mark) {
        this.mark = mark;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id == exam.id;
    }
    public String getDesc() {
        return desc;
    }
    void setDesc(String desc) {
        this.desc = desc;
    }
    long getTime() {
        return time;
    }
    @Override
    public int hashCode() {
        return id;
    }
    @NonNull
    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", result=" + result +
                '}';
    }
}
