package fr.esgi.flic.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import fr.esgi.flic.R;
import fr.esgi.flic.object.User;
import fr.esgi.flic.utils.FirebaseHelper;
import fr.esgi.flic.utils.SPHelper;

public class LinkActivity extends AppCompatActivity {
    FirebaseHelper db;
    ClipboardManager clipboard;
    CharSequence id_user;
    CharSequence id_partner; // can be null
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_link);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        db = new FirebaseHelper();

        TextView idUser = findViewById(R.id.personnalID);
        //id_user = idUser.getText();
        //Usage : getSavedObjectFromPreference(context, "mPreference", "mObjectKey", (Type) SampleClass.class)
        user = SPHelper.getSavedObjectFromPreference(getApplicationContext(), "mPreference", "mObjectKey", User.class);
        if(user == null) {
            id_user = generateId();

            User nu = new User(id_user.toString());
            SPHelper.saveObjectToSharedPreference(getApplicationContext(), "mPreference", "mObjectKey", user);
            db.post("user", nu.getId(), nu);
            Toast.makeText(getApplicationContext(), "User créé !", Toast.LENGTH_SHORT).show();
        }else{
            //getfromlocalstorage
            id_user = generateId();
            Toast.makeText(getApplicationContext(), "User existant !", Toast.LENGTH_SHORT).show();
        }
        idUser.setText(id_user);
        waitingMessage(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCopiedLinkOnStart();
    }

    public void onClickCopyLink(View view) {
        ClipData clip = ClipData.newPlainText("FLIC_my_id", id_user);
        //ClipData clip = ClipData.newPlainText("FLIC_my_id", "a899c435b"); // Test value
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Ton id a été mis dans le presse-papier !", Toast.LENGTH_SHORT).show();
    }

    public void setPartner(View view) {
        TextView partner = findViewById(R.id.companionID);
        id_partner  = partner.getText();
        db.post("user", id_user.toString(), "partner_id", "user/" + id_partner.toString());

        Toast.makeText(getApplicationContext(), "id envoyé", Toast.LENGTH_SHORT).show();
        waitingMessage(true);
    }

    public void applyCopiedLinkOnStart() {
        if(!clipboard.hasPrimaryClip())
            return;
        ClipData exemple = clipboard.getPrimaryClip();
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        id_partner = item.getText();
        if (id_partner != null) {
            if (id_partner.length() == 9) {
                if (!id_partner.toString().equals(id_user.toString())) {
                    EditText idPartner = findViewById(R.id.companionID);
                    idPartner.setText(id_partner);
                    idPartner.setSelection(id_partner.length());
                }
            }
        }
    }

    public void waitingMessage(boolean v) {
        TextView partner = findViewById(R.id.companionID);
        TextView waitMessage = findViewById(R.id.waitText);
        if(v){
            waitMessage.setVisibility(TextView.VISIBLE);
            partner.setFocusable(false);
            partner.setBackgroundColor(Color.argb(200,122,122,122));
        }else{
            waitMessage.setVisibility(TextView.INVISIBLE);
        }

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

    public void createId(){
        String id;
        do {
            id = generateId();
        }while(!db.exist(id));
    }


}
