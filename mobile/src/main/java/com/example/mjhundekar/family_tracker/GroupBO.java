package com.example.mjhundekar.family_tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saif on 11/5/16.
 */

public class GroupBO {


    public static HashMap<String, ArrayList<String>> getGroup_list() {
        return group_list;
    }

    public static void setGroup_list(HashMap<String, ArrayList<String>> group_list) {
        GroupBO.group_list = group_list;
    }

    public static HashMap<String,ArrayList<String>> group_list = null;


}
