package com.example.clay.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        ListView list = (ListView) findViewById(R.id.listView);
        int img = R.drawable.ic_people_black_36dp;

//        list.setAdapter(myAdapter);

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(/*number of chats*/);
        Map<String, Object> m;

//        for (int i = 0; i < texts.length; i++) {
//            m = new HashMap<String, Object>();
//            m.put(ATTRIBUTE_NAME_TEXT, texts[i]);
//            m.put(ATTRIBUTE_NAME_CHECKED, checked[i]);
//            m.put(ATTRIBUTE_NAME_IMAGE, img);
//            data.add(m);
//        }
//
//        // массив имен атрибутов, из которых будут читаться данные
//        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_CHECKED,
//                ATTRIBUTE_NAME_IMAGE };
//        // массив ID View-компонентов, в которые будут вставлять данные
//        int[] to = { R.id.tvText, R.id.cbChecked, R.id.ivImg };
//
//        // создаем адаптер
//        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.item,
//                from, to);
//
//        // определяем список и присваиваем ему адаптер
//        lvSimple = (ListView) findViewById(R.id.lvSimple);
//        lvSimple.setAdapter(sAdapter);

    }
}
