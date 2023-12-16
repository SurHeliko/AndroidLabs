package com.example.fourthlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class ListActivity extends Activity
{
    ArrayList<String> enWords = new ArrayList<>();
    ArrayList<String> ruWords = new ArrayList<>();
    ArrayList<Integer> selectedWords = new ArrayList<>();
    ListView textList;
    ArrayAdapter<String> textAdapter;
    Button buttonAdd;
    Button buttonDelete;
    Button buttonTranslate;
    Button buttonChangeLocale;
    String selectedLanguage ="ru";
    String lang = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DataFormAuth();

        LoadLocale();
        Log.i("ListActivity","List Activity is created");

        setContentView(R.layout.list_layout);

        buttonAdd                   = findViewById(R.id.add_button);
        buttonDelete                = findViewById(R.id.delete_button);
        buttonTranslate             = findViewById(R.id.translate_button);
        buttonChangeLocale          = findViewById(R.id.change_locale);
        textList                    = findViewById(R.id.list1);
        ArrayList<String> buffer    = new ArrayList<>();
        textAdapter                 = new ArrayAdapter<>
                (
                        this,
                        android.R.layout.simple_list_item_multiple_choice,
                        buffer
                );

        textList.setAdapter(textAdapter);

        LoadData();

        textList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if(textList.isItemChecked(position))
                {
                    selectedWords.add(position);
                }
                else {
                    selectedWords.remove((Integer)position);
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddText();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DeleteText();
            }
        });

        buttonTranslate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TranslateText();
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
        Log.i("ListActivity","List Activity is started");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("ListActivity","List Activity is stopped");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i("ListActivity","List Activity is resumed");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("ListActivity","List Activity is destroyed");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i("ListActivity","List Activity is paused");

        SaveSharedPreferencesData();
        SaveLanguage();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        SaveInstanceLanguage(outState);
    }

    protected void onRestoreInstanceState(Bundle outState)
    {
        super.onRestoreInstanceState(outState);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        editor.putString("list_lang", selectedLanguage);
        SaveList(editor);

        LoadInstanceLanguage(outState);
    }

    private void SaveInstanceLanguage(Bundle outState)
    {
        outState.putString("lang", lang);
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

    private void AddText()
    {
        EditText ruEditText = findViewById(R.id.ru_edit_text);
        EditText enEditText = findViewById(R.id.en_edit_text);

        ruWords.add(ruEditText.getText().toString());
        enWords.add(enEditText.getText().toString());

        UpdateAdapterData();

        textAdapter.notifyDataSetChanged();
    }

    private void UpdateAdapterData()
    {
        if(textAdapter!=null)
            textAdapter.clear();

        if (selectedLanguage.equals("ru"))
        {
            textAdapter.addAll(ruWords);
        }
        else
        {
            textAdapter.addAll(enWords);
        }
    }

    private void DeleteText()
    {
        Collections.sort(selectedWords);

        for(int i=selectedWords.size()-1; i>=0 ;i--)
        {
            ruWords.remove((int)selectedWords.get(i));
            enWords.remove((int)selectedWords.get(i));
        }

        UpdateAdapterData();
        textList.clearChoices();
        selectedWords.clear();

        textAdapter.notifyDataSetChanged();
    }

    private void TranslateText()
    {
        if ( selectedLanguage.equals("ru"))
        {
            selectedLanguage="en";
            textAdapter.clear();
            textAdapter.addAll(enWords);
        }
        else
        {
            selectedLanguage="ru";
            textAdapter.clear();
            textAdapter.addAll(ruWords);
        }
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

    private void SaveSharedPreferencesData()
    {
        String currentLanguage = Locale.getDefault().getLanguage();

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        editor.putString("list_lang", selectedLanguage);
        editor.putString("lang", currentLanguage);
        editor.apply();

        SaveList(editor);

        Log.d("ListActivity", "Shared Preferences is saved");
    }

    private void SaveList(SharedPreferences.Editor editor)
    {
        String enArray = "";
        String ruArray = "";

        for(int i = 0 ; i < enWords.size(); i++)
        {
            enArray+=enWords.get(i)+",";
            ruArray+=ruWords.get(i)+",";
        }

        editor.putString("enWords", enArray);
        editor.putString("ruWords", ruArray);
        editor.apply();
    }

    private void SaveLanguage()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("Lang", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentLanguage = Locale.getDefault().getLanguage();

        String loadedLanguage = sharedPreferences.getString("language","");

        if ((loadedLanguage.isEmpty())||(!loadedLanguage.equals(currentLanguage)))
        {
            editor.remove("language");
            editor.putString("language", currentLanguage);
            editor.apply();
        }
    }

    private void DataFormAuth()
    {
        Bundle arguments = getIntent().getExtras();
        if (arguments==null)
        {
            return;
        }

        String authorizationData = arguments.get("data").toString();

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String currentLanguage = sharedPreferences.getString("language","");

        editor.remove("language");
        editor.apply();

        if (currentLanguage.isEmpty())
            currentLanguage= "en";

        SetLocale(currentLanguage);

        Toast.makeText
                (
                        ListActivity.this,
                        authorizationData,
                        Toast.LENGTH_LONG
                ).show();

        Log.i("Data", authorizationData);

        SharedPreferences preferences =this.getPreferences(MODE_PRIVATE);

        editor = preferences.edit();
        editor.remove("lang");
        editor.apply();

        getIntent().removeExtra("data");
    }

    private void LoadLocale()
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        String language        = sharedPreferences.getString("lang", "");
        String currentLanguage = Locale.getDefault().getLanguage();

        lang = currentLanguage;

        if ((!language.isEmpty())&&(!language.equals(currentLanguage)))
        {
            editor.remove("lang");
            editor.apply();

            lang = language;

            SetLocale(language);
        }
    }

    private void LoadList(SharedPreferences sharedPreferences, SharedPreferences.Editor editor)
    {
        String en = sharedPreferences.getString("enWords", "");
        String ru = sharedPreferences.getString("ruWords", "");
        String[] enArray = en.split(",");
        String[] ruArray = ru.split(",");

        if((!en.equals(""))&&(!ru.equals("")))
        {
            enWords = new ArrayList<String>(Arrays.asList(enArray));
            ruWords = new ArrayList<String>(Arrays.asList(ruArray));
        }

        editor.remove("enWords");
        editor.remove("ruWords");
        editor.apply();

        UpdateAdapterData();
    }

    private void LoadData()
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();

        selectedLanguage = sharedPreferences.getString("list_lang","");
        LoadList(sharedPreferences, editor);

        editor.clear();
        editor.apply();
    }
}
