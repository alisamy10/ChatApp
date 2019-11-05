package com.example.newchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchat.R;
import com.example.newchat.database.model.Room;
import com.example.newchat.database.model.User;

import java.util.List;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    private List<Room> rooms;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.name.setText(room.getName());
        holder.desc.setText(room.getDes());
    }
    public Room getNote(int position){

        Room todo = rooms.get(position);
        return todo;
    }

    @Override
    public int getItemCount() {
        return rooms==null? 0 :rooms.size();
    }
    public void changeData(List<Room> items){
        rooms=items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,desc;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);

        }
    }
}
