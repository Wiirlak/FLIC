package fr.esgi.flic.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Random;

import fr.esgi.flic.R;
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.FirebaseHelper;
import fr.esgi.flic.utils.SPHelper;

public class LinkActivity extends AppCompatActivity {
    FirebaseFirestore dbf = FirebaseFirestore.getInstance();
    FirebaseHelper db = new FirebaseHelper();
    ClipboardManager clipboard;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_link);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        db = new FirebaseHelper();

        user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);
        if(user == null) {
            user = new User();
            user.setId(createId());
            SPHelper.saveUserToSharedPreference(getApplicationContext(), user);
            db.post("user", user.getId(), user);
        }else{
            TextView partner = findViewById(R.id.companionID);
            partner.setText(user.getPartner_id()==null?"":user.getPartner_id());
            updateSPFromFB();
        }

        TextView idUser = findViewById(R.id.personnalID);
        idUser.setText(user.getId());
        waitingMessage(user.getPartner_id() != null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCopiedLinkOnStart();
    }

    public void onClickCopyLink(View view) {
        ClipData clip = ClipData.newPlainText("FLIC_my_id", user.getId());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Ton id a été mis dans le presse-papier !", Toast.LENGTH_SHORT).show();
    }

    public void onPartnerIdEdit(View view){
        waitingMessage(false);
    }

    public void setPartner(View view) { setPartner(); }
    public void setPartner() {
        TextView partner = findViewById(R.id.companionID);
        user.setPartner_id(partner.getText().toString());
        db.post("user", user.getId(), user);
        SPHelper.saveUserToSharedPreference(getApplicationContext(), user);

//        Toast.makeText(getApplicationContext(), "id envoyé", Toast.LENGTH_SHORT).show();
        if(partner.getText() != null) {
            waitingMessage(true);
            listenPartner();
        }

    }

    public void listenPartner() {
        if(user.getPartner_id() == null || user.getPartner_id().equals(""))
            return;
        final DocumentReference docRef = dbf.collection("user").document(user.getPartner_id());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    checkLink();
                }
            }
        });
    }

    public void updateSPFromFB(){
        dbf.collection("user")
                .document(user.getId())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document != null)
                            if(document.get("partner_id") != null)
                                if(!document.get("partner_id").equals(user.getId())) {
                                    user.setPartner_id(document.get("partner_id").toString());
                                    SPHelper.saveUserToSharedPreference(getApplicationContext(), user);
                                    TextView partner = findViewById(R.id.companionID);
                                    partner.setText(user.getPartner_id());
                                    listenPartner();
                                }
                    }
                });
    }

    public void checkLink(){
        if(user.getPartner_id() == null)
            return;
        dbf.collection("user")
                .document(user.getPartner_id())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document != null)
                            if(document.get("partner_id") != null)
                                if(document.get("partner_id").equals(user.getId()))
                                    gotoMain();
                    }
                });
    }

    public void gotoMain(){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    public void waitingMessage(boolean v) {
        TextView partner = findViewById(R.id.companionID);
        TextView waitMessage = findViewById(R.id.waitText);
        if(v){
            waitMessage.setVisibility(TextView.VISIBLE);
            partner.setBackgroundColor(Color.argb(50,10,10,10));
        }else{
            waitMessage.setVisibility(TextView.INVISIBLE);
            partner.setBackgroundColor(Color.argb(255,255,255,255));
        }
    }

    public void applyCopiedLinkOnStart() {
        if(!clipboard.hasPrimaryClip())
            return;
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        user.setPartner_id(item.getText().toString());
        if (user.getPartner_id() != null) {
            if (user.getPartner_id().length() == 9) {
                if (!user.getPartner_id().equals(user.getId())) {
                    EditText idPartner = findViewById(R.id.companionID);
                    idPartner.setText(user.getPartner_id());
                    idPartner.setSelection(user.getPartner_id().length());
                }
            }
        }
        setPartner();
    }

    public String generateId(){
        int length = 6;
        char[] base62chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i=0; i<length; i++)
            sb.append(base62chars[random.nextInt(62)]);
        return sb.toString();
    }

    public String createId(){
        String id;
        do {
            id = generateId();
//        }while(!db.exist("user", id));
        }while(false);
        return id;
    }

    @Override
    public void onBackPressed() {
        
    }
}
