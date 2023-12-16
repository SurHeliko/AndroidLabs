package com.example.sixlab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + "("
                + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY," +
                DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT," + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addUser(UserData user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.GetLogin());
        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.GetPassword());

        db.insert(DBContract.UserEntry.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<UserData> GetAllUsers() {
        ArrayList<UserData> usersList = new ArrayList<UserData>();
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserData user= new UserData();
                user.SetId(Integer.parseInt(cursor.getString(0)));
                user.SetLogin(cursor.getString(1));
                user.SetPassword(cursor.getString(2));
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        db.close();

        return usersList;
    }

    public UserData SignIn(String login, String password)
    {
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " = '"+ login + "' AND " + DBContract.UserEntry.COLUMN_NAME_PASS + " = '" + password + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount()!=1)
            return null;

        UserData user= new UserData();

        if (cursor.moveToFirst())
        {
            user.SetId(Integer.parseInt(cursor.getString(0)));
            user.SetLogin(cursor.getString(1));
            user.SetPassword(cursor.getString(2));

            db.close();

            return user;
        }

        db.close();

        return null;
    }

    public boolean DeleteUser(String login, String password)
    {
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " = '"+ login + "' AND " + DBContract.UserEntry.COLUMN_NAME_PASS + " = '" + password + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount()==0)
        {
            db.close();

            return false;
        }

        db.delete(DBContract.UserEntry.TABLE_NAME, DBContract.UserEntry.COLUMN_NAME_LOGIN +"=?",  new String[]{login});

        db.close();

        return true;
    }

    public boolean IsLoginAvailable(String login)
    {
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " = '" +login + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount()==0)
        {
            db.close();

            return true;
        }

        db.close();

        return false;
    }

    public void UpdateUserData(String login, String newPassword)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBContract.UserEntry.COLUMN_NAME_PASS, newPassword);

        int updCount = db.update(DBContract.UserEntry.TABLE_NAME, cv, DBContract.UserEntry.COLUMN_NAME_LOGIN + " = ?", new String[] { login });

        db.close();
    }
}

