package com.example.mjhundekar.family_tracker;


import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class EditGroupActivity extends Activity implements OnItemClickListener{


    ListView listview=null;
    HashMap<String,String> group_member = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        listview=(ListView)findViewById(R.id.listView_main);

        EntryAdapter adapter = new EntryAdapter(this, GroupsActivity.group_items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {

        EntryItem item = (EntryItem)GroupsActivity.group_items.get(position);
        Toast.makeText(this, "You clicked " + item.group_name, Toast.LENGTH_SHORT).show();
    }
}
