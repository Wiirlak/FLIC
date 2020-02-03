package fr.esgi.flic.activities;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import fr.esgi.flic.R;
import fr.esgi.flic.services.Database;
import fr.esgi.flic.services.HeadPhone;
import fr.esgi.flic.services.Locations;
import fr.esgi.flic.services.State;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        setContentView(R.layout.activity_main);
        callAllIntent();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    52
            );
            return;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel headphoneChannel = new NotificationChannel(this.getResources().getString(R.string.HEADPHONE_CHANNEL), name, importance);
            NotificationChannel localisationChannel = new NotificationChannel(this.getResources().getString(R.string.LOCALISATION_CHANNEL), name, importance);
            NotificationChannel stateChannel = new NotificationChannel(this.getResources().getString(R.string.STATE_CHANNEL), name, importance);
            headphoneChannel.setDescription(description);
            localisationChannel.setDescription(description);
            stateChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(headphoneChannel);
            notificationManager.createNotificationChannel(localisationChannel);
            notificationManager.createNotificationChannel(stateChannel);


        }
    }


    private void callAllIntent(){
        Intent localisation = new Intent(this, Locations.class);
        startService(localisation);
        Intent headphone = new Intent(this, HeadPhone.class);
        HeadPhone.context = this;
        startService(headphone);
        Intent state = new Intent(this, State.class);
        State.context = this;
        startService(state);
        Intent database = new Intent(this, Database.class);
        startService(database);
    }

}
