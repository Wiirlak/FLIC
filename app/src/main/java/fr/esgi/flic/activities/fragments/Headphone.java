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
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.SPHelper;
import fr.esgi.flic.utils.Tools;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

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
        User user = SPHelper.getSavedUserFromPreference(getContext(), User.class);

        db.collection("notifications")
                .whereEqualTo("type", "headphone")
                .whereEqualTo("user_id", user.getPartner_id())
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        tv.setText("Appuyez ici pour avoir les états des écouteurs");
                        if(!queryDocumentSnapshots.isEmpty()) {
                            getActivity().runOnUiThread(new Runnable() {


                                @Override
                                public void run() {
                                    for (int i = 0; i < Tools.min(queryDocumentSnapshots.getDocuments().size(), 3); i++) {
                                        //tv.setText(tv.getText() + "\n" + Tools.headphoneSwitch(queryDocumentSnapshots.getDocuments().get(i).get("value").toString()) + DateFormat.format(" le dd/MM/yyyy à hh:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")));
                                    }
                                }
                            });
                        }

                    }
                });
    }

}
