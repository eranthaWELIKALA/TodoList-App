package com.test.myapplication;

import android.os.Handler;
import android.os.Message;
import android.text.PrecomputedText;

public abstract class AsyncTaskExecutor<Result> {

    private Handler externalHandler;

    public abstract void onPreExecute();

    public abstract Result doInBackground();

    public abstract void onPostExecute(Result result);

    // External handler is used to get the result of the query
    public AsyncTaskExecutor<Result> setExternalHandler(Handler handler) {
        this.externalHandler = handler;
        return this;
    }

    public Handler getExternalHandler() {
        return this.externalHandler;
    }

    private void sendResultToExternalHandler(Result result) {
        if (externalHandler != null) {
            Message message = Message.obtain();
            message.obj = result;
            externalHandler.sendMessage(message);
        }
    }

    // Handler is used to run the post execution
    public void execute(PrecomputedText.Params... params) {
        onPreExecute();
        Runnable runnable = () -> {
            final Result result = doInBackground();
            onPostExecute(result);
            sendResultToExternalHandler(result);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
