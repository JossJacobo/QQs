package joss.jacobo.quizapptestproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import joss.jacobo.quizapptestproject.dagger.Dagger;
import joss.jacobo.quizapptestproject.databinding.ActivityMainBinding;
import joss.jacobo.quizapptestproject.models.Question;
import joss.jacobo.quizapptestproject.models.Quiz;
import joss.jacobo.quizapptestproject.question.QuestionActivity;
import joss.jacobo.quizapptestproject.quiz.QuizAdapter;
import joss.jacobo.quizapptestproject.quiz.QuizItem;
import joss.jacobo.quizapptestproject.quiz.QuizListener;
import joss.jacobo.quizapptestproject.quiz.QuizLoader;
import joss.jacobo.quizapptestproject.timer.CountDownTimer;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Quiz>, QuizListener{

    private static final int REQUEST_CODE_QUESTION = 1;
    private static final String QUIZ_URL
            = "https://gist.githubusercontent.com/JossJacobo/a01137c7f80506798041/raw/c45ab5ef43292b836e52f6c45983a41350da290b/qq.json";

    @Inject
    OkHttpClient client;
    @Inject
    Gson gson;
    @Inject
    CountDownTimer countDownTimer;

    ActivityMainBinding binding;
    RecyclerView recyclerView;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimerTitle(countDownTimer.getCurrentTime());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Dagger.mainComponent(this).inject(this);

        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.toggle();
                updateFabImage();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuizAdapter());

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.register(receiver);
        setTitle(String.valueOf(countDownTimer.getCurrentTime()));
        updateFabImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.unregister(receiver);
    }

    @Override
    public Loader<Quiz> onCreateLoader(int id, Bundle args) {
        return new QuizLoader(this, client, gson, QUIZ_URL);
    }

    @Override
    public void onLoadFinished(Loader<Quiz> loader, Quiz quiz) {
        if (quiz != null) {
            List<QuizItem> items = new ArrayList<>();
            items.add(quiz);
            for (Question q : quiz.questions) {
                q.setListener(this);
                items.add(q);
            }
            ((QuizAdapter) recyclerView.getAdapter()).setItems(items);
        }
    }

    @Override
    public void onLoaderReset(Loader<Quiz> loader) {

    }

    @Override
    public void onQuestionClicked(Question question) {
        if (countDownTimer.getStatus() == CountDownTimer.Status.STOPPED) {
            Toast.makeText(this, "Start the timer to begin!", Toast.LENGTH_SHORT) .show();
        }else if (!question.answered) {
            startActivityForResult(QuestionActivity.newIntent(this, question), REQUEST_CODE_QUESTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUESTION) {
            if (resultCode == RESULT_OK && data != null) {
                Question q = data.getParcelableExtra(QuestionActivity.EXTRA_QUESTION);
                QuizAdapter adapter = (QuizAdapter) recyclerView.getAdapter();
                Quiz quiz = (Quiz) adapter.items.get(0);

                quiz.questions.set(quiz.questions.indexOf(q), q);
                adapter.items.set(adapter.items.indexOf(q), q);
                adapter.notifyDataSetChanged();

                if (quiz.getAnsweredCount() == quiz.questions.size()) {
                    finishQuiz();
                }

            } else if (resultCode == RESULT_CANCELED) {
                finishQuiz();
            }
        }
    }

    private void finishQuiz() {
        countDownTimer.clearTimer();
        Toast.makeText(this, "Quiz has finished!", Toast.LENGTH_SHORT).show();
    }

    public void updateTimerTitle(int time) {
        setTitle(String.valueOf(time));

        if (time == 0) {
            finishQuiz();
        }
    }

    private void updateFabImage() {
        binding.fab.setImageResource(countDownTimer.getStatus() == CountDownTimer.Status.STARTED
                ? android.R.drawable.ic_media_pause
                : android.R.drawable.ic_media_play);
    }
}
