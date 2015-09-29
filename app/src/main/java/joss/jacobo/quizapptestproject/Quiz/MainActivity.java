package joss.jacobo.quizapptestproject.Quiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import joss.jacobo.quizapptestproject.R;
import joss.jacobo.quizapptestproject.dagger.Dagger;
import joss.jacobo.quizapptestproject.models.Question;
import joss.jacobo.quizapptestproject.models.Quiz;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Quiz> {

    public static final String TAG = MainActivity.class.getSimpleName();
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
            for (Question question : quiz.questions) {
                items.add(question);
            }

            ((QuizAdapter) recyclerView.getAdapter()).setItems(items);
        }
    }

    @Override
    public void onLoaderReset(Loader<Quiz> loader) {

    }
}
