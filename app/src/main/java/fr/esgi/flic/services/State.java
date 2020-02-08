package fr.esgi.flic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import fr.esgi.flic.provider.DatabaseProvider;
import fr.esgi.flic.utils.StateUtils;

public class State extends Service {

    public static Context context;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    public State() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            System.out.println("ok boomer");
            final int delay = 22000; //milliseconds
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    //do something
                    GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(State.this)
                            .addApi(Awareness.API)
                            .build();
                    mGoogleApiClient.connect();
                    Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                            .setResultCallback(detectedActivityResult -> {
                                if (!detectedActivityResult.getStatus().isSuccess()) {
                                    Log.e("NOKL", "Could not get the current activity.");
                                    return;
                                }
                                ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                                DetectedActivity probableActivity = ar.getMostProbableActivity();
                                if(probableActivity.getType() != 4){/* UNKNOWN   */
                                    if(probableActivity.getConfidence() >= 50){
//                                        Log.i("NOKL", StateUtils.returnStateToString(probableActivity.getType()));
                                        DatabaseProvider.addDataState(context,"notifications", StateUtils.returnStateToString(probableActivity.getType()));

                                    }else{
                                        //Log.i("NOKLeee", probableActivity.toString());
                                    }
                                }else{
                                    //Log.i("NOKLaa", probableActivity.toString());
                                }
                            });
                    handler.postDelayed(this, delay);
                }
            }, delay);
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                56);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new State.ServiceHandler (serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
