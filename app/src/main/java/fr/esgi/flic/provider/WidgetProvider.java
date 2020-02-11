package fr.esgi.flic.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import fr.esgi.flic.R;
import fr.esgi.flic.activities.MainActivity;
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.SPHelper;
import fr.esgi.flic.utils.Tools;

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "AppWidgetProvider";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");

    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        User coupled_id = SPHelper.getSavedUserFromPreference(context, User.class);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flic);

        if (coupled_id.getPartner_id() == null || coupled_id.getPartner_id().equals(""))
            return;
        final DocumentReference docRef = db.collection("user").document(coupled_id.getPartner_id());
        final CollectionReference notifications = db.collection("notifications");

        db.collection("notifications")
                .whereEqualTo("user_id", docRef)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            views.setTextViewText(R.id.widget_description, "Dernière notification reçue" + DateFormat.format(" le dd/MM/yyyy à HH:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")) + " :");
                            views.setTextViewText(R.id.notif_title, Tools.titleSwitch(queryDocumentSnapshots.getDocuments().get(0).get("type").toString()));
                            views.setTextViewText(R.id.notif_value, Tools.notificationSwitch(queryDocumentSnapshots.getDocuments().get(0).get("type").toString(), queryDocumentSnapshots.getDocuments().get(0).get("value").toString()));

                            appWidgetManager.updateAppWidget(appWidgetIds[0], views);
                        }
                    }
                });

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Log.d(TAG, "onUpdate");
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setInt(R.id.LinearLayout, "setBackgroundColor", Color.WHITE);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
    }
}
