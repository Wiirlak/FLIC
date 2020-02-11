package fr.esgi.flic.activities;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity {
    FirebaseHelper db = new FirebaseHelper();
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        setContentView(R.layout.activity_main);
        callAllIntent();

        listenForLogout();
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
    }

    private void createNotificationChannel() {
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


    private void callAllIntent() {
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
        if (findViewById(R.id.placeholder_frame) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder_frame, new HeadphoneList()).commit();
        }
    }

    public void showLocalisationNotifList(View view) {
        if (findViewById(R.id.placeholder_frame) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder_frame, new LocalisationList()).commit();
        }
    }

    public void showStateNotifList(View view) {
        if (findViewById(R.id.placeholder_frame) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder_frame, new StateList()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.big_list_layout) != null)
            findViewById(R.id.big_list_layout).setVisibility(View.INVISIBLE);
    }

    public void unlinkUser(View view) {
        unlinkUser();
    }

    public void unlinkUser() {
        User user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);

        db.post("user", user.getPartner_id(), "partner_id", null);
        user.setPartner_id(null);
        db.post("user", user.getId(), user);

        SPHelper.saveUserToSharedPreference(getApplicationContext(), user);
        Intent i = new Intent(this, LinkActivity.class);
        startActivity(i);
        finish();
    }

    public void listenForLogout() {
        User user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);

        if (user.getPartner_id() == null || user.getPartner_id().equals(""))
            return;
        final DocumentReference docRef = database.collection("user").document(user.getPartner_id());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    if (snapshot.get("partner_id") == null)
                        unlinkUser();
                }
            }
        });
    }
}
