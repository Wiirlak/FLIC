package fr.esgi.flic;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "AppWidgetProvider";

    public Object get(String collection, String field, String value) {
        Task result = db.collection(collection).whereEqualTo(field, value).get();
        return result.getResult();
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
        Task result = db.collection(collection).document(id).delete();
        return result.getResult();
    }

    public boolean exist(String id){
        return false;
    }

}
