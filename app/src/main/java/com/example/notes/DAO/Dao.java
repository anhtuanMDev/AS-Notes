package com.example.notes.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.example.notes.Interface.TestPasser;
import com.example.notes.Model.NotesModel;
import com.example.notes.SQLite.SQlite;

import java.util.ArrayList;
import java.util.Random;

public class Dao {
    Context context;
    SQlite sQlite;

    public Dao(Context context, SQlite sQlite) {
        this.context = context;
        this.sQlite = sQlite;
    }

    public void insertNote(String title, String content, String colorText, String colorBackground) {
        SQLiteDatabase database = sQlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("colorText", colorText);
        values.put("colorBackground", colorBackground);
        database.insert("notes", null, values);
    }

    public ArrayList<NotesModel> getNotes() {
        SQLiteDatabase database = sQlite.getReadableDatabase();
        ArrayList<NotesModel> note = new ArrayList<>();
        Cursor cursor = database.rawQuery("select title, content, colorText, colorBackground from notes", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                note.add(new NotesModel(cursor.getString(0), cursor.getString(1), cursor.getString(3), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return note;
    }

    public void deleted(String title) {
        SQLiteDatabase database = sQlite.getReadableDatabase();
        Cursor cursor = database.rawQuery("select title = ? from notes", new String[]{title});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            database.delete("notes", "title = ?", new String[]{title});
        }
        cursor.close();
    }

    public void editNotes(String oldTitle, String title, String content, String textColor, String backgroundColor) {
        SQLiteDatabase database = sQlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("colorText", textColor);
        values.put("colorBackground", backgroundColor);
        database.update("notes", values, "title = ?", new String[]{oldTitle});
    }

    public ArrayList<NotesModel> getColors(String title){
        SQLiteDatabase database = sQlite.getReadableDatabase();
        ArrayList<NotesModel> note = new ArrayList<>();
        Cursor cursor = database.rawQuery("select colorText, colorBackground from notes", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                note.add(new NotesModel(cursor.getString(1), cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return note;
    }

    public String generateRandomHexColor() {
        Random random = new Random();
        // Sử dụng hàm Random để tạo giá trị RGB ngẫu nhiên
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // Sử dụng lớp Color để chuyển đổi giá trị RGB thành màu hex
        int color = Color.rgb(red, green, blue);
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public String findTextColor(String backgroundColor) {
        // Chuyển màu nền từ chuỗi hex thành giá trị màu RGB
        int bgColor = Color.parseColor(backgroundColor);

        // Tính giá trị độ sáng của màu nền (luma)
        double luma = 0.299 * Color.red(bgColor) + 0.587 * Color.green(bgColor) + 0.114 * Color.blue(bgColor);

        // Chọn màu chữ dựa trên giá trị luma
        if (luma > 128) {
            return "#000000"; // Nền sáng, chọn màu chữ đen
        } else {
            return "#FFFFFF"; // Nền tối, chọn màu chữ trắng
        }
    }

}
