package sheasmith.me.betterkamar.pages.timetable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import sheasmith.me.betterkamar.R;

/**
 * Created by Shay on 18/03/2017.
 */


public class TimeAlarm extends BroadcastReceiver {

    NotificationManager mNotifyMgr;

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle(intent.getStringExtra("title"))
                        .setContentText(intent.getStringExtra("message"))
                        .setPriority(-2)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        // Sets an ID for the notification
        int mNotificationId = 001;

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
