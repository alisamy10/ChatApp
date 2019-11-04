package com.example.newchat.fragment;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.newchat.Base.BaseFragment;
import com.example.newchat.R;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.adapter.UsersAdapter;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.example.newchat.database.model.User;
import com.example.newchat.helper.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FriendsFragment extends BaseFragment {
    RecyclerView recyclerView;
    UsersAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar ;
    private SwipeRefreshLayout swipeRefreshLayout;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_friends, container, false);
        progressBar = view.findViewById(R.id.progress_bar);

        recyclerView= view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new UsersAdapter(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light ));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                       getAllUsers();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

    enableSwipeToDeleteAndUndo();
        return view;
    }
    List<User> users;
    public void getAllUsers(){
        UsersDao.getUsers(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    users=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User room =document.toObject(User.class);
                        users.add(room);
                    }
                    adapter.setList(users);

                }
                else
                {
                    showMessage(task.getException().getLocalizedMessage(),"OK");
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        getAllUsers();

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

                showMessage("delete", "ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        UsersDao.deleteUser(adapter.getNote(viewHolder.getAdapterPosition()), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                getAllUsers();
                                dialog.dismiss();

                            }
                        });


                    }
                }, "no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getAllUsers();
                        dialog.dismiss();



                    }
                },true);



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


}
