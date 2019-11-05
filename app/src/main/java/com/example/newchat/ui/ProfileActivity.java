package com.example.newchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    String name, status;
    private ImageView mImageProfile;
    private TextView mDisplayNameProfile;
    private TextView mStatusProfile;
public static String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getUserOnce();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageProfile = findViewById(R.id.profile_image);
        mDisplayNameProfile = findViewById(R.id.profile_displayName);
        mStatusProfile =  findViewById(R.id.profile_status);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserOnce();
    }
    private void getUserOnce() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            UsersDao.getCurrentUser(auth.getCurrentUser().getUid() , new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataUtil.currentUser = task.getResult().toObject(User.class);
                        name = task.getResult().getString("name");
                        status = task.getResult().getString("status");
                        userId =task.getResult().getString("id");
                        mDisplayNameProfile.setText(name);
                        mStatusProfile.setText(status);
                        Toast.makeText(ProfileActivity.this,task.getResult().getString("name")+" "+task.getResult().getString("status") , Toast.LENGTH_SHORT).show();

                    } else {

                    }
                }
            });
        } else {

        }
    }

}
