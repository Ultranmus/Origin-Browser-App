package com.example.origin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public static final  String DATABASE_NAME="browser.db";
    public static final  int DATABASE_VERSION=1;
    public static final  String TABLE_NAME="Table_Name";
    public static final  String TABLE_URL="URL";
    public static final  String TABLE_ID="ID";

    public DbHelper( Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " +TABLE_NAME + "("+TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+TABLE_URL+" TEXT UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF exists  "+TABLE_NAME);
        onCreate(db);
    }
    public void AddUrl(String url){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TABLE_URL,url);
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

        public ArrayList<Model> arrayListFetch(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from "+TABLE_NAME+" order by "+TABLE_ID+" DESC",null);
        ArrayList<Model> arraylist=new ArrayList<>();
        while (cursor.moveToNext()){
            Model model=new Model();
            model.id=cursor.getInt(0);
            model.url=cursor.getString(1);
            arraylist.add(model);
        }
        cursor.close();
        db.close();
        return arraylist;
    }
    public void deleteUrl(String url){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,TABLE_URL+" = '"+url,null);
        db.close();
    }
    public void check(String url){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+" where "+TABLE_URL+" = '" +url,null);
        if (cursor!=null){
            deleteUrl(url);
            AddUrl(url);
        }else{
            AddUrl(url);
        }
        cursor.close();
        db.close();
    }


}
