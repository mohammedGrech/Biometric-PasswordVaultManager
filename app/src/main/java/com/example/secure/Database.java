package com.example.secure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final String TABLE_USER_INFO = "User_Info";
    public static final String TABLE_WEBSITES = "Websites";
    public static final String TABLE_RECOVERY_CODES = "Recovery_Codes";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_SURNAME = "user_surname";
    public static final String WEB_ID = "web_id";
    public static final String WEB_NAME = "web_name";
    public static final String WEB_URL = "web_url";
    public static final String WEB_username = "web_username";
    public static final String WEB_PASSWORD = "web_password";
    public static final String WEB_NOTE = "web_note";
    public static final String CODE_ID = "code_id";
    public static final String CODE_CODE = "code_code";

    public Database(@Nullable Context context) {
        super(context, "secure.db", null, 1);
    }

    // this is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER_INFO + " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " TEXT," + USER_SURNAME + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_WEBSITES + " (" + WEB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WEB_NAME + " TEXT," + WEB_URL + " TEXT," + WEB_username + " TEXT," + WEB_PASSWORD + " TEXT," + WEB_NOTE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_RECOVERY_CODES + " (" + CODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " INTEGER," + CODE_CODE + " TEXT)");

    }

    //This is called if the database version changes.
    // It prevents previous users apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Adds a website record to the "Websites" table in the database
    public boolean addRecord(WebsiteModel websiteModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(WEB_NAME, websiteModel.getName());
        cv.put(WEB_URL, websiteModel.getUrl());
        cv.put(WEB_username, websiteModel.getusername());
        cv.put(WEB_PASSWORD, websiteModel.getPassword());
        cv.put(WEB_NOTE, websiteModel.getNote());

        long insert = db.insert(TABLE_WEBSITES, null, cv);
        if (insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    // Gets all website records from the "Websites" table
    public List<WebsiteModel> getAllWebsites(){

        List<WebsiteModel> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_WEBSITES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            // loop through the cursor (result set) and create new customer objects, Put them into the retuning list.
            do{
                int websiteID = cursor.getInt(0);
                String websiteName = cursor.getString(1);
                String websiteURL = cursor.getString(2);
                String websiteusername = cursor.getString(3);
                String websitePassword = cursor.getString(4);
                String websiteNote = cursor.getString(5);

                WebsiteModel newWebsite = new WebsiteModel(websiteID,websiteName,websiteURL,websiteusername,websitePassword,websiteNote);
                list.add(newWebsite);
            }while (cursor.moveToNext());
        }
        else{
            // failure. do not add anything to the list.
        }
        //Close both the cursor and the dv when done.
        cursor.close();
        db.close();
        return list;
    }

    /* Deletes a website record from the "Websites" table */
    public boolean deleteRecord(int webID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_WEBSITES + " WHERE " + WEB_ID + " = " + webID;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        }
        else{
            cursor.close();
            db.close();
            return false;
        }
    }

}
