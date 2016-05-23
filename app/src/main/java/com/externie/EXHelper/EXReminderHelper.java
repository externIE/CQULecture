package com.externie.EXHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.externie.EXReceiver.EXNotificationReceiver;

import java.util.Calendar;

/**
 * Created by externIE on 16/5/19.
 */
public class EXReminderHelper {
    private Context mCtx;

    public EXReminderHelper(Context ctx) {
        mCtx = ctx;
    }

    public void addReminder(String title,String date, String time, String address,String url, long execTime) {
        AlarmManager am = (AlarmManager) mCtx.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, execTime, buildPI(title,date, time, address,url));
    }

    public void addReminder(String title, String date, String time, String address,String url) {
        addReminder(title, date, time, address,url, calExecTime(date, time));
    }

    public void addReminderTest(String title,String date, String time, String address,String url){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,15);
        addReminder(title,date, time, address,url, calendar.getTimeInMillis());
    }

    private long calExecTime(String date, String time) {
        String[] str_dates = date.split("/");
        String[] str_times = time.split(":");
        int dyear = Integer.valueOf("20" + str_dates[0]);
        int dmonth = Integer.valueOf(str_dates[1]);
        int ddate = Integer.valueOf(str_dates[2]);
        int dhour = Integer.valueOf(str_times[0]);
        int dmin = Integer.valueOf(str_times[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(dyear, dmonth, ddate, dhour, dmin);
        return calendar.getTimeInMillis();
    }

    private int getPIFlag(String date,String time){
        String[] str_dates = date.split("/");
        String[] str_times = time.split(":");
        int dyear = Integer.valueOf("20" + str_dates[0]);
        int dmonth = Integer.valueOf(str_dates[1]);
        int ddate = Integer.valueOf(str_dates[2]);
        int dhour = Integer.valueOf(str_times[0]);
        int dmin = Integer.valueOf(str_times[1]);
        return dmin+dhour*100+ddate*10000+dmonth+dyear;
    }

    private PendingIntent buildPI(String title,String date, String time, String address,String url) {
        String content = "时间：" + time + " 地点：" + address;
        Intent intent = new Intent(title);
        intent.setAction("com.externie.EXReceiver.EXNotificationReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(EXNotificationReceiver.TICKER, "您有一门讲座即将开始");
        intent.putExtra(EXNotificationReceiver.TITLE, title);
        intent.putExtra(EXNotificationReceiver.CONTENT, content);
        intent.putExtra(EXNotificationReceiver.URL,url);
        intent.setClass(mCtx, EXNotificationReceiver.class);
        int flag = getPIFlag(date,time);
        return PendingIntent.getBroadcast(mCtx, flag, intent, 0);
    }


    public void cancelRemander(String title,String date,String time, String address){
        AlarmManager am = (AlarmManager) mCtx.getSystemService(Context.ALARM_SERVICE);
        am.cancel(buildPI(title,date,time,address,""));
    }
}
