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

public class ProfileActivity extends AppCompatActivity {
    private EditText etName,etAge,etEmail,etSex,etTelephone,etNationality,etOccupation;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        etName = (EditText) findViewById(R.id.etName);
        etTelephone = (EditText) findViewById(R.id.etTelephone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNationality = (EditText) findViewById(R.id.etNationality);
        etSex = (EditText) findViewById(R.id.etSex);
        etAge = (EditText) findViewById(R.id.etAge);
        etOccupation = (EditText) findViewById(R.id.etOccupation);

    }
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }


    public void editProfile(View arg0) {

// Get text from email and passord field
        final String name = etName.getText().toString();
        final String tel = etTelephone.getText().toString();
        final String email = etEmail.getText().toString();
        final String Nationality = etNationality.getText().toString();
        final String Sex = etSex.getText().toString();
        final String Age = etAge.getText().toString();
        final String Occupation = etOccupation.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Username must be input", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfileActivity.this, SuccessActivity.class);
            intent.putExtra("username", etEmail.getText().toString() );
            startActivity(intent);
    }
        new AsyncProfileEdit().execute(name,tel,email,Nationality,Sex,Age,Occupation,username);

    }

    private class AsyncProfileEdit extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ProfileActivity.this);
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
                url = new URL("http://192.168.25.124/test/ProfileEdit.php");

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
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("telephone", params[1])
                        .appendQueryParameter("email", params[2])
                        .appendQueryParameter("nationality", params[3])
                        .appendQueryParameter("sex", params[4])
                       .appendQueryParameter("age", params[5])
                      .appendQueryParameter("occupation", params[6])
                        .appendQueryParameter("username", params[7])
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

                Toast.makeText(ProfileActivity.this, "Payment Success", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(ProfileActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(ProfileActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }
}
