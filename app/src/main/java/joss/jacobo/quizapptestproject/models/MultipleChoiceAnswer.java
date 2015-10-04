package joss.jacobo.quizapptestproject.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import joss.jacobo.quizapptestproject.question.QuestionListener;

public class MultipleChoiceAnswer implements QuestionItem, Parcelable {

    @SerializedName("id")
    public String answerId;

    public String answer;

    private QuestionListener listener;

    public void setListener (QuestionListener listener) {
        this.listener = listener;
    }

    public View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onMultipleChoiceClicked(MultipleChoiceAnswer.this);
            }
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.answerId);
        dest.writeString(this.answer);
    }

    public MultipleChoiceAnswer() {
    }

    protected MultipleChoiceAnswer(Parcel in) {
        this.answerId = in.readString();
        this.answer = in.readString();
    }

    public static final Parcelable.Creator<MultipleChoiceAnswer> CREATOR = new Parcelable.Creator<MultipleChoiceAnswer>() {
        public MultipleChoiceAnswer createFromParcel(Parcel source) {
            return new MultipleChoiceAnswer(source);
        }

        public MultipleChoiceAnswer[] newArray(int size) {
            return new MultipleChoiceAnswer[size];
        }
    };
}
