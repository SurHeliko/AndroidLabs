package com.example.firstlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends Activity
{
    int buttonCount = 2;
    Button[] button = new Button[buttonCount];
    Button buttonReset;
    TextView textView;
    int[] buttonPressed = {0, 0};
    Button buttonIncBright;
    Button buttonDecBright;
    int brightness;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        button[0]   = findViewById(R.id.button1);
        button[1]   = findViewById(R.id.button2);
        buttonReset = findViewById(R.id.reset_button);
        textView    = findViewById(R.id.text_view);
        buttonIncBright = findViewById(R.id.increace_brightning);

        button[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonPressed(1);
            }
        });

        button[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonPressed(2);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ResetAll(v);
            }
        });
    }

    private void timerTick()
    {
        this.runOnUiThread(doTask);
    }

    private Runnable doTask = new Runnable() {
        @Override
        public void run()
        {
            buttonIncBright= findViewById(R.id.increace_brightning);
            ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) buttonIncBright.getLayoutParams();
            ConstraintLayout clpLayout = findViewById(R.id.main_layout);
            //ConstraintLayout.LayoutParams clpLayout = (ConstraintLayout.LayoutParams) clpLayout.getLayoutParams();

            if(clp.leftMargin>205)
            {
                clp.leftMargin=0;
            }
            else
            {
                clp.leftMargin+=5;
            }

            buttonIncBright.setLayoutParams(clp);
        }
    };

    private void ChangeBrightness(short mode)
    {
        Context context = getApplicationContext();
        //WindowManager.LayoutParams wlp = getWindow().getAttributes();

        Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

        try
        {
            brightness = Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            Log.e("IncreaseBrightning", e.toString());
        }

        if(mode==0)
            brightness= brightness+=15;
        else if (mode==1)
            brightness= brightness-=15;

        if (brightness>=255)
            brightness = 255;
        else if (brightness<=0)
            brightness = 0;

        Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, brightness);


        /*if ((brightness/255)>=1)
            wlp.screenBrightness = 1F;
        else if ((brightness/255)<=0)
            wlp.screenBrightness = 0F;
        else
            wlp.screenBrightness = brightness/(float)255;

        getWindow().setAttributes(wlp);*/
    }

    private void ResetAll(View view)
    {
        for(int i = 0; i < buttonCount; i++)
        {
            button[i].setBackgroundColor(Color.LTGRAY);
            button[i].setText("Кнопка "+ (i + 1) + " (не нажата)");
            buttonPressed[i]=0;
        }

        textView.setText("Hello world!");

        CreateNewButton(view);
    }

    private void CreateNewButton(View view)
    {
        Button                        buttonIncBrightness  = new Button(this);
        Button                        buttonDecBrightness  = new Button(this);
        ConstraintLayout              layout   = (ConstraintLayout) findViewById(R.id.main_layout);
        ConstraintLayout.LayoutParams clpReset = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        
        ConstraintLayout.LayoutParams clp      = new ConstraintLayout.LayoutParams
                (
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );

        clp.width=clpReset.width;
        clp.height=clpReset.height+30;
        clp.leftMargin=0;
        clp.topMargin=clpReset.topMargin-170;
        clp.leftToLeft=clpReset.leftToLeft;
        clp.rightToRight=clpReset.rightToRight;
        clp.topToTop=clpReset.topToTop;
        clp.startToStart=clpReset.startToStart;


        buttonIncBrightness.setText("increase brightning");
        buttonIncBrightness.setId(R.id.increace_brightning);
        buttonIncBrightness.setLayoutParams(clp);

        //clp.leftMargin=;

        buttonDecBrightness.setText("decreace brightning");
        buttonDecBrightness.setId(R.id.decreace_brightning);
        buttonDecBrightness.setLayoutParams(clp);

        buttonIncBrightness.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeBrightness((short) 0);
            }
        });

        layout.addView(buttonIncBrightness);

        Timer myTimer = new Timer();

        myTimer.schedule(new TimerTask()
        {
            public void run()
            {
                timerTick();
            }
        },0,50);
    }

    private void ButtonPressed(int buttonNumber)
    {
        if((buttonPressed[buttonNumber-1]%2)==1)
        {
            button[buttonNumber-1].setText("Кнопка " + buttonNumber + " (не нажата)");
            button[buttonNumber-1].setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            button[buttonNumber-1].setText("Кнопка "+ buttonNumber + " (нажата)");
            button[buttonNumber-1].setBackgroundColor(Color.CYAN);
        }

        buttonPressed[buttonNumber-1]++;
        textView.setText("Кнопка 1 нажата: "+ buttonPressed[0]+" раз\n"+"Кнопка 2 нажата: "+ buttonPressed[1]+" раз");
    }
}
