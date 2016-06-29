package com.example.clay.secondtask;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ItemContent {

    /**
     * An array of items.
     */
    public static List<Item> ITEMS = new ArrayList<Item>();

    /**
     * A map of items, by ID.
     */
    public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    public static Integer SIZE = 0;

    public ItemContent(){
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://mobevo.ext.terrhq.ru/shr/j/ru/technology.js");
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[] dataset = new byte[2048];
            int readBytes = 0;
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            while ((readBytes = inputStream.read(dataset)) > 0) {
                byteOutputStream.write(dataset, 0, readBytes);
            }
            byteOutputStream.flush();
            String result = byteOutputStream.toString("utf-8");

            //parse answer
            JSONObject jsonObject = new JSONObject(result);
            JSONObject technology = jsonObject.getJSONObject("technology");
            Iterator<String> iterator = technology.keys();
            int i = 0;

            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONObject jsonItem = technology.getJSONObject(key);
                Item item = new Item(key, jsonItem.getString("title"), "http://mobevo.ext.terrhq.ru/" + jsonItem.getString("picture"), i);

                if (jsonItem.has("info")) {
                    item.setInfo(jsonItem.getString("info"));
                }

                ITEMS.add(item);
                ITEM_MAP.put(key, item);
                i++;
            }
            SIZE = ITEMS.size();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }
    }

    public ItemContent getData(){
        return this;
    }


    public static class Item {
        public final String id;
        public final String title;
        public final String picture;
        public String details = "";
        public int position;

        public Item(String id, String title, String picture, int position) {
            this.id = id;
            this.title = title;
            this.picture = picture;
            this.position = position;
        }

        public void setInfo(String details){
            this.details = details;
        }

    }
}
