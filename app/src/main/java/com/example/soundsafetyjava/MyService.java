/*
Service is run in  the foreground

 */

package com.example.soundsafetyjava;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;


// TO HANDLE MULTIPLE REQUESTS SIMULTANEOUSLY EXTEND "Service" instead of "IntentService"
public class MyService extends IntentService {

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



        return super.onStartCommand(intent, flags, startId);
    }

    // Kill thread in here
    @Override
    public void onDestroy(){

        super.onDestroy();
    }



}


// Class for thread
class CustomThreadBoy extends Thread {

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