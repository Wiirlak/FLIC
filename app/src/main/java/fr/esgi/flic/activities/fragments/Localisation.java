package fr.esgi.flic.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import fr.esgi.flic.R;

public class Localisation extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private String TAG = "FragmentLocalisation";

    public static Localisation newInstance() {
        return new Localisation();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.localisation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*TextView tv = (TextView) getView().findViewById(R.id.latest_localisations_notifications);

        db.collection("notifications")
                .whereEqualTo("type", "localisation")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if(!queryDocumentSnapshots.isEmpty()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < 3; i++) {
                                        //tv.setText(tv.getText() + "\n" + Tools.getLocalisationURL(queryDocumentSnapshots.getDocuments().get(i).get("value").toString()) + DateFormat.format(" le dd/MM/yyyy à hh:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")));
                                        //tv.setText(tv.getText() + "\n" + Html.fromHtml("<a href=\""+ Tools.getLocalisationURL(queryDocumentSnapshots.getDocuments().get(i).get("value").toString()) + "\">Ici</a>\n") + DateFormat.format(" le dd/MM/yyyy à hh:mm:ss", queryDocumentSnapshots.getDocuments().get(0).getDate("date")));
                                    }
                                    //Log.d(TAG, "MAP = " + Tools.getLocalisationURL(queryDocumentSnapshots.getDocuments().get(0).get("value").toString()));
                                }
                            });
                        }

                    }
                });*/

    }

}
