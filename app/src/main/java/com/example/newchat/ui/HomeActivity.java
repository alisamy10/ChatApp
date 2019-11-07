package com.example.newchat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.fragment.FriendsFragment;
import com.example.newchat.fragment.MessageFragment;
import com.example.newchat.fragment.RoomsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends BaseActivity {
    private Fragment fragment ;
    private TabLayout tabLayout;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout ;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawer);

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
                        fragment = new MessageFragment();
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
            } else {
                Toast.makeText(this, "press again to exit ", Toast.LENGTH_SHORT).show();
            }

            backPressedTime = System.currentTimeMillis();
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
            showMessage("Are You Sure ", "Deleting this account will removing your account from the system and you won't be able" +
                    "to access the app", "Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    auth=FirebaseAuth.getInstance();
                    final FirebaseUser user = auth.getCurrentUser();
                    FirebaseFirestore myDataBase;
                    myDataBase=FirebaseFirestore.getInstance();
                    myDataBase.collection("users").document(auth.getCurrentUser().getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(HomeActivity.this, "User account deleted ", Toast.LENGTH_SHORT).show();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                        Toast.makeText(HomeActivity.this, "User account deleted success", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(HomeActivity.this,MainActivity.class));
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }, "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            },true);
        }
        if(item.getItemId() == R.id.main_settings_btn)
            startActivity(new Intent(this,SettingsActivity.class));

        if(item.getItemId() == R.id.main_profile_btn)
            startActivity(new Intent(this,ProfileActivity.class));
        return true;
    }
}
