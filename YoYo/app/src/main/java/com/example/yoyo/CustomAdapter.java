package com.example.yoyo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    List<Responses> allresponse;
    Context context;

    public CustomAdapter(List<Responses> allresponse, Context context) {
        this.allresponse = allresponse;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if(allresponse.get(position).person_or_bot) {
            return R.layout.person_layout;
        }
        //}else{
            return R.layout.bot_layout;
        //}
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setText(allresponse.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return allresponse.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessage);
        }
    }
}
