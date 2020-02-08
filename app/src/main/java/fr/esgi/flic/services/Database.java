package fr.esgi.flic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.esgi.flic.notifications.NotificationsHeadPhone;
import fr.esgi.flic.notifications.NotificationsLocalisation;
import fr.esgi.flic.R;
import fr.esgi.flic.notifications.NotificationsState;
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.SPHelper;

public class Database extends Service {
    private static final String TAG = "DatabaseService";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context = this;

    public Database() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        User u = SPHelper.getSavedUserFromPreference(context, User.class);
        String coupled_id = u.getPartner_id();
//        String coupled_id = "CuWf87";
        final DocumentReference docRef = db.collection("user").document(coupled_id);
        db.collection("notifications")
                .whereEqualTo("user_id", docRef)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        Query query = db.collection("notifications")
                                                .whereEqualTo("user_id", docRef)
                                                .orderBy("date", Query.Direction.DESCENDING)
                                                .limit(1);
                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot document = task.getResult();
                                                    if (!document.isEmpty()) {
                                                        String type = document.getDocuments().get(0).get("type").toString();
                                                        if(type.equals(context.getResources().getString(R.string.HEADPHONE_TEXT))) {
                                                            NotificationsHeadPhone.sendNotification(context,type,document.getDocuments().get(0).get("value").toString());
                                                        }else if(type.equals(context.getResources().getString(R.string.LOCALISATION_TEXT))) {
                                                            NotificationsLocalisation.sendNotification(context,type,document.getDocuments().get(0).get("value").toString());
                                                        }else if(type.equals(context.getResources().getString(R.string.STATE_TEXT))) {
                                                            NotificationsState.sendNotification(context,type,document.getDocuments().get(0).get("value").toString());
                                                        }

                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.e(TAG, task.getException().getLocalizedMessage());
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                });
    }
}
