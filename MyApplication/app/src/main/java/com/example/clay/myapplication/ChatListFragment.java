package com.example.clay.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatListFragment extends Fragment {

    MyChatRecyclerViewAdapter adapter;
    ChannelsAsyncTask channels;
    String sid = "";
    String cid = "";
    String channellist = "";

    public ChatListFragment() {
    }

    public static ChatListFragment newInstance(String uid, String sid){
        ChatListFragment fr = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString("cid", uid);
        args.putString("sid", sid);
        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sid = getArguments().getString("sid");
        cid = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        // Set the adapter
        channels = new ChannelsAsyncTask();
        try {
            String result = channels.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(new MyChatRecyclerViewAdapter());
        return view;
    }

    private class ChannelsAsyncTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            User client = new User(sid, cid);
            channellist = client.get_channellist();
            Log.d("channels", channellist);
            Channels items = new Channels(channellist);
            return channellist;
        }
    }

}
