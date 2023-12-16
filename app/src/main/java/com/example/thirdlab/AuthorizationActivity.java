package com.example.thirdlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilderFactory;

public class AuthorizationActivity extends Activity
{
    private UserData[] users = new UserData[]
            {
                    new UserData("sasha", "123456"),
                    new UserData("admin", "admin")
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i("AuthorizationActivity","Second Activity is created");
        setContentView(R.layout.authorization);

        Button buttonAuthorize = findViewById(R.id.confirm_button);

        buttonAuthorize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckUserData();
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Log.i("AuthorizationActivity","Second Activity is started");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("AuthorizationActivity","Second Activity is stopped");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i("AuthorizationActivity","Second Activity is resumed");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("AuthorizationActivity","Second Activity is destroyed");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i("AuthorizationActivity","Second Activity is paused");
    }

    private void CheckUserData()
    {
        EditText passwordEdit = findViewById(R.id.password_edit_text);
        EditText loginEdit = findViewById(R.id.login_edit_text);

        String password = new String(passwordEdit.getText().toString());
        String login = new String(loginEdit.getText().toString());

        if(login.isEmpty())
        {
            Toast.makeText
                    (
                            AuthorizationActivity.this,
                            "Введите логин!",
                            Toast.LENGTH_SHORT
                    ).show();
            return;
        }
        else if (password.isEmpty())
        {
            Toast.makeText
                    (
                            AuthorizationActivity.this,
                            "Введите пароль!",
                            Toast.LENGTH_SHORT
                    ).show();
            return;
        }

        for(int i = 0;i< users.length;i++)
        {
            if (users[i].GetLogin().equals(login))
            {
                if(users[i].GetPassword().equals(password))
                {
                    ChangeActivity();
                }
                else
                {
                    Toast.makeText
                            (
                                    AuthorizationActivity.this,
                                    "Пароль не верный!",
                                    Toast.LENGTH_SHORT
                            ).show();
                }
                return;
            }
        }

        Toast.makeText
                (
                        AuthorizationActivity.this,
                        "Профиля с таким именем не существует!",
                        Toast.LENGTH_SHORT
                ).show();
    }

    private void ChangeActivity()
    {
        Intent myActivity = new Intent(this, MyActivity.class);

        myActivity.putExtra("data", "Hello from authorization!");

        this.startActivity(myActivity);
    }
}
