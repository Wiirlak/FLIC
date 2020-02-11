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
import com.google.android.gms.awareness.snapshot.HeadphoneStateResponse;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import fr.esgi.flic.provider.DatabaseProvider;

public class HeadPhone extends Service {

    public static Context context;
    public HeadPhone() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            final int delay = 25000;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    Awareness.getSnapshotClient(context).getHeadphoneState()
                            .addOnSuccessListener(new OnSuccessListener<HeadphoneStateResponse>() {
                                @Override
                                public void onSuccess(HeadphoneStateResponse headphoneStateResponse) {
                                    HeadphoneState headphoneState = headphoneStateResponse.getHeadphoneState();
                                    boolean pluggedIn = headphoneState.getState() == HeadphoneState.PLUGGED_IN;
                                    DatabaseProvider.addDataHeadphone(context,"notifications", pluggedIn);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Could not get headphone state: " + e);
                                }
                            });
                    handler.postDelayed(this, delay);
                }
            }, delay);
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                48);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new HeadPhone.ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Headphone service :", "Headphone starting");

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("Headphone service :", "Headphone done");

    }
}
