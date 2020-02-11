package fr.esgi.flic.utils;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import fr.esgi.flic.object.Notifications;
import fr.esgi.flic.object.User;


public class FirebaseHelper {
    private static final String TAG = "AppWidgetProvider";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public Object get(String collection, String field, String value) {
        try {
            return Tasks.await(db.collection(collection).whereEqualTo(field, value).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object get(String collection, String id) {
        Task result;
        try {
            result = db.collection(collection).document(id).get();
            Tasks.await(result);
            return result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object post(String collection, String id, User data) {
        Task result;
        try {
            result = db.collection(collection).document(id).set(data);
            Tasks.await(result);
            return result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object post(String collection, String id, Map<String, Object> data) {
        Task result;
        try {
            result = db.collection(collection).document(id).set(data);
            Tasks.await(result);
            return result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object post(String collection, String id, String field, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(field, value);
        Task result;
        try {
            result = db.collection(collection).document(id).set(data);
            Tasks.await(result);
            return result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object delete(String collection, String id, Map<String, Object> data) {
        return db.collection(collection).document(id).delete();
    }

    public ArrayList<Notifications> getNotifications(String type, int limit) {
        Log.d("Android FirebaseHelper", "getting notifications...");
        Task result;
        try {
            if (limit > 0)
                result = db.collection("notifications").whereEqualTo("type", type).limit(limit).get();
            else
                result = db.collection("notifications").whereEqualTo("type", type).get();
            Tasks.await(result);
            return (ArrayList<Notifications>) result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exist(String collection, String id) {
        final boolean[] existing = new boolean[1];
        DocumentReference docRef = db.collection(collection).document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    existing[0] = document.exists();
                } else {
                    existing[0] = false;
                }
            }
        });
        return existing[0];
    }

}
