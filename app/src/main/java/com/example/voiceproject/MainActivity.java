package com.example.voiceproject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    private final String[] REQUIRED_PERMISSIONS = new String[]
            {
                    "android.permission.RECORD_AUDIO",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
            };

    private String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss";


    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String lastFile;
    private AudioManager man;
    private  int res;
    private float k = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button permissionCheck = findViewById(R.id.permissionCheck);
        permissionCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (hasPermission())
                {
                    requestPermission();
                }
            }
        });
        Button bPlus = findViewById(R.id.playPlus);
        bPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playPlus();
            }
        });

        Button bMinus = findViewById(R.id.playMinus);
        bMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playMinus();
            }
        });
        File file = new File(this.getExternalMediaDirs()[0], "Media");
        if(!file.mkdirs())
        {
            Log.e("Main", "Dir not created");
        }
        else
        {
            Log.d("Main", "Dir created");
        }
    }

    public void recordStart(View v) {
        try {
            releaseRecorder();

            File voiceFile =  new File(getApplicationContext().getExternalMediaDirs()[0],
                    "Media/"+new SimpleDateFormat(FILENAME_FORMAT, Locale.ROOT).format(System.currentTimeMillis()) + ".3gpp");

            lastFile = voiceFile.getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(voiceFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {

            Log.e("Main", e.toString());
        }

    }

    public void recordStop(View v) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
        }
    }

    public void playStart(View v) {
        try {
            releasePlayer();
            man = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            res = man.getStreamVolume(AudioManager.STREAM_MUSIC);
            int xch = man.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int r = (int)(xch*k);
            man.setStreamVolume(AudioManager.STREAM_MUSIC,r,0);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(lastFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e("Main", e.toString());
        }
    }

    public void playStop(View v) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        releaseRecorder();
    }

    private boolean hasPermission()
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions
                (
                        this,
                        REQUIRED_PERMISSIONS,
                        200
                );
    }
    private void playPlus()
    {
        if (k < 1.0f)
            k += 0.1f;
    }

    private void playMinus()
    {
        if (k > 0f)
            k -= 0.1f;
    }
}