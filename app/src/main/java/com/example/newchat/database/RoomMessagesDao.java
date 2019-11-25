package com.example.newchat.database;

import com.example.newchat.database.model.RoomMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class RoomMessagesDao {
    public static void addMessage(RoomMessage message, OnCompleteListener<Void> onCompletionListener){
        DocumentReference document =MyDataBase.getRoomMessageReference().document();
        message.setId(document.getId());
        document.set(message).addOnCompleteListener(onCompletionListener);
    }
    public static void getMessages(String roomId, EventListener<QuerySnapshot> eventListener){
        MyDataBase.getRoomMessageReference().whereEqualTo("roomId",roomId).orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(eventListener);
    }

}
