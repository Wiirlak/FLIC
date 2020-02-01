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
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_link);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        db = new FirebaseHelper();

        TextView idUser = findViewById(R.id.personnalID);
        user = SPHelper.getSavedUserFromPreference(getApplicationContext(), User.class);
        if(user == null) {
            user = new User();
            user.setId(createId());
            SPHelper.saveUserToSharedPreference(getApplicationContext(), user);
            db.post("user", user.getId(), user);
        }else{
            TextView partner = findViewById(R.id.companionID);
            partner.setText(user.getPartner_id());
        }
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
        //ClipData clip = ClipData.newPlainText("FLIC_my_id", "a899c435b"); // Test value
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Ton id a été mis dans le presse-papier !", Toast.LENGTH_SHORT).show();
    }

    public void setPartner(View view) {
        TextView partner = findViewById(R.id.companionID);
        user.setPartner_id(partner.getText().toString());
        db.post("user", user.getId(), user);
        SPHelper.saveUserToSharedPreference(getApplicationContext(), user);

        Toast.makeText(getApplicationContext(), "id envoyé", Toast.LENGTH_SHORT).show();
        waitingMessage(true);
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

    public String createId(){
        String id;
        do {
            id = generateId();
        }while(!db.exist(id));
        return id;
    }


}
