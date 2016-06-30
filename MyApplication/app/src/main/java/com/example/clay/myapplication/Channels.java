package com.example.clay.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by clay on 30.06.16.
 */
public class Channels {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Channel> ITEMS = new ArrayList<Channel>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Channel> ITEM_MAP = new HashMap<String, Channel>();

    public Channels(String list){
        try {
            JSONArray array = new JSONArray(list);
            int i = 0;
            while (i != array.length()) {
                JSONObject jsonItem = array.getJSONObject(i);
                Log.d("json", jsonItem.toString());
                Channel chat = new Channel(jsonItem.getString("name"), jsonItem.getString("descr"),
                        "(" + Integer.toString(jsonItem.getInt("online")) + ")");
                ITEMS.add(chat);
                ITEM_MAP.put(jsonItem.getString("chid"), chat);
                i++;

            }
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class Channel {

        public String title;
        public String description;
        public String members;

        public Channel(String title, String decr, String online) {
            this.title = title;
            this.description = decr;
            this.members = online;
        }
    }
}
