package com.example.mjhundekar.family_tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by umanggala on 11/22/16.
 */

public class UserBO {
    public String username;
    public String email;

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String firebase_id;
    public List<String> friend_list;

    public List<String> getFriend_list() {
        return friend_list;
    }

    public void setFriend_list(List<String> friend_list) {
        this.friend_list = friend_list;
    }

    public UserBO() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserBO(String firebase_id, String username, String email) {
        //this.username = username;
        //this.email = email;
        //this.firebase_id = firebase_id;
        //this.friend_list= new ArrayList<String>();
    }

    public void add_friend(String email_id, String firebase_id)
    {

    }


}
