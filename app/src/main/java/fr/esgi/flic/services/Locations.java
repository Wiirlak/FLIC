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

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import fr.esgi.flic.provider.DatabaseProvider;

public class Locations extends Service {
    public static Context context;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    public Locations() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                50);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Localisation service :", "Localisation starting");

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("Localisation service :", "Localisation done");

    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final int delay = 30000;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Awareness.getSnapshotClient(Locations.this).getLocation()
                            .addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
                                @Override
                                public void onSuccess(LocationResponse locationResponse) {
                                    android.location.Location loc = locationResponse.getLocation();
                                    DatabaseProvider.addDataLocation(context, "notifications", loc.getLatitude(), loc.getLongitude(), loc.getAltitude());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Could not get Localisation state: " + e);
                                }
                            });
                    handler.postDelayed(this, delay);
                }
            }, delay);
            stopSelf(msg.arg1);
        }
    }
}
