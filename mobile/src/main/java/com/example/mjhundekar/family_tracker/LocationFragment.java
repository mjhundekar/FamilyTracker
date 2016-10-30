package com.example.mjhundekar.family_tracker;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] friend_name;
    TypedArray menuIcons;
    String[] friend_address;

    MyAdapter adapter;
    private List<FriendBO> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_location, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        friend_name = getResources().getStringArray(R.array.friend_names);
        menuIcons = getResources().obtainTypedArray(R.array.icons);
        friend_address = getResources().getStringArray(R.array.friend_addresses);
        friends = new ArrayList<FriendBO>();

        for (int i = 0; i < friend_name.length; i++) {
            FriendBO items = new FriendBO(friend_name[i], menuIcons.getResourceId(
                    i, -1),friend_address[i]);

            friends.add(items);
        }

        adapter = new MyAdapter(getActivity(), friends);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Toast.makeText(getActivity(), friend_address[position], Toast.LENGTH_SHORT)
                .show();

    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
    }
    */
}
