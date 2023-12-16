package com.example.fourthlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.Locale;

public class AuthorizationActivity extends Activity
{
    private final UserData[] users = new UserData[]
            {
                    new UserData("sasha", "123456"),
                    new UserData("admin", "admin")
            };

    String lang = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LoadLanguage();

        Log.i("AuthorizationActivity","Authorization Activity is created");
        setContentView(R.layout.authorization_layout);

        LoadData();

        Button buttonAuthorize    = findViewById(R.id.confirm_button);
        Button buttonChangeLocale = findViewById(R.id.change_locale);

        buttonAuthorize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckUserData();
            }
        });

        buttonChangeLocale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeLocale();
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.i("AuthorizationActivity","Authorization Activity is started");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("AuthorizationActivity","Authorization Activity is stopped");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i("AuthorizationActivity","Authorization Activity is resumed");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("AuthorizationActivity","Authorization Activity is destroyed");

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i("AuthorizationActivity","Authorization Activity is paused");

        SavePreferencesData();
        SaveSharedPreferencesData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        SaveInstanceLanguage(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState)
    {
        super.onRestoreInstanceState(outState);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        EditText passwordEdit = findViewById(R.id.password_edit_text);
        EditText loginEdit    = findViewById(R.id.login_edit_text);

        String password = passwordEdit.getText().toString();
        String login    = loginEdit.getText().toString();

        editor.putString("password", password);
        editor.putString("login", login);
        editor.apply();

        LoadInstanceLanguage(outState);
    }
    private void LoadInstanceLanguage(Bundle outState)
    {
        String language = outState.getString("lang");
        String currentLanguage = Locale.getDefault().getLanguage();

        if((language.isEmpty())||(language.equals(currentLanguage)))
            return;

        lang = language;

        SetLocale(language);

        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
    }

    private void SaveInstanceLanguage(Bundle outState)
    {
        outState.putString("lang", lang);
    }

    private void CheckUserData()
    {
        EditText passwordEdit = findViewById(R.id.password_edit_text);
        EditText loginEdit = findViewById(R.id.login_edit_text);

        String password = passwordEdit.getText().toString();
        String login = loginEdit.getText().toString();

        if(login.isEmpty())
        {
            Toast.makeText
                    (
                            AuthorizationActivity.this,
                            R.string.enter_login,
                            Toast.LENGTH_SHORT
                    ).show();
            return;
        }
        else if (password.isEmpty())
        {
            Toast.makeText
                    (
                            AuthorizationActivity.this,
                            R.string.enter_password,
                            Toast.LENGTH_SHORT
                    ).show();
            return;
        }

        for (UserData user : users)
        {
            if (user.GetLogin().equals(login))
            {
                if (user.GetPassword().equals(password))
                {
                    ChangeActivity();
                }
                else
                {
                    Toast.makeText
                            (
                                    AuthorizationActivity.this,
                                    R.string.wrong_password,
                                    Toast.LENGTH_SHORT
                            ).show();
                }
                return;
            }
        }

        Toast.makeText
                (
                        AuthorizationActivity.this,
                        R.string.profile_not_exist,
                        Toast.LENGTH_SHORT
                ).show();
    }

    @SuppressLint("UnsafeIntentLaunch")
    private void ChangeLocale()
    {
        String currentLanguage = Locale.getDefault().getLanguage();
        String language = null;

        if(currentLanguage.equals("ru"))
        {
            language = "en";
        }
        else if(currentLanguage.equals("en"))
        {
            language = "ru";
        }

        SetLocale(language);

        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
    }

    private void ChangeActivity()
    {
        // Saving.
        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentLanguage = Locale.getDefault().getLanguage();

        editor.putString("language",currentLanguage);
        editor.apply();

        EditText passwordEdit = findViewById(R.id.password_edit_text);
        EditText loginEdit    = findViewById(R.id.login_edit_text);

        passwordEdit.setText("");
        loginEdit.setText("");

        // Loading second activity.
        Intent myActivity = new Intent(this, ListActivity.class);

        myActivity.putExtra("data", getString(R.string.intent));

        this.startActivity(myActivity);
    }

    private void SavePreferencesData()
    {
        String currentLanguage = Locale.getDefault().getLanguage();

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        EditText passwordEdit = findViewById(R.id.password_edit_text);
        EditText loginEdit    = findViewById(R.id.login_edit_text);

        String password = passwordEdit.getText().toString();
        String login    = loginEdit.getText().toString();

        editor.putString("password", password);
        editor.putString("login", login);
        editor.putString("lang", currentLanguage);
        editor.apply();

        Log.d("AuthorizationActivity", "Preferences is saved");
    }

    private void SaveSharedPreferencesData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentLanguage = Locale.getDefault().getLanguage();

        editor.putString("language",currentLanguage);
        editor.apply();

        Log.d("AuthorizationActivity", "Shared Preferences is saved");
    }
    private void LoadData()
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        EditText passwordEdit = findViewById(R.id.password_edit_text);
        EditText loginEdit    = findViewById(R.id.login_edit_text);

        String password = sharedPreferences.getString("password","");

        String login    = sharedPreferences.getString("login","");

        if(!password.isEmpty())
            passwordEdit.setText(password);

        if(!login.isEmpty())
            loginEdit.setText(login);

        editor.clear();
        editor.apply();
    }

    private void LoadLanguage()
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        String language        = sharedPreferences.getString("lang", "");
        String currentLanguage = Locale.getDefault().getLanguage();

        lang = currentLanguage;

        if ((!language.isEmpty())&&(!language.equals(currentLanguage)))
        {
            lang = language;
            SetLocale(language);
        }

        editor.remove("lang");
        editor.apply();

        LoadSharedPreferencesLanguage();
    }

    private void LoadSharedPreferencesLanguage()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("Lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String currentLanguage = sharedPreferences.getString("language","");
        if (!currentLanguage.isEmpty())
        {
            SetLocale(currentLanguage);
            lang = currentLanguage;
        }

        editor.remove("language");
        editor.apply();
    }

    private void SetLocale(String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
