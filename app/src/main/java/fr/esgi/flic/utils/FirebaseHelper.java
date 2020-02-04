package fr.esgi.flic.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.esgi.flic.object.Notifications;
import fr.esgi.flic.object.User;
import android.util.Log;


public class FirebaseHelper {
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "AppWidgetProvider";

    public Object get(String collection, String field, String value) {
        return db.collection(collection).whereEqualTo(field, value).get();
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
            if(limit > 0)
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


    public boolean exist(String id){
        return false;
    }

}
