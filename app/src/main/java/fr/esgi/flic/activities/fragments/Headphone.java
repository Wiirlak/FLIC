package fr.esgi.flic.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.esgi.flic.R;
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.SPHelper;
import fr.esgi.flic.utils.Tools;

public class Headphone extends Fragment {

    final private String TAG = "FragmentHeadphone";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.headphone_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tv = getView().findViewById(R.id.latest_headphone_state_notifications);
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
                        if (!queryDocumentSnapshots.isEmpty()) {
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
