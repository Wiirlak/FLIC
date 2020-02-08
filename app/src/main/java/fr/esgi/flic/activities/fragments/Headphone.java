package fr.esgi.flic.activities.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import fr.esgi.flic.R;
import fr.esgi.flic.utils.Tools;

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
        super.onActivityCreated(savedInstanceState);

        TextView tv = (TextView) getView().findViewById(R.id.latest_headphone_state_notifications);

        db.collection("notifications") // TODO ajouter condition user = id de l'utilisateur couplé
                .whereEqualTo("type", "headphone")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        tv.setText("Derniers états des écouteurs : \n");
                        if(!queryDocumentSnapshots.isEmpty()) {
                            getActivity().runOnUiThread(new Runnable() {


                                @Override
                                public void run() {
                                    for (int i = 0; i < 3; i++) {
                                        tv.setText(tv.getText() + "\n" + Tools.headphoneSwitch(queryDocumentSnapshots.getDocuments().get(i).get("value").toString()) + DateFormat.format(" le dd/MM/yyyy à hh:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")));
                                    }
                                }
                            });
                        }

                    }
                });
    }

}
