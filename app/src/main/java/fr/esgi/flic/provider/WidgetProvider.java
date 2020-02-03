package fr.esgi.flic.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import fr.esgi.flic.activities.MainActivity;
import fr.esgi.flic.R;

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "AppWidgetProvider";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //FirebaseHelper fbHelper = new FirebaseHelper(); //TODO retirer les lignes de code pour tester avant de livrer

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        /*Task test = (Task) fbHelper.get("notifications", "type", "headphone");
        test.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()) {
                    QuerySnapshot result = (QuerySnapshot) task.getResult();
                    System.out.println("RESULT = " + result.getDocuments());
                }
            }
        });*/

    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("data", 0);

        String coupled_id = prefs.getString("coupled_id", "senyuhG15nVVusKgX9ul"); //TODO remplacer avec une valeur id par défaut
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flic);

        final DocumentReference docRef = db.collection("user").document(coupled_id);
        final CollectionReference notifications = db.collection("notifications");


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

                        /*HashMap<Object, Object> notifs = new HashMap<Object, Object>();
                        for (QueryDocumentSnapshot doc : value) {
                            notifs.put(doc.get("type"), doc.get("value"));
                        }
                        Log.d(TAG, "Current notifications : " + notifs);*/

                        Log.d(TAG, "getting latest notifications");
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
                                                        //Log.d(TAG, "QuerySnapshot data: " + document.getDocuments());
                                                        Log.d(TAG, "LATEST NOTIF TITLE : " + document.getDocuments().get(0).get("type"));
                                                        Log.d(TAG, "LATEST NOTIF VALUE : " + document.getDocuments().get(0).get("value"));

                                                        views.setTextViewText(R.id.notif_title, document.getDocuments().get(0).get("type").toString());
                                                        views.setTextViewText(R.id.notif_value, document.getDocuments().get(0).get("value").toString());

                                                        appWidgetManager.updateAppWidget(appWidgetIds[0], views);

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

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Log.d(TAG, "onUpdate");
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);
            views.setInt(R.id.LinearLayout, "setBackgroundColor", Color.WHITE);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
    }
}
