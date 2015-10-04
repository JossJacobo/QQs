package joss.jacobo.quizapptestproject.quiz;

import joss.jacobo.quizapptestproject.models.Question;

public interface QuizListener {
    void onQuestionClicked(Question question);
}
