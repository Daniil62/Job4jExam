package ru.job4j.exam.store;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.exam.Exam;

public class ExamStore {
    private List<Exam> exams = new ArrayList<Exam>();
    public  List<Exam> getExams () {
        return exams;
    }
    public void add (Exam exam) {
        exams.add(exam);
    }
    public void set(int index, Exam exam) {
        exams.set(index, exam);
    }
    public Exam get(int index) {
        return exams.get(index);
    }
}
