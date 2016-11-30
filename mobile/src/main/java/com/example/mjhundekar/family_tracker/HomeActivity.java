package com.example.mjhundekar.family_tracker;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.google.firebase.messaging.FirebaseMessaging;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,ResultCallback<Status>,GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    static String user_name = "";
    static GoogleMap mMap;
    int cccc = 0;
    Boolean flag = true;
    private LocationFragment location_fragment;
    private TextView user_address;
    static String[] friend_name;
    TypedArray menuIcons;
    String[] friend_address;
    static List<FriendBO> friends;
    static List<String> friends_fb = new ArrayList<>();
    Marker UserMarker;
    static Location updated_location;
    protected static final String TAG = "HomeActivity";
    ListView listViewGroup = null;
    String group_name = "ALL";
    double dragLat;
    double dragLong;
    Bitmap resizedBitmap;
    Circle c1 = null;
    boolean add_more_geofence_restrictor = false;
    private SeekBar seeker;
    static int progress = 500;
    String memberForGeofence = "";
    private DatabaseReference mdatabase;
    static String email;
    static String uid;
    HashMap<String,Marker> MarkerMap = new HashMap<>();
    HashMap<String,LatLng> LocationMap = new HashMap<>();
    static HashMap<String,Bitmap> ImageMap = new HashMap<>();
    static List<String> friends_email = new ArrayList<>();

    private int mHour, mMinute;
    Dialog dialog_set_time;
    Dialog add_friends;
    EditText start_time;
    EditText end_time;

    EditText add_friends_email;


    //ArrayList<Marker> markerList;

    /**
     * The list of geofences used in this sample.
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;

    // Buttons for kicking off the process of adding or removing geofences.
    //private Button mAddGeofencesButton;
    //private Button mRemoveGeofencesButton;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buildGoogleApiClient();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        // Get the UI widgets.
        //mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        //mRemoveGeofencesButton = (Button) findViewById(R.id.remove_geofences_button);

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
        //setButtonsEnabledState();

        // Get the geofences used. Geofence data is hard coded in this sample.

        location_fragment = ((LocationFragment) getSupportFragmentManager().findFragmentById(R.id.location_fragment));
        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("email");
        //userBO.setEmail(email);
        user_name = bundle.getString("name");
        //userBO.setUser_name(user_name);
        String photo = bundle.getString("photo");
        uid = bundle.getString("uid");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        ImageView image = (ImageView) header.findViewById(R.id.profile_pic);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView email_id = (TextView) header.findViewById(R.id.email);
        name.setText(user_name);
        email_id.setText(email);


        System.out.println("url------->" + photo);
        //------------------MAP CODE-----------------
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);


        //-------------------------------------------


        //getSupportFragmentManager().findFragmentById(R.id.location_fragment);
        //getSupportFragmentManager().beginTransaction()
        //        .add(R.id.location_fragment, new LocationFragment(),"LocationFragment")
        //        .commit();


        ((TextView) location_fragment.getView().findViewById(R.id.user_name)).setText(user_name);

        user_address = ((TextView) location_fragment.getView().findViewById(R.id.user_location));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                add_friends.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new DownloadImageTask(image)
                .execute(photo);
        new DownloadImageTask(((ImageView) location_fragment.getView().findViewById(R.id.user_photo)))
                .execute(photo);

        friend_name = getResources().getStringArray(R.array.friend_names);
        menuIcons = getResources().obtainTypedArray(R.array.icons);
        friend_address = getResources().getStringArray(R.array.friend_addresses);
        friends = new ArrayList<FriendBO>();
        /*for (int i = 0; i < friend_name.length; i++) {
            String[] location = friend_address[i].split(",");
            FriendBO items = new FriendBO(friend_name[i], menuIcons.getResourceId(
                    i, -1), new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])));

            friends.add(items);
        }
        */
        //String friends = mdatabase.getKey();
        mdatabase.child("friends").child(uid).orderByChild(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Children in home 1");
                for (DataSnapshot task : dataSnapshot.getChildren()) {

                    String friendemail = (String) task.getValue();
                    friends_email.add(friendemail);
                    //System.out.println("Saifalikaredia "+friendvalue);
                    mdatabase.child("users").orderByChild("email").equalTo(friendemail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //System.out.println("Saifalikaredia "+dataSnapshot.toString());
                            System.out.println("Children in home 2");
                            for (DataSnapshot task : dataSnapshot.getChildren()) {

                                HashMap<String,Object> friendDetails = (HashMap<String, Object>) task.getValue();
                                //System.out.println("Saifalikaredia "+friendDetails.get("username"));
                                HashMap<String,Object> loc = (HashMap<String, Object>) friendDetails.get("location");
                                String friend_name_fb = friendDetails.get("username").toString();
                                if(friend_name_fb!=null && !friends_fb.contains(friend_name_fb)) {
                                    friends_fb.add(friend_name_fb);

                                        //System.out.println("Friends "+ friends_fb.toString());
                                        new DownloadImageFriend(friend_name_fb).execute(friendDetails.get("photoUrl").toString());
                                        System.out.println("norealtime " + loc.get("latitude"));
                                        if((double)loc.get("latitude")!=0 && (double)loc.get("latitude")!=0){
                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(new LatLng((double) (loc.get("latitude")), (double) loc.get("longitude")))
                                                .title(friend_name_fb);

                                        if (ImageMap.containsKey(friend_name_fb))
                                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ImageMap.get(friend_name_fb)));
                                        Marker marker = mMap.addMarker(markerOptions);
                                        MarkerMap.put(friend_name_fb, marker);

                                    }


                                }
                                //System.out.println("Saifalikaredia " + loc.get("latitude"));

                                //String friendvalue = (String) task.getValue();
                                //System.out.println("Saifalikaredia "+friendvalue);

                            }
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
        mdatabase.child("friends").child(uid).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String friendvalue = (String) dataSnapshot.getValue();
                    System.out.println("Saifalikaredia--> 1"+friendvalue);
                    mdatabase.child("users").orderByChild("email").equalTo(friendvalue).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //System.out.println("Saifalikaredia "+dataSnapshot.toString());
                            System.out.println("Saifalikaredia---> 2"+ dataSnapshot.toString());

                            for (DataSnapshot task : dataSnapshot.getChildren()) {

                                HashMap<String,Object> friendDetails = (HashMap<String, Object>) task.getValue();
                                //System.out.println("Saifalikaredia "+friendDetails.get("username"));
                                HashMap<String,Object> loc = (HashMap<String, Object>) friendDetails.get("location");
                                String friend_name_fb = friendDetails.get("username").toString();
                                if(friend_name_fb!=null && !friends_fb.contains(friend_name_fb)) {
                                    friends_fb.add(friend_name_fb);
                                    System.out.println("Saifalikaredia---> 3 EXCEPTION"+ friend_name_fb);
                                    //System.out.println("Friends "+ friends_fb.toString());
                                    new DownloadImageFriend(friend_name_fb).execute(friendDetails.get("photoUrl").toString());

                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(new LatLng((double)loc.get("latitude"),(double)loc.get("longitude")))
                                            .title(friend_name_fb);

                                    if(ImageMap.containsKey(friend_name_fb))
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ImageMap.get(friend_name_fb)));
                                    Marker marker = mMap.addMarker(markerOptions);
                                    MarkerMap.put(friend_name_fb,marker);

                                }
                                //System.out.println("Saifalikaredia " + loc.get("latitude"));

                                //String friendvalue = (String) task.getValue();
                                //System.out.println("Saifalikaredia "+friendvalue);

                            }
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


        // Kick off the request to build GoogleApiClient.

        Toast.makeText(this, mGoogleApiClient.isConnected() + "", Toast.LENGTH_LONG).show();

        // Get Current Time
        dialog_set_time = new Dialog(HomeActivity.this);
        // Include dialog.xml file
        dialog_set_time.setContentView(R.layout.time_setter_dialog);
        // Set dialog title
        dialog_set_time.setTitle("Set_Time");
        start_time = (EditText) dialog_set_time.findViewById(R.id.show_start_time);
        end_time = (EditText) dialog_set_time.findViewById(R.id.show_end_time);
        System.out.println("Junaid"+start_time);

        //Add Friends
        add_friends = new Dialog(HomeActivity.this);
        add_friends.setContentView(R.layout.add_friends_dialog);
        add_friends.setTitle("Add Time");
        add_friends_email = (EditText) add_friends.findViewById(R.id.add_friends_text);

    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            Log.i(TAG, "OnResume, Connected back!");
        } else {
            buildGoogleApiClient();
        }



    }
    /*
    private void setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mAddGeofencesButton.setEnabled(false);
            mRemoveGeofencesButton.setEnabled(true);
        } else {
            mAddGeofencesButton.setEnabled(true);
            mRemoveGeofencesButton.setEnabled(false);
        }
    }
    */

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            HomeActivity.progress
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());

            //Create a circle
                    /*CircleOptions circleOptions = new CircleOptions()
                    .center( new LatLng(entry.getValue().latitude, entry.getValue().longitude) )
                    .radius( Constants.GEOFENCE_RADIUS_IN_METERS )
                    .fillColor(0x40ff0000)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2);
                    mMap.addCircle(circleOptions);
                    Log.v(TAG,"In create geofence");*/
        }
    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofencesButtonHandler() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            populateGeofenceList();
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, GoogleSignIn.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.create_group_item) {
            Intent intent = new Intent(HomeActivity.this, GroupsActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.edit_group_item) {
            if (GroupsActivity.number_of_groups > 0) {
                Intent intent = new Intent(HomeActivity.this, EditGroupActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "You have no groups to edit", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.view_group_item) {
            if (GroupsActivity.number_of_groups > 0)
                ShowGroupDialog();
            else {
                Toast.makeText(this, "You have no groups to view", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.create_geofence) {
            if (GroupsActivity.number_of_groups > 0) {
                ShowGeoFence();
            } else {
                Toast.makeText(this, "You have no groups to create geofence for", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.add_friends) {
            Intent intent = new Intent(HomeActivity.this, AddFriends.class);
            startActivity(intent);
        }

        else if (id == R.id.request_help) {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ShowGroupDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(HomeActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                HomeActivity.this,
                android.R.layout.select_dialog_singlechoice);

        //Log.v("Hashmap in Home",GroupsActivity.groups.size()+"");
        if (GroupsActivity.number_of_groups > 0) {
            for (String key : GroupsActivity.group_names) {
                arrayAdapter.add(key);
            }
        }
        if (arrayAdapter.isEmpty()) {
            Toast.makeText(this, "You have no Groups", Toast.LENGTH_LONG).show();
            return;
        }

        arrayAdapter.add("ALL");
        builderSingle.setTitle("Select Group:-");

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                group_name = arrayAdapter.getItem(i);

            }
        });

        builderSingle.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        Log.v("Saif", group_name);
                        ShowGroupDetails();
                        dialog.dismiss();
                    }
                });
        builderSingle.show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {

            updated_location = location;
            HashMap<String,Object> updatedlocation = new HashMap<>();
            updatedlocation.put("location",new LatLng(location.getLatitude(),location.getLongitude()));
            //updatedlocation.put("Token",)
            mdatabase.child("users").child(uid).updateChildren(updatedlocation);
            if (UserMarker != null) {
                UserMarker.remove();
            }
            //Log.v("in Location","in location");
            ConvertFromLocationToAddress convert = new ConvertFromLocationToAddress(HomeActivity.this, location.getLatitude() + "", location.getLongitude() + "");
            String address = convert.getAddress();
            user_address.setText(address);

            UserMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("ME")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

            if (mMap != null && flag) {
                flag = false;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mdatabase.child("users").orderByChild("location").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("Children in home 3");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                System.out.println("Children in home 4");
                HashMap<String,Object> friendsLocation = (HashMap<String, Object>) dataSnapshot.getValue();
                //System.out.println("Saifalikaredia " + friendsLocation.get("username"));
                String friends_ans = friendsLocation.get("username").toString();
                HashMap<String,Object> loc = (HashMap<String, Object>) friendsLocation.get("location");
                //System.out.println("Saifalikaredia " + loc.get("latitude"));
                LocationMap.put(friends_ans,new LatLng((double)loc.get("latitude"),(double)loc.get("longitude")));


                if(friends_fb.contains(friends_ans)){
                    if(MarkerMap.get(friends_ans)!=null){
                        MarkerMap.get(friends_ans).remove();
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(LocationMap.get(friends_ans))
                                .title(friends_ans);
                        if(ImageMap.containsKey(friends_ans))
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ImageMap.get(friends_ans)));
                        Marker marker = mMap.addMarker(markerOptions);
                        MarkerMap.put(friends_ans,marker);
                    }

                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println("Children in home 5");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        enableMyLocation();
        //mMap.clear();
        //userBO.setGmap(map);
        /*for (int j = 0; j < friends.size(); j++) {

            LatLng loc = friends.get(j).getLoc();
            //userBO.setLoc(loc);
            Log.v("Inside", friends.get(j).toString());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(loc.latitude, loc.longitude))
                    .title(friends.get(j).getFriend_name());
            Marker marker = mMap.addMarker(markerOptions);
            //markerList.add(marker);

        }*/
        mMap.setOnMyLocationButtonClickListener(this);
        map.setOnMarkerDragListener(this);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);


    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            //setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }

    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofencesButtonHandler() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng dragPosition = marker.getPosition();
        dragLat = dragPosition.latitude;
        dragLong = dragPosition.longitude;
        if (c1 != null)
            c1.remove();
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                .radius(HomeActivity.progress)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);
        c1 = mMap.addCircle(circleOptions);

    }

    public void select_start_time(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        start_time.setText(hourOfDay + ":" + minute);
                        System.out.println("Time:" + hourOfDay + minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    public void select_end_time(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        end_time.setText(hourOfDay + ":" + minute);
                        System.out.println("Time:" + hourOfDay + minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void Ok(View view) {
        AddGeofenceMarker();
        dialog_set_time.dismiss();
    }

    public void add_friends_button(View view) {
        Friends_to_add f = new Friends_to_add();
        f.add_friend(add_friends_email.getText().toString());
    }

    public void Cancel_friends(View view) {
        add_friends.dismiss();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.v("url", urldisplay);
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                resizedBitmap = Bitmap.createScaledBitmap(mIcon11, 100, 100, false);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return resizedBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private class DownloadImageFriend extends AsyncTask<String, Void, Bitmap> {
        String friend;
        DownloadImageFriend(String friend){
            this.friend = friend;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.v("url", urldisplay);
            Bitmap mIcon11 = null;
            Bitmap resizedBitmap1 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                resizedBitmap1 = Bitmap.createScaledBitmap(mIcon11, 100, 100, false);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return resizedBitmap1;
        }

        protected void onPostExecute(Bitmap result) {
            Marker marker = MarkerMap.get(friend);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(result));
            ImageMap.put(friend,result);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    private void ShowGroupDetails() {
        mMap.clear();
        //markerList.clear();

        if (group_name.equals("ALL")) {
            for (int j = 0; j < friends.size(); j++) {

                LatLng loc = friends.get(j).getLoc();
                //userBO.setLoc(loc);
                Log.v("Inside", friends.get(j).toString());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(loc.latitude, loc.longitude))
                        .title("Fake");
                Marker marker = mMap.addMarker(markerOptions);
                //markerList.add(marker);

            }
        } else {

            ArrayList<String> group = GroupsActivity.groups.get(group_name);
            Log.v("Group Name-> ", group_name);
            HashSet<String> set;
            for (int i = 0; i < GroupsActivity.group_details.size(); i++) {
                GroupBO groupBO = GroupsActivity.group_details.get(i);
                if (groupBO.getGroup_name().equals(group_name)) {
                    for (int j = 0; j < friends.size(); j++) {
                        if (groupBO.getMember_name().equals(friends.get(j).getFriend_name())) {
                            LatLng loc = friends.get(j).getLoc();
                            //userBO.setLoc(loc);

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(loc.latitude, loc.longitude))
                                    .title("Fake");
                            Marker marker = mMap.addMarker(markerOptions);
                            //markerList.add(marker);
                        }
                    }
                }
            }
        }
    }

    public void ShowGeoFence() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(HomeActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                HomeActivity.this,
                android.R.layout.select_dialog_singlechoice);

        //Log.v("Hashmap in Home",GroupsActivity.groups.size()+"");
        if (GroupsActivity.number_of_groups > 0) {
            for (String key : GroupsActivity.group_names) {
                arrayAdapter.add(key);
            }
        }
        if (arrayAdapter.isEmpty()) {
            Toast.makeText(this, "You have no Groups to display", Toast.LENGTH_LONG).show();
            return;
        }

        builderSingle.setTitle("Select Group:-");

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                group_name = arrayAdapter.getItem(i);

            }
        });

        builderSingle.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        Log.v("Saif", group_name);
                        boolean adminCreateGeo = false;
                        for (int i = 0; i < GroupsActivity.group_items.size(); i++) {
                            if (!GroupsActivity.group_items.get(i).isSection()) {
                                EntryItem entryItem = (EntryItem) GroupsActivity.group_items.get(i);
                                if (entryItem.group_name.equals(group_name)) {
                                    if (entryItem.member_name.equals("Me") && entryItem.isAdmin.equals("Admin")) {
                                        adminCreateGeo = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (adminCreateGeo) {
                            showgroupmembers(group_name);
                        } else {
                            System.out.println("You are not admin of this group");
                        }
                        dialog.dismiss();

                    }


                });
        builderSingle.show();
    }

    private void showgroupmembers(String Groupname) {
        AlertDialog.Builder builderMember = new AlertDialog.Builder(HomeActivity.this);
        builderMember.setTitle("Select Member");

        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                HomeActivity.this,
                android.R.layout.select_dialog_singlechoice);

        for (int i = 0; i < GroupsActivity.group_items.size(); i++) {
            if (!GroupsActivity.group_items.get(i).isSection()) {
                EntryItem entryItem = (EntryItem) GroupsActivity.group_items.get(i);
                if (entryItem.group_name.equals(group_name)) {
                    if (!entryItem.member_name.equals("Me"))
                        arrayAdapter1.add(entryItem.member_name);
                }
            }
        }


        if (arrayAdapter1.isEmpty()) {
            Toast.makeText(this, "You have no members to display", Toast.LENGTH_LONG).show();
            return;
        }

        builderMember.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderMember.setSingleChoiceItems(arrayAdapter1, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                memberForGeofence = arrayAdapter1.getItem(i);
            }
        });

        builderMember.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setTime_geofence();
                        seeker = (SeekBar) findViewById(R.id.seekBar);
                        seeker.setVisibility(View.VISIBLE);
                        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                if (i == 0)
                                    HomeActivity.progress = 50;
                                else
                                    HomeActivity.progress = i * 2;
                                c1.setRadius(HomeActivity.progress);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });


                    }

                    private void setTime_geofence() {

                        // set values for custom dialog components - text, image and button
                        dialog_set_time.show();



                    }
                });
        builderMember.show();

    }


    private void AddGeofenceMarker() {
        cccc++;
        add_more_geofence_restrictor = false;
        MarkerOptions geofence_markerOptions = new MarkerOptions().
                position(new LatLng(updated_location.getLatitude() + 0.005, updated_location.getLongitude() + 0.005)).title("Geofence")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .draggable(true);
        final Marker geofence_marker = mMap.addMarker(geofence_markerOptions);
        //---------->>>>>>>>>>>>geofence_marker.setTag(cccc);
        Toast.makeText(this, "Drag to adjust geofence", Toast.LENGTH_SHORT).show();
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(updated_location.getLatitude() + 0.005, updated_location.getLongitude() + 0.005))
                .radius(HomeActivity.progress)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);
        c1 = mMap.addCircle(circleOptions);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                //int i =(int)marker.getTag();
                AlertDialog.Builder builderMember = new AlertDialog.Builder(HomeActivity.this);
                builderMember.setTitle("Select Action for " + geofence_marker.getTitle());
                builderMember.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(HomeActivity.this,"Work in progress",Toast.LENGTH_SHORT).show();
                                SeekBar seeker = (SeekBar) findViewById(R.id.seekBar);
                                seeker.setVisibility(View.INVISIBLE);
                                removeGeofencesButtonHandler();
                                c1.remove();
                                marker.remove();
                                dialog.dismiss();
                            }
                        });
                builderMember.setPositiveButton(
                        "Add",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SeekBar seeker = (SeekBar) findViewById(R.id.seekBar);
                                seeker.setVisibility(View.INVISIBLE);
                                Constants.BAY_AREA_LANDMARKS.put("USC", new LatLng(dragLat, dragLong)); //HomeActivity.progress , memberForGeofence , user_name
                                //InsertGeofenceIntoDatabase(dragLat, dragLong, HomeActivity.progress, memberForGeofence, user_name, group_name);
                                if (!add_more_geofence_restrictor) {
                                    addGeofencesButtonHandler();
                                    Toast.makeText(HomeActivity.this, "Geofence created", Toast.LENGTH_SHORT).show();
                                    add_more_geofence_restrictor = true;
                                    geofence_marker.setDraggable(false);
                                }

                            }
                        });
                builderMember.show();

                return true;
            }
        });
    }

}
