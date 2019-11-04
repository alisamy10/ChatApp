package com.example.newchat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.Room;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AddRoomActivity extends BaseActivity implements View.OnClickListener {

    private EditText mName;
    private EditText mDesc;
    private Button mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initView() {
        mName = (EditText) findViewById(R.id.name);
        mDesc = (EditText) findViewById(R.id.desc);
        mAdd = (Button) findViewById(R.id.add);
        mAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                String name =mName.getText().toString().trim();
                String des=mDesc.getText().toString().trim();
                if(isValidForm(name ,des))
                {
                    addRoom(name,des);
                }
                break;
            default:
                break;
        }
    }

    private void addRoom(String name, String des) {
        final Room room =new Room();
       room.setDes(des);
       room.setName(name);
        showProgressDialog("please wait...");
        UsersDao.addRoom(room, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if(task.isSuccessful()){
                    //DataUtil.currentUser=user;
                    showMessage("room added succesful ", "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                           finish();
                        }
                    },false);

                }
                else
                    showMessage(task.getException().getLocalizedMessage(),"OK");
            }
        });


    }

    private boolean isValidForm(String name, String des) {
        boolean isValid=true;
        if(name.isEmpty())
        {
            mName.setError("Required");
            isValid=false;

        }
        else{
            mName.setError(null);
            isValid=true;
        }
        if(des.isEmpty()){

            mDesc.setError("Required");
            isValid=false;

        }
        else{
            mDesc.setError(null);
            isValid=true;
        }
        return isValid;
    }
}
