package ru.job4j.exam;

public class Statistic {
    private int userAnswer;
    private int trueAnswer;
    Statistic(int userAnswer, int trueAnswer) {
        this.userAnswer = userAnswer;
        this.trueAnswer = trueAnswer;
    }
    public int getUserAnswer() {
        return this.userAnswer;
    }
    public int getTrueAnswer() {
        return this.trueAnswer;
    }
}
