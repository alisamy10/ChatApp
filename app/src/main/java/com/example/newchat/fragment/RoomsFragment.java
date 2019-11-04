package com.example.newchat.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.newchat.Base.BaseFragment;
import com.example.newchat.R;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.example.newchat.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsFragment extends BaseFragment {
    RecyclerView recyclerView;
    RoomsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar ;
    FirebaseAuth auth;


    public RoomsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        progressBar = view.findViewById(R.id.progress_bar);

        recyclerView= view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RoomsAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




        return view;
    }
    List<Room> rooms;
    public void getAllRooms(){
        UsersDao.getRooms(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    rooms=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Room room =document.toObject(Room.class);
                        rooms.add(room);
                    }
                    adapter.changeData(rooms);

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
        getAllRooms();

    }



}
