package com.example.newchat.database;


import com.example.newchat.database.model.Room;
import com.example.newchat.database.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class UsersDao {
    public static void addUser(User user, OnCompleteListener<Void> onCompletionListener){
        MyDataBase.getUserReference().document(user.getId()).set(user).addOnCompleteListener(onCompletionListener);
    }
    //read user once
    public static void getCurrentUser(String userId, OnCompleteListener<DocumentSnapshot> onCompletionListener){
       MyDataBase.getUserReference().document(userId).get().addOnCompleteListener(onCompletionListener);
    }

    public static void getUsers(OnCompleteListener<QuerySnapshot> onCompletionListener){
        MyDataBase.getUserReference().get().addOnCompleteListener(onCompletionListener);
    }

    public static void deleteUser(User user, OnCompleteListener<Void> onCompleteListener){
        MyDataBase.getUserReference().document(user.getId()).delete().addOnCompleteListener(onCompleteListener);
    }
    public static void updateUser(String userId,String name , String phone , String image , String status, OnCompleteListener<Void> onCompleteListener){
        MyDataBase.getUserReference().document(userId).update("name",name,"status",status,"image",image,"phone",phone).addOnCompleteListener(onCompleteListener);
    }

}
