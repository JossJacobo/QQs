package joss.jacobo.quizapptestproject.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CountDownTimer {

    private static final String ACTION = "joss.jacob.quizapptestproject.timer.CountDownTime_TIME/";
    public static final String EXTRA_TIME = "time";
    private static final int DEFAULT = 60; // seconds
    private static final int ONE_SECOND = 1000;

    private Status status = Status.STOPPED;
    private int currentTime = 0;
    private Handler handler = new Handler();
    private LocalBroadcastManager localBroadcastManager;

    @Inject
    public CountDownTimer(Context context) {
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public Status getStatus() {
        return status;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void toggle() {
        if (status == Status.STARTED) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer(){
        if (status == Status.STOPPED) {
            status = Status.STARTED;
            if (currentTime == 0) {
                currentTime = DEFAULT;
            }

            broadcastCurrentTime();
            decrementCounter();
        }
    }

    public void stopTimer() {
        if (status == Status.STARTED) {
            status = Status.STOPPED;
        }
        handler.removeCallbacksAndMessages(null);
    }

    public void clearTimer() {
        stopTimer();
        currentTime = 0;
    }

    public void register(BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter(ACTION);
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    public void unregister(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void broadcastCurrentTime() {
        localBroadcastManager.sendBroadcast(new Intent(ACTION)
                .putExtra(EXTRA_TIME, currentTime));
    }

    private void decrementCounter() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTime--;
                broadcastCurrentTime();

                if (currentTime > 0) {
                    decrementCounter();
                } else {
                    status = Status.STOPPED;
                }
            }
        }, ONE_SECOND);
    }

    public enum Status{
        STOPPED, STARTED
    }
}
