package com.example.mjhundekar.family_tracker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;


public class EditGroupActivity extends Activity implements OnItemClickListener{

    private String alertDialogSelect="";
    EntryAdapter adapter;
    ListView listview=null;
    HashMap<String,String> group_member = new HashMap<>();
    String deleteGroup;
    private String add_member_group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        for(int i = 0 ; i< GroupsActivity.group_items.size();i++) {
            if (GroupsActivity.group_items.get(i).isSection()) {
                SectionItem sectionItem = (SectionItem) GroupsActivity.group_items.get(i);
                System.out.println(sectionItem.getGroup_name());
            } else {
                EntryItem entryItem = (EntryItem) GroupsActivity.group_items.get(i);
                System.out.println(entryItem.group_name + "-" + entryItem.member_name + "-" + entryItem.isAdmin);
            }
        }
        listview=(ListView)findViewById(R.id.listView_main);

        adapter = new EntryAdapter(this, GroupsActivity.group_items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {

        final Item item = GroupsActivity.group_items.get(position);
        if(item!=null) {
            if (item.isSection()) {
                final SectionItem group_seperator = (SectionItem) item;
                //final SectionItem group_seperator = (SectionItem) GroupsActivity.group_items.get(position);
                deleteGroup = group_seperator.getGroup_name().toLowerCase();
                add_member_group = group_seperator.getGroup_name();
                Toast.makeText(this, "You clicked " + group_seperator.getGroup_name(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditGroupActivity.this);
                builderSingle.setTitle("Select Action for group " + group_seperator.getGroup_name());

                builderSingle.setNegativeButton(
                        "Delete Group",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteGroup(group_seperator);
                                dialog.dismiss();
                            }

                            private void DeleteGroup(SectionItem group_seperator) {

                                GroupsActivity.group_names.remove(deleteGroup);
                                ArrayList<Item> group_itemBO = GroupsActivity.group_items;
                                Iterator<Item> itr = group_itemBO.iterator();
                                while (itr.hasNext()){
                                    Item i = itr.next();
                                    if(i.isSection()){
                                        SectionItem sectionItem = (SectionItem) i;
                                        String section_group = sectionItem.getGroup_name().toLowerCase();
                                        if (deleteGroup.equals(section_group)) {
                                            //System.out.println("Group name matched");
                                            itr.remove();
                                        }
                                    }
                                    else {
                                        EntryItem entryItem = (EntryItem) i;
                                        String item_group = entryItem.group_name.toLowerCase();
                                        if (deleteGroup.equals(item_group)) {
                                            //System.out.println("Group name matched");
                                            itr.remove();
                                            GroupsActivity.group_names.remove(entryItem.group_name);
                                        }

                                    }
                                }
                                //System.out.println("SIZE->>>>>>>>"+group_itemBO.size());
                                adapter.notifyDataSetChanged();
                            }
                        });

                builderSingle.setPositiveButton("Add member", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int i) {
                        dialog.dismiss();
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(EditGroupActivity.this);
                        final ArrayAdapter<String> arrayAdapter_of_left_members = new ArrayAdapter<String>(
                                EditGroupActivity.this,
                                android.R.layout.select_dialog_singlechoice);
                        System.out.println("----> friends list");
                        ArrayList<String> friends_list = new ArrayList<String>();
                        for(int j = 0; j< HomeActivity.friends.size();j++){
                                friends_list.add(HomeActivity.friends.get(j).getFriend_name());
                                System.out.println(HomeActivity.friends.get(j).getFriend_name());
                        }

                        for(int k = 0 ; k< GroupsActivity.group_items.size();k++) {
                            if (!GroupsActivity.group_items.get(k).isSection()) {
                                EntryItem entryItem = (EntryItem) GroupsActivity.group_items.get(k);
                                if(friends_list.contains(entryItem.member_name)){
                                    System.out.println(entryItem.member_name+ "<--removed");
                                    friends_list.remove(entryItem.member_name);
                                }
                            }
                        }
                        System.out.println("-------------------------");
                        for (String a : friends_list){
                            arrayAdapter_of_left_members.add(a);
                            System.out.println(a);
                        }



                        builderInner.setTitle("Select member to Add");
                        builderInner.setSingleChoiceItems(arrayAdapter_of_left_members,-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialogSelect = arrayAdapter_of_left_members.getItem(i);

                            }
                        });
                        builderInner.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for(int z = 0 ; z< GroupsActivity.group_items.size();z++) {
                                    if (GroupsActivity.group_items.get(z).isSection()) {
                                        SectionItem sectionItem = (SectionItem) GroupsActivity.group_items.get(z);
                                        if(sectionItem.getGroup_name().equals(add_member_group)){
                                            GroupsActivity.group_items.add(z+2,new EntryItem(alertDialogSelect,add_member_group,""));
                                            break;
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        });
                        builderInner.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();


            } else {
                final EntryItem group_item = (EntryItem) item;

                Toast.makeText(this, "You clicked " + group_item.group_name, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditGroupActivity.this);
                builderSingle.setTitle("Select Action for member " + group_item.member_name);

                builderSingle.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteMember(group_item);
                                dialog.dismiss();
                            }

                            private void DeleteMember(EntryItem item) {
                                String grp_name = item.group_name;
                                String member_name = item.member_name;
                                ArrayList<Item> group_itemBO = GroupsActivity.group_items;
                                for (int i = 0; i < GroupsActivity.group_items.size(); i++) {
                                    if (!group_itemBO.get(i).isSection()) {
                                        EntryItem list_item = (EntryItem) group_itemBO.get(i);
                                        if (list_item.group_name.equals(grp_name) && list_item.member_name.equals(member_name)) {
                                            group_itemBO.remove(i);
                                            break;
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

                builderSingle.setPositiveButton(
                        "Make Admin",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {

                                MakeAdmin(group_item);
                                dialog.dismiss();
                            }

                            private void MakeAdmin(EntryItem item) {
                                String grp_name = item.group_name;
                                String member_name = item.member_name;
                                ArrayList<Item> group_itemBO = GroupsActivity.group_items;
                                for (int i = 0; i < GroupsActivity.group_items.size(); i++) {
                                    if (!group_itemBO.get(i).isSection()) {
                                        EntryItem list_item = (EntryItem) group_itemBO.get(i);
                                        if (list_item.group_name.equals(grp_name) && list_item.member_name.equals(member_name)) {
                                            list_item.isAdmin = "Admin";
                                            break;
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                builderSingle.show();
            }
        }


    }


}
