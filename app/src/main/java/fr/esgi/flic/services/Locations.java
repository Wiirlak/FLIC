
package fr.esgi.flic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import fr.esgi.flic.provider.DatabaseProvider;

public class Locations extends Service {
    public static Context context;
    public Locations() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            final int delay = 30000; //milliseconds
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    //do something
                    Awareness.getSnapshotClient(Locations.this).getLocation()
                            .addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
                                @Override
                                public void onSuccess(LocationResponse locationResponse) {
                                    android.location.Location loc = locationResponse.getLocation();
//                                    System.out.println("Alt :"+loc.getAltitude() +" Lat : "+loc.getLatitude()+ " Long :"+ loc.getLongitude());
                                    DatabaseProvider.addDataLocation(context,"notifications", loc.getLatitude(),loc.getLongitude(),loc.getAltitude());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Could not get Location state: " + e);
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
                50);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
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
