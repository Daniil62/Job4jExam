package ru.job4j.exam.store;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.exam.Statistic;

public final class StatisticStore {
    private static List<Statistic> statistic = new ArrayList<>();
    private static float penalty;
    private static long time;
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
    public void penaltyIncrease() {
        penalty += 0.5;
    }
    public void penaltyIncrease(int value) {
        penalty += value;
    }
    public float getPenalty() {
        return penalty;
    }
    public void undoPenalty() {
        penalty = 0;
    }
    public static long getTime() {
        return time;
    }
    public static void setTime(long time) {
        StatisticStore.time = time;
    }
}
