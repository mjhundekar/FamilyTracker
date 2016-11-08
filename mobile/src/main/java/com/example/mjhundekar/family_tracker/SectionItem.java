package com.example.mjhundekar.family_tracker;

/**
 * Created by Saif on 11/7/16.
 */

public class SectionItem implements Item{

    String group_name;

    public SectionItem(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_name(){
        return group_name;
    }

    @Override
    public boolean isSection() {
        return true;
    }

}
