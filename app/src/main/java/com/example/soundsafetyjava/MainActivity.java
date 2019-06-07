package com.example.soundsafetyjava;

import android.content.Context;
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
    boolean running = false;


    // Class for thread
    class CustomThreadBoy extends Thread {

        //String time;
        //int volume;
        //Constructor
        //CustomThreadBoy(int volume, String time) {
            //this.volume = volume;
            //this.time = time;
        //}

        public void run() {
            while (running) {
                // 2 = Ringer,   3 = Media,  4 = Alarm
                valueRead = myAudioManager.getStreamVolume(3);
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Log.d("ThreadRun", "Thread is running noicely: " + currentDateTimeString);
                Log.d("ThreadRun", "Volume level: " + valueRead);

                // Limits max volume
                if (myAudioManager.getStreamVolume(3) > thresholdValueMedia){
                    myAudioManager.setStreamVolume(3, thresholdValueMedia,1);
                }

                if (!running) {
                    Thread.currentThread().interrupt();
                    Log.d("ThreadRun", "THREAD IS INTERRUPTED. KILLING IT.");
                    return;
                }
                // Sleep thread
                try {
                    // thread to sleep for 1000 milliseconds
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    CustomThreadBoy customThreadBoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init UI
        tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText("Thread STOPPED");
        btnToggle = (Button) findViewById(R.id.btnToggle);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /////////////////////// ALL LOGIC GOES IN HERE /////////////////////////

        // Stream1: Ringer
        valueRead = myAudioManager.getStreamVolume(2);
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        // Init Thread
        //customThreadBoy = new CustomThreadBoy(valueRead, currentDateTimeString);
        customThreadBoy = new CustomThreadBoy();

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                // If thread is running
                if (running) {
                    // Toggle logic --> Stop thread
                    tv1.setText("Thread STOPPED");
                    running = false;
                    Log.d("Thread","Thread is STOPPED: " + currentDateTimeString );

                    //Pause/Kill Thread
                    customThreadBoy.interrupt();
                    //Log.d("Thread"," Is interrupted:" + Boolean.toString(customThreadBoy.isInterrupted()) );
                    //Log.d("Thread"," Is Alive:" + Boolean.toString(customThreadBoy.isAlive()) );
                }

                // If thread is stopped
                else {
                    // Toggle logic --> Start/Continue thread
                    tv1.setText("Thread Running");
                    running = true;
                    Log.d("Thread","Thread is running noicely: " + currentDateTimeString );

                    //Start/ Resume thread
                    customThreadBoy.start();
                }

            }
        });
    }

}



      /*

    Below is for setting color to make it nicer too :)
    TextView mTextView = new
    mTextView.setTextColor(Color.parseColor("#bdbdbd"));

    Verify if audio is actually getting limitied with text display
    Audio limit toggled by button

    At all times:
    if volume exceeds XXX, then lower it
    if port is switched from headphone jack to speakers, set volume to middle
    if port is switched from speakers to headphone jack, set volume to half of maximum allowed limit
    if limitation is enabled (via button), display text to be " LIMIT ON", and vice versa

    KEYS:
    AudioManager for all HW audio monitoring and changing

    If raise volume detected, check volume, if exceeds threshold, decrease volume to threshold

  private final volatile running = true;
    ...
    new Thread(new Runnable() {
        public void run() {
            while (running) {
                // do something in the loop
            }
        }
    }).start();
    ...

   later, in another thread, you can shut it down by setting running to false
    running = false;*/


