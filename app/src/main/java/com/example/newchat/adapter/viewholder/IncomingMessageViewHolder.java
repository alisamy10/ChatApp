package com.example.newchat.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.newchat.R;

public class IncomingMessageViewHolder extends  ParentViewHolder {
    public TextView senderName;

    public IncomingMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        senderName=itemView.findViewById(R.id.name);
    }
}
