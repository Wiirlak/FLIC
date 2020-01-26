package fr.esgi.flic;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import fr.esgi.flic.Services.HeadPhone;
import fr.esgi.flic.Services.Locations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeadPhone.context = this;

//        Intent connection = new Intent(this, LinkActivity.class);
//        startActivity(connection);
        Intent intent = new Intent(this, Locations.class);
        startService(intent);
        Intent intent2 = new Intent(this, HeadPhone.class);
        startService(intent2);
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
}
