package fr.esgi.flic.provider;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import fr.esgi.flic.R;
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.SPHelper;


public class DatabaseProvider {
    private static final String TAG = "DatabaseProvider";


    public static void addDataLocation(Context context, String table, double lat, double longi, double alt){
        //ADD DATA TO FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User u = SPHelper.getSavedUserFromPreference(context, User.class);
        String coupled_id = u.getId();
        final DocumentReference docRef = db.collection("user").document(coupled_id);
        Map<String, Object> user = new HashMap<>();
        user.put("date", new Date());
        user.put("type", context.getResources().getString(R.string.LOCALISATION_TEXT));
        user.put("user_id",docRef); // user_id
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
    public static void addDataHeadphone(Context context,String table, boolean plugged){
        //ADD DATA TO FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User u = SPHelper.getSavedUserFromPreference(context, User.class);
        String coupled_id = u.getId();
        final DocumentReference docRef = db.collection("user").document(coupled_id);
        Map<String, Object> user = new HashMap<>();
        user.put("date", new Date());
        user.put("type", context.getResources().getString(R.string.HEADPHONE_TEXT));
        user.put("user_id", docRef); // user_id
        user.put("value", plugged ? "Yes" : "No");

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

    public static void addDataState(Context context,String table, String activity){
        //ADD DATA TO FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User u = SPHelper.getSavedUserFromPreference(context, User.class);
        String coupled_id = u.getId();
        final DocumentReference docRef = db.collection("user").document(coupled_id);
        Map<String, Object> user = new HashMap<>();
        user.put("date", new Date());
        user.put("type", context.getResources().getString(R.string.STATE_TEXT));
        user.put("user_id", docRef); // user_id
        user.put("value", activity);

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
