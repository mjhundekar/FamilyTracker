package com.example.mjhundekar.family_tracker;


import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] friend_name;
    TypedArray menuIcons;
    String[] friend_address;
    HashMap<Integer,String> hashMap = new HashMap<>();
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
            hashMap.put(i,friend_address[i]);
            String[] location = friend_address[i].split(",");
            FriendBO items = new FriendBO(friend_name[i], menuIcons.getResourceId(
                    i, -1),new LatLng(Double.parseDouble(location[0]),Double.parseDouble(location[1])));
            friends.add(items);
        }


        adapter = new MyAdapter(getActivity(), friends);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String loc = hashMap.get(position);
        String message = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(loc.split(",")[0]),
                    Double.parseDouble(loc.split(",")[1]), 1);
            if(addresses != null && addresses.size()!=0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strReturnedAddress.append(returnedAddress.getCountryName());
                message = strReturnedAddress.toString();
            }
            else{
                message = "No Address";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
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
