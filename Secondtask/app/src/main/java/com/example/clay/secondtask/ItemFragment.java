package com.example.clay.secondtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clay.secondtask.ItemContent.Item;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    private OnListFragmentInteractionListener mListener = null;
    private static PictureDownloader<MyItemRecyclerViewAdapter.ViewHolder> picThread;
    public static PictureStorage picStorage;
    private static MyItemRecyclerViewAdapter adapter;

    public ItemFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState == null) initPicThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        recyclerView.setAdapter(adapter);

        picStorage = picThread.getDataStorage();

        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state", "save state");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Item item);
    }

    public void initPicThread(){
        picThread = new PictureDownloader<>(new Handler());
        picThread.start();
        picStorage = new PictureStorage();
        picThread.setDataStorage(picStorage);
        picThread.getLooper();
        adapter = new MyItemRecyclerViewAdapter(mListener, picThread);
    }

    public void onDestroy(){
        picThread.quit();
        picThread.clearQueue();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.list);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commit();
        }

        super.onDestroy();
        getActivity().finish();
    }
}
