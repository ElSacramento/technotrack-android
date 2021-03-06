package com.example.clay.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class AuthFragment extends Fragment {

    final String USER_LOGIN = "login";
    final String USER_PASSWORD = "password";
    EditText login;
    EditText password;
    Button button;
    String status = "";
    AuthAsyncTask asyncTask;

    private void update_user() {
        SharedPreferences pref = getActivity().getSharedPreferences("LocalUser", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(USER_LOGIN, login.getText().toString());
        ed.putString(USER_PASSWORD, password.getText().toString());
        ed.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.authorization, null);

        button = (Button) v.findViewById(R.id.loginButton);
        login = (EditText) v.findViewById(R.id.editLogin);
        password = (EditText) v.findViewById(R.id.editPassword);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User client = new User(login.getText().toString(),
                        password.getText().toString(),
                        getActivity().getSharedPreferences("LocalUser", getActivity().MODE_PRIVATE).getString("nick", ""));

                asyncTask = new AuthAsyncTask();
                try {
                    status = asyncTask.execute(client).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Log.d("my_log", status);
//                Toast.makeText(getActivity(),
//                        new Errors().getError(Integer.parseInt(status)), Toast.LENGTH_LONG).show();
                if (status.equals("0")) {
                    Intent intent = new Intent(getActivity(), FrameActivity.class);
                    intent.putExtra("action", "channellist").putExtra("sid", client.sid).
                            putExtra("uid", client.uid);
                    getActivity().startActivity(intent);
                }
                if (status.equals("2") || status.equals("6") || status.equals("3")){
                    Intent intent = new Intent(getActivity(), FrameActivity.class);
                    intent.putExtra("action", "auth");
                    getActivity().startActivity(intent);
                }

            }
        });

        return v;
    }

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class AuthAsyncTask extends AsyncTask<User, Void, String> {

        String status = "";

        @Override
        protected String doInBackground(User... params) {
            update_user();
            status = params[0].authorize();
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);
        }
    }
}
