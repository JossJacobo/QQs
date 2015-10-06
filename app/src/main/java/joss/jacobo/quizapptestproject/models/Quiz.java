package joss.jacobo.quizapptestproject.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

import joss.jacobo.quizapptestproject.quiz.QuizItem;

public class Quiz extends BaseObservable implements QuizItem {

    public String title;

    public List<Question> questions;

    public String getTitle(){
        return (title == null ? "" : title) + " Quiz!";
    }

    private int getAnsweredCount(){
        int count = 0;
        for (Question q : questions) {
            if (q.answered) {
                count++;
            }
        }
        return count;
    }

    private int getAnsweredCorrectlyCount(){
        int count = 0;
        for (Question q : questions) {
            if (q.correct) {
                count++;
            }
        }
        return count;
    }

    @Bindable
    public String getAnsweredString() {
        return String.format("Answered: %s/%s", getAnsweredCount(), questions.size());
    }

    @Bindable
    public String getCorrectPercentage() {
        double answered = getAnsweredCorrectlyCount();
        double total = questions.size();
        return String.format("%s%%", (int) (answered / total * 100));
    }
}
