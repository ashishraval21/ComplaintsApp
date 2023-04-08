package com.municipal.complaintsapp.util;

import android.os.AsyncTask;

public class BackgroundTask<R> extends AsyncTask<Void, Void, R> {

    private final BackgroundTaskListener<R> listener;
    private final BackgroundTaskExecutor<R> executor;
    private Exception exception;

    public BackgroundTask(BackgroundTaskExecutor<R> executor, BackgroundTaskListener<R> listener) {
        this.executor = executor;
        this.listener = listener;
    }

    @Override
    protected R doInBackground(Void... voids) {
        try {
            return executor.run();
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (exception != null) {
            listener.onFailure(exception);
        } else {
            listener.onSuccess(result);
        }
    }

    public interface BackgroundTaskExecutor<R> {
        R run() throws Exception;
    }

    public interface BackgroundTaskListener<R> {
        void onSuccess(R result);
        void onFailure(Exception e);
    }
}
