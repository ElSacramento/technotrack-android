package com.example.clay.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login;
    EditText password;
    EditText nickname;
    final String LOG_TAG = "My_logs";
    SharedPreferences pref;
    final String USER_LOGIN = "";
    final String USER_PASSWORD = "";
    final String USER_NICKNAME = "";
    Button loginBtn;
    String status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        login = (EditText) findViewById(R.id.editLogin);
        password = (EditText) findViewById(R.id.editPassword);
        nickname = (EditText) findViewById(R.id.editNickname);
        loginBtn = (Button) findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(this);

    }

    private void save_user() {
        pref = getSharedPreferences("LocalUser", MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(USER_LOGIN, login.getText().toString());
        ed.putString(USER_PASSWORD, password.getText().toString());
        ed.putString(USER_NICKNAME, nickname.getText().toString());
        ed.commit();
        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        save_user();
        User client = new User(login.getText().toString(), password.getText().toString(), nickname.getText().toString());
        status = client.registrate();
        if (status == "0") {
            Intent intent = new Intent(this, ChatListActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "error" + new Errors().getError(Integer.getInteger(status)), Toast.LENGTH_LONG).show();
    }
}
