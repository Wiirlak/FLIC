package fr.esgi.flic.activities;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import fr.esgi.flic.R;
import fr.esgi.flic.activities.fragments.HeadphoneList;
import fr.esgi.flic.activities.fragments.LocalisationList;
import fr.esgi.flic.activities.fragments.StateList;
import fr.esgi.flic.object.User;
import fr.esgi.flic.services.Database;
import fr.esgi.flic.services.HeadPhone;
import fr.esgi.flic.services.Locations;
import fr.esgi.flic.services.State;
import fr.esgi.flic.utils.FirebaseHelper;
import fr.esgi.flic.utils.SPHelper;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity {
    final static private String TAG = "AndroidMainActivity";
    FirebaseHelper db = new FirebaseHelper();
    FirebaseFirestore database = FirebaseFirestore.getInstance();


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

    @Override
    protected void onResume() {
        super.onResume();

        User user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);

        database.collection("user")
                .whereEqualTo("user", user.getPartner_id())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if(queryDocumentSnapshots.getDocuments().size() > 0) {
                            if(queryDocumentSnapshots.getDocuments().get(0).get("partner_id") == null) {
                                unlinkUser();
                            }
                        }
                    }
                });
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
        if(findViewById(R.id.big_list_layout) != null)
            findViewById(R.id.big_list_layout).setVisibility(View.INVISIBLE);
    }

    public void unlinkUser(View view) {
        User user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);

        user.setPartner_id(null);
        db.post("user", user.getId(), user);

        Intent i = new Intent(this, LinkActivity.class);
        startActivity(i);
        finish();
    }

    public void unlinkUser() {
        User user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);

        user.setPartner_id(null);
        db.post("user", user.getId(), user);

        Intent i = new Intent(this, LinkActivity.class);
        startActivity(i);
        finish();
    }
}
