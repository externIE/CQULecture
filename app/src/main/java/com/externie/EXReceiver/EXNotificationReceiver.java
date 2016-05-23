package com.externie.EXReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.externie.cqulecture.MainActivity;
import com.externie.cqulecture.PostActivity;
import com.externie.cqulecture.R;

/**
 * Created by externIE on 16/5/19.
 */
public class EXNotificationReceiver extends BroadcastReceiver {
    public final static String TICKER = "ticker";
    public final static String TITLE = "title";
    public final static String CONTENT = "content";
    public final static String URL = "url";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        String ticker = intent.getStringExtra(TICKER);
        String title = intent.getStringExtra(TITLE);
        String content = intent.getStringExtra(CONTENT);
        String url = intent.getStringExtra(URL);
        Intent intentToApp = new Intent(context, PostActivity.class);
        intentToApp.putExtra("url",url);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intentToApp, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        nm.notify((int)System.currentTimeMillis(),notification);
    }
}
