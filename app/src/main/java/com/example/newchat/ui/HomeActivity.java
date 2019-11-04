package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.example.newchat.fragment.FriendsFragment;
import com.example.newchat.fragment.ProfileFragment;
import com.example.newchat.fragment.RoomsFragment;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
                    case 2:
                        fragment = new ProfileFragment();
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
        if(item.getItemId() == R.id.main_deactive_btn){
            auth=FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

             FirebaseFirestore myDataBase;
            myDataBase=FirebaseFirestore.getInstance();

            myDataBase.collection("users").document(auth.getCurrentUser().getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "User account deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();


        }

        if(item.getItemId() == R.id.main_settings_btn){

        }
        return true;
    }


}
