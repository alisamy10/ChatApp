package com.example.newchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileActivity extends BaseActivity {
    private FirebaseAuth auth;
    private String name, status , image ,user_id;
    private ImageView mImageProfile;
    private TextView mDisplayNameProfile , mStatusProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent2 = getIntent();

        Bundle bd2 = intent2.getExtras();
        if(bd2!=null)
        {
            if("current".equals(bd2.get("flag"))) {
                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null)
                getCurrentUser(auth.getCurrentUser().getUid());
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            if("myfriend".equals(bd.get("flag"))) {
                user_id = (String) bd.get("id");
                if(user_id!=null)
                getCurrentUser(user_id);
            }
        }



        mImageProfile = findViewById(R.id.profile_image);
        mDisplayNameProfile = findViewById(R.id.profile_displayName);
        mStatusProfile =  findViewById(R.id.profile_status);

       showProgressDialog("Loading User Data","Please wait while we load the user data.",false);

    }


    private void getCurrentUser(String userId) {
            UsersDao.getCurrentUser(userId , new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataUtil.currentUser = task.getResult().toObject(User.class);
                        name = task.getResult().getString("name");
                        status = task.getResult().getString("status");
                        mDisplayNameProfile.setText(name);
                        mStatusProfile.setText(status);
                        image=task.getResult().getString("image");
                        Glide.with(ProfileActivity.this)
                                .load(image)
                                .into(mImageProfile);
                        hideProgressDialog();
                    }
                }
            });
        }
    }


