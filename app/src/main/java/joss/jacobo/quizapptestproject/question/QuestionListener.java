package joss.jacobo.quizapptestproject.question;

import joss.jacobo.quizapptestproject.models.MultipleChoiceAnswer;

public interface QuestionListener {
    void onMultipleChoiceClicked(MultipleChoiceAnswer answer);
}
