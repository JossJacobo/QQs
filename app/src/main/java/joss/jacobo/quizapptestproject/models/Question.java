package joss.jacobo.quizapptestproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import joss.jacobo.quizapptestproject.Quiz.QuizItem;

public class Question implements QuizItem {

    public String question;

    public String answer;

    @SerializedName("multiple_choice")
    public List<MultipleChoiceAnswer> multipleChoiceAnswers;

}
