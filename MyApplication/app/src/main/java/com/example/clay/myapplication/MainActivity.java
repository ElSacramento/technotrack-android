package com.example.clay.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    final String LOG_TAG = "My_logs";
    final String USER_LOGIN = "login";
    final String USER_PASSWORD = "password";
    final String USER_NICKNAME = "nick";
    MyTask mt;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mt = (MyTask) getLastNonConfigurationInstance();
        if (mt == null) {
            mt = new MyTask();
            String[] user = load_user();
            mt.execute(user);
        }
            // передаем в MyTask ссылку на текущее MainActivity
        mt.link(this);

    }

    private void showToast(String status){
        Toast.makeText(getApplicationContext(), "error " +
                new Errors().getError(Integer.parseInt(status)), Toast.LENGTH_LONG).show();

    }

    private String[] load_user() {
        SharedPreferences pref = getSharedPreferences("LocalUser", MODE_PRIVATE);
        String savedLogin = pref.getString(USER_LOGIN, "");
        String savedPassword = pref.getString(USER_PASSWORD, "");
        String savedNick = pref.getString(USER_NICKNAME, "");
        String[] result = {savedLogin, savedPassword, savedNick};
//        Toast.makeText(MainActivity.this, "User loaded", Toast.LENGTH_SHORT).show();
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast toast = Toast.makeText(MainActivity.this, "Выбраны настройки", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.CENTER, 0, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    public Object onRetainNonConfigurationInstance() {
        // удаляем из MyTask ссылку на старое MainActivity
        mt.unLink();
        return mt;
    }

    class MyTask extends AsyncTask<String, Void, Void> {

        MainActivity activity;

        // получаем ссылку на MainActivity
        void link(MainActivity act) {
            activity = act;
        }

        // обнуляем ссылку
        void unLink() {
            activity = null;
        }

        @Override
        protected Void doInBackground(String... params) {
            //get welcome message
            status = new User().get_welcome_message();
            if (status.equals("0")) {
                //авторизация
                Log.d(LOG_TAG, params[0]+params[1]);
                if (!params[0].equals("") && !params[1].equals("")){
                    User client = new User(params[0], params[1], params[2]);
                    status = client.authorize();
//                    showToast(status);
                    if (status.equals("0")) {
                        Intent intent = new Intent(activity, FrameActivity.class);
                        intent.putExtra("action", "channellist").putExtra("sid", client.sid).putExtra("uid", client.uid);
                        startActivity(intent);
                    }
                    if (status.equals("2") || status.equals("6") || status.equals("3")){
                        Intent intent = new Intent(activity, FrameActivity.class);
                        intent.putExtra("action", "auth");
                        startActivity(intent);
                    }
                    if (status.equals("7")){
                        Intent intent = new Intent(activity, FrameActivity.class);
                        intent.putExtra("action", "registration");
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(activity, FrameActivity.class);
                    intent.putExtra("action", "registration");
                    startActivity(intent);
                }
            }
            else return null;
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }
}
