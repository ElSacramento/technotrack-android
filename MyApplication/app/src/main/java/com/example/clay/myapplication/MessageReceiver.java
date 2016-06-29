package com.example.clay.myapplication;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by clay on 19.06.16.
 */
public class MessageReceiver {
    
    public static final String LOG_TAG = "My_logs";

    public HashMap<String, Message> mMessages = new HashMap<>();

    public MessageReceiver() {
        mMessages.put("welcome", new Welcome());
        mMessages.put("register", new Registration());
        mMessages.put("auth", new Auth());
        mMessages.put("channellist", new Channellist());
    }
    
    public interface Message {
        void parse(JsonObject json);
        String doOutput();
    }

    public static class Welcome implements Message {
        public String message;
        public long time;

        @Override
        public void parse(JsonObject json) {
            message = json.get("message").getAsString();
            time = json.get("time").getAsLong();
        }

        @Override
        public String doOutput() {
//            Log.d(LOG_TAG, "get welcome message");
            return "0";
//                Date serverTime = new Date(time);
//                System.out.println(message);
//                System.out.println("Server time is: "+serverTime);
//                System.out.println();
//                System.out.println("Enter one of commands: exit, login, register, channellist");
        }
    }

    public static class Registration implements Message {
        public int status;
        public String error;

        @Override
        public void parse(JsonObject json) {
            JsonObject data = json.get("data").getAsJsonObject();
            status = data.get("status").getAsInt();
            error = data.get("error").getAsString();
        }

        @Override
        public String doOutput() {
            if (status == 0) {
//                    Toast.makeText(MainActivity.this, "User saved", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "registration successful");
            }
            return Integer.toString(status);
        }
    }

    public static class Auth implements Message {
        public int status;
        public String error;

        @Override
        public void parse(JsonObject json) {
            JsonObject data = json.get("data").getAsJsonObject();
            status = data.get("status").getAsInt();
            error = data.get("error").getAsString();
        }

        @Override
        public String doOutput() {
            if (status == 0) {
                Log.d(LOG_TAG, "authorization successful");
            }
            return Integer.toString(status);
        }
    }

    public static class Channellist implements Message {

        String channelList = "";

        @Override
        public void parse(JsonObject json) {
            JsonObject data = json.get("data").getAsJsonObject();
            JsonArray array = data.get("channels").getAsJsonArray();
            channelList = array.toString();
        }

        @Override
        public String doOutput() {
            Log.d(LOG_TAG, "channellist=" + channelList);
            return get_string();
        }

        public String get_string(){
            return channelList;
        }
    }
}
    
