package com.example.newchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newchat.R;
import com.example.newchat.adapter.viewholder.IncomingMessageViewHolder;
import com.example.newchat.adapter.viewholder.OutcomingMessageViewHolder;
import com.example.newchat.adapter.viewholder.ParentViewHolder;
import com.example.newchat.database.model.RoomMessage;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ParentViewHolder> {


    private List<RoomMessage> roomMessageList;
    public static final int  INCOMING   = 100;
    public static final int  OUTCOMING  = 200;

    @Override
    public int getItemViewType(int position) {
        RoomMessage roomMessage =roomMessageList.get(position);
        if(roomMessage.getSenderID().equals(DataUtil.currentUser.getId()))
            return OUTCOMING;

            return  INCOMING;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view;
        if(viewType==INCOMING){
              view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_incoming, parent, false);
              return new IncomingMessageViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_outcoming, parent, false);
        return new OutcomingMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        RoomMessage roomMessage =roomMessageList.get(position);
        int messageType=getItemViewType(position);
        if(messageType==INCOMING){
            bindIncomingMessage(roomMessage, (IncomingMessageViewHolder) holder);

        }
        else{
              bindOutcomingMessage(roomMessage, (OutcomingMessageViewHolder) holder);
        }

    }

    private void bindIncomingMessage(RoomMessage roomMessage, IncomingMessageViewHolder holder) {

        holder.content.setText(roomMessage.getContent());
        holder.date.setText(roomMessage.formatDate());
        holder.senderName.setText(roomMessage.getSenderName());

    }
    private void bindOutcomingMessage(RoomMessage roomMessage, OutcomingMessageViewHolder holder) {
        holder.content.setText(roomMessage.getContent());
        holder.date.setText(roomMessage.formatDate());

    }
    public void setList(List<RoomMessage> roomMessageList) {
        this.roomMessageList = roomMessageList;
        notifyDataSetChanged();
    }
    public void insertItem(RoomMessage roomMessage) {
        if(roomMessageList==null)
            roomMessageList= new ArrayList<>();

        roomMessageList.add(roomMessage);
        notifyItemInserted(roomMessageList.size()-1);
    }

    @Override
    public int getItemCount() {
        return roomMessageList==null ? 0 :roomMessageList.size();
    }
}
