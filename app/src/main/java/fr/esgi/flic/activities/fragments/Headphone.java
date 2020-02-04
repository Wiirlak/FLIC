package fr.esgi.flic.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

import fr.esgi.flic.R;

public class Headphone extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private String TAG = "FragmentHeadphone";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.headphone_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final RemoteViews views = new RemoteViews(getContext().getPackageName() , R.layout.headphone_fragment);

        super.onActivityCreated(savedInstanceState);

        /*db.collection("notifications")
                .whereEqualTo("type", "headphone")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(3)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();

                        }
                    }
        });*/

        final Query notifs = db.collection("notifications").whereEqualTo("type", "headphone").orderBy("date", Query.Direction.DESCENDING);
        notifs.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                // Log.d(TAG, "LATEST NOTIF TITLE : " + queryDocumentSnapshots.getDocuments().get(0).get("value"));

                for(int i = 0;i < 3;i++) {
                    Log.d("FOR DEBUG", queryDocumentSnapshots.getDocuments().get(i).get("value").toString());
                    views.setTextViewText(R.id.latest_headphone_state_notifications, queryDocumentSnapshots.getDocuments().get(i).get("value").toString());
                }

            }
        });

    }
}
