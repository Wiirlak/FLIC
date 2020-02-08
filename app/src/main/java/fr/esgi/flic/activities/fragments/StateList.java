package fr.esgi.flic.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import fr.esgi.flic.R;
import fr.esgi.flic.utils.Tools;

public class StateList extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private String TAG = "FragmentStateList";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView title = (TextView) view.findViewById(R.id.list_title);
        title.setText("Liste des derniers moyens de transport : ");

        TextView list = (TextView) view.findViewById(R.id.notification_list);

        db.collection("notifications") // TODO ajouter condition user = id de l'utilisateur couplé
                .whereEqualTo("type", "state")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        list.setText("");
                        if(!queryDocumentSnapshots.isEmpty()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                                        list.setText(list.getText() + "\n" + Tools.stateSwitch(queryDocumentSnapshots.getDocuments().get(i).get("value").toString()) + DateFormat.format(" le dd/MM/yyyy à hh:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")));
                                    }

                                }
                            });
                        }

                    }
                });
    }
}
