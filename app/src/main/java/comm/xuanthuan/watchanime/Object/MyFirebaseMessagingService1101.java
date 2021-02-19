package comm.xuanthuan.watchanime.Object;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import comm.xuanthuan.watchanime.Activity.MainActivity;

public class MyFirebaseMessagingService1101 extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    String bodyMessage;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());

        bodyMessage = remoteMessage.getNotification().getBody();
        Log.d(TAG, "onMessageReceived: " + bodyMessage);

        Log.d(TAG, "onMessageReceived: " + remoteMessage.getNotification().getTitle() + " vvvvvvvv " +
                remoteMessage.getNotification().getBody());
    }

    //This method is only generating push notification
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Log.d(TAG, "sendNotification: " + messageBody + title);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).addAction(new NotificationCompat.Action(
                        android.R.drawable.sym_call_missed,
                        "Cancel",
                        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)))
                .addAction(new NotificationCompat.Action(
                        android.R.drawable.sym_call_outgoing,
                        "OK",
                        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)));
        ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

        Log.d(TAG, "sendNotification: " + messageBody + title);
    }

}
