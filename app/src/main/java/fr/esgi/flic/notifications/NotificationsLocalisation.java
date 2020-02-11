package fr.esgi.flic.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import fr.esgi.flic.R;
import fr.esgi.flic.activities.MainActivity;

public class NotificationsLocalisation {

    public static void sendNotification(Context context, String type, String value) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getResources().getString(R.string.LOCALISATION_CHANNEL))
                .setSmallIcon(R.drawable.flic_minimal_nofill)
                .setContentTitle(type)
                .setContentText(value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(R.string.LOCALISATION_NOTIF, builder.build());
    }
}
