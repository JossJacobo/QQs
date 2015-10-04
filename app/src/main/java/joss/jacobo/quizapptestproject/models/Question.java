package joss.jacobo.quizapptestproject.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import joss.jacobo.quizapptestproject.BR;
import joss.jacobo.quizapptestproject.quiz.QuizItem;
import joss.jacobo.quizapptestproject.quiz.QuizListener;

public class Question extends BaseObservable implements QuizItem, QuestionItem, Parcelable {

    public String question;

    public String answer;

    @Bindable
    public boolean answered;

    @Bindable
    public boolean correct;

    @SerializedName("multiple_choice")
    public List<MultipleChoiceAnswer> multipleChoiceAnswers;

    private QuizListener listener;

    public View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onQuestionClicked(Question.this);
            }
        }
    };

    public void setListener(QuizListener listener) {
        this.listener = listener;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeByte(answered ? (byte) 1 : (byte) 0);
        dest.writeByte(correct ? (byte) 1 : (byte) 0);
        dest.writeTypedList(multipleChoiceAnswers);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this.question = in.readString();
        this.answer = in.readString();
        this.answered = in.readByte() != 0;
        this.correct = in.readByte() != 0;
        this.multipleChoiceAnswers = in.createTypedArrayList(MultipleChoiceAnswer.CREATOR);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o instanceof Question) {
            Question other = (Question) o;
            return question.equals(other.question);
        } else {
            return false;
        }
    }
}
