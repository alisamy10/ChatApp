package com.example.newchat.database;

import android.media.MediaPlayer;

import com.example.newchat.database.model.Room;
import com.example.newchat.database.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

public class UsersDao {
    public static void addUser(User user, OnCompleteListener<Void> onCompletionListener){
        MyDataBase.getUserReference().document(user.getId()).set(user).addOnCompleteListener(onCompletionListener);
    }
    //read user once
    public static void getCurrentUser(String userId, OnCompleteListener<DocumentSnapshot> onCompletionListener){
       MyDataBase.getUserReference().document(userId).get().addOnCompleteListener(onCompletionListener);

    }
    public static void addRoom(Room room, OnCompleteListener<Void> onCompletionListener){
        DocumentReference document =MyDataBase.getRoomReference().document();
        room.setId(document.getId());
        document.set(room).addOnCompleteListener(onCompletionListener);
    }
    public static void getRooms(OnCompleteListener<QuerySnapshot> onCompletionListener){
        MyDataBase.getRoomReference().get().addOnCompleteListener(onCompletionListener);

    }
}