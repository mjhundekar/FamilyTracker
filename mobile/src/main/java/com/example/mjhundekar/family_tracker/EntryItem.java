package com.example.mjhundekar.family_tracker;

/**
 * Created by Saif on 11/7/16.
 */

public class EntryItem implements Item{

    String member_name;
    String group_name;
    String isAdmin;

    public EntryItem(String member_name, String group_name, String isAdmin) {
        this.member_name = member_name;
        this.group_name = group_name;
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}
