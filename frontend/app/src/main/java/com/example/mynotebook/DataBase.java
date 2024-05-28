package com.example.mynotebook;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notes.db";
    public static final int DBVERSION = 1;
    private SQLiteDatabase _myDB;

    public class Notes {
        protected String Title, Subtitle, Description, Date;
        protected int ID;
        public Notes(){
            Title=""; Subtitle=""; Description=""; ID=0; Date = "";
        }
        public Notes(String Title, String Subtitle, String Description, String Date,int ID){
            this.Title = Title; this.Subtitle=Subtitle; this.Description=Description;
            this.ID = ID; this.Date = Date;

        }
        public String getTitle(){
            return Title;
        }
        public int getID(){
            return ID;
        }
        public String getSubtitle(){
            return Subtitle;
        }
        public String getDescription(){
            return Description;
        }

        public String getDate(){ return Date; }

    }

    public static String DATABASE_CREATE = "" +
            "CREATE TABLE notes(" +
            "ID integer PRIMARY KEY AUTOINCREMENT, " +
            "Title text not null, " +
            "Subtitle text not null, " +
            "Description text not null, " +
            "Date text not null"+
            ")";


    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DBVERSION);
        _myDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if(i1>i){
            sqLiteDatabase.execSQL("DROP TABLE notes; ");
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }
    }


    public long insert(String Title, String Subtitle, String Description, String Date){
        ContentValues values = new ContentValues();
        values.put("Title", Title);
        values.put("Subtitle", Subtitle);
        values.put("Description", Description);
        values.put("Date", Date);

        return _myDB.insert(
                "notes",null,values

                );


//        Log.e("insert","in"+ _myDB.rawQuery("SELECT last_insert_rowid()", new String[]{}).moveToFirst());

    }

//      "INSERT INTO notes(Title, Subtitle, Description, Date) " +
//              "VALUES(?, ?, ?, ?)",
//              new Object[]{
//        Title, Subtitle, Description, Date
//
//    }
//                );


    public void update(String Title, String Subtitle, String Description, String Date, int ID){
        _myDB.execSQL(
                "UPDATE notes SET Title = ?, Subtitle = ?, Description = ?, Date = ?" +
                        "WHERE ID = ? ",
                new Object[]{
                        Title, Subtitle, Description, Date, ID

                }

        );
    }

    public List<Notes> select(){
        Cursor c = _myDB.rawQuery("SELECT * FROM notes ORDER BY Date DESC;", null);
        List<Notes> l = new ArrayList<>();
        while(c.moveToNext()){
            @SuppressLint("Range") Notes c1 = new Notes(
                    c.getString(c.getColumnIndex("Title")),
                    c.getString(c.getColumnIndex("Subtitle")),
                    c.getString(c.getColumnIndex("Description")),
                    c.getString(c.getColumnIndex("Date")),
                    c.getInt(c.getColumnIndex("ID"))
            );
            l.add(c1);
        }
        return l;
    }

    public void delete(int ID){
        try {
            _myDB.execSQL(
                    "DELETE FROM notes WHERE ID = ?",
                    new Object[]{ID}
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
