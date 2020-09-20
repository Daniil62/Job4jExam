package ru.job4j.exam;

import android.support.annotation.NonNull;

public class Exam {
    private int id;
    private String name;
    private long time;
    private float result;
    private boolean mark;
    Exam(int id, String name, long time, float result, boolean mark) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.result = result;
        this.mark = mark;
    }
    String getName() {
        return name;
    }
    long getTime() {
        return time;
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
                ", time=" + time +
                ", result=" + result +
                '}';
    }
}
