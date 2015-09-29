package joss.jacobo.quizapptestproject.dagger;

import android.content.Context;

public class Dagger {

    private static MainComponent sMainComponent;

    public static MainComponent mainComponent(Context context) {
        if (sMainComponent == null) {
            sMainComponent = DaggerMainComponent.builder()
                    .mainModule(new MainModule(context))
                    .build();
        }
        return sMainComponent;
    }
}
