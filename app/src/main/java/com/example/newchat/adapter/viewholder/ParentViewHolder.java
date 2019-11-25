package com.example.newchat.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchat.R;

public class ParentViewHolder extends RecyclerView.ViewHolder {
        public   TextView content , date;
    public ParentViewHolder(@NonNull View itemView) {
        super(itemView);
        content=itemView.findViewById(R.id.content);
        date=itemView.findViewById(R.id.date);
    }
}
