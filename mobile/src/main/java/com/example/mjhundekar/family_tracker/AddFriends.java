package com.example.mjhundekar.family_tracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddFriends extends AppCompatActivity {

    EditText get_name;
    EditText get_surname;
    EditText get_email_id;
    EditText get_phone_number;
    String friend_id;

    private DatabaseReference mDatabase;


    public String username;
    public String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        get_name = (EditText) findViewById(R.id.name);
        get_surname = (EditText) findViewById(R.id.surname);
        get_email_id = (EditText) findViewById(R.id.emailid);
        get_phone_number = (EditText) findViewById(R.id.phonenumber);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        Button b1 = (Button) findViewById(R.id.add_friend);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(get_email_id.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {


                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.v("Email_name",dataSnapshot.toString());
                                //List<String> friend = dataSnapshot.getch("friend_list");
                                UserBO friend = dataSnapshot.getValue(UserBO.class);
                                friend_id = friend.getFirebase_id();
                                ArrayList<String> a = (ArrayList<String>) GoogleSignIn.userBO.getFriend_list();
                                a.add(friend_id);

                                mDatabase.child("users").child(GoogleSignIn.userBO.firebase_id).child("friend_list").push().setValue(a);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



            }
        });


    }


}
