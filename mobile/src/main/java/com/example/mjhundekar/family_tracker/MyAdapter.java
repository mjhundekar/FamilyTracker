package com.example.mjhundekar.family_tracker;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saif on 10/29/16.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<FriendBO> friendBO;

    MyAdapter(Context context, List<FriendBO> friendBO) {
        this.context = context;
        this.friendBO = friendBO;

    }

    @Override
    public int getCount() {

        return friendBO.size();
    }

    @Override
    public Object getItem(int position) {

        return friendBO.get(position);
    }

    @Override
    public long getItemId(int position) {

        return friendBO.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.friend_list_row, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.list_friend_image);
        TextView friend_name = (TextView) convertView.findViewById(R.id.list_friend_name);
        TextView friend_address = (TextView) convertView.findViewById(R.id.list_friend_address);

        FriendBO row_pos = friendBO.get(position);
        // setting the image resource and title
        imgIcon.setImageResource(row_pos.getIcon());
        friend_name.setText(row_pos.getFriend_name());

        ConvertFromLocationToAddress convert = new ConvertFromLocationToAddress(context,row_pos.toString().split(",")[0],row_pos.toString().split(",")[1]);
        String address = convert.getAddress();
        friend_address.setText(address);

        return convertView;

    }

}