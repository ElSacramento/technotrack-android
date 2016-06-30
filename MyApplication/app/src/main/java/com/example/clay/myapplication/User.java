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
    String login = "";
    String pass = "";
    String nick = "";
    public static Socket socket = null;
    final String LOG_TAG = "My_logs";
    public static BufferedOutputStream outputStream = null;
    public static BufferedInputStream inputStream = null;
    public static final String HOST = "188.166.49.215";
    public static final int PORT = 7777;
    String sid = "";
    String uid = "";

    User(String _login, String _pass, String _nick){
        login = _login;
        pass = _pass;
        nick = _nick;
        try {
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    User(){
        this.login = "";
        this.pass = "";
        this.nick = "";
        try {
            this.socket = new Socket(HOST, PORT);
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    User(String sid, String uid) {
        this.sid = sid;
        this.uid = uid;
    };

    public String get_welcome_message(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();;
        JsonParser parser = new JsonParser();
        int readBytes;
        String result = "";
        String status = "";
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
        String status = "";
        String output = "";

        //create json
        JsonObject action = new JsonObject();
        action.addProperty("action", "auth");
        JsonObject data = new JsonObject();
        data.addProperty("login", this.login);
        data.addProperty("pass", md5(this.pass));
        action.add("data", data);
        Log.d(LOG_TAG, "data is ready");
        byte[] dataset = action.toString().getBytes(Charset.forName("UTF-8"));

        try {
            //send message
            Log.d(LOG_TAG, this.login + this.pass);
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
                output = receiver.mMessages.get(action_.getAsString()).doOutput();
                status = output.substring(8, output.indexOf(" sid"));
                sid = output.substring(output.indexOf("sid")+4, output.indexOf(" uid"));
                uid = output.substring(output.indexOf("uid")+4);
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
        String status = "";

        //create json
        JsonObject action = new JsonObject();
        action.addProperty("action", "register");
        JsonObject data = new JsonObject();
        data.addProperty("login", login);
        data.addProperty("pass", md5(pass));
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
        int action_index = result.lastIndexOf("{\"action\"");
        String res_1 = result.substring(0, action_index);
        Log.d(LOG_TAG, res_1);
        String res_2 = result.substring(action_index);
        Log.d(LOG_TAG, res_2);
        JsonElement element = parser.parse(res_1);
        JsonElement element_auth = parser.parse(res_2);
        JsonObject json = element.getAsJsonObject();
        JsonObject json_auth = element_auth.getAsJsonObject();
        JsonElement action_ = json.get("action");
        JsonElement action_auth = json_auth.get("action");
        if (action_ != null) {
            MessageReceiver receiver = new MessageReceiver();
            if (receiver.mMessages.get(action_.getAsString()) != null) {
                receiver.mMessages.get(action_.getAsString()).parse(json);
                status = receiver.mMessages.get(action_.getAsString()).doOutput();
            }
        }
        String status_auth = "";
        String output = "";
        if (action_auth != null) {
            MessageReceiver receiver = new MessageReceiver();
            if (receiver.mMessages.get(action_auth.getAsString()) != null) {
                receiver.mMessages.get(action_auth.getAsString()).parse(json_auth);
                output = receiver.mMessages.get(action_auth.getAsString()).doOutput();
                status_auth = output.substring(8, output.indexOf(" sid"));
                sid = output.substring(output.indexOf("sid")+4, output.indexOf(" uid"));
                uid = output.substring(output.indexOf("uid")+4);
            }
        }
        if (status_auth.equals("0") && status.equals("0"))
            return "0";
        else {
            if (status.equals("0")) return status_auth;
            else return status;
        }
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
        data.addProperty("cid", uid);
        data.addProperty("sid", sid);
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


