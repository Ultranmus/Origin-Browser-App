package com.example.origin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookmarksActivity extends AppCompatActivity implements Recycler_view_class.ItemClickListener{
    RecyclerView recyclerViewBookmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        recyclerViewBookmark=findViewById(R.id.recyclerViewBookmark);
        recyclerViewBookmark.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        BookmarkDbHelper bookmarkDbHelper=new BookmarkDbHelper(this);


        ArrayList<Model> arrayList=bookmarkDbHelper.FetchBookmark();
        Recycler_view_class recycler_view_class=new Recycler_view_class(this,arrayList,this);
        recyclerViewBookmark.setAdapter(recycler_view_class);
    }

    @Override
    public void onItemClickListener(String emUrl) {
        Intent intent=new Intent();
        intent.putExtra("BookUrl",emUrl);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}