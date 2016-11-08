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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupsActivity extends Activity {

    AutoCompleteTextView text;
    MultiAutoCompleteTextView text1;
    static HashMap<String,ArrayList<String>> groups = new HashMap<>();
    static Set<String> group_names = new HashSet<>();
    EditText group_name;
    static int number_of_groups = 0;
    static ArrayList<Item> group_items = new ArrayList<Item>();

    static ArrayList<GroupBO> group_details = new ArrayList<GroupBO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

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

        number_of_groups++;
        String members[] = text1.getText().toString().split(", ");
        ArrayList<String> member = new ArrayList<>();
        group_name = (EditText) findViewById(R.id.group_name);
        String grp_name = group_name.getText().toString();
        group_names.add(grp_name);
        group_items.add(new SectionItem(group_name.getText().toString()));
        GroupBO groupBO_user = new GroupBO();
        groupBO_user.setAdmin(true);
        groupBO_user.setMember_name(HomeActivity.user_name);
        groupBO_user.setGroup_name(grp_name);
        group_details.add(groupBO_user);
        group_items.add(new EntryItem("Me",group_name.getText().toString(),"Admin"));
        for(String m : members){
            GroupBO groupBO = new GroupBO();
            if(!m.equals("")) {
                groupBO.setAdmin(false);
                groupBO.setGroup_name(group_name.getText().toString());
                groupBO.setMember_name(m);
                group_details.add(groupBO);
                group_items.add(new EntryItem(m,group_name.getText().toString(),""));
            }
        }
        /*for(int i = 0 ; i< group_items.size();i++){
            if(group_items.get(i).isSection()){
                SectionItem sectionItem = (SectionItem) group_items.get(i);
                System.out.println(sectionItem.getGroup_name());
            }
            else {
                EntryItem entryItem = (EntryItem) group_items.get(i);
                System.out.println(entryItem.group_name+"-"+entryItem.member_name+"-"+entryItem.isAdmin);
            }
        }*/

        //groups.put(group_name.getText().toString(),member);
        Log.v("Hashmap in Group",groups.size()+"");
        /*HashMap<String,ArrayList<String>> existing_group = GroupBO.getGroup_list();
        existing_group.put(group_name.getText().toString(),member);
        groupBO.setGroup_list(existing_group);*/
        Log.v("Group","Group created");
        text1.setText("");
        group_name.setText("");
        Toast.makeText(this,"Group Created: "+grp_name,Toast.LENGTH_SHORT).show();

    }



}
