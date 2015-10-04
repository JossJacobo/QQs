package joss.jacobo.quizapptestproject.question;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import joss.jacobo.quizapptestproject.R;
import joss.jacobo.quizapptestproject.dagger.Dagger;
import joss.jacobo.quizapptestproject.databinding.ActivityQuestionBinding;
import joss.jacobo.quizapptestproject.models.MultipleChoiceAnswer;
import joss.jacobo.quizapptestproject.models.Question;
import joss.jacobo.quizapptestproject.models.QuestionAdapter;
import joss.jacobo.quizapptestproject.models.QuestionItem;
import joss.jacobo.quizapptestproject.timer.CountDownTimer;

public class QuestionActivity extends AppCompatActivity implements QuestionListener {

    public static final String EXTRA_QUESTION = "question";

    public static Intent newIntent(Context context, Question question) {
        return new Intent(context, QuestionActivity.class)
                .putExtra(EXTRA_QUESTION, question);
    }

    @Inject
    CountDownTimer timer;

    private Question question;
    private Handler handler = new Handler();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimerTitle(intent.getIntExtra(CountDownTimer.EXTRA_TIME, 0));
        }
    };

    ActivityQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);
        Dagger.mainComponent(this).inject(this);
        setSupportActionBar(binding.toolbar);

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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.toggle();
                updateFabImage();
            }
        });
        setResult(RESULT_OK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.register(receiver);
        updateTimerTitle(timer.getCurrentTime());
        updateFabImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.unregister(receiver);
    }

    @Override
    public void onMultipleChoiceClicked(MultipleChoiceAnswer answer) {
        if (!question.answered) {
            boolean correct = question.answer.equalsIgnoreCase(answer.answerId);

            question.answered = true;
            question.correct = correct;
            setResult(RESULT_OK, new Intent().putExtra(EXTRA_QUESTION, question));

            Toast.makeText(this, correct ? "Correct" : "Oops, wrong.", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    public void updateTimerTitle(int time) {
        setTitle(String.valueOf(time));

        if (time == 0) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void updateFabImage() {
        binding.fab.setImageResource(timer.getStatus() == CountDownTimer.Status.STARTED
                ? android.R.drawable.ic_media_pause
                : android.R.drawable.ic_media_play);
    }
}
