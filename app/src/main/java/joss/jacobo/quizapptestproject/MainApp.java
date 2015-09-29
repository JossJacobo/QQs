package joss.jacobo.quizapptestproject;

import android.app.Application;

import joss.jacobo.quizapptestproject.dagger.Dagger;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dagger.mainComponent(this).inject(this);
    }

}
