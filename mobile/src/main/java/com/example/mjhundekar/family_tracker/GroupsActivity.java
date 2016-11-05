package com.example.mjhundekar.family_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupsActivity extends Activity {

    AutoCompleteTextView text;
    MultiAutoCompleteTextView text1;
    HashMap<Integer,String> friends_map = new HashMap<Integer, String>();
    static HashMap<String,ArrayList<String>> groups = new HashMap<>();
    EditText group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        for(int i = 0; i<HomeActivity.friend_name.length;i++){
            friends_map.put(i,HomeActivity.friend_name[i]);
        }
        ArrayAdapter group_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, HomeActivity.friend_name);
        ListView listView = (ListView) findViewById(R.id.list_of_friends);
        listView.setAdapter(group_adapter);

        text1=(MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView1);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,HomeActivity.friend_name);
        text1.setAdapter(adapter);
        text1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();

                //Toast.makeText(getBaseContext(), text1.getText().toString()+item, Toast.LENGTH_LONG).show();
                text1.setText(text1.getText().toString()+item+", ");
            }
        });
    }

    public void addGroupButtonHandler(View view) {
        String members[] = text1.getText().toString().split(", ");
        ArrayList<String> member = new ArrayList<>();
        for(String m : members){
            if(!m.equals(""))
                member.add(m);
        }
        group_name = (EditText) findViewById(R.id.group_name);
        groups.put(group_name.getText().toString(),member);
        Log.v("Hashmap in Group",groups.size()+"");
        /*HashMap<String,ArrayList<String>> existing_group = GroupBO.getGroup_list();
        existing_group.put(group_name.getText().toString(),member);
        groupBO.setGroup_list(existing_group);*/
        Log.v("Group","Group created");
        text1.setText("");
        group_name.setText("");


    }



}
