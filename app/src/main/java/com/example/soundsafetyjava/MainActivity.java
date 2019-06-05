package com.example.soundsafetyjava;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    AudioManager myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    // Init UI
    TextView tv1 = (TextView)findViewById(R.id.textView1);
    Button btnStop = (Button)findViewById(R.id.btnStop);
    Button btnStart = (Button)findViewById(R.id.btnStart);

    // For managing threads
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // stops thread
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                tv1.setText("STOPPED");
            }
        });

        // Allows thread to run
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
            }
        });


        // Thread goes here
        new Thread(new Runnable() {
            public void run() {
                tv1.post(new Runnable() {
                    public void run() {
                        while (running) {
                            // do stuff here
                            int valueRead = myAudioManager.getStreamVolume(1);
                            tv1.setText("RUNNING " + valueRead);
                            // Sleep thread
                            try {
                                // thread to sleep for 1000 milliseconds
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                });
            }
        }).start();
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

}
