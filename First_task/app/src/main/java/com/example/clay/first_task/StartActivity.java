package com.example.clay.first_task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

/**
 * Created by clay on 19.03.16.
 */
public class StartActivity extends Activity {

    MyAsyncTask someTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

//        Button button = (Button)findViewById(R.id.button);
//        button.setOnClickListener(StartActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        someTask = new MyAsyncTask();
        someTask.execute(this);
    }

    @Override
    public void onBackPressed(){
        someTask.cancel(true);
        this.finish();
        super.onBackPressed();
    }

    class MyAsyncTask extends AsyncTask<StartActivity, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(StartActivity... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
                Intent secondView = new Intent(params[0], ListViewActivity.class);
                startActivity(secondView);
                someTask.cancel(true);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
