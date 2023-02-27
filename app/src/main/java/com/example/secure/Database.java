package com.example.secure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.nio.channels.WritableByteChannel;
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
    public static final String WEB_USERNAME = "web_username";
    public static final String WEB_PASSWORD = "web_password";
    public static final String WEB_NOTE = "web_note";
    public static final String CODE_ID = "code_id";
    public static final String CODE_CODE = "code_code";

    public Database(@Nullable Context context) {
        super(context, "secure.db", null, 1);
    }

    /*
    Create statements for the first time a database is created
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER_INFO + " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " TEXT," + USER_SURNAME + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_WEBSITES + " (" + WEB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WEB_NAME + " TEXT," + WEB_URL + " TEXT," + WEB_USERNAME + " TEXT," + WEB_PASSWORD + " TEXT," + WEB_NOTE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_RECOVERY_CODES + " (" + CODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " INTEGER," + CODE_CODE + " TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_WEBSITES);
        onCreate(db);

    }

    /*
    Adds a website record to the "Websites" table in the database
    */
    public boolean addRecord(WebsiteModel websiteModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(WEB_NAME, websiteModel.getName());
        cv.put(WEB_URL, websiteModel.getName());
        cv.put(WEB_USERNAME, websiteModel.getUsername());
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
    /*
    //Code for the add record button:
    names in [] should be changed to what the EditText variables are called (removing the [])


        WebsiteModel websiteModel;
        try{
            websiteModel = new WebsiteModel(-1, [Website Name].getText().toString(), [Website URL].getText().toString(),
            [Website Username/Email].getText().toString(), [Website Password].getText().toString(), [Website Note].getText().toString());
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,"Error creating this record",Toast.LENGTH_SHORT.show());
        }

        DataBaseHelper databasehelper = new DataBaseHelper(MainActivity.this);

        boolean success = databaseHelper.addRecord(websiteModel);

        // Just a check to see if the record has been added, can be removed after testing
        Toast.makeText(MainActivity.this,"Record Added = " + success,Toast.LENGTH_SHORT.show());
     */


    /*
    Deletes a website record from the "Websites" table
     */
    public boolean deleteRecord(WebsiteModel websiteModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_WEBSITES + " WHERE " + WEB_ID + " = " + websiteModel.getId();

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

    /*
    Gets all website records from the "Websites" table
     */
    public List<WebsiteModel> getAllWebsites(){

        List<WebsiteModel> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_WEBSITES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int websiteID = cursor.getInt(0);
                String websiteName = cursor.getString(1);
                String websiteURL = cursor.getString(2);
                String websiteUsername = cursor.getString(3);
                String websitePassword = cursor.getString(4);
                String websiteNote = cursor.getString(5);

                WebsiteModel newWebsite = new WebsiteModel(websiteID,websiteName,websiteURL,websiteUsername,websitePassword,websiteNote);
                list.add(newWebsite);
            }while (cursor.moveToNext());
        }
        else{

        }

        cursor.close();
        db.close();
        return list;

    }

    /*
    //Code to show all the website records
    :
        DataBaseHelper databasehelper = new DataBaseHelper(MainActivity.this);
        List<WebsiteModel> allRecords = databasehelper.getAllWebsites();

        ArrayAdapter is needed to show the array in list view
     */
}
