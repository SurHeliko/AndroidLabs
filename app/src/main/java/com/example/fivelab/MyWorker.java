package com.example.fivelab;

import android.content.Context;

import androidx.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    DatabaseHandler databaseHandler_;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters, DatabaseHandler databaseHandler) {
        super(context, workerParameters);

        databaseHandler_=databaseHandler;
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Data inputData = getInputData();
        String login = inputData.getString("key");

        return Result.success(inputData);
    }
}
