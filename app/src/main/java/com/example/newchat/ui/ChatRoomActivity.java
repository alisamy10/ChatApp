package com.example.newchat.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;

import com.example.newchat.adapter.ChatRoomAdapter;
import com.example.newchat.adapter.RoomsAdapter;
import com.example.newchat.database.RoomMessagesDao;
import com.example.newchat.database.model.RoomMessage;
import com.example.newchat.database.model.Room;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
public class ChatRoomActivity extends BaseActivity implements View.OnClickListener {
    private Room room;
    private RecyclerView mRecMessage;
    private ImageView mBtnSend;
    private EditText mMessInput;
    private RoomMessage message;
    private ChatRoomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initView();
        initRecView();
        room = (Room) getIntent().getSerializableExtra("room");
        listenForChat();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(room.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void listenForChat() {
        RoomMessagesDao.getMessages(room.getId(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("firestore", "Listen failed.", e);
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            RoomMessage roomMessage =dc.getDocument().toObject(RoomMessage.class);
                            adapter.insertItem(roomMessage);
                            break;
                    }
                }

            }
        });

    }



    private void initRecView() {
        adapter = new ChatRoomAdapter();
        mRecMessage.setLayoutManager(new LinearLayoutManager(this));
        mRecMessage.setAdapter(adapter);

    }

    private void initView() {
        mRecMessage =  findViewById(R.id.message_rec);
        mBtnSend =  findViewById(R.id.send_btn);
        mBtnSend.setOnClickListener(this);
        mMessInput =  findViewById(R.id.input_mess);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if(mMessInput.getText().toString().trim().isEmpty())
                    return;
                else{
                    message=new RoomMessage();
                   message.setContent(mMessInput.getText().toString().trim());
                   message.setSenderID(DataUtil.currentUser.getId());
                   message.setSenderName(DataUtil.currentUser.getName());
                   message.setRoomId(room.getId());

                   message.setDate(new Date());

                    RoomMessagesDao.addMessage(message, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                mMessInput.setText("");
                        }
                    });


                }
                break;
            default:
                break;
        }
    }
}
