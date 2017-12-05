package cs121.ucsc.roomie;

/**
 * Created by Adam on 11/27/2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/*import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;*/
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class RoomieMessagingService extends FirebaseMessagingService {
    //TODO: implement the activities based on the tutorial
    String TAG  = "Messaging Service:";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        //TODO: handle FCM messages here
        Log.d(TAG, "Message data payload" + remoteMessage.getFrom());

        //Check if message contains a data payload
        if (remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message data payload: "+ remoteMessage.getData());
            handleNow();
        }

        //check for a notification payload
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body: "+remoteMessage.getNotification().getBody());
        }


    }

    private void handleNow(){
        Log.d(TAG, "Short lived task is  done");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "House Messages";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        //.setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle("Roomie")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
