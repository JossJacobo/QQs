package joss.jacobo.quizapptestproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {

    public String question;

    public String answer;

    @SerializedName("multiple_choice")
    public List<MultipleChoiceAnswer> multipleChoiceAnswers;

}
