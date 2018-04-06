package travelfeedback.abdul.com.travelfeedback.notificationHandler;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by KnightTuring on 24-03-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.println(Log.INFO , "myTag" , "remoteMessage"+remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0)
        {
            Log.println(Log.INFO , "myTag" , "data is"+remoteMessage.getData());
        }

        if(remoteMessage.getNotification() != null)
        {
            Log.println(Log.INFO , "myTag" , "remoteMessage notif body"+remoteMessage.getNotification().getBody());
        }
    }
}
