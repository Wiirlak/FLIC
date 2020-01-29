package fr.esgi.flic;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FirebaseHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "AppWidgetProvider";

    public Object get(String collection, String field, String value) {
        Task result = db.collection(collection).whereEqualTo(field, value).get();
        return result.getResult();
    }

    public Object post(String collection, String id, Map<String, Object> data) {
        Task result = db.collection(collection).document(id).set(data);
        return result.getResult();
    }

    public Object delete(String collection, String id, Map<String, Object> data) {
        Task result = db.collection(collection).document(id).delete();
        return result.getResult();
    }

}
