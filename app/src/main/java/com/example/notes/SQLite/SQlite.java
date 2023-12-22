package com.example.notes.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQlite extends SQLiteOpenHelper {
    public SQlite(@Nullable Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String notes ="create table notes (" +
                "stt integer primary key autoincrement," +
                "title varchar(200)," +
                "content text," +
                "colorText text," +
                "colorBackground text)";
        db.execSQL(notes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists notes");
        onCreate(db);
    }
}
