package com.example.clay.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by clay on 17.05.16.
 */

public class Registration {
    public static final String HOST = "188.166.49.215";
    public static final int PORT = 7777;
    Socket socket;

    private Registration() {
        String login = "";
        String pass = "";
        String nick = "";
        String line = "";
        BufferedReader keyboard;
        try {
            socket = new Socket(HOST, PORT);
            keyboard = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            login = keyboard.readLine();
            pass = keyboard.readLine();
            nick = keyboard.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User new_user = new User(login, pass, nick);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(new_user);
        json = "{\"action\": \"register\",\"data\":" + json + "}";
        try {
            socket.getOutputStream().write(json.getBytes());
            keyboard = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            line = keyboard.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        class RegistrationResponse {
            String action;
            String data;
            int status;
            String error;
        }
        RegistrationResponse response = gson.fromJson(line, RegistrationResponse.class);
        String response_status = new Errors().getError(response.status);
        //вывести на экран ошибку
    }
}
