package joss.jacobo.quizapptestproject.dagger;

import javax.inject.Singleton;

import dagger.Component;
import joss.jacobo.quizapptestproject.MainActivity;
import joss.jacobo.quizapptestproject.MainApp;
import joss.jacobo.quizapptestproject.question.QuestionActivity;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainApp mainApp);

    void inject(MainActivity activity);

    void inject(QuestionActivity activity);
}
