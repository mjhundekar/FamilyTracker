package com.example.mjhundekar.family_tracker;

/**
 * Created by Saif on 10/29/16.
 */

public class FriendBO {
    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_address() {
        return friend_address;
    }

    public void setFriend_address(String friend_address) {
        this.friend_address = friend_address;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private String friend_name;
    private String friend_address;
    private int icon;

    public FriendBO(String friend_name, int icon, String friend_address) {
        this.friend_address = friend_address;
        this.icon = icon;
        this.friend_name= friend_name;

    }





}

