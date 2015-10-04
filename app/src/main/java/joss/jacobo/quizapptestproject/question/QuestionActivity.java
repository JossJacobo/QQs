package joss.jacobo.quizapptestproject.question;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import joss.jacobo.quizapptestproject.R;
import joss.jacobo.quizapptestproject.models.MultipleChoiceAnswer;
import joss.jacobo.quizapptestproject.models.Question;
import joss.jacobo.quizapptestproject.models.QuestionAdapter;
import joss.jacobo.quizapptestproject.models.QuestionItem;

public class QuestionActivity extends Activity implements QuestionListener {

    public static final String EXTRA_QUESTION = "question";

    public static Intent newIntent(Context context, Question question) {
        return new Intent(context, QuestionActivity.class)
                .putExtra(EXTRA_QUESTION, question);
    }

    private Question question;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = getIntent().getParcelableExtra(EXTRA_QUESTION);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        List<QuestionItem> items = new ArrayList<>();
        items.add(question);
        for (MultipleChoiceAnswer c : question.multipleChoiceAnswers) {
            c.setListener(this);
            items.add(c);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuestionAdapter(items));
    }

    @Override
    public void onMultipleChoiceClicked(MultipleChoiceAnswer answer) {
        if (!question.answered) {
            boolean correct = question.answer.equalsIgnoreCase(answer.answerId);

            question.answered = true;
            question.correct = correct;
            setResult(correct ? RESULT_OK : RESULT_CANCELED,
                    new Intent().putExtra(EXTRA_QUESTION, question));

            Toast.makeText(this, correct ? "Correct" : "Oops, wrong.", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }
}
