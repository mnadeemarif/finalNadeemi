package com.csgradqau.finalexamsectionb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.csgradqau.finalexamsectionb.data.user;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION =400;
    // Database Name
    private static final String DATABASE_NAME = "users_db";



    public DatabaseHelper(Context contex) {
        super(contex,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(user.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + user.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }



    public user getUser(String username, String password) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(user.TABLE_NAME,
                new String[]{user.COLUMN_USERNAME, user.COLUMN_PASSWORD},
                user.COLUMN_USERNAME + "=? and " + user.COLUMN_PASSWORD + "=?",
                new String[]{String.valueOf(username), String.valueOf((password))}, null, null, null, null);

        if (cursor != null && cursor.getCount() != 0)
            cursor.moveToFirst();

        // prepare note object
        user u = new user();
        u.setName(cursor.getString(cursor.getColumnIndex(user.COLUMN_NAME)));
        u.setPassword(cursor.getString(cursor.getColumnIndex(user.COLUMN_PASSWORD)));
        // close the db connection
        cursor.close();

        return u;
    }

    public user getUserProfile(user u) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(user.TABLE_NAME,
                new String [] {user.COLUMN_IMAGE, user.COLUMN_NAME, user.COLUMN_SECTOR, user.COLUMN_GENDER, user.COLUMN_DOJ, user.COLUMN_type},
                user.COLUMN_ID + "=?",
                new String[]{String.valueOf(u.id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() != 0)
            cursor.moveToFirst();

        // prepare note object
        user user = new user();
//        user.setImage(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_IMAGE)));
//        user.setName(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_NAME)));
//        user.setHobbies(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_HOBBIES)));
//        user.setGender(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_GENDER)));
//        user.setDob(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_DOB)));
//        user.setAge(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_AGE)));
        // close the db connection
        cursor.close();

        return user;
    }

    public List<user> getAllUser() {
        List<user> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + user.TABLE_NAME + " ORDER BY " +
                user.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                user user = new user();
//                user.setId(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_ID)));
//                user.setImage(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_IMAGE)));
//                user.setName(cursor.getString(cursor.getColumnIndex(UsersDetail.COLUMN_NAME)));

                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return users;
    }
//
//    public int getNotesCount() {
//        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//
//        // return count
//        return count;
//    }
//
//    public int updateNote(Note note) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(Note.COLUMN_NOTE, note.getNote());
//
//        // updating row
//        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(note.getId())});
//    }
//
//    public void deleteNote(Note note) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(note.getId())});
//        db.close();
//    }
}
