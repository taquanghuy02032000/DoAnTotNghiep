package online.javalab.poly.dto.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import online.javalab.poly.R;

/**
 * Created by CanhNamDinh
 * on 12,December,2021
 */
public class NotifiBroadCast  extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "lemubitNotify")
                .setSmallIcon(R.drawable.java)
                .setContentTitle("Lemubit Academy Alarm notification")
                .setContentText("Hey students, this is an awesome alarm notification...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(101, builder.build());
    }
}
