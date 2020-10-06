package ru.job4j.exam.store;

import ru.job4j.exam.Option;
import ru.job4j.exam.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionStore {
    private static List<Question> questions = new ArrayList<>();
    private List<Option> answers = new ArrayList<>();
    public void addQuestion(Question question) {
        questions.add(question);
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public Question getQuestion(int position) {
        return questions.get(position);
    }
    public void questionsClear() {
        questions.clear();
    }
    public int questionsSize() {
        return questions.size();
    }
    public void addAnswer(Option option) {
        answers.add(option);
    }
    public List<Option> getAnswers() {
        return answers;
    }
    public int answersSize() {
        return answers.size();
    }
    public void answersClear() {
        answers.clear();
    }
}
