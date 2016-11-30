package com.example.mjhundekar.family_tracker;

/**
 * Created by Saif on 11/7/16.
 */


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class EntryAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater vi;

    public EntryAdapter(Context context,ArrayList<Item> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                v = vi.inflate(R.layout.edit_group_seperator, null);


                v.setOnLongClickListener(null);
                v.setLongClickable(false);


                final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getGroup_name());

            }else{
                EntryItem ei = (EntryItem)i;
                v = vi.inflate(R.layout.edit_group_item, null);
                final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
                final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
                final ImageView img = (ImageView)v.findViewById(R.id.list_item_entry_drawable);

                System.out.println("GGGGGG "+ei.member_name);
                if (title != null)
                    title.setText(ei.member_name);
                if(subtitle != null)
                    subtitle.setText(ei.isAdmin);
                if(img!=null)
                    if(ei.member_name.equals("Me"))
                        img.setImageBitmap(HomeActivity.resizedBitmap);
                    else
                        img.setImageBitmap(HomeActivity.ImageMap.get(ei.member_name));
            }
        }
        return v;
    }

}
