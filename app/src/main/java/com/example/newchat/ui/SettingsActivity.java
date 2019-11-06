package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mImageSettings;
    private EditText mUserNameSet , mProfileStatusSet;

    private Button mSettingsButtonUpdate;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        getUserOnce();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initView() {
        mImageSettings =  findViewById(R.id.settings_image);
        mImageSettings.setOnClickListener(this);
        mUserNameSet =  findViewById(R.id.set_user_name);
        mProfileStatusSet =  findViewById(R.id.set_profile_status);
        mSettingsButtonUpdate =  findViewById(R.id.update_settings_button);
        mSettingsButtonUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_image:
                // TODO 19/11/06
                break;
            case R.id.update_settings_button:
                String name=mUserNameSet.getText().toString().trim();
                String status =mProfileStatusSet.getText().toString().trim();
                if(isValidForm( name ,status))
                updateUser(name,status);
                break;
            default:
                break;
        }
    }

    private boolean isValidForm(String name, String status) {
        boolean isValid=true;
        if(name.isEmpty()){
            mUserNameSet.setError("Required");
            isValid=false;
        }
        else{
            mUserNameSet.setError(null);
            isValid=true;
        }
        if(status.isEmpty()){
            mProfileStatusSet.setError("Required");
            isValid=false;
        }
        else{
            mProfileStatusSet.setError(null);
            isValid=true;
        }
        return  isValid;

    }

    private void updateUser(String name , String status) {
        FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        DocumentReference washingtonRef = firebaseFirestore.collection("users").document(auth.getCurrentUser().getUid());

        washingtonRef.update("name", name,"status",status)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getUserOnce() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            UsersDao.getCurrentUser(auth.getCurrentUser().getUid() , new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataUtil.currentUser = task.getResult().toObject(User.class);

                        mUserNameSet.setText(task.getResult().getString("name"));
                        mProfileStatusSet.setText(task.getResult().getString("status"));

                    }
                }
            });
        }
    }
}
