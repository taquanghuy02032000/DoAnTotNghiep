package online.javalab.poly.dto;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

import online.javalab.poly.R;
import online.javalab.poly.activitys.GetStartedActivity;

/**
 * Created by CanhNamDinh
 * on 09,December,2021
 */
public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "1";
    public static final String channelName = "Java";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }


    public NotificationCompat.Builder getChannelNotification() {
        
        Intent resultIntent = new Intent(this, GetStartedActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);
        expandedView.setTextViewText(R.id.timestamp, this.getString(R.string.tittle_java,
                DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME)));
        expandedView.setTextViewText(R.id.content_title, this.getString(R.string.tittle_notifi,
                GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getDisplayName()));
        expandedView.setTextViewText(R.id.content_text, this.getString(R.string.conntent));


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.java)
                .setContentTitle(this.getString(R.string.tittle_notifi,
                        GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getDisplayName()
                ))
                .setCustomBigContentView(expandedView)
                .setContentText(this.getString(R.string.conntent))
                .setSound(uri)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(this.getString(R.string.conntent)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }


}