package com.example.clay.secondtask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

/**
 * Created by clay on 17.04.16.
 */
public class StartActivity extends AppCompatActivity {

    MyAsyncTask downloadTask;
    MySleepAsyncTask sleepTask;
    String LOG_TAG = "my_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        downloadTask = new MyAsyncTask();
        sleepTask = new MySleepAsyncTask();
        downloadTask.execute();
        sleepTask.execute();
    }

    protected void onStop(){
        super.onStop();
        Log.d("main_activity", "finish");
        finish();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        ItemContent parsed_json;
        @Override
        protected Void doInBackground(Void... params) {
            parsed_json = new ItemContent().getData();
            sleepTask.cancel(true);
            if (parsed_json.SIZE == 0) {
                Log.d("error", "No Internet connection");
                return null;
            }

            Intent listActivity = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(listActivity);
            return null;
        }

        @Override
        protected void onPostExecute(Void par){
            super.onPostExecute(par);
            if (parsed_json.SIZE == 0)
                Toast.makeText(getApplicationContext(), "No Internet connection",  Toast.LENGTH_LONG).show();
            finish();
        }

    }

    class MySleepAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(1);
                if (this.isCancelled()) return null;
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
