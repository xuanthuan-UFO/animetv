package comm.xuanthuan.watchanime.Object;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService1101 extends FirebaseInstanceIdService {
    private static final String TAG = "InstanceIDService";

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refresh token: " + refreshToken);
    }
}
