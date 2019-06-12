/*
Service is run in  the foreground

 */

package com.example.soundsafetyjava;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;


// TO HANDLE MULTIPLE REQUESTS SIMULTANEOUSLY EXTEND "Service" instead of "IntentService"
public class MyService extends IntentService {

    // Declare static variables for parameters to be passed
    public static int running;
    public static int thresholdValue;
    public static AudioManager myAudioManager;
    //AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    CustomThreadBoy customThreadBoy = new CustomThreadBoy(running, thresholdValue, myAudioManager) ;

    // Constructor is required
    public MyService(){
        super("MyService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Do work here if needed (can leave blank or just sleep for a few milli seconds)
    }

    // Start thread in here
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // Do all work in here
        customThreadBoy.start();
        Log.d("Service","Thread is Started within onStartCommand" );
        return super.onStartCommand(intent, flags, startId);
    }

    // Kill thread in here
    @Override
    public void onDestroy(){

        running = 0; // stops thread
        Log.d("Service","Thread is Stopped within onDestroy" );
        super.onDestroy();
    }

}


// Class for thread
class CustomThreadBoy extends Thread {
    int valueRead;
    AudioManager myAudioManager;

    String currentDateTimeString;
    private int runningLocal;
    private int thresholdMedia;

    // Constructor
    public CustomThreadBoy(int running, int threshold, AudioManager myAudioManagerInput){
        this.runningLocal = running;
        this.thresholdMedia = threshold;
        this.myAudioManager = myAudioManagerInput;
    }

    public void run() {
        while (runningLocal == 1) {
            // 2 = Ringer,   3 = Media,  4 = Alarm
            valueRead = myAudioManager.getStreamVolume(3);
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            Log.d("ThreadRun", "Thread is running noicely: " + currentDateTimeString);
            Log.d("ThreadRun", "Volume level: " + valueRead);

            // Limits max volume
            if (myAudioManager.getStreamVolume(3) > thresholdMedia){
                myAudioManager.setStreamVolume(3, thresholdMedia,1);
            }

            if (runningLocal != 0) {
                Thread.currentThread().interrupt();
                Log.d("ThreadRun", "THREAD IS INTERRUPTED. KILLING IT.");
                return;
            }
            // Sleep thread
            try {
                // thread to sleep for 1400 milliseconds --> Testing only
                Thread.sleep(1400);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
