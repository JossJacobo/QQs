package joss.jacobo.quizapptestproject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;

import joss.jacobo.quizapptestproject.models.Quiz;
import oak.util.OakAsyncLoader;

public class QuizLoader extends OakAsyncLoader<Quiz> {

    public static final String TAG = QuizLoader.class.getSimpleName();
    
    private OkHttpClient client;
    private Gson gson;
    private String url;

    public QuizLoader(Context context, OkHttpClient client, Gson gson, String url) {
        super(context);
        this.client = client;
        this.gson = gson;
        this.url = url;
    }

    @Override
    public Quiz loadInBackground() {
        Quiz quiz = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Reader stream = response.body().charStream();
                try {
                    quiz = gson.fromJson(stream, Quiz.class);
                } catch (Exception e) {
                    Log.e(TAG, e.toString(), e);
                } finally {
                    stream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return quiz;
    }
}
