package com.example.clay.secondtask;


import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class PictureStorage {

    private final int cacheSize;

    public static LruCache<String, Bitmap> cache;

    public PictureStorage(int cacheSize){
        this.cacheSize = cacheSize;

        cache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public PictureStorage() {
        this((int) (Runtime.getRuntime().maxMemory() / 8));
    }

    public void put(String key, Bitmap value) {
        if (key == null || value == null){
            return;
        }
        cache.put(key, value);
    }

    public Bitmap get(String key) {
        if (key == null){
            return null;
        }
        return cache.get(key);
    }

    public boolean contains(String key) {
        if (key == null){
            return false;
        }
        return cache.get(key) != null;
    }
}
