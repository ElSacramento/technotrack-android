package com.example.clay.secondtask;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clay.secondtask.ItemFragment.OnListFragmentInteractionListener;
import com.example.clay.secondtask.ItemContent.Item;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Item> mValues = ItemContent.ITEMS;
    private OnListFragmentInteractionListener mListener;
    private PictureStorage pictures;
    private PictureDownloader<ViewHolder> picThread;

    public MyItemRecyclerViewAdapter(OnListFragmentInteractionListener listener, PictureDownloader<ViewHolder> picThread) {
        this.mListener = listener;
        this.picThread = picThread;
        this.pictures = picThread.getDataStorage();

        picThread.setListener(new PictureDownloader.Listener<ViewHolder>() {
            @Override
            public void onPictureDownloaded(ViewHolder holder, Bitmap picture) {
                if (picture != null) holder.mPicture.setImageBitmap(picture);
                else holder.mPicture.setImageResource(R.drawable.ic_error_outline_black_36dp);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        String pictureUrl = holder.mItem.picture;
        if (pictures != null && pictures.contains(pictureUrl)) {
            holder.mPicture.setImageBitmap(pictures.get(pictureUrl));
        } else {
            picThread.queuePicture(holder, pictureUrl);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public Item mItem;
        public ImageView mPicture;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mPicture = (ImageView) view.findViewById(R.id.image);
        }
    }
}
