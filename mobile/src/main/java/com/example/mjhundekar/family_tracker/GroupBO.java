package com.example.mjhundekar.family_tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saif on 11/5/16.
 */

public class GroupBO {

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    private String group_name;
    private String member_name;
    private boolean isAdmin;

}
