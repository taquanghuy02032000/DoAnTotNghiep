package online.javalab.poly;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.xiaoyv.javaengine.JavaEngineApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;

import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.DialogUtil;

public class Application extends JavaEngineApplication {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            DataStorageManager.init(getApplicationContext());

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Application getInstance() {
        return instance;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "lemubit Channel";
            String description = "You gotta listen";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("lemubitNotify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}

