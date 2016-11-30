package com.example.mjhundekar.family_tracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.telephony.TelephonyManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.fragment;
import static android.content.Context.TELEPHONY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] friend_name;
    TypedArray menuIcons;
    String[] friend_address;
    HashMap<Integer,String> hashMap = new HashMap<>();
    MyAdapter adapter;
    private List<FriendBO> friends = new ArrayList<>();
    private DatabaseReference mdatabase =FirebaseDatabase.getInstance().getReference();
    ArrayList<String> friends_fb = new ArrayList<>();
    HashMap<String,FriendBO> mapFriendBO = new HashMap<>();
    ArrayList<String> friends_email = new ArrayList<>();


    public LocationFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getListView().setBackgroundColor(Color.WHITE);
        return inflater.inflate(R.layout.fragment_location,null, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mdatabase.child("friends").child(HomeActivity.uid).orderByChild(HomeActivity.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot task : dataSnapshot.getChildren()) {

                    String friendvalue = (String) task.getValue();
                    friends_email.add(friendvalue);
                    mdatabase.child("users").orderByChild("email").equalTo(friendvalue).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //System.out.println("Saifalikaredia "+dataSnapshot.toString());
                            for (DataSnapshot task : dataSnapshot.getChildren()) {

                                HashMap<String,Object> friendDetails = (HashMap<String, Object>) task.getValue();
                                //System.out.println("Saifalikaredia "+friendDetails.get("username"));
                                HashMap<String,Object> loc = (HashMap<String, Object>) friendDetails.get("location");
                                String friend_name_fb = friendDetails.get("username").toString();
                                if(friend_name_fb!=null) {
                                    FriendBO BO = new FriendBO();
                                    BO.setFriend_name(friend_name_fb);
                                    if((double)loc.get("latitude")!=0.0 && (double)loc.get("latitude")!=0.0)
                                        BO.setLoc(new LatLng((double)loc.get("latitude"),(double)loc.get("longitude")));
                                    //System.out.println("Friends "+ friends_fb.toString());
                                    new DownloadImageFriend(friend_name_fb,BO).execute(friendDetails.get("photoUrl").toString());
                                    friends.add(BO);
                                }
                            }

                            adapter = new MyAdapter(getActivity(), friends);
                            setListAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mdatabase.child("users").orderByChild("location").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                HashMap<String,Object> friendsLocation = (HashMap<String, Object>) dataSnapshot.getValue();
                //System.out.println("Saifalikaredia " + friendsLocation.get("username"));
                String friends_ans = friendsLocation.get("username").toString();
                HashMap<String,Object> loc = (HashMap<String, Object>) friendsLocation.get("location");
                //System.out.println("Saifalikaredia " + loc.get("latitude"));

                if(friends.size()>0)
                    friends.clear();
                int count = 0;
                for (Map.Entry<String, FriendBO> entry : mapFriendBO.entrySet()) {
                    FriendBO BO = entry.getValue();
                    if(BO.getFriend_name().equals(friends_ans)){
                        BO.setLoc(new LatLng((double) loc.get("latitude"), (double) loc.get("longitude")));
                    }
                    friends.add(BO);
                    hashMap.put(count, BO.getLoc().latitude + "," + BO.getLoc().longitude);
                    count++;
                }
                if(adapter!=null)
                    adapter.notifyDataSetChanged();
                else {
                    adapter = new MyAdapter(getActivity(), friends);
                    setListAdapter(adapter);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdatabase.child("friends").child(HomeActivity.uid).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String friendvalue = (String) dataSnapshot.getValue();
                friends_email.add(friendvalue);

                mdatabase.child("users").orderByChild("email").equalTo(friendvalue).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //System.out.println("Saifalikaredia "+dataSnapshot.toString());

                        for (DataSnapshot task : dataSnapshot.getChildren()) {

                            HashMap<String,Object> friendDetails = (HashMap<String, Object>) task.getValue();
                            //System.out.println("Saifalikaredia "+friendDetails.get("username"));
                            HashMap<String,Object> loc = (HashMap<String, Object>) friendDetails.get("location");
                            String friend_name_fb = friendDetails.get("username").toString();
                            if(friend_name_fb!=null&& !mapFriendBO.containsKey(friend_name_fb)) {
                                FriendBO BO = new FriendBO();
                                BO.setFriend_name(friend_name_fb);
                                BO.setLoc(new LatLng((double)loc.get("latitude"),(double)loc.get("longitude")));
                                //System.out.println("Friends "+ friends_fb.toString());
                                new DownloadImageFriend(friend_name_fb,BO).execute(friendDetails.get("photoUrl").toString());
                                friends.add(BO);
                            }
                        }

                        adapter = new MyAdapter(getActivity(), friends);
                        setListAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("Children I want this 2");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            }

        });

    }
    private class DownloadImageFriend extends AsyncTask<String, Void, Bitmap> {
        String friend;
        FriendBO BO;
        DownloadImageFriend(String friend,FriendBO BO){
            this.friend = friend;
            this.BO = BO;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.v("url", urldisplay);
            Bitmap mIcon11 = null;
            Bitmap resizedBitmap1 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                resizedBitmap1 = Bitmap.createScaledBitmap(mIcon11, 150, 150, false);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return resizedBitmap1;
        }

        protected void onPostExecute(Bitmap result) {

            BO.setIcon(result);
            mapFriendBO.put(friend,BO);

        }
    }


}