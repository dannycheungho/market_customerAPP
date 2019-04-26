package com.example.danny.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 13/3/2018.
 */

public class MemberShipCard  extends AppCompatActivity {
    private TextView record;
    private String username;
    private List<String> unindexedVectors = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membershipcard);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        record = (TextView)findViewById(R.id.record);
        final String name = username;
        // Initialize  AsyncLogin() class with email and password
        new MemberShipCard.AsyncLogin().execute(name);
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MemberShipCard.this);
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
                url = new URL("http://192.168.25.124/test/card.php");

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
                        .appendQueryParameter("username", params[0]);

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
                //     record.setText(result);

                MemberShipCard.this.finish();

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(MemberShipCard.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MemberShipCard.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                String[] tokens = result.split("%");
                int zzz = 0;
                for (String token : tokens) {
                    if ( unindexedVectors.size() % 3 == 0 )
                        zzz++;

                    unindexedVectors.add(token);

                }
                String zzz2 = Integer.toString(zzz);
                record.append( "      會員卡卡號 ***" + unindexedVectors.get(1) + "\n" + "狀況 : " + unindexedVectors.get(2) + "\n    持有卡數目 " + zzz2 ) ;

            }
        }


    }
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

}
