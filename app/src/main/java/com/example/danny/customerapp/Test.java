package com.example.danny.customerapp;

/**
 * Created by Danny on 12/3/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Test extends AppCompatActivity {
    private String username,price,pid;
    private double getprice;
    private TextView balance;
    private TextView username2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        pid = intent.getStringExtra("pid");
        getprice = intent.getDoubleExtra("price" , 0);
        username2 =  (TextView)findViewById(R.id.textView2);
        balance = (TextView)findViewById(R.id.textView3);

        price =  Double.toString(getprice);
        username2.setText( "總價錢:   " + price);
        balance.setText( "請註意確定後金錢並不會退還" );

      //  new AsyncProfileEdit().execute(price,username);


    }
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }



    public void checkLogin(View arg0) {

        // Get text from email and passord field
        // Initialize  AsyncLogin() class with email and password
        new AsyncProfileEdit().execute(price,pid,username);

    }



    private class AsyncProfileEdit extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Test.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.25.124/test/subbalance.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("price", params[0])
                        .appendQueryParameter("pid", params[1])
                        .appendQueryParameter("username", params[2])
                        ;

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            if (result.equalsIgnoreCase("true")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                Toast.makeText(Test.this, "Edit Profile Successfully", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(Test.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(Test.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }
}
