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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

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
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                56);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new State.ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("State service :", "service starting");

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("State service :", "service done");
    }

    @SuppressWarnings("depreciation")
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final int delay = 22000;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(State.this)
                            .addApi(Awareness.API)
                            .build();
                    mGoogleApiClient.connect();
                    Awareness.getSnapshotClient(State.context).getDetectedActivity()
                            .addOnSuccessListener(detectedActivityResult -> {
                                ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                                DetectedActivity probableActivity = ar.getMostProbableActivity();
                                if (probableActivity.getType() != 4) {
                                    if (probableActivity.getConfidence() >= 50) {
                                        DatabaseProvider.addDataState(context, "notifications", StateUtils.returnStateToString(probableActivity.getType()));

                                    } else {
                                        Log.i("No enought confidence", probableActivity.toString());
                                    }
                                } else {
                                    Log.i("Unknown type", probableActivity.toString());
                                }
                            })
                            .addOnFailureListener(e -> Log.e("State Service","Could not get state: " + e));

                    handler.postDelayed(this, delay);
                }
            }, delay);
            stopSelf(msg.arg1);
        }
    }
}
