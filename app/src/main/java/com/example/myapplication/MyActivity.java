package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyActivity extends Activity
{
    ArrayList<String> enWords = new ArrayList<>();
    ArrayList<String> ruWords = new ArrayList<>();
    ArrayList<Integer> selectedWords = new ArrayList<>();
    ListView textList;
    ArrayAdapter<String> textAdapter;
    Button buttonAdd;
    Button buttonDelete;
    Button buttonTranslate;
    String selectedLanguage ="ru";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        buttonAdd                   = findViewById(R.id.add_button);
        buttonDelete                = findViewById(R.id.delete_button);
        buttonTranslate             = findViewById(R.id.translate_button);
        textList                    = findViewById(R.id.list1);
        ArrayList<String> buffer    = new ArrayList<>();
        textAdapter                 = new ArrayAdapter<>
                (
                        this,
                        android.R.layout.simple_list_item_multiple_choice,
                        buffer
                );

        textList.setAdapter(textAdapter);

        textList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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
        if (selectedLanguage.equals("ru"))
        {
            textAdapter.clear();
            textAdapter.addAll(ruWords);
        }
        else
        {
            textAdapter.clear();
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
}
