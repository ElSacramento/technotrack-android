package com.example.clay.myapplication;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FrameActivity extends AppCompatActivity {

    String action;
    String sid;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        action = getIntent().getExtras().getString("action");

    }

    @Override
    protected void onResume(){
        super.onResume();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (action.equals("channellist")) {
            sid = getIntent().getExtras().getString("sid");
            uid = getIntent().getExtras().getString("uid");
            ChatListFragment fr = ChatListFragment.newInstance(uid, sid);
            ft.replace(R.id.container, fr).addToBackStack(null).commit();
        }
        if (action.equals("registration"))
            ft.replace(R.id.container, new RegFragment()).addToBackStack(null).commit();
        if (action.equals("auth"))
            ft.replace(R.id.container, new AuthFragment()).addToBackStack(null).commit();
    }

}
