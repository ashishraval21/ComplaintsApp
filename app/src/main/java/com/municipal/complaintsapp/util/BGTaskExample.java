package com.municipal.complaintsapp.util;

public class BGTaskExample {

    public  BGTaskExample(){
        BackgroundTask<String> task = new BackgroundTask<>(new BackgroundTask.BackgroundTaskExecutor<String>() {
            @Override
            public String run() throws Exception {
                // Perform background task
                return "result";
            }
        }, new BackgroundTask.BackgroundTaskListener<String>() {
            @Override
            public void onSuccess(String result) {
                // Handle successful result
            }

            @Override
            public void onFailure(Exception e) {
                // Handle exception
            }
        });

        task.execute();
    }

}
