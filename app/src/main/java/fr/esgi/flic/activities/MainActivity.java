package fr.esgi.flic.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import fr.esgi.flic.R;
import fr.esgi.flic.activities.fragments.HeadphoneList;
import fr.esgi.flic.activities.fragments.LocalisationList;
import fr.esgi.flic.activities.fragments.StateList;
import fr.esgi.flic.services.Database;
import fr.esgi.flic.services.HeadPhone;
import fr.esgi.flic.services.Locations;
import fr.esgi.flic.services.State;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    final static private String TAG = "AndroidMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        setContentView(R.layout.activity_main);
        callAllIntent();
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


    private void callAllIntent(){
        Intent connection = new Intent(this, LinkActivity.class);
        startActivity(connection);
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

    public void showHeadphoneNotifList(View view) {
        if(findViewById(R.id.placeholder_frame) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder_frame, new HeadphoneList()).commit();
        }
    }

    public void showLocalisationNotifList(View view) {
        if(findViewById(R.id.placeholder_frame) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder_frame, new LocalisationList()).commit();
        }
    }

    public void showStateNotifList(View view) {
        if(findViewById(R.id.placeholder_frame) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder_frame, new StateList()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.big_list_layout).setVisibility(View.INVISIBLE);
    }
}
