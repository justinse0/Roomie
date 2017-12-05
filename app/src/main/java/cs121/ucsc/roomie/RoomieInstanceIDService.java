package cs121.ucsc.roomie;

/**
 * Created by Adam on 11/27/2017.
 */



        import android.util.Log;

        import com.google.firebase.iid.FirebaseInstanceId;
        import com.google.firebase.iid.FirebaseInstanceIdService;

public class RoomieInstanceIDService extends FirebaseInstanceIdService{
    private final static String TAG = "MyFirebase...Service";;

    @Override
    public void onTokenRefresh(){
        //Get updated InstanceID token

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "refreshed token: "+ refreshedToken);

        //to send messages to this app instance, send the token to the servers
        sendRegistrationToServer(refreshedToken);
    }

    //persist token to third-party servers
    private void sendRegistrationToServer(String token){
        //TODO: send this token to the FCM server
    }
}
