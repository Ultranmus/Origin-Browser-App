package com.example.origin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements Recycler_view_class.ItemClickListener {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        DbHelper dbHelper=new DbHelper(HistoryActivity.this);


        ArrayList<Model> arrayList=dbHelper.arrayListFetch();
        Recycler_view_class recycler_view_class=new Recycler_view_class(HistoryActivity.this,arrayList,this);
        recyclerView.setAdapter(recycler_view_class);

    }

    @Override
    public void onItemClickListener(String emUrl) {
        Intent intent=new Intent();
        intent.putExtra("hisUrl",emUrl);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}