package com.example.soundsafetyjava;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    Button btnToggle;
    String currentDateTimeString;
    int valueRead;
    AudioManager myAudioManager;

    int thresholdValueMedia = 6; // Arbitrary max value (determined by user test)

    // For managing threads
    boolean running = false;    // THIS WILL HAVE TO BE CHECKED --> IF APP GETS RESTARTED WHILE SERVICE IS STILL RUNNING --> TODODOODDOODDODOO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init UI
        tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText("Thread STOPPED");
        btnToggle = (Button) findViewById(R.id.btnToggle);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        valueRead = myAudioManager.getStreamVolume(2);
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        // Toggle button action --> Service Control
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                // If thread is running
                if (running) {
                    // Toggle logic --> Stop thread
                    tv1.setText("Service STOPPED");



                    //Pause/Kill Thread

                    Log.d("Service","Service is STOPPED: " + currentDateTimeString );
                    running = false;

                }

                // If thread is stopped
                else {
                    // Toggle logic --> Start/Continue thread
                    Intent serviceIntent = new Intent(v.getContext(), MyService.class);
                    serviceIntent.putExtra("intParamArray",new int[]{1, 2, 3});     ///////// REPLACE 1,2,3 with array!!!
                    startService(serviceIntent);

                    // Service should do most the heavy lifting

                    Log.d("Service","Service is RUNNING: " + currentDateTimeString );
                    running = true;
                }

            }
        });
    }

}

      /*

    Below is for setting color to make it nicer too :)
    TextView mTextView = new
    mTextView.setTextColor(Color.parseColor("#bdbdbd"));

    At all times:
    if volume exceeds XXX, then lower it
    if port is switched from headphone jack to speakers, set volume to middle
    if port is switched from speakers to headphone jack, set volume to half of maximum allowed limit
    if limitation is enabled (via button), display text to be " LIMIT ON", and vice versa

    KEYS:
    AudioManager for all HW audio monitoring and changing

    If raise volume detected, check volume, if exceeds threshold, decrease volume to threshold
    */


