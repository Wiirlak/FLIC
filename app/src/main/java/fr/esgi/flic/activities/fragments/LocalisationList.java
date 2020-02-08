package fr.esgi.flic.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
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
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.SPHelper;
import fr.esgi.flic.utils.Tools;

public class LocalisationList extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private String TAG = "LocalisationList";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView title = (TextView) view.findViewById(R.id.list_title);
        title.setText("Liste des dernières localisations : ");

        TextView list = (TextView) view.findViewById(R.id.notification_list);
        User user = SPHelper.getSavedUserFromPreference(getContext(), User.class);

        db.collection("notifications")
                .whereEqualTo("type", "localisation")
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

                        list.setText("");
                        if(!queryDocumentSnapshots.isEmpty()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < Tools.min(queryDocumentSnapshots.getDocuments().size(), 50); i++) {
                                        list.setText(list.getText() + "\n" + Tools.getLocalisationURL(queryDocumentSnapshots.getDocuments().get(i).get("value").toString()) + DateFormat.format(" le dd/MM/yyyy à hh:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")));
                                    }

                                }
                            });
                        }

                    }
                });
    }
}
