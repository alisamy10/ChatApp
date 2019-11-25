package com.example.newchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newchat.R;
import com.example.newchat.database.model.User;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Holder> {
    public interface OnClick{

        void onItemClick(int pos, User user);

    }
    private OnClick onClick;
    private List<User> userList = new ArrayList<>();
    public UsersAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    public User getNote(int position){

        User todo = userList.get(position);
        return todo;
    }
    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
         final User user = userList.get(position);
        holder.displayName.setText(user.getName());
        holder.displayStatus.setText(user.getStatus());
        Glide.with(holder.itemView)
                .load(user.getImage())
                .into(holder.profileImage);
        if(onClick!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onItemClick(position,user);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return userList == null ? 0 : userList.size();
    }

    public void setList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView displayName;
        private TextView displayStatus;

        private Holder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.user_single_image);
            displayName=itemView.findViewById(R.id.user_single_name);
            displayStatus=itemView.findViewById(R.id.user_single_status);

        }
    }
}
