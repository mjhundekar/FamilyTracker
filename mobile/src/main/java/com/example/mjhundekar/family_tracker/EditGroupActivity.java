package com.example.mjhundekar.family_tracker;


import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class EditGroupActivity extends ListActivity {

    private EditGroupAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new EditGroupAdapter(this);
        
        for (String key : GroupsActivity.groups.keySet()) {
            mAdapter.addSectionHeaderItem(key);
            ArrayList<String> members =  GroupsActivity.groups.get(key);
            for (int i = 0; i<members.size();i++){
                mAdapter.addItem(members.get(i));
            }
        }
        setListAdapter(mAdapter);
    }

}

