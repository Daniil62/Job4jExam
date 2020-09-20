package ru.job4j.exam;

class ExamDbSchema {
    static final class ExamTable {
        static final String TAB_NAME = "Exams";
        static final class Cols {
            static final String INDEX = "exam_id";
            static final String TITLE = "exam_name";
            static final String TIME = "exam_date";
            static final String RESULT = "exam_result";
            static final String MARK = "exam_mark";
        }
    }
    static final class QuestionTable {
        static final String TAB_NAME = "Questions";
        static final class Cols {
            static final String POSITION = "question_id";
            static final String QUESTION_TEXT = "question_text";
            static final String TRUE_ANSWER = "true_answer";
        }
    }
    static final class AnswerTable {
        static final String TAB_NAME = "Answers";
        static final class Cols {
            static final String ANSWER_ID = "answer_id";
            static final String ANSWER_TEXT = "answer_text";
        }
    }
}