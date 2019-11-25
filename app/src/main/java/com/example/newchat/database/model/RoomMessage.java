package com.example.newchat.database.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RoomMessage {
    String id , roomId,content , senderID , senderName  ;
     @ServerTimestamp
     Date date;

     public String formatDate(){
         SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("E, dd/MMMM/yyyy   HH:mm:ss a ");
         return  simpleDateFormat.format(date);
     }

    public RoomMessage() {
    }


    public RoomMessage(String id, String roomId, String content, String senderID, String senderName, Date date) {
        this.id = id;
        this.roomId = roomId;
        this.content = content;
        this.senderID = senderID;
        this.senderName = senderName;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
