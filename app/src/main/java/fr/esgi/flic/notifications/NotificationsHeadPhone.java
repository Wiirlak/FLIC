package fr.esgi.flic.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import fr.esgi.flic.activities.MainActivity;
import fr.esgi.flic.R;

public class NotificationsHeadPhone {

    public static void sendNotification(Context context, String type, String value){

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,context.getResources().getString(R.string.HEADPHONE_CHANNEL) )
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(type)
                .setContentText(value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(R.string.HEADPHONE_NOTIF,builder.build());
    }
}
