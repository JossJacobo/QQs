package joss.jacobo.quizapptestproject.dagger;

import javax.inject.Singleton;

import dagger.Component;
import joss.jacobo.quizapptestproject.Quiz.MainActivity;
import joss.jacobo.quizapptestproject.MainApp;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainApp mainApp);

    void inject(MainActivity activity);
}
