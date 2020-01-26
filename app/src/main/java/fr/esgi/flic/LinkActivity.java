package fr.esgi.flic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LinkActivity extends AppCompatActivity {
    ClipboardManager clipboard;
    CharSequence id_user;
    CharSequence id_partner; // can be null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_link);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        TextView idUser = findViewById(R.id.personnalID);
        id_user = idUser.getText();
        waitingMessage();
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

    public void applyCopiedLinkOnStart() {
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        id_partner  = item.getText();
        if (id_partner != null) {
            if(!id_partner.toString().equals(id_user.toString())) {
                EditText idPartner = findViewById(R.id.companionID);
                idPartner.setText(id_partner);
                idPartner.setSelection(id_partner.length());
            }
        }

    }

    public void waitingMessage() {
        TextView waitMessage = findViewById(R.id.waitText);
        if(false){
            waitMessage.setVisibility(TextView.VISIBLE);
        }else{
            waitMessage.setVisibility(TextView.INVISIBLE);
        }

    }


}
