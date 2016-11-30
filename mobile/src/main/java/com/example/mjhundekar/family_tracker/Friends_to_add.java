package com.example.mjhundekar.family_tracker;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by umanggala on 11/30/16.
 */

public class Friends_to_add {

    public void add_friend(final String email_id) {
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String friendKey = "";
                        //System.out.println("Children in home 6");
                        HashMap<String, Object> friendDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                        for (DataSnapshot task : dataSnapshot.getChildren()) {
                            Log.v("email_", task.getKey());
                            friendKey = task.getKey().toString();
                        }

                        friendDetails.put(friendKey, email_id);
                        HashMap<String, Object> myDetails = new HashMap<String, Object>();
                        myDetails.put(HomeActivity.uid, HomeActivity.email);
                        FirebaseDatabase.getInstance().getReference().child("friends").child(HomeActivity.uid).updateChildren(friendDetails);
                        FirebaseDatabase.getInstance().getReference().child("friends").child(friendKey).updateChildren(myDetails);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
