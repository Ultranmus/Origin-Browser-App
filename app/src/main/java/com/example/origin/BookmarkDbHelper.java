package com.example.origin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BookmarkDbHelper extends SQLiteOpenHelper {
    public static final  String BOOKMARK_DATABASE_NAME="browserBookmark.db";
    public static final  int BOOKMARK_DATABASE_VERSION=100;
    public static final  String BOOKMARK_TABLE_NAME="Table_Name";
    public static final  String BOOKMARK_TABLE_URL="URL";
    public static final  String BOOKMARK_TABLE_ID="ID";

    public BookmarkDbHelper( Context context) {
        super(context, BOOKMARK_DATABASE_NAME, null, BOOKMARK_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table " +BOOKMARK_TABLE_NAME + "("+BOOKMARK_TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+BOOKMARK_TABLE_URL+" TEXT UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF exists  "+BOOKMARK_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void AddUrlBookmark(String url){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(BOOKMARK_TABLE_URL,url);
        db.insert(BOOKMARK_TABLE_NAME,null,values);
        db.close();
    }

        public ArrayList<Model> FetchBookmark(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from "+BOOKMARK_TABLE_NAME+" order by "+BOOKMARK_TABLE_ID+" desc",null);
        ArrayList<Model> arrayList=new ArrayList<>();
        while(cursor.moveToNext()){
            Model model=new Model();
            model.id=cursor.getInt(0);
            model.url=cursor.getString(1);
            arrayList.add(model);
        }
        cursor.close();
        db.close();
        return arrayList;
    }
}
