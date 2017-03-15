package map.parth.com.checkinternet;

import android.app.Application;

/**
 * Created by krishna on 2017-03-14.
 */

public class CheckInternet extends Application {

    private static CheckInternet mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized CheckInternet getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}