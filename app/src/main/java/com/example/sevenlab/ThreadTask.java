package com.example.sevenlab;

import android.os.Build;
import android.os.Message;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import android.os.Handler;

public class ThreadTask
{
    Handler thr_handler;
    final Message message = Message.obtain();
    boolean answer;
    DatabaseHandler databaseHandler_;

    ThreadTask(Handler main_handler, DatabaseHandler databaseHandler)
    {
        this.thr_handler = main_handler;
        databaseHandler_ = databaseHandler;
    }

    public void SignIn(String login, String password)
    {
        new Thread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run()
            {
                answer = databaseHandler_.IsLoginAvailable(login);

                if(answer)
                {
                    message.sendingUid = 2;
                    thr_handler.sendMessage(message);

                    return;
                }

                answer = databaseHandler_.SignIn(login, password);
                if(answer)
                    message.sendingUid = 7;
                else
                    message.sendingUid = 8;

                thr_handler.sendMessage(message);
            }
        }).start();
    }

    public void SignUp(UserData user)
    {
        new Thread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run()
            {
                answer = databaseHandler_.IsLoginAvailable(user.GetLogin());

                if(!answer)
                {
                    message.sendingUid = 5;
                    thr_handler.sendMessage(message);

                    return;
                }

                databaseHandler_.addUser(user);

                message.sendingUid = 6;

                thr_handler.sendMessage(message);
            }
        }).start();
    }
    public void DeleteUser(String login, String password)
    {
        new Thread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run()
            {
                answer = databaseHandler_.IsLoginAvailable(login);

                if(answer)
                {
                    message.sendingUid = 4;
                    thr_handler.sendMessage(message);

                    return;
                }

                answer = databaseHandler_.DeleteUser(login, password);

                if (answer)
                    message.sendingUid = 1;
                else
                    message.sendingUid = 2;

                thr_handler.sendMessage(message);
            }
        }).start();
    }

    public void UpdatePassword(String login, String newPassword)
    {
        new Thread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run()
            {
                answer = databaseHandler_.IsLoginAvailable(login);

                if(answer)
                {
                    message.sendingUid = 4;
                    thr_handler.sendMessage(message);

                    return;
                }

                databaseHandler_.UpdateUserData(login, newPassword);

                message.sendingUid = 3;

                thr_handler.sendMessage(message);
            }
        }).start();
    }
}
