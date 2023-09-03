package com.example.origin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler_view_class extends RecyclerView.Adapter<Recycler_view_class.ViewHolder> {
    Context context;
    ArrayList<Model> arrayList;
    private final ItemClickListener itemClickListener;
    public Recycler_view_class(Context context,ArrayList<Model> arrayList,ItemClickListener itemClickListener){
        this.context=context;
        this.arrayList=arrayList;
        this.itemClickListener=itemClickListener;
    }


    @NonNull
    @Override
    public Recycler_view_class.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false);
        return new ViewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_view_class.ViewHolder holder, int position) {
        holder.recycler_view_textView.setText(arrayList.get(position).url);
        holder.recycler_view_textView.setOnClickListener(view -> itemClickListener.onItemClickListener(arrayList.get(position).url));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recycler_view_textView;
        LinearLayout recycler_view_ll;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView,ItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener=itemClickListener;
            recycler_view_textView=itemView.findViewById(R.id.recycler_view_textView);
            recycler_view_ll=itemView.findViewById(R.id.recycler_view_ll);

        }
    }

    public interface ItemClickListener{
        void onItemClickListener(String emUrl);
    }
}

