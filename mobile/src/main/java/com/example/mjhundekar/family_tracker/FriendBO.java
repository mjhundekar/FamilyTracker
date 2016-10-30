package com.example.mjhundekar.family_tracker;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Saif on 10/29/16.
 */

public class FriendBO {
    private String friend_name;
    private LatLng loc;
    private int icon;

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public LatLng getLoc() {
        return loc;
    }

    public void setLoc(LatLng loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return loc.latitude+","+loc.longitude;
    }

    public FriendBO(String friend_name, int icon, LatLng loc) {
        this.loc = loc;
        this.icon = icon;
        this.friend_name= friend_name;

    }





}

