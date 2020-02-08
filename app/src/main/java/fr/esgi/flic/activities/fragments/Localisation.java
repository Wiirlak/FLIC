package fr.esgi.flic.activities.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import fr.esgi.flic.R;
import fr.esgi.flic.object.LocationViewModel;
import fr.esgi.flic.utils.Tools;

public class Localisation extends Fragment {

    private LocationViewModel mViewModel;
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
        mViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        TextView tv = (TextView) getView().findViewById(R.id.latest_localisations_notifications);

        db.collection("notifications")// TODO ajouter condition user = id de l'utilisateur couplé
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
                                    Log.d(TAG, "MAP = " + Tools.getLocalisationURL(queryDocumentSnapshots.getDocuments().get(0).get("value").toString()));
                                }
                            });
                        }

                    }
                });

    }

}
