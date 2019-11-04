package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.example.newchat.fragment.FriendsFragment;
import com.example.newchat.fragment.RoomsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    Fragment fragment ;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat app");

        tabLayout=findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new FriendsFragment();
                        break;
                    case 1:
                        fragment = new RoomsFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        tabLayout.getTabAt(0).select();

        fragment=new FriendsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();


    }



FirebaseAuth auth;
    @Override
    protected void onStart() {
        super.onStart();

        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }


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
