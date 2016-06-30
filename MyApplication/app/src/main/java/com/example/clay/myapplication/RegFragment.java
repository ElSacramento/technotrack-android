package com.example.clay.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegFragment extends Fragment {
//    private String login = "";
//    private String password = "";
//    private String nick = "";

    final String USER_LOGIN = "login";
    final String USER_PASSWORD = "password";
    final String USER_NICKNAME = "nick";
    EditText login;
    EditText password;
    EditText nick;
    Button button;
    String status = "";
    RegAsyncTask asyncTask;

    private void save_user() {
        SharedPreferences pref = getActivity().getSharedPreferences("LocalUser", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(USER_LOGIN, login.getText().toString());
        ed.putString(USER_PASSWORD, password.getText().toString());
        ed.putString(USER_NICKNAME, nick.getText().toString());
        ed.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration, null);

        button = (Button) v.findViewById(R.id.loginButton);
        login = (EditText) v.findViewById(R.id.editLogin);
        password = (EditText) v.findViewById(R.id.editPassword);
        nick = (EditText) v.findViewById(R.id.editNickname);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User client = new User(login.getText().toString(),
                        password.getText().toString(), nick.getText().toString());

                asyncTask = new RegAsyncTask();
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
                if (status.equals("6")){
                    Intent intent = new Intent(getActivity(), FrameActivity.class);
                    intent.putExtra("action", "auth");
                    getActivity().startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), FrameActivity.class);
                    intent.putExtra("action", "registration");
                    getActivity().startActivity(intent);
                }

            }
        });

        return v;
    }

    public RegFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class RegAsyncTask extends AsyncTask<User, Void, String> {

        String status = "";

        @Override
        protected String doInBackground(User... params) {
            save_user();
            status = params[0].registrate();
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);
        }
    }
}
