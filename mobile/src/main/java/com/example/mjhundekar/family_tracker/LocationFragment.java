package com.example.mjhundekar.family_tracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

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
                LatLng dest = new LatLng(Double.parseDouble(loc.split(",")[0]),Double.parseDouble(loc.split(",")[1]));
                LatLng origin = new LatLng(HomeActivity.updated_location.getLatitude(), HomeActivity.updated_location.getLongitude());
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

                //startActivity(browserIntent);
                //Close dialog

            }

            private String getDirectionsUrl(LatLng origin, LatLng dest) {


                    // Origin of route
                    String str_origin = "origin="+origin.latitude+","+origin.longitude;

                    // Destination of route
                    String str_dest = "destination="+dest.latitude+","+dest.longitude;

                    // Sensor enabled
                    String sensor = "sensor=false";

                    // Building the parameters to the web service
                    String parameters = str_origin+"&"+str_dest+"&"+sensor;

                    // Output format
                    String output = "json";

                    // Building the url to the web service
                    String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

                    return url;
                }

        });


        /*
        Button message_button = (Button) dialog.findViewById(R.id.MessageButton);
        // if decline button is clicked, close the custom dialog
        direction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
                //Close dialog

            }

            private void sendSMSMessage() {
                TelephonyManager tm = (TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE);
                String Telephone_Number = tm.getLine1Number();

            }
        });*/
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
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            HomeActivity.mMap.addPolyline(lineOptions);
        }
    }
}
