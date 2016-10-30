package com.example.mjhundekar.family_tracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

    public LocationFragment(){

    }

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
    public void onItemClick(AdapterView<?> parent, View view, final int position,
                            long id) {

        final String loc = hashMap.get(position);
        ConvertFromLocationToAddress convert = new ConvertFromLocationToAddress(getContext(),loc.split(",")[0],loc.split(",")[1]);
        String address = convert.getAddress();

        final Dialog dialog = new Dialog(getContext());
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        // set values for custom dialog components - text, image and button
        dialog.show();

        Button location_button = (Button) dialog.findViewById(R.id.LocationButton);
        // if decline button is clicked, close the custom dialog
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GoogleMap mMap = HomeActivity.mMap;
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc.split(",")[0]), Double.parseDouble(loc.split(",")[1])), 16.0f));
                }
                // Close dialog

            }
        });

        Button direction_button = (Button) dialog.findViewById(R.id.directionButton);
        // if decline button is clicked, close the custom dialog
        direction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location current_position = HomeActivity.updated_location;
                dialog.dismiss();

                String current_latitude = current_position.getLatitude()+"";
                String current_longitude = current_position.getLongitude()+"";
                String destination_latitude = loc.split(",")[0];
                String destination_longitude = loc.split(",")[1];
                Log.v("Location","https://maps.google.com?saddr=Current+Location" +
                "&daddr="+destination_latitude+","+destination_longitude);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com?saddr=Current+Location" +
                        "&daddr="+destination_latitude+","+destination_longitude));

                startActivity(browserIntent);
                //Close dialog

            }
        });
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
