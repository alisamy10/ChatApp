package com.example.newchat.database;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyDataBase {
    private static FirebaseFirestore myDataBase;
    public static final String USER_REF="users";
    public static final String Room_REF="rooms";

    public static  FirebaseFirestore getInsatnce(){

        if(myDataBase==null)
           myDataBase=FirebaseFirestore.getInstance();
        return  myDataBase;

    }
    public static CollectionReference getUserReference(){
        return getInsatnce().collection(USER_REF);
    }
    public static CollectionReference getRoomReference(){
        return getInsatnce().collection(Room_REF);
    }

}
