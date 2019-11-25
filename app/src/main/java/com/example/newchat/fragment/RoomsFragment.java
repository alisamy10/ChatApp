package com.example.newchat.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.newchat.Base.BaseFragment;
import com.example.newchat.R;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.database.RoomsDao;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.example.newchat.helper.SwipeToDeleteCallback;
import com.example.newchat.ui.AddRoomActivity;
import com.example.newchat.ui.ChatRoomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RoomsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Room> rooms;


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
        enableSwipeToDeleteAndUndo();
            adapter.setOnClick(new RoomsAdapter.OnClick() {
                @Override
                public void onItemClick(int pos, Room room) {
                    Intent intent =new Intent(getContext(), ChatRoomActivity.class);
                    //intent.putExtra("id",room.getId());
                    //intent.putExtra("name",room.getName());
                    intent.putExtra("room",room);
                    startActivity(intent);
                }
            });


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddRoomActivity.class));


            }
        });
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

                        getAllRooms();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        return view;
    }
    private void getAllRooms(){
        RoomsDao.getRooms(new OnCompleteListener<QuerySnapshot>() {
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
                    showMessage(task.getException().getLocalizedMessage(),"OK");
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        getAllRooms();

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                showMessage("delete", "ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        RoomsDao.deleteRoom(adapter.getNote(viewHolder.getAdapterPosition()), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getAllRooms();
                                dialog.dismiss();
                            }
                        });
                    }
                }, "no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getAllRooms();
                        dialog.dismiss();
                    }
                },true);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
