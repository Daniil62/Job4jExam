package ru.job4j.exam.store;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.exam.Statistic;

public final class StatisticStore {
    private static List<Statistic> statistic = new ArrayList<>();
    public int getUserAnswer(int position) {
        return statistic.get(position).getUserAnswer();
    }
    public int getTrueAnswer(int position) {
        return statistic.get(position).getTrueAnswer();
    }
    public void add(Statistic stat) {
        statistic.add(stat);
    }
    public void clear() {
        statistic.clear();
    }
    public void remove(int index) {
        statistic.remove(index);
    }
    public List<Statistic> getStatistic() {
        return statistic;
    }
}
