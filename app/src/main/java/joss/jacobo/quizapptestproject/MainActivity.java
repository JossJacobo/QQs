package joss.jacobo.quizapptestproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import joss.jacobo.quizapptestproject.dagger.Dagger;
import joss.jacobo.quizapptestproject.models.Question;
import joss.jacobo.quizapptestproject.models.Quiz;
import joss.jacobo.quizapptestproject.question.QuestionActivity;
import joss.jacobo.quizapptestproject.quiz.QuizAdapter;
import joss.jacobo.quizapptestproject.quiz.QuizItem;
import joss.jacobo.quizapptestproject.quiz.QuizListener;
import joss.jacobo.quizapptestproject.quiz.QuizLoader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Quiz>, QuizListener {

    private static final int REQUEST_CODE_QUESTION = 1;
    private static final String QUIZ_URL
            = "https://gist.githubusercontent.com/JossJacobo/a01137c7f80506798041/raw/c45ab5ef43292b836e52f6c45983a41350da290b/qq.json";

    @Inject
    OkHttpClient client;
    @Inject
    Gson gson;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dagger.mainComponent(this).inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuizAdapter());

        getSupportLoaderManager().initLoader(0, null, this);
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
        if (!question.answered) {
            startActivityForResult(QuestionActivity.newIntent(this, question), REQUEST_CODE_QUESTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUESTION) {
            Question q = data.getParcelableExtra(QuestionActivity.EXTRA_QUESTION);
            QuizAdapter adapter = (QuizAdapter) recyclerView.getAdapter();
            Quiz quiz = (Quiz) adapter.items.get(0);

            quiz.questions.set(quiz.questions.indexOf(q), q);
            adapter.items.set(adapter.items.indexOf(q), q);
            adapter.notifyDataSetChanged();
        }
    }
}
