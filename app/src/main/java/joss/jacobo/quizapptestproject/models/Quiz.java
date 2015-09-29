package joss.jacobo.quizapptestproject.models;

import java.util.List;

import joss.jacobo.quizapptestproject.Quiz.QuizItem;

public class Quiz implements QuizItem {

    public String title;

    public List<Question> questions;

    public String getTitle(){
        return (title == null ? "" : title) + "Quiz!";
    }

}
