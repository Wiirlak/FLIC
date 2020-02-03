package fr.esgi.flic.notifications;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import fr.esgi.flic.utils.FirebaseHelper;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DbListener {
    private FirebaseHelper fdb;

    public DbListener() {
        fdb = new FirebaseHelper();
    }

    public DbListener(FirebaseHelper db) {
        fdb = db;
    }

    public void listenChange(String collection, String id){
        final DocumentReference docRef = fdb.db.collection(collection).document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public boolean isLinked(String collection1, String id1, String collection2, String id2) {
        return false;
    }

    public boolean isLinked(String collection, String id1, String id2) {
//        User main = (User) fdb.get(collection, "U2w9yq");
        return false;
    }

//    public String isLinked(String collection, String id1, String id2) {
//        User main = (User) fdb.get(collection, "U2w9yq");
//        return main.getId();
//    }
}
