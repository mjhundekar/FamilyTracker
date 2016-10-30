package com.example.mjhundekar.family_tracker;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saif on 10/29/16.
 */

public class ConvertFromLocationToAddress {
    private Context context;
    private String latitude;
    private String longitude;

    ConvertFromLocationToAddress(Context context,String latitude, String longitude){
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress(){
        String address ="";
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);
            if(addresses != null && addresses.size()!=0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strReturnedAddress.append(returnedAddress.getCountryName());
                address = strReturnedAddress.toString();
            }
            else{
                address = "No Address";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
