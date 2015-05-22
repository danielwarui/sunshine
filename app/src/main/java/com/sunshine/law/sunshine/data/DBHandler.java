package com.sunshine.law.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by law on 05/22/2015.
 */
public class DBHandler extends SQLiteOpenHelper{

    final static String dbName = "sunshine";
    final static int dbVersion = 1;

    SQLiteDatabase database;

    public DBHandler(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists sunshine(" +
                "date text not null," +
                "weather text not null," +
                "maxtemp int not null," +
                "mintemp int not null)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    Insert data to DB
     */
    public void insertWeatherData(String date, String weather, int maxTemp, int minTemp){
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("weather", weather);
        values.put("maxtemp", maxTemp);
        values.put("mintemp", minTemp);
        database.insert("sunshine", null, values);
    }

    /*
    Retrieve Data from DB
     */
    public List<String[]> getWeatherData(){
        List<String[]> list = new ArrayList<>();
        database = getReadableDatabase();
        String select = "select * from sunshine";

        Cursor cursor = database.rawQuery(select, null);
        while(cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String weather = cursor.getString(cursor.getColumnIndex("weather"));
            String maxTemp = cursor.getString(cursor.getColumnIndex("maxtemp"));
            String minTemp = cursor.getString(cursor.getColumnIndex("mintemp"));
            list.add(new String[]{date, weather, maxTemp, minTemp});
        }
        return list;
    }
}
