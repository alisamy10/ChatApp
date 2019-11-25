package com.example.newchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newchat.R;
import com.example.newchat.database.model.Room;
import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
public interface OnClick{

    void onItemClick(int pos,Room room);

}
private OnClick onClick;
    private List<Room> rooms;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room,parent,false));
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Room room = rooms.get(position);
        holder.name.setText(room.getName());
        holder.desc.setText(room.getDes());
        if(onClick!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onItemClick(position,room);
                }
            });
        }

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
