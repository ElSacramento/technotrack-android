package com.example.clay.secondtask;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PictureDownloader<Token> extends HandlerThread {
    private final static String TAG = PictureDownloader.class.getSimpleName();

    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler handler;
    private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    private Handler responseHandler;
    private Listener<Token> listener;

    private PictureStorage picturesData = null;

    public interface Listener<Token>{
        void onPictureDownloaded(Token token, Bitmap picture);
    }

    public void setListener(Listener<Token> listener) {
        this.listener = listener;
    }

    public PictureDownloader(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    public void queuePicture(Token token, String url) {
        requestMap.put(token, url);
        handler.obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "PictureDownloader request for url: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    private void handleRequest(final Token token){

        final String url = requestMap.get(token);

        Bitmap bitMap = null;
        HttpURLConnection connection = null;
        try {
            if (url == null)
                return;
            URL parse_url = new URL(url);
            connection = (HttpURLConnection) parse_url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[] dataset = new byte[2048];
            int readBytes = 0;
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            while ((readBytes = inputStream.read(dataset)) > 0) {
                byteOutputStream.write(dataset, 0, readBytes);
            }
            byteOutputStream.flush();
            byte[] bitmapBytes = byteOutputStream.toByteArray();
            bitMap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

            Log.i(TAG, "PictureDownloader picture downloaded");

        } catch (IOException e) {
            Log.e(TAG, "PictureDownloader Error downloading image", e);
        }

        picturesData.put(url, bitMap);
        processRespond(token, bitMap, url);
    }

    private void processRespond(final Token token, final Bitmap bitmap, final String url){
        responseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (requestMap.get(token) == null || !requestMap.get(token).equals(url)) {
                    return;
                }

                requestMap.remove(token);
                listener.onPictureDownloaded(token, bitmap);
            }
        });
    }

    public void clearQueue() {
        handler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    public void setDataStorage(PictureStorage dataStorage) {
        this.picturesData = dataStorage;
    }

    public PictureStorage getDataStorage() {
        return picturesData;
    }
}