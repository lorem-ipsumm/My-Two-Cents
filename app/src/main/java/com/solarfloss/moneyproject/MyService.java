package com.solarfloss.moneyproject;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas on 10/12/2016.
 */

public class MyService extends Service{
    public boolean running = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void mainLoop(){
        final Intent bitMaker = getPackageManager().getLaunchIntentForPackage("com.cakecodes.bitmaker");
        final Intent freeBTC = getPackageManager().getLaunchIntentForPackage("com.free.bitcoin.maker");
        final Intent bitcoinFarm = getPackageManager().getLaunchIntentForPackage("cap.pw.bitcoin.farm");


        final String[][] appInfo = {
                {"com.cakecodes.bitmaker","460","1402"},
                {"com.free.bitcoin.maker","1232","1358"},
                {"cac.pw.bitcoin.farm","718","1133"}
        };

        final String tap = "/system/bin/input tap ";
        final String forceStop = "am force-stop ";
        final String adb = "adb shell\n";

        running = true;


        new Thread(new Runnable() {
            @Override
                public void run() {
                    while(true) {
                        if (bitMaker != null) {
                            try {

                                Process process = Runtime.getRuntime().exec("su");
                                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                                Intent currentApp;


                                //Package Name, x coordinate, y coordinate
                                for(int apps = 0;apps < appInfo.length; apps++){

                                    //Get the variables ready
                                    currentApp = getPackageManager().getLaunchIntentForPackage(appInfo[apps][0]);
                                    String currentTap = tap + appInfo[apps][1] + " " + appInfo[apps][2];
                                    Log.i("INF",currentTap);

                                    //Start the current app and wait for it to initialize
                                    startActivity(currentApp);
                                    Thread.sleep(15000);

                                    //Send a tap to the collect button in the app and wait 30 seconds in case there is a video ad
                                    os.writeBytes(currentTap + "\n");
                                    os.flush();
                                    Thread.sleep(30000);


                                    //Force close whatever app is open
                                    os.writeBytes(adb);
                                    os.flush();
                                    os.writeBytes(forceStop + appInfo[apps][0] + "\n");


                                    //Wait some time in between apps
                                    Thread.sleep(5000);
                                }


                                //Wait 30 minutes and then do it all again
                                Thread.sleep(1800000);

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Something Broke", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
                }

        }).start();
    }




    @Override
    public void onCreate(){
        super.onCreate();
        //Toast.makeText(getApplicationContext(),"Service Created", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Service Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(running != true)
            mainLoop();
        return START_STICKY;
    }
}
