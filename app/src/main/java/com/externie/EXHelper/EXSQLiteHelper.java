package com.externie.EXHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.externie.EXAdapter.EXNavListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by externIE on 16/5/18.
 */
public class EXSQLiteHelper extends SQLiteOpenHelper {
    public final static String col_title = "title";
    public final static String col_date = "exdate";
    public final static String col_time = "extime";
    public final static String col_address = "address";
    public final static String col_organizer = "organizer";
    public final static String col_coorganizer = "coorganizer";
    public final static String col_content = "content";
    public final static String col_url = "url";

    private final String TAG = "EXSQLiteHelper:";

    private final static String database_name = "externie_db";

    private final static String table_name = "collectlectures";

    private final static String sql_query = "select * from " + table_name + " order by exdate,extime ASC";

    private final String sql_create_table = "create table " + table_name +
            "(" + col_url + " varchar(140)," +
            col_title + " varchar(40)," +
            col_date + " varchar(10)," + col_time + " varchar(10)," +
            col_address + " varchar(40)," +
            col_organizer + " varchar(60)," + col_coorganizer + " varchar(60)," +
            col_content + " varchar(1000))";


    private EXSQLiteHelper(Context ctx, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(ctx, name, factory, version);
    }

    private EXSQLiteHelper(Context ctx, int version) {
        super(ctx, database_name, null, version);
    }

    /**
     * @param title       标题
     * @param date        日期
     * @param time        时间
     * @param organizer   主办方
     * @param coorganizer 协办方
     * @param content     内容
     */
    public static void storeData(Context ctx, String url, String title, String date, String time, String address, String organizer, String coorganizer, String content) {
        ContentValues cv = new ContentValues();
        cv.put(col_url, url);
        cv.put(col_title, title);
        cv.put(col_date, date);
        cv.put(col_time, time);
        cv.put(col_address, address);
        cv.put(col_organizer, organizer);
        cv.put(col_coorganizer, coorganizer);
        cv.put(col_content, content);
        EXSQLiteHelper dbHelper = new EXSQLiteHelper(ctx, database_name, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(table_name, null, cv);
        db.close();
    }

    public static void storeData(Context ctx, String url, String title, String date, String time, String address) {
        EXReminderHelper helper = new EXReminderHelper(ctx);
        helper.addReminderTest(title,date,time,address,url);
        storeData(ctx, url, title, date, time, address, "", "", "");
    }

    public static List<ContentValues> getData(Context ctx) {
        List<ContentValues> cvs = new ArrayList<>();
        EXSQLiteHelper dbHelper = new EXSQLiteHelper(ctx, database_name, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql_query, null);
        while (cursor.moveToNext()) {
            ContentValues cv = new ContentValues();
            String url = cursor.getString(cursor.getColumnIndex(col_url));
            String title = cursor.getString(cursor.getColumnIndex(col_title));
            String date = cursor.getString(cursor.getColumnIndex(col_date));
            String time = cursor.getString(cursor.getColumnIndex(col_time));
            String address = cursor.getString(cursor.getColumnIndex(col_address));
            String organizer = cursor.getString(cursor.getColumnIndex(col_organizer));
            String coorganizer = cursor.getString(cursor.getColumnIndex(col_coorganizer));
            String content = cursor.getString(cursor.getColumnIndex(col_content));
            cv.put(col_url, url);
            cv.put(col_title, title);
            cv.put(col_date, date);
            cv.put(col_time, time);
            cv.put(col_address, address);
            cv.put(col_organizer, organizer);
            cv.put(col_coorganizer, coorganizer);
            cv.put(col_content, content);
            cvs.add(cv);
        }
        db.close();
        return cvs;
    }

    public static void dumpData2Adapter(Context ctx,EXNavListAdapter adapter){
        List<ContentValues> cvs = getData(ctx);
        for (int i = 0 ; i < cvs.size() ; i++){
            ContentValues cv = cvs.get(i);
            String url = cv.getAsString(col_url);
            String title = cv.getAsString(col_title);
            String date = cv.getAsString(col_date);
            String time = cv.getAsString(col_time);
            String address = cv.getAsString(col_address);

            adapter.addItem(url,title,date,time,address);
        }
    }

    public static boolean existedData(Context ctx, String title) {
        EXSQLiteHelper dbHelper = new EXSQLiteHelper(ctx, database_name, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql_query, null);
        while (cursor.moveToNext()) {
            String dbtitle = cursor.getString(cursor.getColumnIndex(col_title));
            if (dbtitle.equals(title)) {
                db.close();
                return true;
            }
        }
        db.close();
        return false;
    }

    public static int removeData(Context ctx,String title,String date,String time,String address){
        EXReminderHelper remindhelper = new EXReminderHelper(ctx);
        remindhelper.cancelRemander(title, date, time, address);

        EXSQLiteHelper helper = new EXSQLiteHelper(ctx,database_name,null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(table_name,col_title+"=?",new String[]{title});
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_create_table);
        Log.i(TAG, "Create Table!!!!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrade Database!!!!!");
    }
}
