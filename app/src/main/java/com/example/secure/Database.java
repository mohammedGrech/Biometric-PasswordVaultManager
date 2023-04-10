package com.example.secure;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

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
    public static final String WEB_LOGO = "web_logo";
    public static final String WEB_NOTE = "web_note";
    public static final String CODE_ID = "code_id";
    public static final String CODE_CODE = "code_code";

    Database(@Nullable Context context) {
        super(context, "secure.db", null, 1);
    }

    // this is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER_INFO + " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " TEXT," + USER_SURNAME + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_WEBSITES + " (" + WEB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WEB_NAME + " TEXT," + WEB_URL + " TEXT," + WEB_username + " TEXT," + WEB_PASSWORD + " TEXT," + WEB_NOTE + " TEXT," + WEB_LOGO + " TEXT)");
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
        String sql = "INSERT INTO " + TABLE_WEBSITES + " (" + WEB_NAME + ", " + WEB_URL + ", " + WEB_username + ", " + WEB_PASSWORD + ", " + WEB_NOTE + ", " + WEB_LOGO + ") VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, websiteModel.getName());
        statement.bindString(2, websiteModel.getUrl());
        statement.bindString(3, websiteModel.getusername());
        statement.bindString(4, websiteModel.getPassword());
        statement.bindString(5, websiteModel.getNote());
        statement.bindString(6, websiteModel.getWeb_logo());

        long insert = statement.executeInsert();
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
                String webLogo = cursor.getString(6);

                WebsiteModel newWebsite = new WebsiteModel(websiteID,websiteName,websiteURL,websiteusername,websitePassword,websiteNote, webLogo);
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
        String sql = "DELETE FROM " + TABLE_WEBSITES + " WHERE " + WEB_ID + " = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, webID);

        long delete = statement.executeUpdateDelete();
        if (delete == -1){
            return false;
        }
        else {
            return true;
        }

    }

    @SuppressLint("RestrictedApi")
    void updateData(Integer row_id, String name, String email, String password, String url, String note, String webLogo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "UPDATE " + TABLE_WEBSITES + " SET " + WEB_NAME + " = ?, " + WEB_URL + " = ?, " + WEB_username + " = ?, " + WEB_PASSWORD + " = ?, " + WEB_NOTE + " = ?, " + WEB_LOGO + " = ? WHERE " + WEB_ID + "=" + row_id;
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, name);
            statement.bindString(2, url);
            statement.bindString(3, email);
            statement.bindString(4, password);
            statement.bindString(5, note);
            statement.bindString(6, webLogo);

            statement.executeUpdateDelete();
        }catch (android.database.sqlite.SQLiteConstraintException exception){
            Log.d(TAG, "failure to update word,", exception);
        }
    }



}
