package fr.esgi.flic;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;


public class DatabaseProvider {
    private static final String TAG = "DatabaseProvider";


    public static void addDataLocation(String table,String type, String user_id, double lat, double longi, double alt){
        //ADD DATA TO FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("date", new Date());
        user.put("type", type);
        user.put("user_id", "senyuhG15nVVusKgX9ul"); // user_id
        user.put("value", "lat:"+lat+";long:"+longi+";alt:"+alt);

// Add a new document with a generated ID
        db.collection(table)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public static void addDataHeadphone(String table,String type, String user_id, boolean plugged){
        //ADD DATA TO FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("date", new Date());
        user.put("type", type);
        user.put("user_id", "senyuhG15nVVusKgX9ul"); // user_id
        user.put("value", plugged);

// Add a new document with a generated ID
        db.collection(table)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
