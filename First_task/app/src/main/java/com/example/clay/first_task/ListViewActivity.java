package com.example.clay.first_task;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by clay on 19.03.16.
 */
public class ListViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);
        //resources
        String[] numbers = getResources().getStringArray(R.array.numbers);
        //String[] numbers = new RussianNumbers().createMillion();
        //find particular view
        ListView listView = (ListView) findViewById(R.id.listView);
        //use adapter
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, numbers);
        MyArrayAdapter adapter = new MyArrayAdapter(this, numbers);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        this.finish();
        super.onBackPressed();
    }

    static class MyArrayAdapter extends BaseAdapter {

        Context context;
        ArrayList numbers;

        public static class ViewHolder {
            public TextView textView;
        }

        MyArrayAdapter(Context context, String[] list) {
            this.context = context;
            numbers = new ArrayList<String>(Arrays.asList(list));
        }

        @Override
        public int getCount() {
            return numbers.size();
        }

        @Override
        public Object getItem(int position) {
            return numbers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View resultView, ViewGroup parent) {
            String viewText = (String) getItem(position);
            ViewHolder holder;
            if (resultView == null) {
                //get view from lis_item layout
                resultView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView)resultView.findViewById(R.id.textView);
                //to remember a view
                resultView.setTag(holder);
            } else {
                holder = (ViewHolder)resultView.getTag();
            }
            holder.textView.setText(viewText);
            if ((position + 1) % 2 == 1) {
                resultView.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                resultView.setBackgroundColor(Color.parseColor("#aaaaaa"));
            }
            return resultView;
        }
    }

}
