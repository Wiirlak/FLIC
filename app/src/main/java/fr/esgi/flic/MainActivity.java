package fr.esgi.flic;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import fr.esgi.flic.Services.Database;
import fr.esgi.flic.Services.HeadPhone;
import fr.esgi.flic.Services.Locations;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        setContentView(R.layout.activity_main);
        HeadPhone.context = this;

//        Intent connection = new Intent(this, LinkActivity.class);
//        startActivity(connection);
        Intent intent = new Intent(this, Locations.class);
        startService(intent);
        Intent headphone = new Intent(this, HeadPhone.class);
        startService(headphone);
        Intent database = new Intent(this, Database.class);
        startService(database);
    }


    @Override
    protected void onStart() {
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
        super.onStart();
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
            headphoneChannel.setDescription(description);
            localisationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(headphoneChannel);
            notificationManager.createNotificationChannel(localisationChannel);


        }
    }
}
