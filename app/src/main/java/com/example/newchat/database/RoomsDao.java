package com.example.newchat.database;

import com.example.newchat.database.model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public class RoomsDao {
    public static void addRoom(Room room, OnCompleteListener<Void> onCompletionListener){
        DocumentReference document =MyDataBase.getRoomReference().document();
        room.setId(document.getId());
        document.set(room).addOnCompleteListener(onCompletionListener);
    }
    public static void getRooms(OnCompleteListener<QuerySnapshot> onCompletionListener){
        MyDataBase.getRoomReference().get().addOnCompleteListener(onCompletionListener);
    }
    public static void deleteRoom(Room room, OnCompleteListener<Void> onCompleteListener){
        MyDataBase.getRoomReference().document(room.getId()).delete().addOnCompleteListener(onCompleteListener);
    }
}
