package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    RecyclerView recyclerView;
    RoomsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        initRecyclerView();


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat app");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AddRoomActivity.class));


            }
        });
    }
    public void initRecyclerView(){
        recyclerView= findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RoomsAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


FirebaseAuth auth;
    @Override
    protected void onStart() {
        super.onStart();
        getAllRooms();
        /*auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

         */
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){


            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();

        }

        if(item.getItemId() == R.id.main_settings_btn){



        }

        return true;
    }
}
