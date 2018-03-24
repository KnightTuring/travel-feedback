package travelfeedback.abdul.com.travelfeedback;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by KnightTuring on 24-03-2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.println(Log.INFO , "FirebaseToken", "Refreshed Token is "+refreshedToken);
        Log.d("Mytag" , "Refreseshed Token is"+refreshedToken);
    }
}
