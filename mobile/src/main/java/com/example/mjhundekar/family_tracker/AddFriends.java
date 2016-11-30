package com.example.mjhundekar.family_tracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class AddFriends extends Activity {

    EditText get_name;
    EditText get_surname;
    EditText get_email_id;
    EditText get_phone_number;
    String friend_id;
    private DatabaseReference mDatabase;
    public String friend_username;
    public String friend_email;
    public String abcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        get_name = (EditText) findViewById(R.id.name);
        get_surname = (EditText) findViewById(R.id.surname);
        get_email_id = (EditText) findViewById(R.id.emailid);
        get_phone_number = (EditText) findViewById(R.id.phonenumber);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        Button b1 = (Button) findViewById(R.id.add_friend);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friend_email = get_email_id.getText().toString().toLowerCase();
                FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(friend_email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String friendKey = "";
                                //System.out.println("Children in home 6");
                                HashMap<String,Object> friendDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                                for (DataSnapshot task : dataSnapshot.getChildren()) {
                                    Log.v("email_",task.getKey());
                                    friendKey = task.getKey().toString();
                                }

                                friendDetails.put(friendKey,friend_email);
                                HashMap<String,Object> myDetails = new HashMap<String, Object>();
                                myDetails.put(HomeActivity.uid,HomeActivity.email);
                                FirebaseDatabase.getInstance().getReference().child("friends").child(HomeActivity.uid).updateChildren(friendDetails);
                                FirebaseDatabase.getInstance().getReference().child("friends").child(friendKey).updateChildren(myDetails);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



            }
        });
    }
}
