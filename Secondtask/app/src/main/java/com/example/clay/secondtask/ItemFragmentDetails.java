package com.example.clay.secondtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFragmentDetails extends Fragment {

    private final List<ItemContent.Item> mValues = ItemContent.ITEMS;

    private PictureStorage pictures = ItemFragment.picStorage;

    public ItemFragmentDetails() {
        // Required empty public constructor
    }

    String key;

    public static ItemFragmentDetails newInstance(String index){
        ItemFragmentDetails item = new ItemFragmentDetails();
        Bundle args = new Bundle();
        args.putString("item_id", index);
        item.setArguments(args);
        return item;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        key = getArguments().getString("item_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        TextView title = (TextView) view.findViewById(R.id.title_details);
        TextView descr = (TextView) view.findViewById(R.id.description);
        ImageView image = (ImageView) view.findViewById(R.id.image_details);
        ItemContent.Item item = mValues.get(Integer.parseInt(key));

        title.setText(item.title);
        descr.setText(item.details);
        if (pictures.get(item.picture) != null)
            image.setImageBitmap(pictures.get(item.picture));
        else image.setImageResource(R.drawable.ic_error_outline_black_36dp);
        return view;
    }

    public void onDestroy(){
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_item_details);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commit();
            getFragmentManager().popBackStack();
        }

        super.onDestroy();
        getActivity().finish();
    }
}
