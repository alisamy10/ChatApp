package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegBtnStart;
    private Button mLoginBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mRegBtnStart = findViewById(R.id.start_reg_btn);
        mRegBtnStart.setOnClickListener(this);
        mLoginBtnStart = findViewById(R.id.start_login_btn);
        mLoginBtnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_reg_btn:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.start_login_btn:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                // TODO 19/11/04
                break;
            default:
                break;
        }
    }


}

