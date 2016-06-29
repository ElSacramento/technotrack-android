package com.example.clay.myapplication;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by clay on 17.05.16.
 */
public class User {
    String login;
    String pass;
    String nick;
    Socket socket = null;
    final String LOG_TAG = "My_logs";
    String HOST = "188.166.49.215";
    int PORT = 7777;
    BufferedOutputStream outputStream;
    BufferedInputStream inputStream;

    User(String _login, String _pass, String _nick){
        login = _login;
        pass = _pass;
        nick = _nick;
    }

    User(){
        login = "";
        pass = "";
        nick = "";
        try {
            socket = new Socket(HOST, PORT);
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get_welcome_message(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();;
        JsonParser parser = new JsonParser();
        int readBytes;
        String result = "";
        String status = "0";
        byte[] dataset;

        try {
            dataset = new byte[32768];
            readBytes = inputStream.read(dataset);
//            Log.d(LOG_TAG, "read bytes");
            byteOutputStream.write(dataset, 0, readBytes);
//            Log.d(LOG_TAG, "write bytes");
            byteOutputStream.flush();
            result = byteOutputStream.toString("utf-8");
//            Log.d(LOG_TAG, result);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Connection closed or lost");
            e.printStackTrace();
        }

        //parse answer
        Log.d(LOG_TAG, result);
        JsonElement element = parser.parse(result);
        JsonObject json = element.getAsJsonObject();
        JsonElement action_ = json.get("action");
        if (action_ != null) {
            MessageReceiver receiver = new MessageReceiver();
            if (receiver.mMessages.get(action_.getAsString()) != null) {
                receiver.mMessages.get(action_.getAsString()).parse(json);
                status = receiver.mMessages.get(action_.getAsString()).doOutput();
            }
        }
        return status;
    }

    public String authorize(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();;
        JsonParser parser = new JsonParser();
        int readBytes;
        String result = "";
        String status = "0";

        //create json
        JsonObject action = new JsonObject();
        action.addProperty("action", "auth");
        JsonObject data = new JsonObject();
        data.addProperty("login", this.login);
        data.addProperty("pass", this.pass);
        action.add("data", data);
        byte[] dataset = action.toString().getBytes(Charset.forName("UTF-8"));

        try {
            //send message
            outputStream.write(dataset);
//            Log.d(LOG_TAG, "write message");
            outputStream.flush();
//            Log.d(LOG_TAG, "send msg");

            //receive answer
//            Log.d(LOG_TAG, "start receiving");
//              Log.d(LOG_TAG, "connect");
            dataset = new byte[32768];
            readBytes = inputStream.read(dataset);
//            Log.d(LOG_TAG, "read bytes");
            byteOutputStream.write(dataset, 0, readBytes);
//            Log.d(LOG_TAG, "write bytes");
            byteOutputStream.flush();
            result = byteOutputStream.toString("utf-8");
//            Log.d(LOG_TAG, result);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Connection closed or lost");
            e.printStackTrace();
//            Log.d(LOG_TAG, e.getStackTrace().toString());
        }

        //parse answer
        Log.d(LOG_TAG, result);
        JsonElement element = parser.parse(result);
        JsonObject json = element.getAsJsonObject();
        JsonElement action_ = json.get("action");
//        Log.d(LOG_TAG, action_.toString());
        if (action_ != null) {
            MessageReceiver receiver = new MessageReceiver();
            if (receiver.mMessages.get(action_.getAsString()) != null) {
                receiver.mMessages.get(action_.getAsString()).parse(json);
                status = receiver.mMessages.get(action_.getAsString()).doOutput();
            }
        }
//        Log.d(LOG_TAG, Integer.toString(status));
        return status;
    }

    public String registrate(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();;
        JsonParser parser = new JsonParser();
        int readBytes;
        String result = "";
        String status = "0";

        //create json
        JsonObject action = new JsonObject();
        action.addProperty("action", "register");
        JsonObject data = new JsonObject();
        data.addProperty("login", login);
        data.addProperty("pass", pass);
        data.addProperty("nick", nick);
        action.add("data", data);
        byte[] dataset = action.toString().getBytes(Charset.forName("UTF-8"));

        try {
            //send message
            outputStream.write(dataset);
            outputStream.flush();

            //receive answer
            dataset = new byte[32768];
            readBytes = inputStream.read(dataset);
            byteOutputStream.write(dataset, 0, readBytes);
            byteOutputStream.flush();
            result = byteOutputStream.toString("utf-8");
        } catch (IOException e) {
            Log.d(LOG_TAG, "Connection closed or lost");
            e.printStackTrace();
        }

        //parse answer
        Log.d(LOG_TAG, result);
        JsonElement element = parser.parse(result);
        JsonObject json = element.getAsJsonObject();
        JsonElement action_ = json.get("action");
        if (action_ != null) {
            MessageReceiver receiver = new MessageReceiver();
            if (receiver.mMessages.get(action_.getAsString()) != null) {
                receiver.mMessages.get(action_.getAsString()).parse(json);
                status = receiver.mMessages.get(action_.getAsString()).doOutput();
            }
        }
        return status;
    }

    public String get_channellist(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();;
        JsonParser parser = new JsonParser();
        int readBytes;
        String result = "";
        String channellist = "";

        //create json
        JsonObject action = new JsonObject();
        action.addProperty("action", "channellist");
        JsonObject data = new JsonObject();
        action.add("data", data);
        byte[] dataset = action.toString().getBytes(Charset.forName("UTF-8"));

        try {
            //send message
            outputStream.write(dataset);
            outputStream.flush();

            //receive answer
            dataset = new byte[32768];
            readBytes = inputStream.read(dataset);
            byteOutputStream.write(dataset, 0, readBytes);
            byteOutputStream.flush();
            result = byteOutputStream.toString("utf-8");
        } catch (IOException e) {
            Log.d(LOG_TAG, "Connection closed or lost");
            e.printStackTrace();
        }

        //parse answer
        Log.d(LOG_TAG, result);
        JsonElement element = parser.parse(result);
        JsonObject json = element.getAsJsonObject();
        JsonElement action_ = json.get("action");
        if (action_ != null) {
            MessageReceiver receiver = new MessageReceiver();
            if (receiver.mMessages.get(action_.getAsString()) != null) {
                receiver.mMessages.get(action_.getAsString()).parse(json);
                channellist = receiver.mMessages.get(action_.getAsString()).doOutput();
            }
        }
        return channellist;
    }

    public static String md5(String s) {
        String md5sum = null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            md5sum = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5sum;
    }
}





//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//        String json = gson.toJson(this.login + this.pass);
//        json = "{\"action\": \"auth\",\"data\":" + json + "}";

